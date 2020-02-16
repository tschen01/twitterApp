package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.RegisterResponse;

public class RegisterService {

    private static RegisterService instance;
    private final ServerFacade serverFacade;
    private User currentUser;

    public static RegisterService getInstance() {
//        if(instance == null) {
//            instance = new RegisterService();
//        }
//
         return instance;
    }

//    private RegisterService() {
//        // TODO: Remove when the actual login functionality exists.
//        currentUser = new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//        serverFacade = new ServerFacade();
//        setCurrentUser(currentUser);
//    }

    public RegisterService(RegisterRequest request) {
        // TODO: Remove when the actual login functionality exists.
        currentUser = new User(request.getFirstName(), request.getLastName(),
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        instance = this;
        serverFacade = new ServerFacade();
        setCurrentUser(currentUser);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public RegisterResponse signup(RegisterRequest request){
        Boolean login = true;
        System.out.println(request.getFirstName()+ "yooooooooooooooo");
        LoginRequest loginRequest = new LoginRequest(request.getFirstName(),request.getLastName());
        System.out.println(loginRequest.getFirstName() + "hiiiiiiiiiiiiiiiiii");
        LoginService.getInstance().login(loginRequest);
        RegisterResponse response = new RegisterResponse(login);
        return response;
    }

}
