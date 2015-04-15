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
public class PriorityUpdater
{
    private Context mContext;

    private DBTasksHelper dbTaskHelper;

    private PriorityCalculatorNew Cal;


    public PriorityUpdater(Context context)
    {
        this.mContext = context;
        this.Cal = new PriorityCalculatorNew();
        this.dbTaskHelper = new DBTasksHelper(mContext);
    }
    public void PirorityUpdate()
    {
        //get number of events
        int count = dbTaskHelper.getCount();
        if(count==0)return; //if no event can be change , stop
        //--------------------------------------------------------------------------------------
        HandlerThread mHandlerThread = new HandlerThread("PrU");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper());
        Runnable r =new update_thread_2(mContext);
        Thread mThread = new Thread(r);
        mHandler.post(mThread);

    }

    public class update_thread_2 implements Runnable
    {
        private Context mContext;
        private DBTasksHelper dbTaskHelper;
        private DBLocationHelper dbLocationHelper;

        public update_thread_2(Context con)
        {
            this.mContext = con;
            this.dbLocationHelper = new DBLocationHelper(mContext);
            this.dbTaskHelper = new DBTasksHelper(mContext);
        }

        public void run()
        {
            CurrentLocation b = new CurrentLocation(this.mContext);
            b.setOnLocListener(new CurrentLocation.onDistanceListener()
            {
                @Override
                public void onGetLatLng(Double lat, Double lon)
                {
                    Log.wtf("PrU", "onGetLatLng get LAT=" + lat + ", LON=" + lon);
                    int count = dbTaskHelper.getCount();
                    if (count == 0) return; //if no event can be change , stop
                    //------------------------------------------------------
                    ArrayList<Integer> ids = dbTaskHelper.getIDArrayListOfTask();
                    if (ids != null)
                        for (int task_id : ids)
                        {
                            int locID = dbTaskHelper.getItemInt(task_id, ColumnTask.KEY.location_id);
                            if (locID == 0)
                            {
                                PriorityCalculatorNew Cal = new PriorityCalculatorNew();
                                long due_time, left_time;

                                due_time = dbTaskHelper.getItemLong(task_id, ColumnTask.KEY.due_date_millis);
                                if (due_time == 0) left_time = 0;
                                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                                else left_time = due_time - System.currentTimeMillis();

                                dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority, Cal.getweight(left_time, 0));
                            }
                            else
                            {
                                double itemlat = dbLocationHelper.getItemDouble(locID, ColumnLocation.KEY.lat);
                                double itemlon = dbLocationHelper.getItemDouble(locID, ColumnLocation.KEY.lon);

                                double distance = DistanceCalculator.haversine(lat, lon, itemlat, itemlon);

                                PriorityCalculatorNew Cal = new PriorityCalculatorNew();
                                long due_time, left_time;

                                due_time = dbTaskHelper.getItemLong(task_id, ColumnTask.KEY.due_date_millis);
                                if (due_time == 0) left_time = 0;
                                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                                else left_time = due_time - System.currentTimeMillis();

                                int pri =Cal.getweight(left_time, distance);

                                dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority,pri);

                                Log.wtf("PrU", "PrU ID:"+task_id +", onDistance :"+distance +", duetime:" + left_time + ", pri:" +pri);
                            }

                        }


                }
            });

        }
    }


}
