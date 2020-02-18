package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.presenter.PostPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.PostTask;

public class PostActivity extends AppCompatActivity implements PostPresenter.View, PostTask.PostContext {

    private Button postButton;
    private EditText postMessage;

    PostPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_popup);

        presenter = new PostPresenter(this);

        postButton = findViewById(R.id.postButton);
        postMessage = findViewById(R.id.postMessage);


    }

    @Override
    public void onExecuteComplete(String message, Boolean success){
        System.out.println(message);
        if(!success) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void postStatus(View v){
        PostTask postTask = new PostTask(this, presenter);
        postTask.execute(postMessage.getText().toString());
    }
}
