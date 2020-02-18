package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;

public class FeedService {

    private static FeedService instance;
    private final ServerFacade serverFacade;


    public static FeedService getInstance() {
        if(instance == null) {
            instance = new FeedService();
        }

        return instance;
    }

    private FeedService() { serverFacade = ServerFacade.getInstance(); }

    public FeedResponse getFeed(FeedRequest feedRequest) {
        ServerFacade server = ServerFacade.getInstance();
        FeedResponse feedResponse = server.getFeed(feedRequest);
        return feedResponse;
    }

}
