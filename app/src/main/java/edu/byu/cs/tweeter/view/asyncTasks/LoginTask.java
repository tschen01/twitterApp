package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

    private LoginPresenter presenter;
    private getLoginObserver observer;

    public interface getLoginObserver {
        void loginRetrieved(LoginResponse loginResponse);
    }

    public LoginTask(LoginPresenter presenter, getLoginObserver observer )
    {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... logReqs)
    {
        LoginResponse loginResponse = presenter.loginUser(logReqs[0]);
        return loginResponse;
    }

    @Override
    protected void onPostExecute(LoginResponse loginResponse)
    {
        if(observer!= null){
            observer.loginRetrieved(loginResponse);
        }
    }
}
