package edu.byu.cs.tweeter.presenter;

import android.view.View;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;

public class MainPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
        void signOut();
        void goToPostActivity();
        void followUser(android.view.View v);
    }

    public MainPresenter(View view) {
        this.view = view;
    }


    public boolean isFollowing(Follow follow){
        return FollowingService.getInstance().isFollowing(follow);
    }

}
