package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Document.Request;

public class DeanObserver implements RequestObserver{

    @Override
    public void notifyRequest(Request request) {
        System.out.println("A new request has been created");
    }
}
