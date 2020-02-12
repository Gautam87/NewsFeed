package com.gautam.newsapp.news_feed;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import com.gautam.newsapp.R;
import com.gautam.newsapp.data.remote.SyncJobService;

public class NewsFeedActivity extends AppCompatActivity {


    private NewsFeedFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        if(savedInstanceState==null){
            //activity is launched for the first time
            mFragment = new NewsFeedFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }else{
            // activity is launched second time
            mFragment = (NewsFeedFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        }

        JobInfo job = new JobInfo.Builder(SyncJobService.JOB_ID, new ComponentName(this, SyncJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler != null) {
            jobScheduler.schedule(job);
        }

    }

}
