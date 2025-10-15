package com.example.fantasticosback.dto.response;

import lombok.Data;

@Data
public class ResponseDTO<T> {
    private String status;
    private String message;
    private T data;

    public ResponseDTO(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDTO<T> success(T data, String message) {
        return new ResponseDTO<>("Success", message, data);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>("Error", message, null);
    }
}
