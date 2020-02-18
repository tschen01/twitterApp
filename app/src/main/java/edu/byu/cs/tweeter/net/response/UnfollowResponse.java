package edu.byu.cs.tweeter.net.response;

public class UnfollowResponse extends Response {

    public UnfollowResponse(boolean success)
    {
        super(success);
    }

    public UnfollowResponse(boolean success, String message)
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
