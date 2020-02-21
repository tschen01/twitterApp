package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;

public class LoginService {

    private static LoginService instance;
    private final ServerFacade serverFacade;
    private User currentUser;
    private User UserLoggedIn;

    public static LoginService getInstance() {
        if(instance == null) {
            instance = new LoginService();
        }

        return instance;
    }

    private LoginService() {serverFacade = ServerFacade.getInstance();}


    public LoginResponse CheckUser(LoginRequest loginRequest){
        LoginResponse loginResponse = serverFacade.CheckUser(loginRequest);
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

    /** server to get the @ in status */

    public User mentionToUser(String alias){
        return serverFacade.mentionToUser(alias);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getLoggedInUser() {
        return UserLoggedIn;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.UserLoggedIn = loggedInUser;
    }

    /** FOR SIGNING OUT NOT LOGIN **/

    public SignOutResponse signOutUser(User currentUser) {
       setCurrentUser(null);
       setLoggedInUser(null);
       return new SignOutResponse(true, "Signed out!");
    }



}
