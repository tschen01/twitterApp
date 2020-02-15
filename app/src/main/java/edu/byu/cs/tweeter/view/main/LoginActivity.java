package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.presenter.Presenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View, LoginTask.GetLoginObserver
        {
private Button mLoginButton;
private Button mSignUpButton;
private EditText mUserField;
private EditText mPasswordField;
private String mUserAlias;
private String mPassword;
private Presenter presenter;

@Override
protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton=findViewById(R.id.login_button);
        presenter=new LoginPresenter(this);

        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
@Override
public void onClick(View v)
        {
        LoginTask loginTask=new LoginTask((LoginPresenter)presenter);
        LoginRequest loginRequest=new LoginRequest(getFirstName(),getLastName());
        loginTask.execute(loginRequest);
        }
        });


mSignUpButton=findViewById(R.id.sign_up_button);
mSignUpButton.setOnClickListener(new View.OnClickListener()
        {
@Override
public void onClick(View v)
        {

        }
        });

        }
            private EditText hostEditText;
            private EditText portEditText;
            private EditText userNameEditText;
            private EditText passwordEditText;
            private EditText firstNameEditText;
            private EditText lastNameEditText;
            private EditText emailEditText;
            private String gender = "";
            private RadioButton maleRadioButton, femaleRadioButton;
            private RadioGroup genderGroup;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
                View view = inflater.inflate(R.layout.activity_login, container, false);

                mLoginButton = (Button) view.findViewById(R.id.login_button);
                mSignUpButton = (Button) view.findViewById(R.id.sign_up_button);

                //make button disabled in the beginning
                mLoginButton.setEnabled(false);
                mSignUpButton.setEnabled(false);

                // a watch for the button
                TextWatcher buttonWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        loginButtonChange();
                        registerButtonChange();
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                };
                // variable
                hostEditText = (EditText) view.findViewById(R.id.serverHostEditText);
                hostEditText.addTextChangedListener(buttonWatcher);
                portEditText = (EditText) view.findViewById(R.id.serverPortEditText);
                portEditText.addTextChangedListener(buttonWatcher);
                userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
                userNameEditText.addTextChangedListener(buttonWatcher);
                passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
                passwordEditText.addTextChangedListener(buttonWatcher);
                firstNameEditText = (EditText) view.findViewById(R.id.firstNameEditText);
                firstNameEditText.addTextChangedListener(buttonWatcher);
                lastNameEditText = (EditText) view.findViewById(R.id.lastNameEditText);
                lastNameEditText.addTextChangedListener(buttonWatcher);
                emailEditText = (EditText) view.findViewById(R.id.emailEditText);
                emailEditText.addTextChangedListener(buttonWatcher);
                genderGroup = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
                maleRadioButton = (RadioButton) view.findViewById(R.id.maleRadioButton);
                femaleRadioButton = (RadioButton) view.findViewById(R.id.femaleRadioButton);

                //check if female or male base on radio button
                genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.maleRadioButton) {
                            gender = "m";
                            loginButtonChange();
                            registerButtonChange();
                        } else if (checkedId == R.id.femaleRadioButton) {
                            gender = "f";
                            loginButtonChange();
                            registerButtonChange();
                        } else {
                            loginButtonChange();
                            registerButtonChange();
                        }
                    }
                });

                //do the login task
                mLoginButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        LoginRequest loginRequest = new LoginRequest(getUsername(), getPassword());
                        LoginTask login = new LoginTask();
                        login.execute(loginRequest);

                    }
                });
                //loginRequest and loginTask

                //do the register task
                mSignUpButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                     ;
                    }
                });

                return view;
            };

            private void loginButtonChange() {
                if(!portEditText.getText().toString().equals("")
                        && !userNameEditText.getText().toString().equals("")
                        && !passwordEditText.getText().toString().equals("")
                        && !hostEditText.getText().toString().equals("")) {
                    mLoginButton.setEnabled(true);
                }else {
                    mLoginButton.setEnabled(false);
                }
            };
            private void registerButtonChange(){
                if(!portEditText.getText().toString().equals("")
                        && !userNameEditText.getText().toString().equals("")
                        && !passwordEditText.getText().toString().equals("")
                        && !firstNameEditText.getText().toString().equals("")
                        && !lastNameEditText.getText().toString().equals("")
                        && !hostEditText.getText().toString().equals("")
                        && !emailEditText.getText().toString().equals("")
                        && !gender.equals("")) {
                    mSignUpButton.setEnabled(true);
                }
                else {
                    mSignUpButton.setEnabled(false);
                }
            };


            //functions that return whatever the user fill in
            public String getServerHost() {
                return hostEditText.getText().toString();
            }
            public String getUsername(){
                return userNameEditText.getText().toString();
            }
            public String getPassword(){
                return passwordEditText.getText().toString();
            }
            public String getServerPort(){
                return portEditText.getText().toString();
            }
            public String getEmail(){
                return emailEditText.getText().toString();
            }
            public String getFirstName(){
                return firstNameEditText.getText().toString();
            }
            public String getLastName(){
                return lastNameEditText.getText().toString();
            }
            public String getGender(){
                return gender;
            }

            @Override
            public void loginRetrieved(LoginResponse loginResponse) {
                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        }
