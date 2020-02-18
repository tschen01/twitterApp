package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;

public class LoginService {

    private static LoginService instance;
    private final ServerFacade serverFacade;
    private User currentUser;
    private User loggedInUser;

    public static LoginService getInstance() {
        if(instance == null) {
            instance = new LoginService();
        }

        return instance;
    }

    private LoginService() {serverFacade = ServerFacade.getInstance();}

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest){
        LoginResponse loginResponse = serverFacade.authenticateUser(loginRequest);
        if (loginResponse.isError()){
            return loginResponse;
        }
        else {
            currentUser = loginResponse.getUser();
            setCurrentUser(currentUser);
            setLoggedInUser(currentUser);
            return loginResponse;
        }
    }

    public User aliasToUser(String alias){
        return serverFacade.aliasToUser(alias);
    }
}
