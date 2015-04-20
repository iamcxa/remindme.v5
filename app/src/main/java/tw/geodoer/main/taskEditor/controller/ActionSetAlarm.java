package tw.geodoer.main.taskEditor.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import tw.geodoer.mPriority.receiver.BroadcastReceiver_TaskAlert;

public class ActionSetAlarm {
    private final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    private final String MSG = "me.iamcxa.remindme.alarm";
    private final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;
    private final int TYPE = AlarmManager.RTC_WAKEUP;

    private Context context;
    private int taskID;

    private AlarmManager AM;
    private Intent intent;
    private PendingIntent pi;

    public ActionSetAlarm(Context cont, int taskID)
    {
        // TODO Auto-generated constructor stub
        super();

        this.context = cont;
        this.taskID = taskID;

        this.AM = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.intent = new Intent(this.context, BroadcastReceiver_TaskAlert.class);
        this.intent.setAction(BC_ACTION);
        this.intent.putExtra("msg", MSG);
        this.intent.putExtra("taskID", this.taskID);
        this.pi = PendingIntent.getBroadcast(context, taskID, intent, FLAG);
    }
    public void SetIt(long due_time_millis)
    {
        if(due_time_millis == 0 )CancelIt();
        else AM.set(TYPE,due_time_millis,pi);
    }
    public void CancelIt()
    {
        this.AM.cancel(this.pi);
    }

}



