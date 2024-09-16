package com.core.api.domain.models;

import org.springframework.http.HttpStatus;

public class Response<T> {
    private final HttpStatus status;
    private final String message;
    private final T data;
    public Response() {
        this.status = null;
        this.message = null;
        this.data = null;
    }

    public Response(HttpStatus status) {
        this.status = status;
        this.message = null;
        this.data = null;
    }

    public Response(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public Response(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }



    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}