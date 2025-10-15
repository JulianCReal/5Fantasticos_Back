package com.example.fantasticosback.model.Observers;

import com.example.fantasticosback.model.Document.Request;

public class RequestStatusChangeObserver implements  StatusChangeObserver{
    @Override
    public void notifyStatusChange(Object object) {
        Request request = (Request) object;
        System.out.println("Request status has changed. New status: " + request.getStateName());
    }
}
