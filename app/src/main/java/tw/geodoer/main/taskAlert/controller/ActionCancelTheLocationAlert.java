package tw.geodoer.main.taskAlert.controller;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.Toast;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskEditor.controller.ActionSetLocationAlarm;

public class ActionCancelTheLocationAlert extends IntentService {

    private int task_ID;

    public ActionCancelTheLocationAlert() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle b = intent.getExtras();

        String taskID = b.getString("taskID");

        NotificationManager nm =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        nm.cancel(LocationAlertHandler.TAG, Integer.valueOf(taskID));

        this.task_ID = Integer.valueOf(taskID);

        HandlerThread mHandlerThread = new HandlerThread("PrU");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                DBTasksHelper mDBT = new DBTasksHelper(getApplicationContext());
                DBLocationHelper mDBL =new DBLocationHelper(getApplicationContext());
                ActionSetLocationAlarm ASA = new ActionSetLocationAlarm(getApplicationContext(),task_ID);
                long due_date_millis =mDBT.getItemLong(task_ID,ColumnTask.KEY.due_date_millis);
                int loc_ID = mDBT.getItemInt(task_ID,ColumnTask.KEY.location_id);
                double lat = mDBL.getItemDouble(loc_ID,ColumnLocation.KEY.lat);
                double lon = mDBL.getItemDouble(loc_ID,ColumnLocation.KEY.lon);
                ASA.SetIt(due_date_millis,lat,lon);
            }
        },5*60*1000);

    }

    public void ShowToastInIntentService(final String sText) {
        final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }

    ;

}
