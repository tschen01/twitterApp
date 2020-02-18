package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.presenter.PostPresenter;
import edu.byu.cs.tweeter.presenter.SearchPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.PostTask;

public class SearchActivity extends AppCompatActivity implements SearchPresenter.View {

    private Button searchButton;
    private EditText searchUser;
    private SearchPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_activity);

        presenter = new SearchPresenter(this);

        searchButton = findViewById(R.id.searchButton);
        searchUser = findViewById(R.id.userSearchText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String userAlias = searchUser.getText().toString();
                User newUser = presenter.getUserByAlias(userAlias);
                if(newUser != null){
                    LoginService.getInstance().setCurrentUser(newUser);

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(view.getContext(), userAlias + " does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
