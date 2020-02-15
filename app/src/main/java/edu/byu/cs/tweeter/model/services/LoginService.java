package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;

public class LoginService {

    private static LoginService instance;
    private final ServerFacade serverFacade;
    private User currentUser;

    public static LoginService getInstance() {
        if(instance == null) {
            instance = new LoginService();
        }

        return instance;
    }

    private LoginService() {
        // TODO: Remove when the actual login functionality exists.
        currentUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        serverFacade = new ServerFacade();
        setCurrentUser(currentUser);
    }

    public LoginService(LoginRequest request) {
        // TODO: Remove when the actual login functionality exists.
        currentUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        instance = this;
        serverFacade = new ServerFacade();
        setCurrentUser(currentUser);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
