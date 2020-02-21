package edu.byu.cs.tweeter.view.main.story;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class StoryFragment extends Fragment implements StoryPresenter.View {

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private StoryPresenter presenter;
    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        presenter = new StoryPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);
        storyRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }


    private class StatusHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView message;
        private final TextView timestamp;

        StatusHolder(@NonNull final View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);
        }

        void bindUser(Status status) {
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(status.getUser()));
            userAlias.setText(status.getUser().getAlias());
            userName.setText(status.getUser().getName());
            message.setText(status.getMessage());
            timestamp.setText(status.getTimeStamp().toString());

//            String text = status.getMessage().toString();
//            if(text.contains("@")){
//                int startIndex = text.indexOf("@");
//                String substring = text.substring(startIndex);
//                int endIndex;
//                if(substring.contains(" ")){
//                    endIndex = substring.indexOf(" ");
//                }
//                else{
//                    endIndex = substring.length();
//                }
//                SpannableString ss = new SpannableString(text);
//                ClickableSpan clickableSpan = new ClickableSpan() {
//                    @Override
//                    public void onClick(@NonNull View widget) {
//                        Intent intent = new Intent(getActivity(),ViewUserActivity.class);
//                    }
//                }
//            }

//            String copy = message.getText().toString();
//            SpannableString spannableString = new SpannableString(copy);
//
//            ClickableSpan userMentionsSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View textView) {
//                    TextView textView1 = (TextView) textView;
//                    String mentions = textView1.getText().toString();
//
//                    User newUser = presenter.getUserByAlias(mentions);
//                    if(newUser != null){
//                        LoginService.getInstance().setCurrentUser(newUser);
//
//                        Intent intent = new Intent(textView.getContext(), MainActivity.class);
//                        itemView.getContext().startActivity(intent);
//                    }
//                    else {
//                        Toast.makeText(getContext(), mentions + " does not exist!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };
//
//
//            ClickableSpan linksSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View textView) {
//                    TextView tx = (TextView) textView;
//                    String url = tx.getText().toString();
//
//                    if(!url.startsWith("https://")){
//                        url = "http://" + url;
//                    }
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(browserIntent);
//                }
//            };
//
//
//            List<String> userMentions = status.getUserMentions();
//            List<String> links = status.getLinks();
//
//            for(int i = 0; i < userMentions.size(); i++){
//                int beginningIndex = copy.indexOf(userMentions.get(i));
//                int endingIndex = userMentions.get(i).length() + beginningIndex;
//                spannableString.setSpan(userMentionsSpan, beginningIndex, endingIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//
//            for(int i = 0; i < links.size(); i++){
//                int beginningIndexOfLinks = copy.indexOf(links.get(i));
//                int endingIndex = links.get(i).length() + beginningIndexOfLinks;
//                spannableString.setSpan(linksSpan, beginningIndexOfLinks, endingIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//
//            message.setText(spannableString);
//            message.setMovementMethod(LinkMovementMethod.getInstance());
//            message.setHighlightColor(Color.RED);

            String messageCopy = message.getText().toString();
            SpannableString ss = new SpannableString(messageCopy);

//            String substring;
//            if(messageCopy.contains("@")) {
//                int startIndex = messageCopy.indexOf("@");
//                int endIndex;
//              substring = messageCopy.substring(startIndex);
//                if (substring.contains(" ")) {
//                    endIndex = substring.indexOf(" ");
//                } else {
//                    endIndex = substring.length();
//                }
//                substring = messageCopy.substring(startIndex,endIndex);
//            }
            //--------------- User mentions
            ClickableSpan userMentionsSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    TextView tx = (TextView) textView;
                    System.out.println(tx.getText().toString() + "TESTING");

                    String s = tx.getText().toString();
                    String substring = s;
                    if(s.contains("@")) {
                        int startIndex = s.indexOf("@");
                        int endIndex;
                        substring = s.substring(startIndex);
                        if (substring.contains(" ")) {
                            endIndex = substring.indexOf(" ");
                        } else {
                            endIndex = substring.length();
                        }
                        substring = s.substring(startIndex,endIndex);
                    }
                    User newUser = presenter.getUserByAlias(substring);
                    if(newUser != null){
                        LoginService.getInstance().setCurrentUser(newUser);
                        Intent intent = new Intent(textView.getContext(), MainActivity.class);
                        itemView.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(), substring + " does not exist!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            //-------------- Links
            ClickableSpan linksSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    TextView tx = (TextView) textView;
                    String url = tx.getText().toString();
                    String substring = url;
                    System.out.println(url + " URL");
                    System.out.println(substring + "SUBSTRING");
                    if(url.contains("www")) {
                        int startIndex = url.indexOf("www");
                        int endIndex;
                        substring = url.substring(startIndex);
                        if (substring.contains(" ")) {
                            endIndex = substring.indexOf(" ");
                        } else {
                            endIndex = substring.length();
                        }
                        substring = substring.substring(0,endIndex);
                    }

                    if (!substring.startsWith("http://") && !substring.startsWith("https://")) {
                        substring = "http://" + substring;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(substring));
                    startActivity(browserIntent);
                }
            };


            List<String> userMentions = status.getUserMentions();
            List<String> links = status.getLinks();

            for(int i = 0; i < userMentions.size(); i++){
                int beginningIndex = messageCopy.indexOf(userMentions.get(i));
                int endingIndex = userMentions.get(i).length() + beginningIndex;

                ss.setSpan(userMentionsSpan, beginningIndex, endingIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for(int i = 0; i < links.size(); i++){
                int beginningIndex = messageCopy.indexOf(links.get(i));
                int endingIndex = links.get(i).length() + beginningIndex;

                ss.setSpan(linksSpan, beginningIndex, endingIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            message.setText(ss);
            message.setMovementMethod(LinkMovementMethod.getInstance());
            message.setHighlightColor(Color.BLUE);

        }
    }

    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StatusHolder> implements GetStoryTask.GetStoryObserver {

        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;
        private boolean hasMorePages;
        private boolean isLoading = false;

        StoryRecyclerViewAdapter() {
            loadMoreItems();
        }

        void addItems(List<Status> newStatuses) {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        void addItem(Status status) {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        void removeItem(Status status) {
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if(isLoading) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.story_row, parent, false);
            }

            return new StatusHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusHolder statusHolder, int position) {
            if(!isLoading) {
                statusHolder.bindUser(statuses.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return statuses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }


        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetStoryTask getStoryTask = new GetStoryTask(presenter, this);
            StoryRequest request = new StoryRequest(presenter.getCurrentUser(), PAGE_SIZE, lastStatus);
            getStoryTask.execute(request);
        }

        @Override
        public void storyRetrieved(StoryResponse storyResponse) {
            List<Status> statusList = storyResponse.getStatusList();

            lastStatus = (statusList.size() > 0) ? statusList.get(statusList.size() -1) : null;
            hasMorePages = storyResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(statusList);
        }

        private void addLoadingFooter() {
            addItem(new Status(new User("Dummy", "User", ""), "This is a message"));
        }

        private void removeLoadingFooter() {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }


    private class StatusRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        StatusRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
