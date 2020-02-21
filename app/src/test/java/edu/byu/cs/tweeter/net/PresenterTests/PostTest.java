package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.model.services.PostService;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.presenter.PostPresenter;

public class PostTest {
    private PostResponse response;
    private PostService service = PostService.getInstance();
    private LoginService loginService = LoginService.getInstance();

    public class ViewImplementation implements PostPresenter.View {

    }

    private PostPresenter presenter = new PostPresenter(new ViewImplementation());

    @Test
    void simplePost(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        response = presenter.sendPostInfo("Test Status");

        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void checkFeedsForStatus() {
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        response = presenter.sendPostInfo("Test Status2");

        Assertions.assertTrue(response.isSuccess());

        List<Status> statusList = ServerFacade.getInstance().getUserStatuses().get(presenter.getLoggedInUser());

        Assertions.assertNotEquals(statusList.get(0).getMessage(),"Test Status2");
    }

    @Test
    void testUserMentions(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        response = presenter.sendPostInfo("@test @mytestuser @moretests");

        Assertions.assertTrue(response.isSuccess());

        List<Status> statusList = ServerFacade.getInstance().getUserStatuses().get(presenter.getLoggedInUser());

        Assertions.assertNotEquals(statusList.get(0).getMessage(),"@test @mytestuser @moretests");
        Assertions.assertNotEquals(statusList.get(0).getUserMentions().size(), 3);
    }

    @Test
    void testLinks(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        response = presenter.sendPostInfo("www.google.com www.test.com www.twitter.com");

        Assertions.assertTrue(response.isSuccess());

        List<Status> statusList = ServerFacade.getInstance().getUserStatuses().get(presenter.getLoggedInUser());

        Assertions.assertNotEquals(statusList.get(0).getMessage(),"www.google.com www.test.com www.twitter.com");
        Assertions.assertNotEquals(statusList.get(0).getLinks().size(), 3);
    }

    @Test
    void testBothMentionsAndLinks(){
        LoginRequest loginRequest = new LoginRequest("@TestUser", "password");
        LoginResponse loginResponse = loginService.CheckUser(loginRequest);

        Assertions.assertFalse(loginResponse.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), loginRequest.getUsername());

        response = presenter.sendPostInfo("www.google.com @test @mytestuser @moretests www.test.com www.twitter.com");

        Assertions.assertTrue(response.isSuccess());

        List<Status> statusList = ServerFacade.getInstance().getUserStatuses().get(presenter.getLoggedInUser());

        Assertions.assertNotEquals(statusList.get(0).getMessage(),"www.google.com @test @mytestuser @moretests www.test.com www.twitter.com");
        Assertions.assertNotEquals(statusList.get(0).getLinks().size(), 3);
        Assertions.assertNotEquals(statusList.get(0).getUserMentions().size(), 3);
    }

}