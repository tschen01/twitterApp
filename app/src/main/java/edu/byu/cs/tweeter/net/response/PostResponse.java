package edu.byu.cs.tweeter.net.response;

public class PostResponse extends Response {

    public PostResponse(boolean success)
    {
        super(success);
    }

    public PostResponse(boolean success, String message)
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
