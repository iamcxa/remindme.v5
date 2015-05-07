package tw.geodoer.main.taskAlert.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.geodoer.geotodo.R;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mPriority.controller.DBtoGeoinfo;
import tw.geodoer.mPriority.controller.NeoGeoInfo;
import tw.geodoer.main.taskList.view.AppMainActivity;

/**
 * Created by MurasakiYoru on 2015/5/1.
 */
public class GeoAlertService extends Service implements Runnable
{

    private static String TAG = "GeoService";
    private static int ID = 1;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.wtf(TAG,"@GeoAlertService onCreate "+System.currentTimeMillis());

        new Thread(this).start();
        stopSelf();
    }

    @Override
    public void onDestroy()
    {
        Log.wtf(TAG,"@GeoAlertService onDestroy "+System.currentTimeMillis());

        super.onDestroy();
    }


    @Override
    public void run()
    {
        HandlerThread mHT = new HandlerThread("GeoService");


        DBTasksHelper DBT= null;
        DBT = new DBTasksHelper(this);
        ArrayList<Integer> ids = null;
        ids = DBT.getIDArrayListOfTask();

        if(ids!=null)
            for(int i:ids)
            {
                if(DBT.getItemInt(i, ColumnTask.KEY.status)==0)
                    Log.wtf(TAG, "@Service: id=" + i);
            }
    }

    private void noti()
    {
        NotificationManager nNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);


        Intent notificationIntent = new Intent(this, AppMainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("GeoService")
                .setContentText("Running")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.remindme_logo)
                .build();

        nNotificationManager.notify(TAG,ID, notification);
    }
}



class AlertCheck_Thread implements Runnable
{
    private Context mContext = null;
    public AlertCheck_Thread(Context context)
    {
        super();
        mContext = context;
    }
    @Override
    public void run()
    {

    }
}

class LocationAlertCheck_Thread implements Runnable
{
    @Override
    public void run() {

    }
}
