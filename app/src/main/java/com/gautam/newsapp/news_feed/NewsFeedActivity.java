package com.gautam.newsapp.news_feed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gautam.newsapp.R;
import com.gautam.newsapp.data.remote.GetNewsTask;

public class NewsFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        new GetNewsTask().execute("https://newsapi.org/v2/everything?q=bitcoin&from=2020-01-12&sortBy=publishedAt&apiKey=686cc10b0378462b9f6fe6273bed4595");
    }
}
