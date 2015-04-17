package tw.geodoer.main.taskEditor.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mPriority.receiver.BroadcastReceiver_TaskAlert;

public class ActionSetLocationAlarm {
    private final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    private final String MSG = "me.iamcxa.remindme.location";
    private final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;
    private final float radius = 1000f;

    private Context context;
    private int taskID;
    private int locID;

    private DBTasksHelper mDBT;
    private DBLocationHelper mDBL;

    private LocationManager LM;
    private Intent intent;
    private PendingIntent pi;

    public ActionSetLocationAlarm(Context cont, int taskID)
    {
        // TODO Auto-generated constructor stub
        super();

        this.context = cont;
        this.taskID = taskID;

        this.mDBT = new DBTasksHelper(context);
        this.mDBL = new DBLocationHelper(context);

        this.LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.intent = new Intent(this.context, BroadcastReceiver_TaskAlert.class);
        this.intent.setAction(BC_ACTION);
        this.intent.putExtra("msg", MSG);
        this.intent.putExtra("taskID", this.taskID);
        this.pi =  PendingIntent.getBroadcast(context, 1, intent, FLAG);

    }
    public void SetIt()
    {
        locID = mDBT.getItemInt( taskID, ColumnTask.KEY.location_id);

        if(locID == 0) {
            this.CancelIt();
            return;
        }


        double lat = mDBL.getItemDouble(locID, ColumnLocation.KEY.lat);
        double lon = mDBL.getItemDouble(locID, ColumnLocation.KEY.lon);

        long due_date_millis = mDBT.getItemLong(taskID,ColumnTask.KEY.due_date_millis);
        if(due_date_millis == 0)
            LM.addProximityAlert(lat, lon, radius, -1, pi);
        else if (due_date_millis > System.currentTimeMillis())
            LM.addProximityAlert( lat , lon , radius, due_date_millis - System.currentTimeMillis(), pi);
        else
            this.CancelIt();
    }

    public void CancelIt()
    {
        this.LM.removeProximityAlert(this.pi);
    }

}



