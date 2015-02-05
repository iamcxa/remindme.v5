package tw.geodoer.mPriority.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import tw.geodoer.main.taskList.view.AppMainActivity;
import tw.moretion.geodoer.R;

/**
 * Created by MurasakiYoru on 2015/1/20.
 */
public class GeoServiceNotification extends Service {
    public static final String MESSAGE = "Message";
    private static final int ID_My_Notification = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.noti("智慧提醒", intent.getExtras().getString(this.MESSAGE));
        stopSelf();
        return flags;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void noti(String title, String content) {
        //Manager
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nNotificationManager = (NotificationManager) getSystemService(ns);

        //element of notification
        //CharSequence tickerText = "RemindmeMe22";
        long when = System.currentTimeMillis();
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.remindme_logo);

        Intent notificationIntent = new Intent(getBaseContext(), AppMainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.remindme_logo).setLargeIcon(bm)
                .setWhen(when)
                .setContentIntent(contentIntent)
                .build();

        //notification.flags =Notification.FLAG_NO_CLEAR;

        nNotificationManager.notify(ID_My_Notification, notification);

    }
}
