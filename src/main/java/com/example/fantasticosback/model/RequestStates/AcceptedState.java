package com.example.fantasticosback.model.RequestStates;

import com.example.fantasticosback.model.Document.Request;

public class AcceptedState implements RequestState {

    @Override
    public void changeState(Request request, String userRole) {
        throw new IllegalStateException("The request has already been accepted. It cannot change state.");
    }

    @Override
    public String getStateName() {
        return "Accepted";
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }
}
