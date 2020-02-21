package edu.byu.cs.tweeter.view.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.presenter.PostPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.PostTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class PostActivity extends AppCompatActivity implements PostPresenter.View, PostTask.getPostObserver {

    private Button postButton;
    private EditText postMessage;
    PostPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        presenter = new PostPresenter(this);

        postButton = findViewById(R.id.postButton);
        postMessage = findViewById(R.id.postMessage);
    }

    public void postStatus(View v){
        PostTask postTask = new PostTask(presenter,this);
        postTask.execute(postMessage.getText().toString());
    }

    @Override
    public void postRetrieved(PostResponse postResponse) {
        System.out.println(postResponse.getMessage());
        if(!postResponse.isSuccess()) {
            Toast.makeText(this, "Post error", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Post Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
