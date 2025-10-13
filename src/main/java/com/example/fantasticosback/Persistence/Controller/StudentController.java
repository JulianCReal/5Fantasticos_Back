package com.example.fantasticosback.Persistence.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Persistence.Service.StudentService;
import com.example.fantasticosback.Model.Document.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<ResponseDTO<StudentDTO>> create(@RequestBody StudentDTO dto) {
        Student newStudent = studentService.convertToDomain(dto);
        Student saved = studentService.save(newStudent);
        StudentDTO response = studentService.convertToStudentDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(response, "Student created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> list() {
        List<StudentDTO> students = studentService.convertList(studentService.findAll());
        return ResponseEntity.ok(ResponseDTO.success(students, "List of students"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> get(@PathVariable String id) {
        Student student = studentService.findById(id);
        StudentDTO dto = studentService.convertToStudentDTO(student);
        return ResponseEntity.ok(ResponseDTO.success(dto, "Student found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> update(@PathVariable String id, @RequestBody StudentDTO dto) {
        Student updated = studentService.convertToDomain(dto);
        Student saved = studentService.update(id, updated);
        StudentDTO response = studentService.convertToStudentDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(response, "Student updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Student deleted successfully"));
    }

    @GetMapping("/career/{career}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getByCareer(@PathVariable String career) {
        List<StudentDTO> students = studentService.convertList(studentService.findByCareer(career));
        return ResponseEntity.ok(ResponseDTO.success(students, "Students by career"));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getBySemester(@PathVariable int semester) {
        List<StudentDTO> students = studentService.convertList(studentService.findBySemester(semester));
        return ResponseEntity.ok(ResponseDTO.success(students, "Students by semester"));
    }

    // Endpoints para gestión de materias del estudiante

    // 1. Ver materias disponibles para inscribir
    @GetMapping("/{studentId}/available-subjects")
    public ResponseEntity<ResponseDTO<List<Object>>> getAvailableSubjects(@PathVariable String studentId) {
        List<Object> availableSubjects = studentService.getAvailableSubjectsForStudent(studentId);
        return ResponseEntity.ok(ResponseDTO.success(availableSubjects, "Available subjects retrieved"));
    }

    // 2. Ver grupos disponibles de una materia específica usando abreviatura
    @GetMapping("/{studentId}/subjects/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<List<Object>>> getAvailableGroups(
            @PathVariable String studentId,
            @PathVariable String subjectCode) {
        List<Object> availableGroups = studentService.getAvailableGroupsForSubjectByCode(studentId, subjectCode);
        return ResponseEntity.ok(ResponseDTO.success(availableGroups, "Available groups retrieved for " + subjectCode));
    }

    // 3. Inscribir materia por abreviatura y grupo (flujo mejorado)
    @PostMapping("/{studentId}/subjects/{subjectCode}/groups/{groupId}/enroll")
    public ResponseEntity<ResponseDTO<String>> enrollInSubject(
            @PathVariable String studentId,
            @PathVariable String subjectCode,
            @PathVariable String groupId) {
        boolean success = studentService.enrollStudentInSubjectGroupByCode(studentId, subjectCode, groupId);
        return ResponseEntity.ok(ResponseDTO.success("Subject enrolled successfully", "Student enrolled in " + subjectCode + " group " + groupId));
    }

    @DeleteMapping("/{studentId}/subjects/remove")
    public ResponseEntity<ResponseDTO<String>> removeSubject(
            @PathVariable String studentId,
            @RequestParam String enrollmentId) {
        boolean success = studentService.removeSubjectFromStudent(studentId, enrollmentId);
        return ResponseEntity.ok(ResponseDTO.success("Subject removed successfully", "Subject withdrawal completed"));
    }

    @PostMapping("/{studentId}/subjects/cancel")
    public ResponseEntity<ResponseDTO<String>> cancelSubject(
            @PathVariable String studentId,
            @RequestParam String enrollmentId) {
        boolean success = studentService.cancelSubjectFromStudent(studentId, enrollmentId);
        return ResponseEntity.ok(ResponseDTO.success("Subject cancelled successfully", "Subject cancellation completed"));
    }

    @GetMapping("/{studentId}/current-subjects")
    public ResponseEntity<ResponseDTO<List<Object>>> getCurrentSubjects(@PathVariable String studentId) {
        List<Object> currentSubjects = studentService.getCurrentSubjects(studentId);
        return ResponseEntity.ok(ResponseDTO.success(currentSubjects, "Current subjects retrieved"));
    }


    // Endpoint para visualizar el horario actual de un estudiante
    @GetMapping("/{studentId}/schedule")
    public ResponseEntity<ResponseDTO<List<Object>>> getStudentCurrentSchedule(@PathVariable String studentId) {
        List<Object> schedule = studentService.getCurrentSubjects(studentId);
        return ResponseEntity.ok(ResponseDTO.success(schedule, "Current schedule retrieved for student " + studentId));
    }

    // Endpoint para visualizar el horario de un semestre específico del estudiante
    @GetMapping("/{studentId}/schedule/semester/{semesterNumber}")
    public ResponseEntity<ResponseDTO<List<Object>>> getStudentScheduleBySemester(
            @PathVariable String studentId,
            @PathVariable int semesterNumber) {
        List<Object> schedule = studentService.getStudentScheduleBySemester(studentId, semesterNumber - 1);
        return ResponseEntity.ok(ResponseDTO.success(schedule, "Schedule retrieved for student " + studentId + " semester " + semesterNumber));
    }

    // Endpoint para visualizar el historial completo de horarios del estudiante
    @GetMapping("/{studentId}/schedule/history")
    public ResponseEntity<ResponseDTO<Object>> getStudentAllSchedules(@PathVariable String studentId) {
        Object allSchedules = studentService.getStudentAllSchedules(studentId);
        return ResponseEntity.ok(ResponseDTO.success(allSchedules, "Complete schedule history retrieved for student " + studentId));
    }
}
