package com.example.fantasticosback.Model.Observers;


import com.example.fantasticosback.Model.Request;

public class StudentRequestObserver implements RequestObserver {
    @Override
    public void notifyRequest(Request request) {
        System.out.println("Tu solicitud ha sido modificada a: " + request.getState().getStateName());
    }
}
