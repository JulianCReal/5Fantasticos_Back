package com.example.fantasticosback.controller;

import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.service.GroupService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final StudentService studentService;

    public GroupController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @PutMapping("/{groupId}/teacher/{teacherId}")
    public ResponseEntity<Group> assignTeacher(
            @PathVariable String groupId,
            @PathVariable String teacherId) {
        return ResponseEntity.ok(groupService.assignTeacher(groupId, teacherId));
    }

    @PostMapping("/{groupId}/sessions")
    public ResponseEntity<Group> addSession(
            @PathVariable String groupId,
            @RequestBody ClassSession session) {
        return ResponseEntity.ok(groupService.addSession(groupId, session));
    }

    @GetMapping("/{groupId}/schedule")
    public ResponseEntity<List<ClassSession>> getGroupSchedule(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroupSchedule(groupId));
    }

    @PutMapping("/{groupId}/students")
    public ResponseEntity<?> addStudent(
            @PathVariable String groupId,
            @RequestBody Student student) {
        groupService.addStudentToGroup(groupId, student);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<?> removeStudent(
            @PathVariable String groupId,
            @PathVariable String studentId) {
        Student student = studentService.findById(studentId);
        groupService.removeStudentFromGroup(groupId, student);
        return ResponseEntity.ok().build();
    }
}
