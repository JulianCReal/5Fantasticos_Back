package com.example.fantasticosback.model.RequestCreatrors;

import com.example.fantasticosback.dto.request.RequestDTO;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.service.DeanService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.service.TeacherService;
import com.example.fantasticosback.enums.RequestPriority;
import com.example.fantasticosback.enums.RequestType;
import com.example.fantasticosback.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RequestCreator {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DeanService deanService;

    public abstract Request createRequest(RequestDTO requestDTO);
    public abstract RequestType getRequestType(RequestType requestType);

    public RequestPriority assignPriority(RequestDTO requestDTO) {
        switch (requestDTO.getRequestType()) {
            case CHANGE_GROUP:
                return RequestPriority.MEDIUM;
            case JOIN_GROUP, SPECIAL:
                return RequestPriority.HIGH;
            case LEAVE_GROUP:
                return RequestPriority.LOW;
            default:
                return RequestPriority.LOW;
        }
    }
    public Role assignCreationRole(RequestDTO requestDTO) {
        if(studentService.findById(requestDTO.getUserId()) != null){
            return Role.STUDENT;
        } else if(teacherService.findById(requestDTO.getUserId()) != null){
            return Role.TEACHER;
        } else if (deanService.getById(requestDTO.getUserId()) != null){
            return Role.DEAN;
        }
        return null;
    }
}
