package com.example.fantasticosback.controller;

import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.service.EnrollmentService;
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
    public ResponseEntity<Enrollment> enrollStudent(
            @PathVariable String studentId,
            @PathVariable int groupId,
            @RequestParam String semester) {

        Enrollment enrollment = enrollmentService.enrollStudentInGroup(studentId, groupId, semester);
        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/students/{studentId}/enrollments/{enrollmentId}")
    public ResponseEntity<Void> cancelEnrollment(
            @PathVariable String studentId,
            @PathVariable String enrollmentId) {

        enrollmentService.cancelEnrollment(studentId, enrollmentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(
            @PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<Enrollment>> getGroupEnrollments(
            @PathVariable String groupId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByGroupId(groupId));
    }

    @PutMapping("/{enrollmentId}/grade")
    public ResponseEntity<Enrollment> updateGrade(
            @PathVariable String enrollmentId,
            @RequestParam double grade) {

        Enrollment enrollment = enrollmentService.updateGrade(enrollmentId, grade);
        return ResponseEntity.ok(enrollment);
    }
}
