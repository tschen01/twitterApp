package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

    private LoginContext context;
    private LoginPresenter presenter;

    ///////// Interface //////////
    public interface LoginContext {
        void onExecuteComplete(String message, Boolean error);
    }

    // ========================== Constructor ========================================
    public LoginTask(LoginContext c, LoginPresenter p)
    {
        presenter = p;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected LoginResponse doInBackground(LoginRequest... logReqs)
    {
        LoginResponse loginResponse = presenter.loginUser(logReqs[0]);
        return loginResponse;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(LoginResponse loginResponse)
    {
            context.onExecuteComplete(loginResponse.getMessage(), loginResponse.isError());
    }
}
