package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mGeoInfo.API.DistanceCalculator;
import tw.geodoer.mPriority.API.BroadcastSender;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/1/23.
 */
public class GeoServicePosition extends Service
{
    private double Now_Lat, Now_Lon;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        MyDebug.MakeLog(2,"@ Position Service Start");

        boolean check = this.GetNowPos(); //false: when load now position fail

        if( check)
            check = this.distance_updater(); //false: no event can be change
        if( check)
            BroadcastSender.send(this, BroadcastSender.KEY_WEIGHT);


            //MyDebug.MakeLog(2,"call out Service Weight");

//            final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
//            Intent intent = new Intent(this,GeoBroadcastReceiver_TaskAlert.class);
//            // 設定Intent action屬性
//            intent.setAction(BC_ACTION);
//            intent.putExtra("msg", "me.iamcxa.remindme.weight");
//            sendBroadcast(intent);


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

        LocationManager lms = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
        Location location;

        //由程式判斷用GPS_provider
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) )
            location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        else if ( lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        else
            location = null;


        if(location != null)
        {
            this.Now_Lat = location.getLatitude();     //取得緯度
            this.Now_Lon = location.getLongitude();   //取得經度

            MyDebug.MakeLog(2,"Now position =Lat: " +this.Now_Lat +" ,Lon: "+this.Now_Lon);
        }
        else
        {
            //MyDebug.MakeLog(2,"now position get error");
            return false;
        }

        //MyDebug.MakeLog(2,"get now position success");
        return  true;
    }


    public boolean distance_updater()
    {
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());
        DBAlertHelper dbAlertHelper = new  DBAlertHelper(getApplicationContext());

        //get number of events
        int count=dbAlertHelper.getCountOfUnFinishedTask();
        //MyDebug.MakeLog(2,"there are "+count+" events can be updated with distance");
        //count = 0;
        if(count==0)return false; //if no event can be change , stop thread
        //--------------------------------------------------------------------------------------

        //get all id witch need update distance in db_alert
        try
        {
            //MyDebug.MakeLog(2, "1");
            ArrayList<Integer> db_alert_list = dbAlertHelper.getIDArrayListOfUnFinishedTask();

            int loc_id;
            double itemLat, itemLon, distance;
            //MyDebug.MakeLog(2, "2");

            if(db_alert_list!=null && (!db_alert_list.isEmpty()) )
            //loop all event witch need update
            for (int i : db_alert_list)  //i
            {
                //MyDebug.MakeLog(2,""+i);

                loc_id = dbAlertHelper.getItemInt(i, ColumnAlert.KEY.loc_id);

                itemLat = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lat);
                itemLon = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lon);

                //if event have no position will be (-1,-1) and distance be zero
                if (itemLat != -1 || itemLon != -1)
                {
                    distance = DistanceCalculator.haversine(this.Now_Lat, this.Now_Lon, itemLat, itemLon);
                }
                else
                {
                    distance = 0;
                }
                dbLocationHelper.setItem(loc_id, ColumnLocation.KEY.distance, distance);

            }
            //MyDebug.MakeLog(2, "distance_updater success");
            return true;
        }
        catch (Exception e)
        {
            MyDebug.MakeLog(2,"Exception :"+e.toString());
            return false;
        }
    }
}
