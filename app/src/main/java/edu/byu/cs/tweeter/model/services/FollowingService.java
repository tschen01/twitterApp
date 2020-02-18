package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;

public class FollowingService {

    private static FollowingService instance;

    private final ServerFacade serverFacade;

    public static FollowingService getInstance() {
        if(instance == null) {
            instance = new FollowingService();
        }

        return instance;
    }

    private FollowingService() {
        serverFacade = ServerFacade.getInstance();
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        return serverFacade.getFollowing(request);
    }

    public UnfollowResponse unfollowUser(Follow follow){
        return serverFacade.unfollowUser(follow);
    }

    public FollowResponse followUser(Follow follow){
        return serverFacade.followUser(follow);
    }

    public boolean isFollowing(Follow follow){
        return serverFacade.isFollowing(follow);
    }
}
