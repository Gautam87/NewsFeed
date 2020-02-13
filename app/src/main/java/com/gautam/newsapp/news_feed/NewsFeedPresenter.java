package com.gautam.newsapp.news_feed;

import com.gautam.newsapp.R;
import com.gautam.newsapp.base.BasePresenter;
import com.gautam.newsapp.data.database.DBManager;
import com.gautam.newsapp.data.model.Article;
import com.gautam.newsapp.data.remote.ApiConstants;
import com.gautam.newsapp.data.remote.GetNewsTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedPresenter extends BasePresenter<NewsFeedContract.View> implements NewsFeedContract.Presenter, GetNewsTask.OnNewsTaskListener {

    private static final String TAG = "NewsFeedPresenter";

//    private CompositeDisposable compositeDisposable;
//    private ApiManager apiManager = new ApiManager();


    public NewsFeedPresenter() {
    }

    @Override
    public void attachView(NewsFeedContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void checkUserListInDb() {
        getMvpView().showLoader();
        DBManager dbManager = new DBManager(getMvpView().getContext());
        dbManager.open();
        List<Article> articles = dbManager.fetch();
        dbManager.close();
        if (articles.size() > 0) {
            handleArticleListFromDb(articles);
        } else {
            fetchArticleListFromApi();
        }
    }

    @Override
    public void handleEmptyDb() {
        getMvpView().setInfoViewMessage(R.string.error_no_articles_in_db);
        getMvpView().showInfoView();
    }

    @Override
    public void handleArticleListFromDb(List<Article> articles) {
        getMvpView().hideLoader();
        getMvpView().hideInfoView();
        getMvpView().showArticleList(articles);
        getMvpView().setTitle(R.string.articles_list_db);

    }

    @Override
    public void fetchArticleListFromApi() {
        if (checkNetwork()) {
            getMvpView().showLoader();
            getMvpView().showInfoView();

            new GetNewsTask(getMvpView().getContext(),this).execute(ApiConstants.BASE_URL + "everything?q=bitcoin&from=2020-01-13&sortBy=publishedAt&apiKey=" + ApiConstants.API_KEY);


        } else {
            getMvpView().hideLoader();
            getMvpView().setInfoViewMessage(R.string.error_network_unavailable);
            getMvpView().showInfoView();
        }
    }

    @Override
    public void handleArticleListFromApi(List<Article> articles) {
        if (articles != null) {

            if (articles.size() > 0) {
                getMvpView().showArticleList(articles);
                getMvpView().hideLoader();
                getMvpView().hideInfoView();
                getMvpView().setTitle(R.string.articles_list_api);
            } else {
                getMvpView().hideLoader();
                getMvpView().setInfoViewMessage(R.string.error_no_articles);
                getMvpView().showInfoView();
            }
        } else {
            getMvpView().hideLoader();
            getMvpView().setInfoViewMessage(R.string.error_incorrect_response_code);
            getMvpView().showInfoView();
        }
    }


    @Override
    public boolean checkNetwork() {

        try {
            return isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    @Override
    public void handleNetworkAvailable() {
        this.fetchArticleListFromApi();
    }

    @Override
    public void handleNetworkNotAvailable() {
        getMvpView().setInfoViewMessage(R.string.error_network_unavailable);
        getMvpView().showInfoView();
    }

    @Override
    public void handleTryAgainClicked() {
        fetchArticleListFromApi();
    }

    @Override
    public void handleError() {
        getMvpView().hideLoader();
        getMvpView().setInfoViewMessage(R.string.error_something_went_wrong);
        getMvpView().showInfoView();
    }

    @Override
    public void bookmarkClicked(String url, int position) {
        DBManager dbManager = new DBManager(getMvpView().getContext());
        dbManager.open();
        boolean isBookmarked= dbManager.isArticleBookmarked(url);
        dbManager.updateBookmark(url,!isBookmarked);
        dbManager.close();
        getMvpView().notifyRecyclerDataChange(position, !isBookmarked);
    }

    @Override
    public void fetchedArticleList(List<Article> articles) {
        DBManager dbManager = new DBManager(getMvpView().getContext());
        dbManager.open();
        handleArticleListFromApi(dbManager.fetch());
        dbManager.close();
    }
}
