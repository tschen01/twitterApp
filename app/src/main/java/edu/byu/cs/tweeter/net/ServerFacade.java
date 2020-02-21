package edu.byu.cs.tweeter.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.FollowResponse;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.PostResponse;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.net.response.UnfollowResponse;

public class ServerFacade {

    private static ServerFacade instance;
    private static Map<User, List<Status>> userStatuses;
    private static Map<User, List<Status>> userFeeds;
    private static List<User> allUsers;
    private static Map<User, List<User>> userFollowing;
    private static Map<User, List<User>> userFollowers;
    private static List<Follow> follows;
    private static User fakeTwitter  = new User("First", "Twitter", "@FirstTwitter","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    // private static User fakeTwitter2 = new User("Fake", "Twitter", "@FakeTwitter","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    public static ServerFacade getInstance() {
        if(instance == null) {
            instance = new ServerFacade();
        }

        return instance;
    }

    public ServerFacade(){
//        initalizeFollowees();
//        initalizeFollowers();
        intializeFollow();
        initializeStatuses();
        initializeFeeds();
        allUsers = new ArrayList<>();
        allUsers.add(new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
    }

    /** RETURN RESPONSES **/
    /** getStory **/
    public StoryResponse getStory(StoryRequest storyRequest){
        User user = storyRequest.getUser();

        if(BuildConfig.DEBUG) {
            if(storyRequest.getLimit() < 0) {
                throw new AssertionError();
            }

            if(storyRequest.getUser() == null) {
                throw new AssertionError();
            }
        }

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


        return new StoryResponse("Story Statuses", responseStatuses, hasMorePages, true);
    }

    /** getFeed **/
    public FeedResponse getFeed(FeedRequest feedRequest){
        User user = feedRequest.getUser();

        if(BuildConfig.DEBUG) {
            if(feedRequest.getLimit() < 0) {
                throw new AssertionError();
            }

            if(feedRequest.getUser() == null) {
                throw new AssertionError();
            }
        }

        boolean hasMorePages = false;

        List<Status> statusList = userFeeds.get(user);
        List<User> following = userFollowing.get(user);

        List<Status> feedResponse = new ArrayList<>();

        if(feedRequest.getLimit() > 0) {
            if (statusList != null) {
                int storyIndex = getStoryStartingIndex(feedRequest.getLastStatus(), statusList);

                for(int limitCounter = 0; storyIndex < statusList.size() && limitCounter < feedRequest.getLimit(); storyIndex++, limitCounter++) {
                    feedResponse.add(statusList.get(storyIndex));
                }

                hasMorePages = storyIndex < statusList.size();
            }
        }

        Collections.sort(feedResponse, new Comparator<Status>() {
            @Override
            public int compare(Status status1, Status status2) {
                int result = status1.getTimeStamp().compareTo(
                        status2.getTimeStamp());

                if(result == 0) {
                    return status1.getTimeStamp().compareTo(
                            status2.getTimeStamp());
                }

                return result;
            }
        });




        return new FeedResponse(true, "No Error", hasMorePages, feedResponse, following);

    }

    /** get Followers **/
    public FollowerResponse getFollowers(FollowerRequest request){
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollower() == null) {
                throw new AssertionError();
            }
        }

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

    /** get Followings **/
    public FollowingResponse getFollowing(FollowingRequest request) {
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollower() == null) {
                throw new AssertionError();
            }
        }

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


    /** METHODS **/
    /** isFollowing Check**/
    public boolean isFollowing(Follow follow){
        return userFollowing.get(follow.getFollower()).contains(follow.getFollowee());
    }
    /** Follow **/
    public FollowResponse followUser(Follow follow){
        if (userFollowing.get(follow.getFollower()).add(follow.getFollowee())
                && userFollowers.get(follow.getFollowee()).add(follow.getFollower())){
            return new FollowResponse(true, "Followed");
        }
        else{
            return new FollowResponse(false, "Can't Follow");
        }
    }

    /** UNFOLLOW **/
    public UnfollowResponse unfollowUser(Follow follow){

        if (userFollowing.get(follow.getFollower()).remove(follow.getFollowee())
                && userFollowers.get(follow.getFollowee()).remove(follow.getFollower())){
            return new UnfollowResponse(true, "Unfollowed");
        }
        else{
            return new UnfollowResponse(false, "Can't Unfollow");
        }
    }

    /** LOG IN **/
    public LoginResponse CheckUser(LoginRequest loginRequest){

        for(int i = 0; i < allUsers.size(); i++){
            if(loginRequest.getUsername().equals(allUsers.get(i).getAlias())){
                return new LoginResponse("Login successful!", false, allUsers.get(i));
            }
        }
        return new LoginResponse("Invalid Login", true);

    }

    /** register **/

