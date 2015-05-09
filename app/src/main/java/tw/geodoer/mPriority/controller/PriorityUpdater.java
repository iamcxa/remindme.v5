package tw.geodoer.mPriority.controller;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mGeoInfo.API.CurrentLocation;
import tw.geodoer.mGeoInfo.API.DistanceCalculator;

/**
 * Created by MurasakiYoru on 2015/4/11.
 */
public class PriorityUpdater implements Runnable
{
    private Context mContext;
    private DBTasksHelper dbTaskHelper = null;
    private DBLocationHelper dbLocationHelper = null;
    private PriorityCalculatorNew Cal = null;

    private long now_time;

    private long due_time, left_time;
    private double distance, item_lat, item_lon;
    private int priority;

    public PriorityUpdater(Context context)
    {
        this.mContext = context;
        this.dbLocationHelper = new DBLocationHelper(mContext);
        this.dbTaskHelper = new DBTasksHelper(mContext);
        this.Cal = new PriorityCalculatorNew();
    }
    public void PirorityUpdate()
    {
        HandlerThread mHandlerThread = new HandlerThread("PrU");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper());
        Thread mThread = new Thread(this);
        mHandler.post(mThread);
    }
    private long left_time_cal(long due_time)
    {
        if (due_time == 0) return 0;
        else if (due_time - now_time <= 0) return  0;
        else return due_time - now_time;
    }
    @Override
    public void run()
    {
        CurrentLocation b = new CurrentLocation(this.mContext);
        b.setOnLocListener(new CurrentLocation.onDistanceListener()
        {
            @Override
            public void onGetLatLng(Double lat, Double lon)
            {
                //Log.wtf("PrU", "onGetLatLng get LAT=" + lat + ", LON=" + lon);
                int count = dbTaskHelper.getCount();
                if (count == 0) return; //if no event can be change , stop
                //------------------------------------------------------
                now_time = System.currentTimeMillis();

                ArrayList<Integer> ids = dbTaskHelper.getIDArrayListOfTask();
                if (ids != null)
                    for (int task_id : ids)
                    {
                        int locID = dbTaskHelper.getItemInt(task_id, ColumnTask.KEY.location_id);
                        int state = dbTaskHelper.getItemInt(task_id, ColumnTask.KEY.status);

                        if (locID == 0 && state ==0)
                        {
                            due_time = dbTaskHelper.getItemLong(task_id, ColumnTask.KEY.due_date_millis);

                            left_time = left_time_cal(due_time);

                            priority = Cal.get_weight(left_time, 0);

                            dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority,priority);
                        }
                        else if(state == 0 )
                        {
                            item_lat = dbLocationHelper.getItemDouble(locID, ColumnLocation.KEY.lat);
                            item_lon = dbLocationHelper.getItemDouble(locID, ColumnLocation.KEY.lon);

                            distance = DistanceCalculator.haversine(lat, lon, item_lat, item_lon);

                            due_time = dbTaskHelper.getItemLong(task_id, ColumnTask.KEY.due_date_millis);

                            left_time = left_time_cal(due_time);

                            priority = Cal.get_weight(left_time, distance);

                            //dbLocationHelper.setItem(locID,ColumnLocation.KEY.distance,distance);

                            dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority, priority);

                            Log.wtf("PrU", "ID:"+task_id +",Distance:"+distance +", lefttime:" + left_time + ", pri:" +priority);
                        }

                    }


            }
        });

    }
}


