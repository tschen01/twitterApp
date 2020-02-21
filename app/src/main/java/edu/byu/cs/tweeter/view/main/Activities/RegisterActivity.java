package edu.byu.cs.tweeter.view.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class RegisterActivity extends AppCompatActivity implements SignUpPresenter.View, RegisterTask.SignUpObserver {

    private SignUpPresenter presenter;

    private EditText Username;
    private EditText Password;
    private EditText FirstName;
    private EditText LastName;
    private ImageView Image;
    private Button UploadButton;
    private String imageToUpload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        presenter = new SignUpPresenter(this);

        Username = this.findViewById(R.id.username);
        Password = this.findViewById(R.id.password);
        FirstName = this.findViewById(R.id.firstName);
        LastName = this.findViewById(R.id.lastName);
        UploadButton = this.findViewById(R.id.uploadButton);
        Image = this.findViewById(R.id.imageView);

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

    }

    @Override
    public void register(View v){
        RegisterTask signUpTask = new RegisterTask(presenter,this);
        RegisterRequest signUpRequest = new RegisterRequest(Username.getText().toString(),
                Password.getText().toString(),
                FirstName.getText().toString(),
                LastName.getText().toString(),
                imageToUpload);
        signUpTask.execute(signUpRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

    }



    @Override
    public void signUpRetrieved(RegisterResponse postResponse) {
        if(postResponse.isError()) {
            Toast.makeText(this, "Registered Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Registered Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
