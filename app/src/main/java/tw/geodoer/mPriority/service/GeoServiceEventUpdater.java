package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mGeoInfo.API.DistanceCalculator;
import tw.geodoer.mPriority.API.WeightCalculator;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/1/23.
 */
public class GeoServiceEventUpdater extends Service
{
    private double Now_Lat, Now_Lon;
    private final WeightCalculator Cal = new WeightCalculator();
    private long currentTimeMillis = System.currentTimeMillis();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        MyDebug.MakeLog(2,"@ Position Service Start");

        switch ( this.GetNow() )
        {
            case 1:
                this.updater_TP();
                break;
            case 2:
                this.updater_T();
                break;
            default:
        }
        stopSelf();

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return flags;
    }


    public int GetNow()
    {
        //Get Now position from GPS
        //true: load success / false: load fail

        LocationManager lms = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
        Location location;

        //判斷用GPS_provider
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
            return 1;
        }
        else
        {
            MyDebug.MakeLog(2,"get no position, start only with time");
            return 2;
        }
        //else return true;
    }

    public boolean updater_TP()
    {
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());
        DBAlertHelper dbAlertHelper = new  DBAlertHelper(getApplicationContext());
        DBTasksHelper dbTaskHelper = new DBTasksHelper(getApplicationContext());
        //get number of events
        int count=dbAlertHelper.getCountOfUnFinishedTask();
        if(count==0)return false; //if no event can be change , stop thread
        //--------------------------------------------------------------------------------------

        ArrayList<Integer> db_alert_list = dbAlertHelper.getIDArrayListOfUnFinishedTask();
        long left_time;
        int loc_id,task_id,weight;
        double itemLat, itemLon, distance;
        //boolean safe_check;

        //get all id witch need update distance in db_alert
        try
        {
            if(db_alert_list!=null && (!db_alert_list.isEmpty()) )
            //loop all event witch need update
            for (int i : db_alert_list)  //i
            {
                loc_id = dbAlertHelper.getItemInt(i, ColumnAlert.KEY.loc_id);
                task_id = dbAlertHelper.getItemInt(i,ColumnAlert.KEY.task_id);


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




                //get left time
                left_time = dbAlertHelper.getItemLong(i, ColumnAlert.KEY.due_date_millis) - currentTimeMillis;

                //cal the weight
                weight = Cal.getweight(left_time, distance);

                //set back weight to db_loc
                dbLocationHelper.setItem(loc_id,ColumnLocation.KEY.weight,weight);

                //set back weight to db_task
                dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority,weight);


                MyDebug.MakeLog(2,"al_id="+i+" ,loc_id="+loc_id+" ,pT=" +left_time+" ,pL=" +distance+" ,wei=" + weight);

            }

            return true;
        }
        catch (Exception e)
        {
            MyDebug.MakeLog(2,"Exception :"+e.toString());
            return false;
        }
    }

    public boolean updater_T()
    {
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());
        DBAlertHelper dbAlertHelper = new  DBAlertHelper(getApplicationContext());
        DBTasksHelper dbTaskHelper = new DBTasksHelper(getApplicationContext());
        //get number of events
        int count=dbAlertHelper.getCountOfUnFinishedTask();
        if(count==0)return false; //if no event can be change , stop thread
        //--------------------------------------------------------------------------------------

        ArrayList<Integer> db_alert_list = dbAlertHelper.getIDArrayListOfUnFinishedTask();
        long left_time;
        int loc_id,task_id,weight;
        //boolean safe_check;
        currentTimeMillis = System.currentTimeMillis();
        //get all id witch need update distance in db_alert
        try
        {
            if(db_alert_list!=null && (!db_alert_list.isEmpty()) )
                //loop all event witch need update
                for (int i : db_alert_list)  //i
                {
                    loc_id = dbAlertHelper.getItemInt(i, ColumnAlert.KEY.loc_id);
                    task_id = dbAlertHelper.getItemInt(i,ColumnAlert.KEY.task_id);


                    //get left time
                    left_time = dbAlertHelper.getItemLong(i, ColumnAlert.KEY.due_date_millis) - currentTimeMillis;

                    //cal the weight
                    weight = Cal.getweight(left_time, 0);

                    //set back weight to db_loc
                    //dbLocationHelper.setItem(loc_id,ColumnLocation.KEY.weight,weight);

                    //set back weight to db_task
                    dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority ,weight);

                    MyDebug.MakeLog(2,"tk_id="+task_id +"pT=" +left_time+" ,pL=0" +",wei=" + weight);

                }

            return true;
        }
        catch (Exception e)
        {
            MyDebug.MakeLog(2,"Exception :"+e.toString());
            return false;
        }

    }
}
