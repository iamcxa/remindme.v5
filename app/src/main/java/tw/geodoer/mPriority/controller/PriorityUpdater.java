package tw.geodoer.mPriority.controller;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mGeoInfo.API.CurrentLocation;

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

        ArrayList<Integer> ids = dbTaskHelper.getIDArrayListOfTask();
        if( ids!=null )
            for (int task_id : ids)
            {

                Runnable r =new update_thread(mContext,task_id);
                Thread mThread = new Thread(r);
                mHandler.post(mThread);
            }

    }
    public class update_thread implements  Runnable
    {

        private Context mContext;
        private int taskID;
        private DBTasksHelper dbTaskHelper;
        private DBLocationHelper dbLocationHelper;

        public update_thread(Context con,int task_id)
        {
            this.mContext = con;
            this.taskID = task_id;
            this.dbLocationHelper=new DBLocationHelper(mContext);
            this.dbTaskHelper = new DBTasksHelper(mContext);

        }
        public void run()
        {
            int locID = dbTaskHelper.getItemInt(taskID, ColumnTask.KEY.location_id);
            if (locID == 0)
            {
                PriorityCalculatorNew Cal = new PriorityCalculatorNew();
                long due_time, left_time;
                due_time = dbTaskHelper.getItemLong(taskID, ColumnTask.KEY.due_date_millis);
                if (due_time == 0) left_time = 0;
                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                else left_time = due_time - System.currentTimeMillis();
                dbTaskHelper.setItem(taskID, ColumnTask.KEY.priority, Cal.getweight(left_time, 0));
            }
            else
            {
                CurrentLocation b = new CurrentLocation(this.mContext);
                b.setOnLocListener(
                        new CurrentLocation.onDistanceListener()
                        {
                            @Override
                            public void onGetDistance(Double mDistance)
                            {
                                PriorityCalculatorNew Cal = new PriorityCalculatorNew();
                                mDistance = (mDistance == -1) ? 0 : mDistance;
                                long due_time, left_time;
                                due_time = dbTaskHelper.getItemLong(taskID, ColumnTask.KEY.due_date_millis);
                                if (due_time == 0) left_time = 0;
                                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                                else left_time = due_time - System.currentTimeMillis();
                                dbTaskHelper.setItem(taskID, ColumnTask.KEY.priority, Cal.getweight(left_time, mDistance * 1000));
                                Log.wtf("PrU", "PrU ID:"+taskID+"onDistance :"+mDistance +"duetime :"+left_time+"pri:" + Cal.getweight(left_time, mDistance * 1000d));

                            }

                            @Override
                            public void onGetLatLng(Double lat, Double lng) {
                                Log.wtf("PrUr",lat+","+lng);
                            }
                        });

            }
        }
    }



}
