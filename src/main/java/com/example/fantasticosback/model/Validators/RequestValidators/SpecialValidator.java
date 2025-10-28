package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.model.Document.Request;
import org.springframework.stereotype.Component;

@Component
public class SpecialValidator extends RequestValidator {
    @Override
    public boolean handle(Request request) {
        return false;
    }
}
