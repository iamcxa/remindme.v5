package tw.geodoer.main.taskAlert.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by MurasakiYoru on 2015/5/1.
 */
public class GeoServiceStarter
{
    public static void startService(Context context)
    {
        //Log.wtf("GeoService", "@GeoServiceStarter startService");

        AlarmManager AM = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent start_intent = new Intent(context, GeoAlertService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                start_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long sec = 5;
        long triggerAtTime;
        triggerAtTime = System.currentTimeMillis();
        triggerAtTime -= triggerAtTime%(1000*sec);
        triggerAtTime += 1000*sec;
        //Log.wtf("GeoService", "@GeoServiceStarter "+System.currentTimeMillis());
        //Log.wtf("GeoService", "@GeoServiceStarter "+triggerAtTime);
        //AM.setRepeating(AlarmManager.RTC, triggerAtTime, sec*1000, pendingIntent);
    }

}
