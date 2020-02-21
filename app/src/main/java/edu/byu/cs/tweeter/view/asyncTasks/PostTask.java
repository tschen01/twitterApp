package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.presenter.PostPresenter;

public class PostTask extends AsyncTask<String, Void, PostResponse> {

    private getPostObserver observer;
    private PostPresenter presenter;

    public interface getPostObserver {
        void postRetrieved(PostResponse postResponse);
    }

    public PostTask(PostPresenter presenter, getPostObserver observer )
    {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected PostResponse doInBackground(String ...message)
    {
        PostResponse response = presenter.sendPostInfo(message[0]);
        return response;
    }

    @Override
    protected void onPostExecute(PostResponse loginResponse)
    {
        if(observer!= null){
            observer.postRetrieved(loginResponse);
        }
    }
}
