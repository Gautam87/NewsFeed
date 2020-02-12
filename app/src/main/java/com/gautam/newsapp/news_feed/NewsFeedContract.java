package com.gautam.newsapp.news_feed;

import com.gautam.newsapp.base.MvpView;
import com.gautam.newsapp.data.model.Article;
import com.gautam.newsapp.data.model.NewsFeedResponse;

import java.util.List;

public class NewsFeedContract {

    public interface View extends MvpView {

        void showLoader();

        void hideLoader();

        void showArticleList(List<Article> userItems);

        void showInfoView();

        void setInfoViewMessage(int resId);

        void setInfoViewMessage(String message);

        void hideInfoView();

        void setTitle(int resId);

        void tryAgainClicked();

        void notifyRecyclerDataChange(int idLocal, Boolean bookmarked);
    }

    interface Presenter {

        void checkUserListInDb();

        void handleEmptyDb();

        void handleArticleListFromDb(List<Article> userItems);

        void fetchArticleListFromApi();

        void handleArticleListFromApi(List<Article> userItems);

        boolean checkNetwork();

        void handleNetworkAvailable();

        void handleNetworkNotAvailable();

        void handleTryAgainClicked();

        void handleError();

        void bookmarkClicked(String title, int position);
    }
}
