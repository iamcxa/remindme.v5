package tw.geodoer.main.taskEditor.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;

import java.util.Locale;

import tw.geodoer.mPriority.receiver.BroadcastReceiver_TaskAlert;

public class ActionSetLocationAlarm {
    private final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    private final String MSG = "me.iamcxa.remindme.location";
    private final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;
    private final float radius = 100f;

    private Context context;
    private long taskID;

    private LocationManager LM;
    private Intent intent;
    private PendingIntent pi;

    public ActionSetLocationAlarm(Context context, int taskID)
    {
        // TODO Auto-generated constructor stub
        super();

        this.context = context;
        this.taskID = taskID;

        this.LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.intent = new Intent(this.context, BroadcastReceiver_TaskAlert.class);
        this.intent.setAction(BC_ACTION);
        this.intent.putExtra("msg", MSG);
        this.intent.putExtra("taskID", this.taskID);
        this.pi =  PendingIntent.getBroadcast(context, 1, intent, FLAG);

    }
    public void SetIt(double lat, double lon, long expiration)
    {
        //MyDebug.MakeLog(2, "@SetLocationAlarm SeiIt taskID=" + taskID);
        this.LM.addProximityAlert( lat , lon , radius, expiration, this.pi);
    }
    public void SetIt(double lat, double lon)
    {
        //MyDebug.MakeLog(2, "@SetLocationAlarm SeiIt taskID=" + taskID);
        this.SetIt(lat,lon,-1);
    }
    public void CancelIt()
    {
        this.LM.removeProximityAlert(this.pi);
    }

}



