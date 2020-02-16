package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class LogoutTask extends AsyncTask<Void, Void, Void> {

    private final LogoutPresenter presenter; // dont need to create


    public LogoutTask(LogoutPresenter presenter) {
        this.presenter = presenter;
    }



    @Override
    protected Void doInBackground(Void... voids) {
        presenter.deleteUser();
        return null;
    }


}
