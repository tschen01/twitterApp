package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class UnfollowUserTask extends AsyncTask<Follow, Void, UnfollowResponse> {

    private UnfollowUserContext context;
    private MainPresenter presenter;

    ///////// Interface //////////
    public interface UnfollowUserContext {
        void onUnfollowComplete(String message, Boolean error);
    }

    public UnfollowUserTask(UnfollowUserContext c, MainPresenter p)
    {
        presenter = p;
        context = c;
    }

    @Override
    protected UnfollowResponse doInBackground(Follow ...follow)
    {
        UnfollowResponse response = presenter.unFollowUser(follow[0]);
        return response;
    }

    @Override
    protected void onPostExecute(UnfollowResponse signOutResponse)
    {
        context.onUnfollowComplete(signOutResponse.getMessage(), signOutResponse.isSuccess());
    }
}