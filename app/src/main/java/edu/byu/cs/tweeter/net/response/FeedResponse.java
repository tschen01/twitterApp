package edu.byu.cs.tweeter.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedResponse extends PagedResponse  {
    private List<Status> statuses;
    private List<User> following;

    public FeedResponse(boolean success, String message, boolean hasMorePages, List<Status> statuses, List<User> users)
    {
        super(success, message, hasMorePages);
        this.statuses = statuses;
        this.following = users;
    }

    public List<Status> getStatuses()
    {
        return statuses;
    }

    public void setStatuses(List<Status> statuses)
    {
        this.statuses = statuses;
    }

    public List<User> getFollowing() {
        return following;
    }
}
