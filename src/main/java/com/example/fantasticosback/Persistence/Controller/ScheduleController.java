package com.example.fantasticosback.Persistence.Controller;

import com.example.fantasticosback.Persistence.Server.ScheduleService;
import com.example.fantasticosback.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private static final Logger log = Logger.getLogger(ScheduleController.class.getName());

    @Autowired
    private ScheduleService scheduleService;


    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentSchedule(
            @PathVariable String studentId,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String userRoleStr) {

        try {

            UserRole userRole;
            try {
                userRole = UserRole.valueOf(userRoleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warning("Invalid user role: " + userRoleStr);
                return ResponseEntity.badRequest()
                    .body("Invalid user role. Valid roles: STUDENT, TEACHER, DEAN_OFFICE");
            }

            log.info("Schedule query - Student: " + studentId +
                    ", User: " + userId + ", Role: " + userRole);

            Map<String, Object> schedule = scheduleService.getStudentSchedule(studentId, userId, userRole);
            return ResponseEntity.ok(schedule);

        } catch (IllegalArgumentException e) {
            log.warning("Invalid argument: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (IllegalAccessException e) {
            log.warning("Access denied: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Internal error when querying schedule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
        }
    }



    @GetMapping("/my-schedule/{studentId}")
    public ResponseEntity<?> getMySchedule(@PathVariable String studentId) {
        try {
            log.info("Own schedule query - Student: " + studentId);

            Map<String, Object> schedule = scheduleService.getStudentSchedule(
                studentId, studentId, UserRole.STUDENT);
            return ResponseEntity.ok(schedule);

        } catch (IllegalArgumentException e) {
            log.warning("Student not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Internal error when querying schedule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
        }
    }
}