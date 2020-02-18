package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.SearchPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FollowUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.asyncTasks.PostTask;
import edu.byu.cs.tweeter.view.asyncTasks.SignOutTask;
import edu.byu.cs.tweeter.view.asyncTasks.SignUpTask;
import edu.byu.cs.tweeter.view.asyncTasks.UnfollowUserTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;

public class MainActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, MainPresenter.View, SignOutTask.SignOutContext, FollowUserTask.FollowUserContext, UnfollowUserTask.UnfollowUserContext {

    private MainPresenter presenter;
    private User user;
    private ImageView userImageView;
    private Button signOutButton;
    private Button followButton;
    private Button userSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        user = presenter.getCurrentUser();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        signOutButton = findViewById(R.id.signOutButton);
        followButton = findViewById(R.id.follow_toggle);
        userSearch = findViewById(R.id.goUser);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                goToPostActivity();

            }
        });

        if(user != presenter.getLoggedInUser()){
            fab.hide();
            signOutButton.setVisibility(View.INVISIBLE);
            if (presenter.isFollowing(new Follow(presenter.getLoggedInUser(), presenter.getCurrentUser()))){
                System.out.print("Logged in user is following current user");
                followButton.setText(R.string.unfollow_button);
            }
            else {
                System.out.print("Logged in user is NOT following current user");
                followButton.setText(R.string.follow_button);
            }
        }
        else {
            followButton.setVisibility(View.INVISIBLE);
        }

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                signOut();
            }
        });

        userImageView = findViewById(R.id.userImage);



        // Asynchronously load the user's image
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(presenter.getCurrentUser().getImageUrl());

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());
    }


    @Override
    public void imageLoadProgressUpdated(Integer progress) {
        // We're just loading one image. No need to indicate progress.
    }

    @Override
    public void imagesLoaded(Drawable[] drawables) {
        ImageCache.getInstance().cacheImage(user, drawables[0]);

        if(drawables[0] != null) {
            userImageView.setImageDrawable(drawables[0]);
        }
    }

    public void goUser(View v){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void signOut()
    {
        SignOutTask signOutTask = new SignOutTask(this, presenter);
        signOutTask.execute();
    }

    @Override
    public void goToPostActivity(){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    @Override
    public void followUser(View v) {
        if (presenter.isFollowing(new Follow(presenter.getLoggedInUser(), presenter.getCurrentUser()))){
            UnfollowUserTask unfollowUserTask = new UnfollowUserTask(this, presenter);
            followButton.setText(R.string.follow_button);
            unfollowUserTask.execute(new Follow(presenter.getLoggedInUser(), presenter.getCurrentUser()));


        }
        else {
            FollowUserTask followUserTask = new FollowUserTask(this, presenter);
            followButton.setText(R.string.unfollow_button);
            followUserTask.execute(new Follow(presenter.getLoggedInUser(), presenter.getCurrentUser()));
        }



    }

    @Override
    public void onExecuteComplete(String message, Boolean success){
        System.out.println(message);
        if(!success) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onFollowComplete(String message, Boolean success)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnfollowComplete(String message, Boolean success)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        LoginService.getInstance().setCurrentUser(LoginService.getInstance().getLoggedInUser());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}