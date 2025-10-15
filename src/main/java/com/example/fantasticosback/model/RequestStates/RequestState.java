package com.example.fantasticosback.model.RequestStates;

import com.example.fantasticosback.model.Document.Request;

public interface RequestState {
    void changeState(Request request, String userRole);
    String getStateName();
    boolean canBeDeleted();
}
