package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;

public class SignOutPresenter extends Presenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public SignOutPresenter(View view) {
        this.view = view;
    }

    public SignOutResponse signOutUser(){
        return LoginService.getInstance().signOutUser(getCurrentUser());
    }
}
