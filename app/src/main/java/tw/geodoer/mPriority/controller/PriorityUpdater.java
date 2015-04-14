package tw.geodoer.mPriority.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
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

    private final PriorityCalculatorNew Cal;
    private long currentTimeMillis ;

    public PriorityUpdater(Context context)
    {
        this.mContext = context;
        this.Cal = new PriorityCalculatorNew();

        this.dbLocationHelper=new DBLocationHelper(mContext);
        this.dbAlertHelper = new  DBAlertHelper(mContext);
        this.dbTaskHelper = new DBTasksHelper(mContext);
    }


    public void PirorityUpdate()
    {
        HandlerThread mHandlerThread = new HandlerThread("PrU");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(PrU);
    }
    private Runnable PrU = new Runnable()
    {
        public void run()
        {
            MyDebug.MakeLog(2,"PirorityUpdating~");
            if (true/*GetNowPosition()*/)
            {
                Now_Lat = 0;
                Now_Lon = 0;
                MyDebug.MakeLog(2,"PirorityUpdating Updating");
                Update();
            }
            else
            {
                //MyDebug.MakeLog(2,"PirorityUpdating GetNowposition = false");
                Toast.makeText(mContext,"No GPS provide, Refresh Stoped",Toast.LENGTH_SHORT);
                return;
            }
            return;
        }
    };


    private void GetNowTime()
    {
        this.currentTimeMillis = System.currentTimeMillis();
    }
    private boolean GetNowPosition() {
        //GetNowTime();

        //Get Now position from GPS
        //true: load success / false: load fail

        LocationManager lms = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE); //取得系統定位服務
        Location location;

        //判斷用GPS_provider
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            MyDebug.MakeLog(2, "Location = GPS_PROVIDER");
        }
        else if (lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            MyDebug.MakeLog(2, "Location = NETWORK_PROVIDER");
        }
        else
        {
            location = null;
            MyDebug.MakeLog(2, "Location = null");
        }



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
//                    loc_id   = dbTaskHelper.getItemInt(task_id, ColumnAlert.KEY.loc_id);

                    //get position
//                    if(loc_id >0)
//                    {
//                        //get positions from loc
//                        itemLat = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lat);
//                        itemLon = dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lon);
//
//                        distance = DistanceCalculator.haversine(this.Now_Lat, this.Now_Lon, itemLat, itemLon);
//                        dbLocationHelper.setItem(loc_id, ColumnLocation.KEY.distance, distance);
//                    }
//                    else
//                    {
                        distance = 0;
//                    }

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

                    MyDebug.MakeLog(2, "PirorityUpdate: id="+task_id + " ,pT=" + left_time + " ,pL=" + distance + " ,pir=" + weight);
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
