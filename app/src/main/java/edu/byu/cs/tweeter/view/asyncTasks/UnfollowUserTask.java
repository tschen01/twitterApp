package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.presenter.FollowPresenter;
import edu.byu.cs.tweeter.presenter.UnFollowPresenter;

public class UnfollowUserTask extends AsyncTask<Follow, Void, UnfollowResponse> {

    private getUnFollowObserver observer;
    private UnFollowPresenter presenter;

    public interface getUnFollowObserver {
        void unfollowRetrieved(UnfollowResponse loginResponse);
    }

    public UnfollowUserTask(UnFollowPresenter presenter, getUnFollowObserver observer )
    {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected UnfollowResponse doInBackground(Follow ...follow)
    {
        UnfollowResponse response = presenter.unfollowUser(follow[0]);
        return response;
    }

    @Override
    protected void onPostExecute(UnfollowResponse loginResponse)
    {
        if(observer!= null){
            observer.unfollowRetrieved(loginResponse);
        }
    }
}


