package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;

import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.UnFollowPresenter;

public class UnfollowTest {
    private UnfollowResponse response;
    private FollowingService service = FollowingService.getInstance();
    private LoginService loginService = LoginService.getInstance();

    public class ViewImplementation implements MainPresenter.View {
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
    private UnFollowPresenter unFollowPresenter = new UnFollowPresenter((new UnFollowPresenter.View() {
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }));


    @Test
    void testUnfollow(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());


        List<User> following = ServerFacade.getInstance().getFollowing(new FollowingRequest(presenter.getLoggedInUser(), 1000, null)).getFollowees();
        int size = following.size();
        User userToUnfollow = presenter.getUserByAlias(following.get(0).getAlias());

        Assertions.assertNotEquals(following.size(), 0);

        loginService.setCurrentUser(userToUnfollow);

        response = unFollowPresenter.unfollowUser(new Follow(presenter.getLoggedInUser(), presenter.getCurrentUser()));

        Assertions.assertTrue(response.isSuccess());
        following = ServerFacade.getInstance().getFollowing(new FollowingRequest(loginService.getLoggedInUser(), 1000, null)).getFollowees();

        Assertions.assertEquals(following.size(), size - 1);
        Assertions.assertNotEquals(following.get(0), presenter.getCurrentUser());

        List<User> followers = ServerFacade.getInstance().getFollowers(new FollowerRequest(presenter.getCurrentUser(), 1000, null)).getFollowers();

        Assertions.assertFalse(followers.contains(presenter.getLoggedInUser()));

    }


}