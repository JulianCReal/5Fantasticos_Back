package com.example.fantasticosback.Model.RequestStates;

import com.example.fantasticosback.Model.Document.Request;

public interface RequestState {
    void changeState(Request request, String userRole);
    String getStateName();
    boolean canBeDeleted();
}
