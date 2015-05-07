package tw.geodoer.main.taskAlert.Neocontroller;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


public class ActionCancelTheNotification extends IntentService
{
    public static final String TAG = "remindme alert";
    public static final int ID = 0;

    public ActionCancelTheNotification() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // TODO Auto-generated method stub
        NotificationManager nm =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(TAG,ID);

    }

}
