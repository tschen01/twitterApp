package edu.byu.cs.tweeter.presenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.byu.cs.tweeter.model.services.RegisterService;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;

public class SignUpPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
        void register(android.view.View v);
    }

    public SignUpPresenter(View view) {
        this.view = view;
    }

    public RegisterResponse signUpUser(RegisterRequest signUpRequest){
        RegisterResponse signUpResponse = RegisterService.getInstance().authenticateUser(signUpRequest);
        return signUpResponse;
    }
}
