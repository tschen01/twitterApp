package edu.byu.cs.tweeter.net.response;

public class RegisterResponse {

    private String message;
    private boolean error;

    public RegisterResponse(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

}
