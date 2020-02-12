package com.gautam.newsapp.data.remote;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

public class SyncJobService extends JobService {

    public static final int JOB_ID = 101;

    JobParameters params;
    GetNewsTask getNewsTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.params = jobParameters;
        Toast.makeText(this, "Job Starts", Toast.LENGTH_SHORT).show();
        getNewsTask = new GetNewsTask();
        getNewsTask.execute("https://newsapi.org/v2/everything?q=bitcoin&from=2020-01-12&sortBy=publishedAt&apiKey=686cc10b0378462b9f6fe6273bed4595");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (getNewsTask != null)
            getNewsTask.cancel(true);
        return false;
    }
}