package tw.geodoer.main.taskAlert.controller;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.main.taskEditor.controller.ActionSetLocationAlarm;
import tw.geodoer.utils.MyDebug;

public class ActionFinishAndDeleyTheLocationAlert extends IntentService {

    public ActionFinishAndDeleyTheLocationAlert() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle b = intent.getExtras();

        String taskID = b.getString("taskID");

        NotificationManager nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm.cancel(LocationAlertHandler.TAG, Integer.valueOf(taskID));


        //AlertHandler alertHandler = AlertHandler.getInstance();
        //ShowToastInIntentService("任務 " + alertHandler.getTaskName(this, taskID) + "完成！");

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
