package edu.byu.cs.tweeter.view.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View, LoginTask.getLoginObserver {

    private EditText Username;
    private EditText Password;
    private LoginPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        presenter = new LoginPresenter(this);

        Username = this.findViewById(R.id.usernameInput);
        Password = this.findViewById(R.id.passwordInput);

    }

    @Override
    public void login(View v){
        LoginTask loginTask = new LoginTask(presenter,this);
        LoginRequest loginRequest = new LoginRequest(Username.getText().toString(), Password.getText().toString());
        loginTask.execute(loginRequest);
    }

    @Override
    public void signUp(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginRetrieved(LoginResponse loginResponse) {
        System.out.println(loginResponse.getMessage());
        if(loginResponse.isError()){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
