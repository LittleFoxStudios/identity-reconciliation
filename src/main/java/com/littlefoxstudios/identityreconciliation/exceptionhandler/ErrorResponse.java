package com.littlefoxstudios.identityreconciliation.exceptionhandler;

public class ErrorResponse {
    private String message;
    private String details;
    private int statusCode;

    public ErrorResponse(String message, String details, int statusCode) {
        this.message = message;
        this.details = details;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
