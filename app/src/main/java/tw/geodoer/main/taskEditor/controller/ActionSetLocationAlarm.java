package tw.geodoer.main.taskEditor.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import tw.geodoer.main.taskAlert.receiver.BroadcastReceiver_TaskAlert;

public class ActionSetLocationAlarm {
    private static final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    private static final String MSG = "me.iamcxa.remindme.location";
    private static final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;
    private static final float radius = 1000f;

    private Context context;
    private int taskID;

    private LocationManager LM;
    private Intent intent;
    private PendingIntent pi;

    public ActionSetLocationAlarm(Context cont, int taskID)
    {
        // TODO Auto-generated constructor stub
        super();

        this.context = cont;
        this.taskID = taskID;

        this.LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.intent = new Intent(this.context, BroadcastReceiver_TaskAlert.class);
        this.intent.setAction(BC_ACTION);
        this.intent.putExtra("msg", MSG);
        this.intent.putExtra("taskID", this.taskID);
        this.pi =  PendingIntent.getBroadcast(context, taskID+Integer.MAX_VALUE/2, intent, FLAG);

    }
    public void SetIt(long due_date_millis , double lat,double lon)
    {
        if(due_date_millis == 0)
            LM.addProximityAlert(lat, lon, radius, -1, pi);
        else if (due_date_millis > System.currentTimeMillis())
            LM.addProximityAlert( lat , lon , radius, due_date_millis - System.currentTimeMillis(), pi);
        else
            this.CancelIt();
    }
    public void CancelIt()
    {
        this.LM.removeProximityAlert(pi);
    }

}



