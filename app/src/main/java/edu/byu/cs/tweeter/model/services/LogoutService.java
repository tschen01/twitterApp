package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;

public class LogoutService {

    private static LogoutService instance;
    private final ServerFacade serverFacade;
    private User currentUser;

    public static LogoutService getInstance() {
        if(instance == null) {
            instance = new LogoutService();
        }

        return instance;
    }

    public LogoutService() {
        serverFacade = new ServerFacade();
    }

    public void logout(){
        serverFacade.logout();
    }
}
