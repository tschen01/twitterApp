package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FeedService;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;

public class FeedPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public FeedPresenter(View view) {
        this.view = view;
    }

    public FeedResponse getFeed(FeedRequest request) {
        return FeedService.getInstance().getFeed(request);
    }
//    public User getUserByAlias(String alias) { return LoginService.getInstance().mentionToUser(alias); }
}
