package com.example.fantasticosback.model.RequestStates;

import com.example.fantasticosback.enums.Role;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Request;

public class ProcessState implements RequestState {

    @Override
    public void changeState(Request request, Role userRole) {
        if (userRole.equals(Role.DEAN) || userRole.equals(Role.DEAN_OFFICE)) {
            boolean evaluation = request.getEvaluationApproved();
            if (evaluation) {
                request.setState(new AcceptedState());
            } else {
                request.setState(new RejectedState());
            }
        } else {
            throw new BusinessValidationException("Only dean office or administrative staff can move a request from 'In Process' to 'Accepted' or 'Rejected'");
        }
    }

    @Override
    public String getStateName() {
        return "In Process";
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }
}