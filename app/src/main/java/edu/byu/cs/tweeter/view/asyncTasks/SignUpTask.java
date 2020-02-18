package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.response.SignUpResponse;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;


public class SignUpTask extends AsyncTask<SignUpRequest, Void, SignUpResponse> {

    private SignUpContext context;
    private SignUpPresenter presenter;

    ///////// Interface //////////
    public interface SignUpContext {
        void onExecuteComplete(String message, Boolean error);
    }

    public SignUpTask(SignUpContext c, SignUpPresenter p)
    {
        presenter = p;
        context = c;
    }

    @Override
    protected SignUpResponse doInBackground(SignUpRequest... signUpRequests)
    {
        SignUpResponse signUpResponse = presenter.signUpUser(signUpRequests[0]);
        return signUpResponse;
    }

    @Override
    protected void onPostExecute(SignUpResponse signUpResponse)
    {
        context.onExecuteComplete(signUpResponse.getMessage(), signUpResponse.isError());
    }
}

