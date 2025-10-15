package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Document.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestCreationObserver implements CreationObserver{
    @Override
    public void notifyCreation(Object object) {
        Request request = (Request) object;
        System.out.println("A new request has been created. Type: " + request.getType().getDescription());
    }
}
