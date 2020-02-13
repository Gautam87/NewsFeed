package com.gautam.newsapp;


import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gautam.newsapp.news_feed.NewsFeedActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
	private static final String TAG = "MessagingService";
	public static final String CHANNEL_ID = "admin";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		if (remoteMessage.getData() != null) {
			showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
		}
	}

	public void showNotification(String title, String message) {

		Intent notificationIntent = new Intent(this, NewsFeedActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setContentTitle(title)
				.setContentText(message)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentIntent(pendingIntent);

		NotificationManagerCompat.from(this).notify(1, notification.build());
	}


	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. Note that this is called when the InstanceID token
	 * is initially generated so this is where you would retrieve the token.
	 */
	@Override
	public void onNewToken(String token) {
		Log.d(TAG, "Refreshed token: " + token);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
		//todo sendRegistrationToServer(token);
	}

}
