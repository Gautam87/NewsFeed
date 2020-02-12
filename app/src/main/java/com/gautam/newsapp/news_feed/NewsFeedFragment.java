package com.gautam.newsapp.news_feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gautam.newsapp.R;
import com.gautam.newsapp.adapters.ArticleListAdapter;
import com.gautam.newsapp.data.model.Article;

import java.util.List;


public class NewsFeedFragment extends Fragment implements NewsFeedContract.View, ArticleListAdapter.OnItemInteractionListener {

    private TextView mInfoView;
    private RecyclerView mRecyclerView;

    private static final String TAG = "NewsFeedFragment";
    private NewsFeedPresenter mNewsFeedPresenter;
    private ArticleListAdapter mArticleListAdapter;


    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mNewsFeedPresenter = new NewsFeedPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mInfoView = view.findViewById(R.id.info_view);
        mNewsFeedPresenter.attachView(this);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mNewsFeedPresenter.checkUserListInDb();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //avoid fragment to be destroyed on screen rotation
        setRetainInstance(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewsFeedPresenter.detachView();
    }

    @Override
    public void showComplete() {
    }

    @Override
    public void showLoader() {
        //Todo show progress indicator
    }

    @Override
    public void hideLoader() {
        //Todo hide progress indicator
    }

    @Override
    public void showArticleList(List<Article> articles) {
        mArticleListAdapter = new ArticleListAdapter(getActivity(), articles, this);
        mRecyclerView.setAdapter(mArticleListAdapter);

    }

    @Override
    public void showInfoView() {
        mInfoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setInfoViewMessage(String message) {
        mInfoView.setText(message);
    }
    @Override
    public void setInfoViewMessage(int resId) {
        mInfoView.setText(resId);
    }

    @Override
    public void hideInfoView() {
        mInfoView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTitle(int resId) {
        if (getActivity() != null ) {
            getActivity().setTitle(resId);
        }
    }

//    @Override
//    public void showSnackBar(String message) {
//        if (getActivity() != null) {
//            Snackbar.make(getActivity().findViewById(android.R.id.content),
//                    message, Snackbar.LENGTH_LONG).show();
//        }
//    }

    @Override
    public void tryAgainClicked() {
        mNewsFeedPresenter.fetchArticleListFromApi();
    }

    @Override
    public void notifyRecyclerDataChange(int position, Boolean bookmarked) {
        if(mArticleListAdapter !=null) {
            mArticleListAdapter.setBookmark(position, bookmarked);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news_feed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                tryAgainClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookmarkClicked(String title, int position) {
        mNewsFeedPresenter.bookmarkClicked(title,position);
    }
}


