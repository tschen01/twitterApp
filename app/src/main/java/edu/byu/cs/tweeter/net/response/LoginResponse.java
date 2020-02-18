package edu.byu.cs.tweeter.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class LoginResponse {

    private String message;
    private boolean error;
    private User user;

    public LoginResponse(String message, boolean error) {
        this.message = message;
        this.error = error;
        this.user = null;
    }

    public LoginResponse(String message, boolean error, User user){
        this.message = message;
        this.error = error;
        this.user = user;
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

    public void setError(boolean error) {
        this.error = error;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
