package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.PostService;
import edu.byu.cs.tweeter.net.response.PostResponse;

public class PostPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public PostPresenter(View view) {
        this.view = view;
    }


    public PostResponse sendPostInfo(String postMessage){
        return PostService.getInstance().postStatus(postMessage);
    }

}
