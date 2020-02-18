package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.presenter.FollowingPresenter;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class FollowUserTask extends AsyncTask<Follow, Void, FollowResponse> {

    private FollowUserContext context;
    private MainPresenter presenter;

    ///////// Interface //////////
    public interface FollowUserContext {
        void onFollowComplete(String message, Boolean error);
    }

    public FollowUserTask(FollowUserContext c, MainPresenter p)
    {
        presenter = p;
        context = c;
    }

    @Override
    protected FollowResponse doInBackground(Follow ...follow)
    {
        FollowResponse response = presenter.followUser(follow[0]);
        return response;
    }

    @Override
    protected void onPostExecute(FollowResponse signOutResponse)
    {
        context.onFollowComplete(signOutResponse.getMessage(), signOutResponse.isSuccess());
    }
}
