package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Document.Request;

public interface RequestObserver {
    void notifyRequest(Request request);
}
