package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.presenter.PostPresenter;

public class PostTask extends AsyncTask<String, Void, PostResponse> {

    private PostContext context;
    private PostPresenter presenter;

    ///////// Interface //////////
    public interface PostContext {
        void onExecuteComplete(String message, Boolean error);
    }

    public PostTask(PostContext c, PostPresenter p)
    {
        presenter = p;
        context = c;
    }

    @Override
    protected PostResponse doInBackground(String ...message)
    {
        PostResponse response = presenter.sendPostInfo(message[0]);
        return response;
    }

    @Override
    protected void onPostExecute(PostResponse signOutResponse)
    {
        context.onExecuteComplete(signOutResponse.getMessage(), signOutResponse.isSuccess());
    }
}
