package tw.geodoer.main.taskAlert.controller;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.Calendar;

import tw.geodoer.main.taskEditor.controller.ActionSetAlarm;
import tw.geodoer.utils.MyDebug;

public class ActionDelayTheAlert extends IntentService {

    public ActionDelayTheAlert() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle b = intent.getExtras();

        String taskID = b.getString("taskID");

        MyDebug.MakeLog(2, "@Delay Alert taskID=" + taskID);

        NotificationManager nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel("remindme", Integer.valueOf(taskID));

        //AlertHandler alertHandler=AlertHandler.getInstance();

        AlertHandler alertHandler = new AlertHandler();

        Calendar calendar = Calendar.getInstance();

        //calendar.clear();

        //calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.MINUTE, 5);

        ActionSetAlarm action_SetAlarm = new ActionSetAlarm(
                this, calendar.getTimeInMillis(), Integer.valueOf(taskID));
        action_SetAlarm.SetIt();

        ShowToastInIntentService("延遲任務 " + alertHandler.getTaskName(this, taskID) + " 5  分鐘");
    }

    public void ShowToastInIntentService(final String sText) {
        final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast1 = Toast.makeText(MyContext, sText, 5);
                toast1.show();
            }
        });
    }

    ;


}
