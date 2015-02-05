package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mGeoInfo.API.DistanceCalculator;
import tw.geodoer.mPriority.eventReceiver.GeoBroadcastReceiver_TaskAlert;
import tw.geodoer.main.taskList.view.AppMainActivity;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/1/23.
 */
public class GeoServicePosition extends Service
{
    private double Last_Lat, Last_Lon;
    private double Now_Lat, Now_Lon;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        MyDebug.MakeLog(2,"@Position Service Start");

        boolean check = this.GetNowPos(); //false: when load now position fail


        if( check)
        {
            check = this.distance_updater(); //false: no event can be change
            MyDebug.MakeLog(2,"distance_updater success");
        }








        if( check)
        {
            MyDebug.MakeLog(2,"call out Service Weight");


            final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
            Intent intent = new Intent(this,GeoBroadcastReceiver_TaskAlert.class);
            // 設定Intent action屬性
            intent.setAction(BC_ACTION);
            intent.putExtra("msg", "me.iamcxa.remindme.weight");
            sendBroadcast(intent);
        }
        stopSelf();

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return flags;
    }


    public boolean GetNowPos()
    {
        //Get Now position from GPS
        //true: load success / false: load fail

        this.Now_Lat = 23.0;
        this.Now_Lon = 120.0;

        return  true;
    }


    public boolean distance_updater()
    {
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());
        DBAlertHelper dbAlertHelper = new  DBAlertHelper(getApplicationContext());
        double itemLat,itemLon,distance;


        //get number of events
        int count=dbAlertHelper.getCountByUnFinishedTask();
        MyDebug.MakeLog(2,"there are "+count+" events can be updated with distance");

        //count = 0;
        if(count==0)return true; //if no event can be change , stop thread
        //--------------------------------------------------------------------------------------


        //get all id witch need update distance in db_alert

        //get all the loc id in db_alert



        //loop all event witch need update
        for(int i =0;i<count ; i++)  //i
        {






            itemLat= dbLocationHelper.getItemDouble(i, ColumnLocation.KEY.lat);
            itemLon= dbLocationHelper.getItemDouble(i, ColumnLocation.KEY.lon);

            //if event have no position will be (-1,-1) and distance be zero
            if(itemLat!=-1 || itemLon!= -1)
            {
                distance = DistanceCalculator.haversine(this.Last_Lat, this.Last_Lon, itemLat, itemLon);
            }
            else
            {
                distance = 0;
            }
            dbLocationHelper.setItem(i, ColumnLocation.KEY.distance, distance);



            MyDebug.MakeLog(2,"set the "+i+" event's distance success");
        }
        return true;
    }
}
