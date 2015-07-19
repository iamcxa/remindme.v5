package tw.geodoer.main.taskAlert.Neocontroller;

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
import android.preference.PreferenceManager;

import com.geodoer.geotodo.R;

import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.AppMainActivity;
import tw.geodoer.main.taskPreference.controller.MyPreferences;

public class NeoAlertHandler extends IntentService
{
    public static final String TAG = "remindme alert";
    public static final int ID = 0;

    private int task_id;
    private String msg;

    public NeoAlertHandler() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle b = intent.getExtras();
        task_id = b.getInt("taskID");
        msg = b.getString("msg");

        setNotification(this);

    }

    public void setNotification(Context context)
    {

        Intent intentMain = new Intent(context, AppMainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pedingIntentMain = PendingIntent.getActivity(context, ID,
                intentMain, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCheckDialog = new Intent(context, ActionFastCheck.class);
        PendingIntent pedingIntentCheckDialog = PendingIntent.getService(context, ID,
                intentCheckDialog, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentFinish = new Intent(context, ActionCancelTheNotification.class);
        intentFinish.putExtra("taskID", ID);
        PendingIntent pedingIntentFinish = PendingIntent.getService(context, ID,
                intentFinish, PendingIntent.FLAG_ONE_SHOT);


        MyPreferences.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        long[] tVibrate = {0, 100, 200, 300, 400, 500};

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_action_alarms);

        Notification noti = new Notification.Builder(context)
                .setContentTitle("智慧提醒:" + task_id)
                .setContentText(msg)
                .addAction(R.drawable.ic_action_labels, "快速查看", pedingIntentCheckDialog)
                .addAction(R.drawable.ic_action_accept, "我知道了", pedingIntentFinish)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.remindme_logo)
                .setLargeIcon(bm)
                .setWhen(System.currentTimeMillis())
                //.setFullScreenIntent(pedingintentDialog, true)
                .setContentIntent(pedingIntentMain)
                .setVibrate(tVibrate)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(Uri.parse(MyPreferences.mPreferences.getString("ringtonePref", context.getFilesDir().getAbsolutePath() + "/fallbackring.ogg")))
                .build();

        NotificationManager nm =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(TAG, ID, noti);

    }

    public String getTaskName(Context context, String taskID)
    {
        DBTasksHelper mDBtaskhelper = new DBTasksHelper(context);
        return mDBtaskhelper.getItemString(Integer.valueOf(taskID), ColumnTask.KEY.title);
    }

}
