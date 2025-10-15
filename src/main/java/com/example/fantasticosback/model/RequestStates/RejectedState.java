package com.example.fantasticosback.model.RequestStates;

import com.example.fantasticosback.model.Document.Request;

public class RejectedState implements RequestState {

    @Override
    public void changeState(Request request, String userRole) {
        throw new IllegalStateException("The request was rejected. It cannot change state.");
    }

    @Override
    public String getStateName() {
        return "Rejected";
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }
}
