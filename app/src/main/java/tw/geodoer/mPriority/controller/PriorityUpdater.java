package tw.geodoer.mPriority.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mGeoInfo.API.CurrentLocation;
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
        this.currentTimeMillis = System.currentTimeMillis();
    }
    public void PirorityUpdate()
    {
        HandlerThread mHandlerThread = new HandlerThread("PrU");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post( new Runnable()
        {
            public void run()
            {
                Update();
                return;
            }
        });
    }


    private boolean Update()
    {
        //get number of events
        int count = dbTaskHelper.getCount();
        if(count==0)return false; //if no event can be change , stop thread
        //--------------------------------------------------------------------------------------
        ArrayList<Integer> ids = dbTaskHelper.getIDArrayListOfTask();
        if( ids!=null )
            for (int task_id : ids)
                update_item(task_id);

        return true;
    }
    private void update_item(final int ID)
    {
        try
        {
            int loc_id = dbTaskHelper.getItemInt(ID, ColumnTask.KEY.location_id);

            if (loc_id == 0)
            {
                long due_time, left_time;
                due_time = dbTaskHelper.getItemLong(ID, ColumnTask.KEY.due_date_millis);

                if (due_time == 0) left_time = 0;
                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                else left_time = due_time - System.currentTimeMillis();

                dbTaskHelper.setItem(ID, ColumnTask.KEY.priority, Cal.getweight(left_time, 0));
            }
            else
            {
                CurrentLocation b = new CurrentLocation(mContext);
                b.setOnDistanceListener(dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lat),
                        dbLocationHelper.getItemDouble(loc_id, ColumnLocation.KEY.lon),
                        new CurrentLocation.onDistanceListener() {
                            @Override
                            public void onGetDistance(Double mDistance) {
                                long due_time, left_time;
                                due_time = dbTaskHelper.getItemLong(ID, ColumnTask.KEY.due_date_millis);
                                if (due_time == 0) left_time = 0;
                                else if (due_time - System.currentTimeMillis() <= 0) left_time = 0;
                                else left_time = due_time - System.currentTimeMillis();
                                dbTaskHelper.setItem(ID, ColumnTask.KEY.priority, Cal.getweight(left_time, mDistance*1000));
                            }
                        });

            }
        }catch (Exception e){ Log.wtf("PrU","update_item error:"+ID); }
    }


}