    public RegisterResponse registerNewUser(RegisterRequest signUpRequest){

        if(signUpRequest.getFirstName() == null || signUpRequest.getLastName() == null
                || signUpRequest.getPassword() == null || signUpRequest.getUsername() == null){
            return new RegisterResponse("Fill all!", true);
        }

        User RegisteredUser = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(),
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        for (User user: allUsers) {
            if(user.getAlias().equals(RegisteredUser.getAlias())){
                return new RegisterResponse("Username already exists!", true);
            }
        }

        LoginService.getInstance().setCurrentUser(RegisteredUser);
        LoginService.getInstance().setLoggedInUser(RegisteredUser);
        List<User> newUserFollowees = new ArrayList<>();
        List<User> newUserFollowers = new ArrayList<>();


        newUserFollowees.add(fakeTwitter);
        newUserFollowers.add(fakeTwitter);


        userFollowing.put(RegisteredUser,newUserFollowees);
        userFollowing.get(fakeTwitter).add(RegisteredUser);

        userFollowers.get(fakeTwitter).add(RegisteredUser);
        userFollowers.put(RegisteredUser, newUserFollowers);

        Status status = new Status(fakeTwitter,"Welcome to Twitter!");
        List<Status> currentFeed = new ArrayList<>();
        currentFeed.add(status);

        userStatuses.get(fakeTwitter).add(status);
        userFeeds.put(RegisteredUser, currentFeed);

        List<Status> newStatusList = new ArrayList<>();
        newStatusList.add(new Status(RegisteredUser, "My first Status! Hi everybody!"));
        userStatuses.put(RegisteredUser, newStatusList);

        allUsers.add(RegisteredUser);

        return new RegisterResponse("Registered successfully!", false);
    }

    /** POST **/
    public PostResponse post(Status postedStatus){
        System.out.println("POOOOSTED"+ postedStatus);
        User user = postedStatus.getUser();
        System.out.println("USER"+ postedStatus.getUser());
        userStatuses.get(user).add(postedStatus);

        Collections.sort(userStatuses.get(user), new Comparator<Status>() {
            @Override
            public int compare(Status status1, Status status2) {
                int result = status1.getTimeStamp().compareTo(
                        status2.getTimeStamp());

                if(result == 0) {
                    return status1.getTimeStamp().compareTo(
                            status2.getTimeStamp());
                }

                return result;
            }
        });
        return new PostResponse(true, "Posted");
    }


    /** INITIALIZER **/
    /** put Followers **/
    private void initalizeFollowers(){
        userFollowers = new HashMap<>();
        follows = getFollowGenerator().generateUsersAndFollows(100,
                0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);
        for(Follow follow : follows) {
            List<User> followers = userFollowers.get(follow.getFollowee());

            if(followers == null) {
                followers = new ArrayList<>();
                userFollowers.put(follow.getFollowee(), followers);
            }

            followers.add(follow.getFollower());
        }
        userFollowers.put(fakeTwitter, new ArrayList<User>());
    }
    /** put Followees **/
    private void initalizeFollowees() {
        userFollowing = new HashMap<>();
        follows = getFollowGenerator().generateUsersAndFollows(100,
                0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);
        for(Follow follow : follows) {
            List<User> followees = userFollowing.get(follow.getFollower());

            if(followees == null) {
                followees = new ArrayList<>();
                userFollowing.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }
        userFollowing.put(fakeTwitter, new ArrayList<User>());
    }

    /** PUT STATUS IN **/
    private void initializeStatuses() {

        userStatuses = new HashMap<>();
        for (Map.Entry<User, List<User>> entry : userFollowing.entrySet()) {
            User currentUser = entry.getKey();
            List<Status> statusList = new ArrayList<>();
            for(int j = 1; j < 3; j++){
                statusList.add(new Status(currentUser, "Test post " + j));
            }
            userStatuses.put(currentUser, statusList);
        }
        userStatuses.put(fakeTwitter, new ArrayList<Status>());
    }

    /** put status in feed */
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

    /** Put Followers in **/
    private void intializeFollow() {

        userFollowing = new HashMap<>();
        userFollowers = new HashMap<>();

        follows = getFollowGenerator().generateUsersAndFollows(100,
                0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);

        for(Follow follow : follows) {
            List<User> followees = userFollowing.get(follow.getFollower());

            if(followees == null) {
                followees = new ArrayList<>();
                userFollowing.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }

        for(Follow follow : follows) {
            List<User> followers = userFollowers.get(follow.getFollowee());

            if(followers == null) {
                followers = new ArrayList<>();
                userFollowers.put(follow.getFollowee(), followers);
            }

            followers.add(follow.getFollower());
        }
        userFollowing.put(fakeTwitter, new ArrayList<User>());
        userFollowers.put(fakeTwitter, new ArrayList<User>());
        return;
    }


    /** INDEX GENERATOR **/
    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the last followee that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
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


    /** Alias to User **/
    public User mentionToUser(String alias){
        for (Map.Entry<User, List<User>> entry : userFollowing.entrySet()) {
            if(entry.getKey().getAlias().equals(alias)){
                return entry.getKey();
            }
        }
        return null;
    }

    /** GETTERS AND SETTERS FUNCTIONS **/

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();
    }

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


