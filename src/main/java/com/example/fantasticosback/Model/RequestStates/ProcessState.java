package com.example.fantasticosback.Model.RequestStates;

import com.example.fantasticosback.Model.Entities.Request;

public class ProcessState implements RequestState {

    @Override
    public void changeState(Request request, String userRole) {
        if (!userRole.equals("DeanOffice") && !userRole.equals("Administrative")) {
            throw new IllegalStateException("Only dean office or administrative staff can accept or reject requests.");
        }
        boolean evaluation = request.getEvaluationApproved();

        if (evaluation) {
            request.setState(new AcceptedState());
        } else {
            request.setState(new RejectedState());
        }
    }

    @Override
    public String getStateName() {
        return "In Process";
    }
}
