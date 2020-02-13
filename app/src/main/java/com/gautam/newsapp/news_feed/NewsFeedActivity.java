package com.gautam.newsapp.news_feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gautam.newsapp.MessagingService;
import com.gautam.newsapp.R;
import com.gautam.newsapp.data.remote.SyncJobService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class NewsFeedActivity extends AppCompatActivity {


    private static final String TAG = "NewsFeedActivity";
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

        createNotificationChannel();
        subscribeTheClientAppToATopic();


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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(MessagingService.CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(serviceChannel);
        }
    }

    private void subscribeTheClientAppToATopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("admin")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "msg_subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
						Log.d(TAG,msg);
                    }
                });
    }
}
