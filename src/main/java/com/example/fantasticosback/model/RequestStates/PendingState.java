package com.example.fantasticosback.model.RequestStates;

import com.example.fantasticosback.model.Document.Request;

public class PendingState implements RequestState {

    @Override
    public void changeState(Request request, String userRole) {
        if (!userRole.equals("DeanOffice") && !userRole.equals("Administrative")) {
            throw new IllegalStateException("Only dean office or administrative staff can move a request to 'In Process'");
        }
        request.setState(new ProcessState());
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
