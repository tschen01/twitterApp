package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class SignOutTask extends AsyncTask<Void, Void, SignOutResponse> {

    private SignOutContext context;
    private MainPresenter presenter;

    ///////// Interface //////////
    public interface SignOutContext {
        void onExecuteComplete(String message, Boolean error);
    }

    public SignOutTask(SignOutContext c, MainPresenter p)
    {
        presenter = p;
        context = c;
    }

    @Override
    protected SignOutResponse doInBackground(Void ...params)
    {
        SignOutResponse signUpResponse = presenter.signOut();
        return signUpResponse;
    }

    @Override
    protected void onPostExecute(SignOutResponse signOutResponse)
    {
        context.onExecuteComplete(signOutResponse.getMessage(), signOutResponse.isSuccess());
    }
}
