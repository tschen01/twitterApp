package edu.byu.cs.tweeter.net.PresenterTests;

import android.util.Log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.model.services.StoryService;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

public class UserFeedTest {
    private LoginService loginService = LoginService.getInstance();


    public class ViewImplementation implements StoryPresenter.View {

    }

    private StoryPresenter presenter = new StoryPresenter(new ViewImplementation());

    @Test
    void viewOtherUserStory(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        List<User> following = ServerFacade.getInstance().getFollowing(new FollowingRequest(presenter.getLoggedInUser(), 1000, null)).getFollowees();
        StoryResponse response = presenter.getStory(new StoryRequest(presenter.getLoggedInUser(), 10, null));
        Assertions.assertFalse(response.isError());
        List<Status> storyUserLoggedIn = response.getStatusList();

        loginService.setCurrentUser(following.get(0));

        response = presenter.getStory(new StoryRequest(presenter.getCurrentUser(), 10, null));
        Assertions.assertFalse(response.isError());
        List<Status> storyOtherUser = response.getStatusList();

        Assertions.assertNotEquals(storyOtherUser, storyUserLoggedIn);

        for (Status status: storyOtherUser) {
            Assertions.assertEquals(status.getUser(), presenter.getCurrentUser());
        }
    }
}