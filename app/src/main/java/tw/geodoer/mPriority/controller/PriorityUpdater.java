package tw.geodoer.mPriority.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mGeoInfo.API.DistanceCalculator;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/4/11.
 */
public class PriorityUpdater
{
    private Context mContext;
    private double Now_Lat, Now_Lon;

    private DBTasksHelper dbTaskHelper;
    private DBLocationHelper dbLocationHelper;
    private DBAlertHelper dbAlertHelper;

    private final WeightCalculator Cal;
    private long currentTimeMillis ;

    public PriorityUpdater(Context context)
    {
        this.mContext = context;
        this.Cal = new WeightCalculator();

        this.dbLocationHelper=new DBLocationHelper(mContext);
        this.dbAlertHelper = new  DBAlertHelper(mContext);
        this.dbTaskHelper = new DBTasksHelper(mContext);
    }


    public void PirorityUpdate()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                //MyDebug.MakeLog(2,"PirorityUpdating~");
                if (GetNowPosition())
                {
                    Update();
                }
                else
                {
                    Toast.makeText(mContext,"No GPS provide, Refresh Stoped",Toast.LENGTH_SHORT);
                    return;
                }



                return;

            }
        }).start();

    }
    private void GetNowTime()
    {
        this.currentTimeMillis = System.currentTimeMillis();
    }
    private boolean GetNowPosition()
    {
        //GetNowTime();

        //Get Now position from GPS
        //true: load success / false: load fail

        LocationManager lms = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE); //取得系統定位服務
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
            return true;
        }
        else
        {

            return false;
        }
        //else return true;
    }

    private boolean Update()
    {
        //get number of events
        //int count=dbAlertHelper.getCountOfUnFinishedTask();
        int count = dbTaskHelper.getCount();
        if(count==0)return false; //if no event can be change , stop thread
        //--------------------------------------------------------------------------------------

        //ArrayList<Integer> db_alert_list = dbAlertHelper.getIDArrayListOfUnFinishedTask();
        ArrayList<Integer> db_list = dbTaskHelper.getIDArrayListOfTask();

        long left_time,item_time;
        int loc_id,alert_id;
        int weight;

        double itemLat, itemLon, distance;
        //boolean safe_check;

        //get all id witch need update distance in db_alert
        try
        {
            if(db_list!=null && (!db_list.isEmpty()) )
            {
                //loop all event witch need update
                for (int task_id : db_list)  //i
                {
                    //get ids for any row
                    loc_id   = dbTaskHelper.getItemInt(task_id, ColumnAlert.KEY.loc_id);
                    //alert_id = dbTaskHelper.getItemInt(task_id, ColumnAlert.KEY.actFri);

                    //get position
                    if(loc_id!=0)
                    {
                        //get positions from loc
                        itemLat = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lat);
                        itemLon = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lon);

                        distance = DistanceCalculator.haversine(this.Now_Lat, this.Now_Lon, itemLat, itemLon);
                        dbLocationHelper.setItem(loc_id, ColumnLocation.KEY.distance, distance);
                    }
                    else
                    {
                        distance = 0;
                    }

                    //get time
                    item_time = dbTaskHelper.getItemLong(task_id, ColumnAlert.KEY.due_date_millis);
                    if(item_time > 0)
                    {
                        GetNowTime();
                        left_time = item_time - this.currentTimeMillis;
                    }
                    else left_time=0;

                    //cal the weight
                    weight = Cal.getweight(left_time, distance);


                    //set back weight to db_loc
                    //dbLocationHelper.setItem(loc_id, ColumnLocation.KEY.weight, weight);

                    //set back weight to db_task
                    dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority, weight);

                    MyDebug.MakeLog(2, "task_id="+task_id+"loc_id="+loc_id + " ,pT=" + left_time + " ,pL=" + distance + " ,wei=" + weight);
                }
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
