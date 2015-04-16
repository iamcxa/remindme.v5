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

import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
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
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(AlertHandler.TAG, Integer.valueOf(taskID));

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, 5);

//        ActionSetAlarm AA = new ActionSetAlarm( this, Integer.valueOf(taskID));
//        AA.ReSetIt(calendar.getTimeInMillis());

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
