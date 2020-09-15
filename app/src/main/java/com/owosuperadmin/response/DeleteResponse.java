package com.owosuperadmin.response;

public class DeleteResponse {
    private boolean error;
    private String message;

    public DeleteResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
