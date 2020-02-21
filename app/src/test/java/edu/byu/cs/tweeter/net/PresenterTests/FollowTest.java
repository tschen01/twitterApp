package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowerService;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.model.services.RegisterService;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.FollowPresenter;
import edu.byu.cs.tweeter.presenter.FollowerPresenter;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class FollowTest {
    private FollowResponse response;
    private FollowingService service = FollowingService.getInstance();
    private LoginService loginService = LoginService.getInstance();

    public class ViewImplementation implements MainPresenter.View, FollowPresenter.View {
        @Override
        public void signOut()
        {

        }

        @Override
        public void goToPostActivity()
        {

        }

        @Override
        public void followUser(View v)
        {

        }
    }

    private MainPresenter presenter = new MainPresenter(new ViewImplementation());
    private FollowPresenter followPresenter = new FollowPresenter(new FollowPresenter.View() {
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    });

    @Test
    void testFollow(){
        RegisterRequest signUpRequest = new RegisterRequest("Username6", "password", "Test", "SignUP", null);
        RegisterResponse signUpResponse = RegisterService.getInstance().authenticateUser(signUpRequest);
        User signedUpUser = presenter.getUserByAlias("@Username6");

        Assertions.assertNotNull(signedUpUser);
        Assertions.assertFalse(signUpResponse.isError());


        List<User> following = ServerFacade.getInstance().getFollowing(new FollowingRequest(presenter.getLoggedInUser(), 1000, null)).getFollowees();

        Assertions.assertEquals(following.size(), 1);
        loginService.setCurrentUser(presenter.getUserByAlias("@TestUser"));
        response = followPresenter.followUser(new Follow(presenter.getLoggedInUser(), presenter.getCurrentUser()));

        Assertions.assertTrue(response.isSuccess());

        FollowingResponse followingResponse = service.getFollowees(new FollowingRequest(presenter.getLoggedInUser(), 1000, null));
        Assertions.assertTrue(followingResponse.isSuccess());
        following = followingResponse.getFollowees();

        Assertions.assertEquals(following.size(), 2);
        Assertions.assertEquals(following.get(1), presenter.getCurrentUser());

        FollowerResponse followerResponse = FollowerService.getInstance().getFollowers(new FollowerRequest(presenter.getCurrentUser(), 1000, null));
        Assertions.assertTrue(followerResponse.isSuccess());
        List<User> followers = followerResponse.getFollowers();

        Assertions.assertTrue(followers.contains(presenter.getLoggedInUser()));
    }

}