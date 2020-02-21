package edu.byu.cs.tweeter.net.PresenterTests;

import android.view.View;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;

import edu.byu.cs.tweeter.model.services.RegisterService;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;

public class SignUpTest {

    private RegisterService service = RegisterService.getInstance();
    private LoginService loginService = LoginService.getInstance();
    private RegisterRequest request;
    private RegisterResponse response;

    public class ViewImplementation implements SignUpPresenter.View {
        @Override
        public void register(View v)
        {

        }
    }

    private SignUpPresenter presenter = new SignUpPresenter(new ViewImplementation());


    @AfterEach
    void cleanUp(){
        request = null;
        response = null;

        loginService.setCurrentUser(null);
        loginService.setLoggedInUser(null);
    }

    @Test
    void testSignUpNewUser(){
        request = new RegisterRequest("Username", "password", "Test", "SignUP", null);
        response = presenter.signUpUser(request);
        User signedUpUser = presenter.getUserByAlias("@Username");

        Assertions.assertNotNull(signedUpUser);
        Assertions.assertFalse(response.isError());
    }

    @Test
    void testSignUpAndLogIn(){
        request = new RegisterRequest("Username2", "password", "Test", "SignUP", null);
        response = presenter.signUpUser(request);

        Assertions.assertFalse(response.isError());

        Assertions.assertNotNull(presenter.getLoggedInUser());
        Assertions.assertNotNull(presenter.getCurrentUser());
        Assertions.assertEquals(presenter.getCurrentUser().getAlias(), "@Username2");
    }


    @Test
    void testSignUpWithErrors(){
        request = new RegisterRequest("Username3", null, "Test", "SignUP", null);
        response = presenter.signUpUser(request);
        Assertions.assertTrue(response.isError());

        User signedUpUser = presenter.getUserByAlias("@Username3");
        Assertions.assertNull(signedUpUser);
    }

    @Test
    void signUpAlreadyExistingUser(){
        request = new RegisterRequest("Username", "password", "Test", "SignUP", null);
        response = presenter.signUpUser(request);

        Assertions.assertTrue(response.isError());
        User signedUpUser = presenter.getUserByAlias("@Username");
        Assertions.assertNotNull(signedUpUser);
        Assertions.assertEquals(signedUpUser.getAlias(), "@Username");
    }
}