package com.example.fantasticosback.model.RequestStates;

import com.example.fantasticosback.enums.Role;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Request;

public class PendingState implements RequestState {

    @Override
    public void changeState(Request request, Role userRole) {
        if (userRole.equals(Role.DEAN) || userRole.equals(Role.DEAN_OFFICE)) {
            request.setState(new ProcessState());
        }
        throw new BusinessValidationException("Only dean office or administrative staff can move a request to 'In Process'");
    }

    @Override
    public String getStateName() {
        return "Pending";
    }

    @Override
    public boolean canBeDeleted() {
        return true;
    }
}
