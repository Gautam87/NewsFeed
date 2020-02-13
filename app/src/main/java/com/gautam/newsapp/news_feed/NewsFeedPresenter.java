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
    public void bookmarkClicked(String url, int position) {
        DBManager dbManager = new DBManager(getMvpView().getContext());
        dbManager.open();
        boolean isBookmarked= dbManager.isArticleBookmarked(url);
        dbManager.updateBookmark(url,!isBookmarked);
        dbManager.close();
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
