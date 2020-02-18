package edu.byu.cs.tweeter.view.main.feed;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
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
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFeedTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class FeedFragment extends Fragment implements FeedPresenter.View{

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private FeedPresenter presenter;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        presenter = new FeedPresenter(this);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);
        feedRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }


    private class StatusHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView message;

        StatusHolder(@NonNull final View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            message = itemView.findViewById(R.id.message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "You selected '" + userName.getText() + "'.", Toast.LENGTH_SHORT).show();
                    LoginService.getInstance().setCurrentUser(presenter.getUserByAlias(userAlias.getText().toString()));

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        void bindUser(Status status) {
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(status.getUser()));
            userAlias.setText(status.getUser().getAlias());
            userName.setText(status.getUser().getName());
            message.setText(status.getMessage());

            String messageCopy = message.getText().toString();
            SpannableString ss = new SpannableString(messageCopy);

            //--------------- User mentions
            ClickableSpan userMentionsSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {        //TODO: CHANGE SERVER FACADE TO ONLY DO SO MANY STATUSES
                    TextView tx = (TextView) textView;
                    String s = tx.getText().toString();
                    User newUser = presenter.getUserByAlias(tx.getText().toString());
                    if(newUser != null){
                        LoginService.getInstance().setCurrentUser(newUser);

                        Intent intent = new Intent(textView.getContext(), MainActivity.class);
                        itemView.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(), tx.getText().toString() + " does not exist!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            //-------------- Links
            ClickableSpan linksSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    TextView tx = (TextView) textView;
                    String url = tx.getText().toString();

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
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

    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<StatusHolder> implements GetFeedTask.GetFeedObserver {

        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;
        private boolean hasMorePages;
        private boolean isLoading = false;

        FeedRecyclerViewAdapter() {
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
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if(isLoading) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.feed_row, parent, false);
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

            GetFeedTask getfeedTask = new GetFeedTask(presenter, this);
            FeedRequest request = new FeedRequest(presenter.getCurrentUser(), PAGE_SIZE, lastStatus);
            getfeedTask.execute(request);
        }

        @Override
        public void feedRetrieved(FeedResponse feedResponse) {
            List<Status> statusList = feedResponse.getStatuses();

            lastStatus = (statusList.size() > 0) ? statusList.get(statusList.size() -1) : null;
            hasMorePages = feedResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            feedRecyclerViewAdapter.addItems(statusList);
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

            if (!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    feedRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
