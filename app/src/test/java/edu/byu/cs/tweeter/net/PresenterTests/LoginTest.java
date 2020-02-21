package edu.byu.cs.tweeter.net.PresenterTests;

import android.util.Log;
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
import edu.byu.cs.tweeter.presenter.LoginPresenter;

public class LoginTest {

    private LoginService loginService = LoginService.getInstance();
    private LoginRequest request;
    private LoginResponse response;

    public class ViewImplementation implements LoginPresenter.View {
        @Override
        public void login(View v)
        {
            //Called in View
        }

        @Override
        public void signUp(View v)
        {
            //Called in View
        }
    }

    private LoginPresenter presenter = new LoginPresenter(new ViewImplementation()); //TODO: Check this



    @AfterEach
    void cleanUp(){
        request = null;
        response = null;

        loginService.setCurrentUser(null);
        loginService.setLoggedInUser(null);
    }

    @Test
    void testBasicLoginTestWithTestUser(){
        request = new LoginRequest("@TestUser", "password");
        response = presenter.loginUser(request);

        Assertions.assertFalse(response.isError());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), request.getUsername());

    }

    @Test
    void testInvalidUserCredentials(){
        request = new LoginRequest("NotValid", "password");
        response = presenter.loginUser(request);

        Assertions.assertTrue(response.isError());
        Assertions.assertNull(presenter.getCurrentUser());
    }

    @Test
    void testInvalidPassword(){
        request = new LoginRequest("@TestUser", "notValid");
        response = presenter.loginUser(request);

        Assertions.assertFalse(response.isError());
        Assertions.assertNotNull(presenter.getCurrentUser());
    }

    @Test
    void testLoginUserThatJustSignedUp(){
        RegisterRequest signUpRequest = new RegisterRequest("Username5", "password", "Test", "Me", null);
        RegisterResponse signUpResponse = RegisterService.getInstance().authenticateUser(signUpRequest);

        Assertions.assertFalse(signUpResponse.isError());

        Assertions.assertNotNull(presenter.getCurrentUser());
        Assertions.assertNotNull(presenter.getLoggedInUser());

        User signedInUser = presenter.getLoggedInUser();

        loginService.setLoggedInUser(null);
        loginService.setCurrentUser(null);

        request = new LoginRequest("@Username5", "password");
        response = presenter.loginUser(request);

        Assertions.assertFalse(response.isError());
        Assertions.assertNotNull(presenter.getLoggedInUser());
        Assertions.assertNotNull(presenter.getCurrentUser());
        Assertions.assertEquals(presenter.getCurrentUser(), signedInUser);
    }
}