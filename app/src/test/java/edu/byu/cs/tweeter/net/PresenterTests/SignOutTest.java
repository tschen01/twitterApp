package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.model.services.RegisterService;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class SignOutTest {
    private SignOutResponse response;
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


    @AfterEach
    void cleanUp(){
        loginService.setLoggedInUser(null);
        loginService.setCurrentUser(null);
    }

    @Test
    void loginAndLogout() {
        LoginRequest request = new LoginRequest("@TestUser", "password");
        LoginResponse response = loginService.CheckUser(request);

        Assertions.assertFalse(response.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), request.getUsername());

        loginService.setCurrentUser(null);
        loginService.setLoggedInUser(null);

        Assertions.assertNull(presenter.getCurrentUser());
        Assertions.assertNull(presenter.getLoggedInUser());
    }

    @Test
    void signUpAndLogout(){
        RegisterRequest request = new RegisterRequest("Username9", "password", "Test", "User", null);
        RegisterResponse signUpResponse = RegisterService.getInstance().authenticateUser(request);
        User signedUpUser = presenter.getUserByAlias("@Username9");

        Assertions.assertNotNull(signedUpUser);
        Assertions.assertFalse(signUpResponse.isError());

        loginService.setLoggedInUser(null);
        loginService.setCurrentUser(null);

        signedUpUser = presenter.getUserByAlias("@Username9");

        Assertions.assertNotNull(signedUpUser);
        Assertions.assertNull(presenter.getCurrentUser());
        Assertions.assertNull(presenter.getLoggedInUser());
    }

}