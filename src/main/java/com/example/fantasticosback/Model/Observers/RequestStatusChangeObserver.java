package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Document.Request;

public class RequestStatusChangeObserver implements  StatusChangeObserver{
    @Override
    public void notifyStatusChange(Object object) {
        Request request = (Request) object;
        System.out.println("Request status has changed. New status: " + request.getStateName());
    }
}
