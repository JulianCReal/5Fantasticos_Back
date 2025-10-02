package com.example.fantasticosback.Dtos;

public class ResponseDTO<T> {
    private String status;
    private String message;
    private T data;

    public ResponseDTO() {}

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

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public T getData() { return data; }

    public void setStatus(String status) { this.status = status; }

    public void setMessage(String message) { this.message = message; }

    public void setData(T data) { this.data = data; }
}
