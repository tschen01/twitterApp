package edu.byu.cs.tweeter.net;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.net.response.SignUpResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;

public class ServerFacade {

    private static ServerFacade instance;
    private static Map<User, List<User>> userFollowing;
    private static Map<User, List<User>> userFollowers;

    private static List<Follow> follows;
    private static User tweeterBot = new User("Tweeter", "Bot", "@TweeterBot","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    private static Map<User, List<Status>> userStatuses;
    private static Map<User, List<Status>> userFeeds;
    private static List<User> allUsers;


    /*
        Constructors
     */
    public static ServerFacade getInstance() {
        if(instance == null) {
            instance = new ServerFacade();
        }

        return instance;
    }

    private ServerFacade(){
        intializeFollowData();
        initializeStatuses();
        initializeFeeds();
        allUsers = new ArrayList<>();
        allUsers.add(new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
    }


    /*
             --------------------- Get Followers

  */
    public FollowerResponse getFollowers(FollowerRequest request){
        assert request.getLimit() >= 0;
        assert request.getFollower() != null;

        List<User> allFollowers = userFollowers.get(request.getFollower());
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowers);

                for(int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    /*
                 --------------------- get Following

      */
    public FollowingResponse getFollowing(FollowingRequest request) {

        assert request.getLimit() >= 0;
        assert request.getFollower() != null;

        System.out.print(userFollowing);

        List<User> allFollowees = userFollowing.get(request.getFollower());
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }


    /*
                 --------------------- get following starting index

      */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                }
            }
        }

