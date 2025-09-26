package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Repository.TeacherRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.SesionClase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class HorarioService {

    private static final Logger log = Logger.getLogger(HorarioService.class.getName());

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DecanaturaRepository decanaturaRepository;


    public Map<String, Object> obtenerHorarioEstudiante(String estudianteId, String usuarioId, UserRole userRole) 
            throws IllegalAccessException, IllegalArgumentException {
        
        log.info("Consultando horario para estudiante: " + estudianteId + " por usuario: " + usuarioId + " con rol: " + userRole);


        Estudiante estudiante = studentRepository.findById(estudianteId).orElse(null);
        if (estudiante == null) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId);
        }

        switch (userRole) {
            case ESTUDIANTE:
                validarAccesoEstudiante(estudianteId, usuarioId);
                break;
            case PROFESOR:
                validarAccesoProfesor(estudiante, usuarioId);
                break;
            case DECANATURA:
                validarAccesoDecanatura(estudiante, usuarioId);
                break;
            default:
                throw new IllegalAccessException("Rol de usuario no válido");
        }

        return generarHorario(estudiante);
    }


    private void validarAccesoEstudiante(String estudianteId, String usuarioId) throws IllegalAccessException {
        if (!estudianteId.equals(usuarioId)) {
            log.warning("Acceso denegado: Estudiante " + usuarioId + " intentó consultar horario de " + estudianteId);
            throw new IllegalAccessException("Los estudiantes solo pueden consultar su propio horario");
        }
    }


    private void validarAccesoProfesor(Estudiante estudiante, String profesorId) throws IllegalAccessException {
        Profesor profesor = teacherRepository.findById(profesorId).orElse(null);
        if (profesor == null) {
            throw new IllegalArgumentException("Profesor no encontrado con ID: " + profesorId);
        }

        boolean tieneEstudiante = false;
        
        for (Semestre semestre : estudiante.getSemestres()) {
            for (Inscripcion inscripcion : semestre.getMaterias()) {
                Grupo grupo = inscripcion.getGrupo();
                if (grupo.getProfesor() != null && profesorId.equals(grupo.getProfesor().getId())) {
                    tieneEstudiante = true;
                    break;
                }
            }
            if (tieneEstudiante) break;
        }

        if (!tieneEstudiante) {
            log.warning("Acceso denegado: Profesor " + profesorId + " no tiene asignado al estudiante " + estudiante.getIdEstudiante());
            throw new IllegalAccessException("Los profesores solo pueden consultar el horario de sus estudiantes asignados");
        }
    }


    private void validarAccesoDecanatura(Estudiante estudiante, String decanaturaId) throws IllegalAccessException {
        Decanatura decanatura = decanaturaRepository.findById(decanaturaId).orElse(null);
        if (decanatura == null) {
            throw new IllegalArgumentException("Decanatura no encontrada con ID: " + decanaturaId);
        }


        if (!estudiante.getCarrera().equals(decanatura.getFacultad())) {
            log.warning("Acceso denegado: Decanatura " + decanaturaId + " intentó consultar estudiante de otra facultad");
            throw new IllegalAccessException("La decanatura solo puede consultar estudiantes de su facultad");
        }
    }


    private Map<String, Object> generarHorario(Estudiante estudiante) {
        List<Map<String, Object>> clases = new ArrayList<>();

        Semestre semestreActual = null;
        for (Semestre semestre : estudiante.getSemestres()) {
            if (semestre.isEstado()) { // Asumiendo que hay un método isEstado() o similar
                semestreActual = semestre;
                break;
            }
        }

        if (semestreActual == null && !estudiante.getSemestres().isEmpty()) {
            semestreActual = estudiante.getSemestres().get(estudiante.getSemestres().size() - 1);
        }

        if (semestreActual != null) {
            for (Inscripcion inscripcion : semestreActual.getMaterias()) {
                if (!"cancelada".equals(inscripcion.getEstado())) {
                    clases.addAll(generarClasesDeInscripcion(inscripcion));
                }
            }
        }

        Map<String, Object> horario = new HashMap<>();
        horario.put("estudianteId", estudiante.getIdEstudiante());
        horario.put("estudianteNombre", estudiante.getNombre() + " " + estudiante.getApellido());
        horario.put("carrera", estudiante.getCarrera());
        horario.put("semestre", estudiante.getSemestre());
        horario.put("clases", clases);

        return horario;
    }


    private List<Map<String, Object>> generarClasesDeInscripcion(Inscripcion inscripcion) {
        List<Map<String, Object>> clases = new ArrayList<>();
        Grupo grupo = inscripcion.getGrupo();
        Materia materia = grupo.getMateria();
        Profesor profesor = grupo.getProfesor();

        String profesorNombre = (profesor != null) ? 
            profesor.getNombre() + " " + profesor.getApellido() : "Sin asignar";

        for (SesionClase sesion : grupo.getSesiones()) {
            Map<String, Object> clase = new HashMap<>();
            clase.put("materia", materia.getNombre());
            clase.put("codigoMateria", String.valueOf(materia.getIdMateria()));
            clase.put("creditos", materia.getCreditos());
            clase.put("grupoNumero", grupo.getNumero());
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