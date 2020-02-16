package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, User> {

    private final RegisterPresenter presenter; // dont need to create
    private final GetRegisterObserver observer;

    public RegisterTask(RegisterPresenter presenter, GetRegisterObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    public interface GetRegisterObserver {
        void registerRetrieved(User user);
    }



    @Override
    protected User doInBackground(RegisterRequest... registerRequests) {
        User result = presenter.setCurrentUser(registerRequests[0]);
        return result;
    }


    @Override
    protected void onPostExecute(User result) {
        if(observer != null) {
            observer.registerRetrieved(result);
        }
    }
}
