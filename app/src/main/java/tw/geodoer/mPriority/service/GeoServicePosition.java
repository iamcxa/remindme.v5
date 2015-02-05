package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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

        boolean check = this.GetLastPos() && this.GetNowPos(); //false: when load position fail
        if( check)
        {
            check = this.position_updater(); //false: when at the same position
            MyDebug.MakeLog(2,"position_updater success");
        }
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

    public boolean GetLastPos()
    {
        //Load the last position from database
        //true: load success / false: load fail

        this.Last_Lat = 22.0;
        this.Last_Lon = 120.0;




        return  true;
    }
    public boolean GetNowPos()
    {
        //Get Now position from GPS
        //true: load success / false: load fail

        this.Now_Lat = 23.0;
        this.Now_Lon = 120.0;




        return  true;
    }

    public boolean position_updater()// true = update success  / false = at the same position
    {

        if(this.Last_Lat == this.Now_Lat && this.Last_Lon ==this.Now_Lon)
        {
            MyDebug.MakeLog(2,"get the same position");
            //if at the same position , it will jump out the thread
            return false;
        }
        else
        {
            //set new position in last position in database and

            this.Last_Lat = this.Now_Lat;
            this.Last_Lon = this.Now_Lon;

            MyDebug.MakeLog(2,"get not the same position");
            return true;
        }
    }

    public boolean distance_updater()
    {
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());

        //get number of events
        int count=dbLocationHelper.getCount();

        MyDebug.MakeLog(2,"there are "+count+" event can be updated distance");
        count = 0;
        if(count==0)return true; //if no event can be change , stop thread


        double itemLat,itemLon,distance;
        //DistanceCalculator Cal = new DistanceCalculator();

        for(int i =0;i<count ; i++)
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
