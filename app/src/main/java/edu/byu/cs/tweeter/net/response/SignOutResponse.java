package edu.byu.cs.tweeter.net.response;

public class SignOutResponse extends Response {

    public SignOutResponse(boolean success)
    {
        super(success);
    }

    public SignOutResponse(boolean success, String message)
    {
        super(success, message);
    }

    @Override
    public boolean isSuccess()
    {
        return super.isSuccess();
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
