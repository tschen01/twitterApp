package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.net.response.FollowResponse;

public class FollowPresenter extends Presenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public FollowPresenter(View view) {
        this.view = view;
    }

    public FollowResponse followUser(Follow follow){
        return FollowingService.getInstance().followUser(follow);
    }
}
