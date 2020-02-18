package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View, LoginTask.LoginContext {

    private EditText mUsername;
    private EditText mPassword;

    private LoginPresenter presenter;

    private Button mLoginButton;
    private Button mSignUpButton;

    //________________________ onCreate and other Fragment functions ____________________________________
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        presenter = new LoginPresenter(this);

        mUsername = this.findViewById(R.id.usernameInput);
        mPassword = this.findViewById(R.id.passwordInput);

    }

    @Override
    public void login(View v){
        LoginTask loginTask = new LoginTask(this, presenter);
        LoginRequest loginRequest = new LoginRequest(mUsername.getText().toString(), mPassword.getText().toString());

        loginTask.execute(loginRequest);
    }

    @Override
    public void signUp(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onExecuteComplete(String message, Boolean error){
        System.out.println(message);
        if(error) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
