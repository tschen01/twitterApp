package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;

public abstract class Presenter {

    public User getCurrentUser() {
        return LoginService.getInstance().getCurrentUser();
    }

    public User getLoggedInUser() {
        return LoginService.getInstance().getLoggedInUser();
    }

    public User getUserByAlias(String alias) { return LoginService.getInstance().aliasToUser(alias); }
}
