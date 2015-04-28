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
        int task_id = Integer.valueOf(taskID);

        NotificationManager nm =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(AlertHandler.TAG, task_id);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);

        ActionSetAlarm AA = new ActionSetAlarm( this, task_id);
        AA.SetIt(calendar.getTimeInMillis());

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
