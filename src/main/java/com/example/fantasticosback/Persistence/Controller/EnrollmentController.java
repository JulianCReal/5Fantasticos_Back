package com.example.fantasticosback.Persistence.Controller;

import com.example.fantasticosback.Model.Entities.Enrollment;
import com.example.fantasticosback.Persistence.Server.EnrollmentService;
import com.example.fantasticosback.Exception.BusinessValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/students/{studentId}/groups/{groupId}")
    public ResponseEntity<?> enrollStudent(
            @PathVariable String studentId,
            @PathVariable int groupId,
            @RequestParam String semester) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudentInGroup(studentId, groupId, semester);
            return ResponseEntity.ok(enrollment);
        } catch (BusinessValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/students/{studentId}/enrollments/{enrollmentId}")
    public ResponseEntity<?> cancelEnrollment(
            @PathVariable String studentId,
            @PathVariable String enrollmentId) {
        try {
            enrollmentService.cancelEnrollment(studentId, enrollmentId);
            return ResponseEntity.ok().build();
        } catch (BusinessValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(
            @PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<Enrollment>> getGroupEnrollments(
            @PathVariable int groupId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByGroupId(groupId));
    }

    @PutMapping("/{enrollmentId}/grade")
    public ResponseEntity<?> updateGrade(
            @PathVariable String enrollmentId,
            @RequestParam double grade) {
        try {
            Enrollment enrollment = enrollmentService.updateGrade(enrollmentId, grade);
            return ResponseEntity.ok(enrollment);
        } catch (BusinessValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
