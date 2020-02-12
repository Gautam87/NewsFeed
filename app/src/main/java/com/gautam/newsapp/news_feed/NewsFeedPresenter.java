package com.gautam.newsapp.news_feed;

import com.gautam.newsapp.R;
import com.gautam.newsapp.base.BasePresenter;
import com.gautam.newsapp.data.model.Article;
import com.gautam.newsapp.data.remote.GetNewsTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<Article> articles = new ArrayList<>();
//        List<Article> articles = SQLite.select().
//                from(UserItem.class).queryList();
        //Todo load from db
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
        getMvpView().setDbTitle();

    }

    @Override
    public void fetchArticleListFromApi() {
        if (checkNetwork()) {
            getMvpView().showLoader();
            getMvpView().showInfoView();

            new GetNewsTask(this).execute("https://newsapi.org/v2/everything?q=bitcoin&from=2020-01-12&sortBy=publishedAt&apiKey=686cc10b0378462b9f6fe6273bed4595");


        } else {
            getMvpView().hideLoader();
            getMvpView().setInfoViewMessage(R.string.error_network_unavailable);
            getMvpView().showInfoView();
        }
    }

    @Override
    public void handleArticleListFromApi(List<Article> articles) {
        if (articles != null) {
            getMvpView().showArticleList(articles);
            getMvpView().hideLoader();
            getMvpView().hideInfoView();
            getMvpView().setApiTitle();

//            HashMap<Integer, Article> userItemHashMap = new HashMap<>();
//            if (getUserListResponsePojo.getData() != null) {
//                for (UserItem item : getUserListResponsePojo.getData()) {
//                    if (validateID(item) && userItemHashMap.get(item.getId()) == null) {
//                        userItemHashMap.put(item.getId(), item);
//                    }
//                }
//                if (userItemHashMap.size() > 0) {
//                    List<UserItem> userItems = new ArrayList<>(userItemHashMap.values());
//                    getMvpView().showUserList(userItems);
//                    try {
//                        //clear previously stored data
//                        Delete.table(UserItem.class);
//                        //save in bulk
//                        FlowManager.getModelAdapter(UserItem.class).saveAll(userItems);
//                    } catch (Exception e) {
//                        //Exception in unit testing
//                    }
//                    getMvpView().hideLoader();
//                    getMvpView().hideInfoView();
//                    getMvpView().setApiTitle();
//
//                } else {
//                    getMvpView().hideLoader();
//                    getMvpView().setInfoViewMessage(R.string.error_no_articles);
//                    getMvpView().showInfoView();
//                }
//            }
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
    public void bookmarkClicked(int idLocal, int position) {
//        UserItem userItem = SQLite.select().
//                from(UserItem.class).
//                where(UserItem_Table.idLocal.is(idLocal)).
//                querySingle();
//        if (userItem != null) {
//            if (userItem.isBookmarked() == null || !userItem.isBookmarked()) {
//                userItem.setBookmarked(true);
//            } else {
//                userItem.setBookmarked(false);
//            }
//            userItem.save();
//        }
//        getMvpView().notifyRecyclerDataChange(position, userItem.isBookmarked());
    }

    @Override
    public void fetchedArticleList(List<Article> articles) {
        handleArticleListFromApi(articles);
    }
}
