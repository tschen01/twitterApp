package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;

public class StoryService {

    private static StoryService instance;
    private final ServerFacade serverFacade;


    public static StoryService getInstance() {
        if(instance == null) {
            instance = new StoryService();
        }

        return instance;
    }

    private StoryService() { serverFacade = ServerFacade.getInstance(); }

    public StoryResponse getStory(StoryRequest storyRequest){
        StoryResponse signUpResponse = serverFacade.getStory(storyRequest);
        return signUpResponse;
    }
}
