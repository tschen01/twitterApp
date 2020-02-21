package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.presenter.FollowPresenter;

public class FollowUserTask extends AsyncTask<Follow, Void, FollowResponse> {

    private getFollowObserver observer;
    private FollowPresenter presenter;

    public interface getFollowObserver {
        void followRetrieved(FollowResponse loginResponse);
    }

    public FollowUserTask(FollowPresenter presenter, getFollowObserver observer )
    {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected FollowResponse doInBackground(Follow ...follow)
    {
        FollowResponse response = presenter.followUser(follow[0]);
        return response;
    }

    @Override
    protected void onPostExecute(FollowResponse loginResponse)
    {
        if(observer!= null){
            observer.followRetrieved(loginResponse);
        }
    }
}


