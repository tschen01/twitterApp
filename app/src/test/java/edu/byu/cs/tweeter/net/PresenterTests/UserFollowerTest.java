package edu.byu.cs.tweeter.net.PresenterTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowerService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.FollowerPresenter;

public class UserFollowerTest {

    private LoginService loginService = LoginService.getInstance();

    public class ViewImplementation implements FollowerPresenter.View {

    }

    private FollowerPresenter presenter = new FollowerPresenter(new ViewImplementation());

    @Test
    void viewOtherUserFollowers(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        FollowerResponse response = presenter.getFollowing(new FollowerRequest(presenter.getLoggedInUser(), 1000, null));
        Assertions.assertTrue(response.isSuccess());

        List<User> followers = response.getFollowers();
        loginService.setCurrentUser(followers.get(0));


        response = presenter.getFollowing(new FollowerRequest(presenter.getCurrentUser(), 1000, null));
        Assertions.assertTrue(response.isSuccess());

        List<User> followersOtherUser = response.getFollowers();

        Assertions.assertNotEquals(followers, followersOtherUser);
        Assertions.assertNotEquals(followersOtherUser.size(), 0);


        for (User user: followersOtherUser) {
            Assertions.assertNotEquals(user.getAlias(), loginService.getCurrentUser());
        }
    }
}