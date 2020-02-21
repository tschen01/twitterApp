package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;


public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

    private SignUpObserver observer;
    private SignUpPresenter presenter;

    public interface SignUpObserver {
        void signUpRetrieved(RegisterResponse postResponse);
    }

    public RegisterTask(SignUpPresenter presenter, SignUpObserver observer)
    {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... signUpRequests)
    {
        RegisterResponse signUpResponse = presenter.signUpUser(signUpRequests[0]);
        return signUpResponse;
    }

    @Override
    protected void onPostExecute(RegisterResponse signUpResponse)
    {
        if(observer!= null){
            observer.signUpRetrieved(signUpResponse);
        }
    }
}

