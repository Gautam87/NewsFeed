package com.gautam.newsapp.data.remote;

import android.os.AsyncTask;
import android.util.Log;

import com.gautam.newsapp.data.model.Article;
import com.gautam.newsapp.data.model.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Async task class to get json by making HTTP call
 */
public class GetNewsTask extends AsyncTask<String, Integer, List<Article>> {

    private static final String TAG = "GetNewsTask";

    private OnNewsTaskListener mListener;

    public GetNewsTask(OnNewsTaskListener mListener) {
        this.mListener = mListener;
    }

    public GetNewsTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Todo Show loading

    }

    @Override
    protected List<Article> doInBackground(String... params) {
        HttpHandler sh = new HttpHandler();
        ArrayList<Article> articles = null;
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(params[0]);

        Log.i(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                String status = jsonObj.getString("status");

                if (status.equals("ok")) {
                    // Getting JSON Array node
                    JSONArray array = jsonObj.getJSONArray("articles");
                    articles = new ArrayList<>();

                    // looping through All Articles
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject a = array.getJSONObject(i);

                        String author = a.getString("author");
                        String title = a.getString("title");
                        String description = a.getString("description");
                        String url = a.getString("url");
                        String urlToImage = a.getString("urlToImage");
                        String publishedAt = a.getString("publishedAt");
                        String content = a.getString("content");

                        // Source node is JSON Object
                        JSONObject obj = a.getJSONObject("source");
                        Object id = obj.getString("id");
                        String name = obj.getString("name");
                        Source source = new Source(id, name);

                        Article article = new Article(source, author, title, description, url, urlToImage, publishedAt, content, false);

                        // adding articles to articles list
                        articles.add(article);
                    }
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");

        }

        return articles;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
        if (mListener != null) {
            mListener.fetchedArticleList(articles);
        }
        // Dismiss the loading
        //Todo display in ui
    }

    public interface OnNewsTaskListener {
        void fetchedArticleList(List<Article> articles);
    }
}
