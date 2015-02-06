package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mPriority.API.WeightCalculator;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/1/23.
 */
public class GeoServiceWeight extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        MyDebug.MakeLog(2,"@ Service Weight Start");
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
        DBAlertHelper dbAlertHelper = new  DBAlertHelper(getApplicationContext());
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());
        WeightCalculator Cal = new WeightCalculator();
        long left_time,currentTimeMillis = System.currentTimeMillis();
        int loc_id;
        double left_distance,weight;
        boolean safe_check;

        //get every events index from db_alarm
        ArrayList<Integer> db_alert_list = dbAlertHelper.getIDArrayListOfUnFinishedTask();

        //start for each event
        if(db_alert_list!=null && (!db_alert_list.isEmpty()) )
        for(int i : db_alert_list)
        {
            safe_check=false;

            try
            {
                //get loc_id
                loc_id = dbAlertHelper.getItemInt(i, ColumnAlert.KEY.loc_id);

                //get left distance
                left_distance = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.distance);

                //get left time
                left_time = dbAlertHelper.getItemLong(i, ColumnAlert.KEY.due_date_millis) - currentTimeMillis;

                //cal the weight
                weight = Cal.getweight(left_time, left_distance);

                //set back weight to db_loc
                dbLocationHelper.setItem(loc_id,ColumnLocation.KEY.weight,weight);

                //MyDebug.MakeLog(2,"weight update to event="+loc_id+" success");
            }
            catch(Exception e)
            {
                safe_check=true;
                MyDebug.MakeLog(2,"weight update exception :"+e.toString());
            }
            if(safe_check)continue;
        }

        //MyDebug.MakeLog(2,"weight_updater success");
        return true;
    }
}