        return followeesIndex;
    }

       /*
                --------------------- Generates follow data

     */
    /**
     * Generates the followee data.
     */
    private void intializeFollowData() {

        userFollowing = new HashMap<>();

        follows = getFollowGenerator().generateUsersAndFollows(100,
                0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);

        // Populate a map of followees, keyed by follower so we can easily handle followee requests
        for(Follow follow : follows) {
            List<User> followees = userFollowing.get(follow.getFollower());

            if(followees == null) {
                followees = new ArrayList<>();
                userFollowing.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }

        userFollowers = new HashMap<>();

        // Now do the same for a map of followers, keyed by followee to handle follower requests
        for(Follow follow : follows) {
            List<User> followers = userFollowers.get(follow.getFollowee());

            if(followers == null) {
                followers = new ArrayList<>();
                userFollowers.put(follow.getFollowee(), followers);
            }

            followers.add(follow.getFollower());
        }

        userFollowing.put(tweeterBot, new ArrayList<User>());
        userFollowers.put(tweeterBot, new ArrayList<User>());

        return;
    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest){                   //When backend is up, authenticate password with username there

        for(int i = 0; i < allUsers.size(); i++){
            if(loginRequest.getUsername().equals(allUsers.get(i).getAlias())
            && (loginRequest.getPassword().equals("password")
            || loginRequest.getPassword().equals("x"))){
                return new LoginResponse("Login successful!", false, allUsers.get(i));
            }
        }
        return new LoginResponse("Invalid credentials", true);

    }

    /*
             --------------------- Initialize Statuses

  */
    private void initializeStatuses() {

        userStatuses = new HashMap<>();

        for (Map.Entry<User, List<User>> entry : userFollowing.entrySet()) {
            User currentUser = entry.getKey();
            List<Status> statusList = new ArrayList<>();
            for(int i = 1; i < 5; i++){
                statusList.add(new Status(currentUser, "Test status " + i));
            }
            userStatuses.put(currentUser, statusList);
        }

        userStatuses.put(tweeterBot, new ArrayList<Status>());

    }

        /*
             --------------------- Initialize User Feeds

  */
    private void initializeFeeds(){

        userFeeds = new HashMap<>();

        for (Map.Entry<User, List<User>> entry : userFollowing.entrySet()) {
            User currentUser = entry.getKey();
            List<Status> statusList = new ArrayList<>();
            for (User following: entry.getValue()) {
                statusList.addAll(userStatuses.get(following));
            }
            userFeeds.put(currentUser, statusList);
        }
    }

    /*
                --------------------- Sign Up User

     */
    public SignUpResponse registerNewUser(SignUpRequest signUpRequest){

        if(signUpRequest.getFirstName() == null || signUpRequest.getLastName() == null
                || signUpRequest.getPassword() == null || signUpRequest.getUsername() == null){
            return new SignUpResponse("Not all forms filled out!", true);
        }


        User signedUpUser = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(),
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        for (User user: allUsers) {
            if(user.getAlias().equals(signedUpUser.getAlias())){
                return new SignUpResponse("Username already exists!", true);
            }
        }

        LoginService.getInstance().setCurrentUser(signedUpUser);
        LoginService.getInstance().setLoggedInUser(signedUpUser);
        List<User> newUserFollowees = new ArrayList<>();
        List<User> newUserFollowers = new ArrayList<>();


        newUserFollowees.add(tweeterBot);           //Two different arrays because if one gets altered the other would too
        newUserFollowers.add(tweeterBot);


        userFollowing.put(signedUpUser,newUserFollowees);           //Have new user follow tweeterbot
        userFollowing.get(tweeterBot).add(signedUpUser);

        userFollowers.get(tweeterBot).add(signedUpUser);            //Have tweeterBot follow new person
        userFollowers.put(signedUpUser, newUserFollowers);

        Status status = new Status(tweeterBot,"Welcome to Tweeter!");
        List<Status> currentFeed = new ArrayList<>();
        currentFeed.add(status);

        userStatuses.get(tweeterBot).add(status);
        userFeeds.put(signedUpUser, currentFeed);

        List<Status> newStatusList = new ArrayList<>();
        newStatusList.add(new Status(signedUpUser, "My first Status! Hi everybody!"));
        userStatuses.put(signedUpUser, newStatusList);

        allUsers.add(signedUpUser);


        return new SignUpResponse("Signed up successfully!", false);
    }

    /*
             --------------------- Get Story

  */
    public StoryResponse getStory(StoryRequest storyRequest){
        User user = storyRequest.getUser();

        assert storyRequest.getLimit() >= 0;        //Error check this
        assert storyRequest.getUser() != null;


        boolean hasMorePages = false;

        List<Status> statusList = userStatuses.get(user);
        List<Status> responseStatuses = new ArrayList<>();

        if(storyRequest.getLimit() > 0) {
            if (statusList != null) {
                int storyIndex = getStoryStartingIndex(storyRequest.getLastStatus(), statusList);

                for(int limitCounter = 0; storyIndex < statusList.size() && limitCounter < storyRequest.getLimit(); storyIndex++, limitCounter++) {
                    responseStatuses.add(statusList.get(storyIndex));
                }

                hasMorePages = storyIndex < statusList.size();
            }
        }


        return new StoryResponse("Good stuff", responseStatuses, hasMorePages, true);
    }

    private int getStoryStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                }
            }
        }

        return statusIndex;
    }

    /*
             --------------------- Get Feed

  */
    public FeedResponse getFeed(FeedRequest feedRequest){
        User user = feedRequest.getUser();

        assert feedRequest.getLimit() >= 0;
        assert feedRequest.getUser() != null;

        boolean hasMorePages = false;

        List<Status> statusList = userFeeds.get(user);
        List<User> following = userFollowing.get(user);

        List<Status> feedResponse = new ArrayList<>();

        if(feedRequest.getLimit() > 0) {
            if (statusList != null) {
                int storyIndex = getFeedStartingIndex(feedRequest.getLastStatus(), statusList);

                for(int limitCounter = 0; storyIndex < statusList.size() && limitCounter < feedRequest.getLimit(); storyIndex++, limitCounter++) {
                    feedResponse.add(statusList.get(storyIndex));
                }

                hasMorePages = storyIndex < statusList.size();
            }
        }


        Collections.sort(feedResponse, new Comparator<Status>() {
            public int compare(Status o1, Status o2) {
                return o2.getTimeStamp().compareTo(o1.getTimeStamp());
            }
        });




        return new FeedResponse(true, "No Error", hasMorePages, feedResponse, following);

    }


    private int getFeedStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                }
            }
        }

        return statusIndex;
    }


    /*
             --------------------- Post Status

  */
    public PostResponse post(Status postedStatus){
        User user = postedStatus.getUser();

        userStatuses.get(user).add(postedStatus);

        Collections.sort(userStatuses.get(user), new Comparator<Status>() {
            public int compare(Status o1, Status o2) {
                return o2.getTimeStamp().compareTo(o1.getTimeStamp());
            }
        });


        //Add that status to every follower's feed
        List<User> followers = userFollowers.get(user);

        for(int i = 0; i < followers.size(); i++){
            userFeeds.get(followers.get(i)).add(postedStatus);

        }


        return new PostResponse(true, "Everything smooth");
    }


    /*
                 --------------------- Get user from Alias

      */
    public User aliasToUser(String alias){
        for (Map.Entry<User, List<User>> entry : userFollowing.entrySet()) {
            if(entry.getKey().getAlias().equals(alias)){
                return entry.getKey();
            }
        }
        return null;
    }

    /*
             --------------------- isFollowing

  */
    public boolean isFollowing(Follow follow){
        return userFollowing.get(follow.getFollower()).contains(follow.getFollowee());
    }

    /*
             --------------------- followUser

  */
    public FollowResponse followUser(Follow follow){                //Should I resort the list alphabetically?
        if (userFollowing.get(follow.getFollower()).add(follow.getFollowee())
                && userFollowers.get(follow.getFollowee()).add(follow.getFollower())){
            return new FollowResponse(true, "User successfully followed");
        }
        else{
            return new FollowResponse(false, "Something went wrong following user");
        }
    }


    /*
             --------------------- unfollowUser

  */
    public UnfollowResponse unfollowUser(Follow follow){

        if (userFollowing.get(follow.getFollower()).remove(follow.getFollowee())
            && userFollowers.get(follow.getFollowee()).remove(follow.getFollower())){

            List<Status> statusList = new ArrayList<>(userFeeds.get(follow.getFollower()));

            Iterator<Status> itr = statusList.iterator();

            while (itr.hasNext()) {
                Status status = itr.next();

                if (status.getUser() == follow.getFollowee()) {
                    itr.remove();
                }
            }


            userFeeds.remove(follow.getFollower());
            userFeeds.put(follow.getFollower(), statusList);

            return new UnfollowResponse(true, "User successfully unfollowed");
        }
        else{
            return new UnfollowResponse(false, "Something went wrong unfollowing user");
        }
    }


    /*
    Getters and setter for testing
 */
    public static void setInstance(ServerFacade instance)
    {
        ServerFacade.instance = instance;
    }

    public static Map<User, List<User>> getUserFollowing()
    {
        return userFollowing;
    }

    public static void setUserFollowing(Map<User, List<User>> userFollowing)
    {
        ServerFacade.userFollowing = userFollowing;
    }

    public static Map<User, List<User>> getUserFollowers()
    {
        return userFollowers;
    }

    public static void setUserFollowers(Map<User, List<User>> userFollowers)
    {
        ServerFacade.userFollowers = userFollowers;
    }

    public static List<Follow> getFollows()
    {
        return follows;
    }

    public static void setFollows(List<Follow> follows)
    {
        ServerFacade.follows = follows;
    }

    public static Map<User, List<Status>> getUserStatuses()
    {
        return userStatuses;
    }

    public static void setUserStatuses(Map<User, List<Status>> userStatuses)
    {
        ServerFacade.userStatuses = userStatuses;
    }

    public static Map<User, List<Status>> getUserFeeds()
    {
        return userFeeds;
    }

    public static void setUserFeeds(Map<User, List<Status>> userFeeds)
    {
        ServerFacade.userFeeds = userFeeds;
    }

    public static List<User> getAllUsers()
    {
        return allUsers;
    }

    public static void setAllUsers(List<User> allUsers)
    {
        ServerFacade.allUsers = allUsers;
    }


}
