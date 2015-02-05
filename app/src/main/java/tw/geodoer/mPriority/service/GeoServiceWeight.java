package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import tw.geodoer.mPriority.API.WeightCalculator;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/1/23.
 */
public class GeoServiceWeight extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        MyDebug.MakeLog(2,"@Service Weight Start");
        weight_updater();
        stopSelf();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return flags;
    }

    public boolean weight_updater()
    {


        //get every events index from db_alarm

        // start all event
        // (loop start)

        //get due date from db_alarm

        //get loc id from db_alarm
        //get distance from db_loc

        //cal weight with due date(millis) and distance(km)
        final WeightCalculator Cal = new WeightCalculator();
        //double weight =  Cal.getweight(date(millis),distance(km) )

        //get task_id from db_alarm
        //set weight in db_task

        //(loop end)

        MyDebug.MakeLog(2,"weight_updater success");
        return true;
    }
}
