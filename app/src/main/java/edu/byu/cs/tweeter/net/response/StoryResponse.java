package edu.byu.cs.tweeter.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryResponse extends PagedResponse {

    private String message;
    private List<Status> statusList;
    private boolean error;

    public StoryResponse(String message, List<Status> statusList, boolean error, boolean hasMorePages) {
        super(true, hasMorePages);

        this.message = message;
        this.statusList = statusList;
        this.error = error;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<Status> getStatusList()
    {
        return statusList;
    }

    public void setStatusList(List<Status> statusList)
    {
        this.statusList = statusList;
    }

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }
}
