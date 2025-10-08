package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Entities.Request;

public interface RequestObserver {
    void notifyRequest(Request request);
}
