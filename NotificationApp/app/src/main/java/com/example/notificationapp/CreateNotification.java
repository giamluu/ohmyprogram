package com.example.notificationapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notificationapp.Service.NotificationActionService;

public class CreateNotification {
    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PREVIOUS = "actionPrevious";
    public static final String ACTION_PLAY = "actionPlay";
    public static final String ACTION_NEXT = "actionNext";

    public static Notification notification;

    public static void CreateNotification(Context context, Track track, int playButton, int position, int size) {
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getImage());

            //previous
            PendingIntent pendingIntentPrevious;
            int drw_previous;
            if (position == 0) {
                pendingIntentPrevious = null;
                drw_previous = 0;
            } else {
                Intent intentPrevious = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_PREVIOUS);
                pendingIntentPrevious = PendingIntent.getBroadcast(context, 0, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);

                drw_previous = R.drawable.ic_skip_previous_black_24dp;
            }

            //play
            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

            //next
            PendingIntent pendingIntentNext;
            int drw_next;
            if (position == size) {
                pendingIntentNext = null;
                drw_next = 0;
            } else {
                Intent intentNext = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_NEXT);
                pendingIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);

                drw_next = R.drawable.ic_skip_next_black_24dp;
            }

            //create notification
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_music)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(drw_previous,"previous", pendingIntentPrevious)
                    .addAction(playButton,"play", pendingIntentPlay)
                    .addAction(drw_next,"next", pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationManagerCompat.notify(1, notification);

        }
    }
}
