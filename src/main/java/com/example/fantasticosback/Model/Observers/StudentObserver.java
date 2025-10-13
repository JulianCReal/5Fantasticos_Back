package com.example.fantasticosback.Model.Observers;


import com.example.fantasticosback.Model.Document.Request;

public class StudentObserver implements RequestObserver {
    @Override
    public void notifyRequest(Request request) {
        System.out.println("Your request has been modified to: " + request.getState().getStateName());
    }
}
