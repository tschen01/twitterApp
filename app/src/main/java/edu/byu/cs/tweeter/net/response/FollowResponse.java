package edu.byu.cs.tweeter.net.response;

public class FollowResponse extends Response {

    public FollowResponse(boolean success)
    {
        super(success);
    }

    public FollowResponse(boolean success, String message)
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
