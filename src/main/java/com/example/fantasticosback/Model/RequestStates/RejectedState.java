package com.example.fantasticosback.Model.RequestStates;

import com.example.fantasticosback.Model.Request;

public class RejectedState implements RequestState {

    @Override
    public void changeState(Request request, String userRole) {
        throw new IllegalStateException("The request was rejected. It cannot change state.");
    }

    @Override
    public String getStateName() {
        return "Rejected";
    }
}
