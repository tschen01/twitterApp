package edu.byu.cs.tweeter.net.request;

public class LoginRequest {
    private String firstName;
    private String lastName;

    public LoginRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
}
