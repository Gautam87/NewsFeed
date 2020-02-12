package com.gautam.newsapp.news_feed;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import com.gautam.newsapp.R;
import com.gautam.newsapp.data.model.Article;
import com.gautam.newsapp.data.remote.GetNewsTask;
import com.gautam.newsapp.data.remote.SyncJobService;

import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements GetNewsTask.OnNewsTaskListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        JobInfo job = new JobInfo.Builder(SyncJobService.JOB_ID, new ComponentName(this, SyncJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler != null) {
            jobScheduler.schedule(job);
        }

        new GetNewsTask(this).execute("https://newsapi.org/v2/everything?q=bitcoin&from=2020-01-12&sortBy=publishedAt&apiKey=686cc10b0378462b9f6fe6273bed4595");


    }

    @Override
    public void fetchedArticleList(List<Article> articles) {

    }
}
