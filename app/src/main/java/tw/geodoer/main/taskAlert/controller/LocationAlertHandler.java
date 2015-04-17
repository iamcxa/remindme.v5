package tw.geodoer.main.taskAlert.controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.geodoer.geotodo.R;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskAlert.view.dialog.AlertNotiDialog;
import tw.geodoer.main.taskList.view.AppMainActivity;
import tw.geodoer.main.taskPreference.controller.MyPreferences;

public class LocationAlertHandler extends IntentService {


    public static LocationAlertHandler alertHandler = new LocationAlertHandler();
    public static final String TAG = "remindme locationalert";

    public LocationAlertHandler() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    public static LocationAlertHandler getInstance() {
        return alertHandler;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle b = intent.getExtras();

        String taskID = b.getString("taskID");

        //MyDebug.MakeLog(2, "@locationalertHandler taskID=" + taskID);

        setNotification(getApplicationContext(), taskID);

    }

    //
    public String getTaskName(Context context, String taskID)
    {
        DBTasksHelper mDBtaskhelper = new DBTasksHelper(context);
        return mDBtaskhelper.getItemString(Integer.valueOf(taskID), ColumnTask.KEY.title);
    }

    //
    public void setNotification(Context context, String taskID) {

        Intent intentMain = new Intent(context, AppMainActivity.class);
        intentMain.putExtra("taskID", taskID);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pedingIntentMain = PendingIntent.getActivity(context, 0,
                intentMain, PendingIntent.FLAG_ONE_SHOT);

        Intent intentDialog = new Intent(context, AlertNotiDialog.class);
        intentDialog.putExtra("taskID", taskID);
        PendingIntent pedingIntentDialog = PendingIntent.getActivity(context, 0,
                intentDialog, PendingIntent.FLAG_ONE_SHOT);

        Intent intentFinish = new Intent(context, ActionFinishAndDeleyTheLocationAlert.class);
        intentFinish.putExtra("taskID", taskID);
        PendingIntent pedingIntentFinish = PendingIntent.getService(context, 0,
                intentFinish, PendingIntent.FLAG_ONE_SHOT);

        MyPreferences.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        long[] tVibrate = {0, 100, 200, 300, 400, 500};

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_action_alarms);

        Notification noti = new Notification.Builder(context)
                .setContentTitle("接近待辦任務地點")
                .setContentText(getTaskName(context, taskID))
                        //.setContentInfo("ContentInfo")
                //.addAction(R.drawable.ic_action_alarms, "延遲", pedingIntentDelay)
                .addAction(R.drawable.ic_action_accept, "我知道了 下次再提醒我", pedingIntentFinish)
                .setNumber(1)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.remindme_logo)
                .setLargeIcon(bm)
                .setWhen(System.currentTimeMillis())
                        //.setFullScreenIntent(pedingIntentDialog, true)
                .setContentIntent(pedingIntentMain)
                .setVibrate(tVibrate)
                //.setOngoing(true)
                .setPriority(16)
                .setSound(Uri.parse(MyPreferences.mPreferences.getString("ringtonePref", context.getFilesDir().getAbsolutePath() + "/fallbackring.ogg")))
                .build();

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(TAG, Integer.valueOf(taskID), noti);

    }

    public void ShowToastInIntentService(final String sText) {
        final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
                toast1.show();
            }
        });
    }

    ;

}
