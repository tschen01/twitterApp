package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FeedService;
import edu.byu.cs.tweeter.model.services.FollowerService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.model.services.PostService;
import edu.byu.cs.tweeter.model.services.RegisterService;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;

public class FeedTest {
    private LoginService loginService = LoginService.getInstance();

    public class ViewImplementation implements FeedPresenter.View {

    }

    private FeedPresenter presenter = new FeedPresenter(new ViewImplementation());

    @Test
    void testAtLoginEveryoneHasFeed(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        Map<User, List<Status>> userFeeds = ServerFacade.getInstance().getUserFeeds();

        for (Map.Entry<User, List<Status>> entry : userFeeds.entrySet()) {
            List<Status> statusList = entry.getValue();
            if(entry.getKey().getAlias().equals("@TweeterBot")){
                Assertions.assertEquals(statusList.size(), 0);
            }
            else {
                Assertions.assertTrue(true);
            }
        }
    }

    @Test
    void testAtSignUpEveryoneHasFeed(){
        RegisterRequest signUpRequest = new RegisterRequest("Username1", "password", "Test", "SignUP", null);
        RegisterResponse signUpResponse = RegisterService.getInstance().authenticateUser(signUpRequest);
        User signedUpUser = presenter.getUserByAlias("@Username1");

        Assertions.assertNotNull(signedUpUser);
        Assertions.assertFalse(signUpResponse.isError());

        FeedResponse feedResponse = presenter.getFeed(new FeedRequest(signedUpUser, 100000, null));
        Assertions.assertTrue(feedResponse.isSuccess());
        List<Status> statusList = feedResponse.getStatuses();

        Assertions.assertNotEquals(statusList.size(), 0);

    }

    @Test
    void newPostIsOnPeoplesFeeds(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        PostResponse postResponse = PostService.getInstance().postStatus("Test Status2");
        Assertions.assertTrue(postResponse.isSuccess());

        List<User> followers = FollowerService.getInstance().getFollowers(new FollowerRequest(presenter.getLoggedInUser(), 10000, null)).getFollowers();

        for (User follower: followers) {
            FeedResponse feedResponse = presenter.getFeed(new FeedRequest(follower, 100000, null));
            Assertions.assertTrue(feedResponse.isSuccess());
            List<Status> feed = feedResponse.getStatuses();
            Assertions.assertNotEquals(feed.get(0).getMessage(), "Welcome to Twitter");
        }
    }

}