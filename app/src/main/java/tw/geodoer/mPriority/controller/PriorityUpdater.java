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
import tw.geodoer.main.taskEditor.controller.ActionSetAlarm;

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

        mHandler.post(new Thread(new update_thread_setalert(mContext)));

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
                            int state = dbTaskHelper.getItemInt(task_id, ColumnTask.KEY.status);
                            if (locID == 0 && state ==0)
                            {
                                PriorityCalculatorNew Cal = new PriorityCalculatorNew();
                                long due_time, left_time;

                                due_time = dbTaskHelper.getItemLong(task_id, ColumnTask.KEY.due_date_millis);
                                if (due_time == 0) left_time = 0;
                                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                                else left_time = due_time - System.currentTimeMillis();

                                dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority, Cal.getweight(left_time, 0));
                            }
                            else if(state == 0 )
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

                                dbLocationHelper.setItem(locID,ColumnLocation.KEY.distance,distance);


                                // added by kent @20150416
                                dbTaskHelper.setItem(task_id, ColumnTask.KEY.priority, pri);

                                Log.wtf("PrU", "PrU ID:"+task_id +", onDistance :"+distance +", duetime:" + left_time + ", pri:" +pri);
                            }

                        }


                }
            });

        }
    }

    public class update_thread_setalert implements Runnable
    {
        private Context mContext;
        private DBTasksHelper dbTaskHelper;
        public update_thread_setalert(Context con)
        {
            this.mContext = con;
            this.dbTaskHelper = new DBTasksHelper(mContext);
        }
        @Override
        public void run()
        {
            int count = dbTaskHelper.getCount();
            if (count == 0) return;

            ArrayList<Integer> ids = dbTaskHelper.getIDArrayListOfTask();
            if (ids != null)
                for (int task_id : ids)
                {
                    if(dbTaskHelper.getItemInt(task_id,ColumnTask.KEY.status) == 0)
                    if(dbTaskHelper.getItemLong(task_id,ColumnTask.KEY.due_date_millis)> System.currentTimeMillis())
                    {
                        ActionSetAlarm AA = new ActionSetAlarm(mContext,task_id);
                        AA.SetIt(dbTaskHelper.getItemLong(task_id,ColumnTask.KEY.due_date_millis));
                    }
                }
        }
    }


}
