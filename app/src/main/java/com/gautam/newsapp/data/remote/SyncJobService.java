package com.gautam.newsapp.data.remote;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class SyncJobService extends JobService {

    public static final int JOB_ID = 101;

    JobParameters params;
    GetNewsTask getNewsTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.params = jobParameters;
//        Toast.makeText(this, "Job Starts", Toast.LENGTH_SHORT).show();
        getNewsTask = new GetNewsTask(this,null);
        getNewsTask.execute(ApiConstants.BASE_URL+"everything?q=bitcoin&from=2020-01-13&sortBy=publishedAt&apiKey="+ApiConstants.API_KEY);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (getNewsTask != null)
            getNewsTask.cancel(true);
        return false;
    }
}