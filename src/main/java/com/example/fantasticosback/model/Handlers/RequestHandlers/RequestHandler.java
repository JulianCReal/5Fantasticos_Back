package com.example.fantasticosback.model.Handlers.RequestHandlers;

import com.example.fantasticosback.model.Document.Request;

public abstract class RequestHandler {
    public abstract boolean handle(Request request);

    public boolean response(Request request){
        return handle(request);
    }
}
