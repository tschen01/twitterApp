package edu.byu.cs.tweeter.net.request;

public class RegisterRequest {
    private String firstName;
    private String lastName;

    public RegisterRequest(String firstName, String lastName) {
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
