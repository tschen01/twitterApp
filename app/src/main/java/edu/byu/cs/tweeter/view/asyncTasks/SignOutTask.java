package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.SignOutPresenter;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;

public class SignOutTask extends AsyncTask<Void, Void, SignOutResponse> {

    private getSignOutObserver observer;
    private SignOutPresenter presenter;

    public interface getSignOutObserver {
        void signOutRetrieved(SignOutResponse postResponse);
    }

    public SignOutTask( SignOutPresenter presenter, getSignOutObserver observer)
    {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected SignOutResponse doInBackground(Void ...params)
    {
        SignOutResponse signUpResponse = presenter.signOutUser();
        return signUpResponse;
    }

    @Override
    protected void onPostExecute(SignOutResponse signOutResponse)
    {
        if(observer!= null){
            observer.signOutRetrieved(signOutResponse);
        }
    }
}
