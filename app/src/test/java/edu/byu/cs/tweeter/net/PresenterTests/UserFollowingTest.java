package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowerService;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.model.services.StoryService;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.FollowerPresenter;
import edu.byu.cs.tweeter.presenter.FollowingPresenter;

public class UserFollowingTest {

    private LoginService loginService = LoginService.getInstance();

    public class ViewImplementation implements FollowingPresenter.View {

    }

    private FollowingPresenter presenter = new FollowingPresenter(new ViewImplementation());


    @Test
    void viewOtherUserFollowings(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        FollowingResponse response = presenter.getFollowing(new FollowingRequest(presenter.getLoggedInUser(), 1000, null));
        Assertions.assertTrue(response.isSuccess());

        List<User> following = response.getFollowees();
        loginService.setCurrentUser(following.get(0));


        response = presenter.getFollowing(new FollowingRequest(presenter.getCurrentUser(), 1000, null));
        Assertions.assertTrue(response.isSuccess());

        List<User> followingOtherUser = response.getFollowees();

        Assertions.assertNotEquals(following, followingOtherUser);

        for (User user: followingOtherUser) {
            Assertions.assertNotEquals(user.getAlias(), presenter.getCurrentUser());
        }
    }
}