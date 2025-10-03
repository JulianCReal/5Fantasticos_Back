package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Repository.DeanOfficeRepository;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Repository.ProfessorRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class ScheduleService {

    private static final Logger log = Logger.getLogger(ScheduleService.class.getName());

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DeanOfficeRepository deanOfficeRepository;


    public Map<String, Object> obtenerHorarioEstudiante(String estudianteId, String usuarioId, UserRole userRole) 
            throws IllegalAccessException, IllegalArgumentException {
        
        log.info("Consultando horario para estudiante: " + estudianteId + " por usuario: " + usuarioId + " con rol: " + userRole);


        Student student = studentRepository.findById(estudianteId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId);
        }

        switch (userRole) {
            case STUDENT:
                validarAccesoEstudiante(estudianteId, usuarioId);
                break;
            case PROFESSOR:
                validarAccesoProfesor(student, usuarioId);
                break;
            case DEANOFFICE:
                validarAccesoDecanatura(student, usuarioId);
                break;
            default:
                throw new IllegalAccessException("Rol de usuario no válido");
        }

        return generarHorario(student);
    }


    private void validarAccesoEstudiante(String estudianteId, String usuarioId) throws IllegalAccessException {
        if (!estudianteId.equals(usuarioId)) {
            log.warning("Acceso denegado: Estudiante " + usuarioId + " intentó consultar horario de " + estudianteId);
            throw new IllegalAccessException("Los estudiantes solo pueden consultar su propio horario");
        }
    }


    private void validarAccesoProfesor(Student student, String profesorId) throws IllegalAccessException {
        Professor professor = professorRepository.findById(profesorId).orElse(null);
        if (professor == null) {
            throw new IllegalArgumentException("Profesor no encontrado con ID: " + profesorId);
        }

        boolean tieneEstudiante = false;
        
        for (Semester semester : student.getSemestres()) {
            for (Enrollment enrollment : semester.getSubjects()) {
                Group group = enrollment.getGrupo();
                if (group.getProfesor() != null && profesorId.equals(group.getProfesor().getId())) {
                    tieneEstudiante = true;
                    break;
                }
            }
            if (tieneEstudiante) break;
        }

        if (!tieneEstudiante) {
            log.warning("Acceso denegado: Profesor " + profesorId + " no tiene asignado al estudiante " + student.getStudentId());
            throw new IllegalAccessException("Los profesores solo pueden consultar el horario de sus estudiantes asignados");
        }
    }


    private void validarAccesoDecanatura(Student student, String decanaturaId) throws IllegalAccessException {
        DeanOffice deanOffice = deanOfficeRepository.findById(decanaturaId).orElse(null);
        if (deanOffice == null) {
            throw new IllegalArgumentException("Decanatura no encontrada con ID: " + decanaturaId);
        }


        if (!student.getDegreeProgram().equals(deanOffice.getFaculty())) {
            log.warning("Acceso denegado: Decanatura " + decanaturaId + " intentó consultar estudiante de otra facultad");
            throw new IllegalAccessException("La decanatura solo puede consultar estudiantes de su facultad");
        }
    }


    private Map<String, Object> generarHorario(Student student) {
        List<Map<String, Object>> clases = new ArrayList<>();

        Semester semesterActual = null;
        for (Semester semester : student.getSemestres()) {
            if (semester.isState()) { // Asumiendo que hay un método isState() o similar
                semesterActual = semester;
                break;
            }
        }

        if (semesterActual == null && !student.getSemestres().isEmpty()) {
            semesterActual = student.getSemestres().get(student.getSemestres().size() - 1);
        }

        if (semesterActual != null) {
            for (Enrollment enrollment : semesterActual.getSubjects()) {
                if (!"cancelada".equals(enrollment.getState())) {
                    clases.addAll(generarClasesDeInscripcion(enrollment));
                }
            }
        }

        Map<String, Object> horario = new HashMap<>();
        horario.put("estudianteId", student.getStudentId());
        horario.put("estudianteNombre", student.getNombre() + " " + student.getLastName());
        horario.put("carrera", student.getDegreeProgram());
        horario.put("semestre", student.getSemester());
        horario.put("clases", clases);

        return horario;
    }


    private List<Map<String, Object>> generarClasesDeInscripcion(Enrollment enrollment) {
        List<Map<String, Object>> clases = new ArrayList<>();
        Group group = enrollment.getGrupo();
        Subject subject = group.getMateria();
        Professor professor = group.getProfesor();

        String profesorNombre = (professor != null) ?
            professor.getNombre() + " " + professor.getLastName() : "Sin asignar";

        for (ClassSession sesion : group.getSesiones()) {
            Map<String, Object> clase = new HashMap<>();
            clase.put("materia", subject.getName());
            clase.put("codigoMateria", String.valueOf(subject.getSubjectId()));
            clase.put("creditos", subject.getCredits());
            clase.put("grupoNumero", group.getNumber());
            clase.put("profesorNombre", profesorNombre);
            clase.put("dia", sesion.getDia());
            clase.put("horaInicio", sesion.getHoraInicio());
            clase.put("horaFin", sesion.getHoraFin());
            clase.put("aula", sesion.getSalon());
            
            clases.add(clase);
        }

        return clases;
    }
}