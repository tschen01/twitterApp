package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class LoginTask extends AsyncTask<LoginRequest, Void, User> {

    private final LoginPresenter presenter; // dont need to create
    private final GetLoginObserver observer;

    public LoginTask(LoginPresenter presenter, GetLoginObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    public interface GetLoginObserver {
        void loginRetrieved(LoginResponse loginResponse);
    }


    public LoginTask(LoginPresenter presenter){this.presenter = presenter;}

    @Override
    protected User doInBackground(LoginRequest... loginRequests) {
        User result = presenter.getCurrentUser(loginRequests[0]);
        return result;
    }

    @Override
    protected void onPostExecute(User result) {

        DataCache.getInstance().cacheUser(currentUser);
    }
}
