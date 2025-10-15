package com.example.fantasticosback.Model.Handlers.RequestHandlers;

import com.example.fantasticosback.Model.Document.Request;

public abstract class RequestHandler {
    public abstract boolean handle(Request request);

    public boolean response(Request request){
        return handle(request);
    }
}
