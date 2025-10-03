package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Request;

public class DeanRequestObserver implements RequestObserver {

    @Override
    public void notifyRequest(Request request) {
        System.out.println("Se ha creado una nueva solicitud");
    }
}
