package edu.byu.cs.tweeter.net.request;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest {

    private User user;
    private final int limit;
    private Status lastStatus;


    public StoryRequest(User user, int limit, Status lastStatus){
        this.user = user;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public StoryRequest(User user){
        this.user = user;
        this.limit = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLimit() {
        return limit;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
