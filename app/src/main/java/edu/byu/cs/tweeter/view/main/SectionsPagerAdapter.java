package edu.byu.cs.tweeter.view.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.view.main.follower.FollowerFragment;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.main.story.StoryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int FEED_FRAGMENT_POSITION = 0;
    private static final int STORY_FRAGMENT_POSITION = 1;
    private static final int FOLLOWING_FRAGMENT_POSITION = 2;
    private static final int FOLLOWER_FRAGMENT_POSITION = 3;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private static final int[] TAB_TITLES_NOT_LOGGED_IN = new int[]{R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private final Context mContext;
    private final boolean isLoggedIn;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        isLoggedIn = (LoginService.getInstance().getLoggedInUser() == LoginService.getInstance().getCurrentUser());
    }

    @Override
    public Fragment getItem(int position) {
        if(isLoggedIn) {
            if (position == FOLLOWING_FRAGMENT_POSITION) {
                return new FollowingFragment();
            }
            else if (position == FEED_FRAGMENT_POSITION) {
                return new FeedFragment();
            }
            else if (position == FOLLOWER_FRAGMENT_POSITION) {
                return new FollowerFragment();
            }
            else if (position == STORY_FRAGMENT_POSITION) {
                return new StoryFragment();
            }
            else {
                return PlaceholderFragment.newInstance(position + 1);
            }
        }
        else {
            if (position == FOLLOWING_FRAGMENT_POSITION) {
                return new FollowingFragment();
            }
            else if (position == STORY_FRAGMENT_POSITION) {
                return new FollowerFragment();
            }
            else if (position == FEED_FRAGMENT_POSITION) {
                return new StoryFragment();
            }
            else {
                return PlaceholderFragment.newInstance(position + 1);
            }
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (isLoggedIn) {
            return mContext.getResources().getString(TAB_TITLES[position]);
        }
        else {
            return mContext.getResources().getString(TAB_TITLES_NOT_LOGGED_IN[position]);
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        if(isLoggedIn) {
            return 4;
        }
        else{
            return 3;
        }
    }
}