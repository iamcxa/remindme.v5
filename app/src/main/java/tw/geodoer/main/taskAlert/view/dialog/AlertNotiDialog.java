package tw.geodoer.main.taskAlert.view.dialog;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;

import tw.geodoer.geotodo.R;


/**
 * @author iamcxa 提醒方法
 */
public class AlertNotiDialog extends Activity {

    public static final int ID = 1;
    private static NotificationManager nm;
    private static Notification noti;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialoglayout);

//		Intent intentmain = new Intent(this, AppMainActivity.class);
//		
//		PendingIntent contentIntentmain = PendingIntent.getActivity(this, 0,
//				intentmain, Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		MyPreferences.mPreferences= PreferenceManager.getDefaultSharedPreferences(this);
//				
//		long[] tVibrate = {0,100,200,300,400,500};
//		
//		Bitmap bm = BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_action_alarms);
//		
//		Notification noti = new Notification.Builder(this)
//		.setContentTitle("待辦任務到期：")
//		.setContentText("點這裡查看")
//		.setContentInfo("ContentInfo")
//        .addAction(R.drawable.ic_action_alarms, "延遲", contentIntentmain)
//        .addAction(R.drawable.ic_action_accept, "完成", null)
//		.setNumber(1)
//		.setAutoCancel(true)
//		.setSmallIcon(R.drawable.remindme_logo)
//		.setLargeIcon(bm)
//		.setWhen(System.currentTimeMillis())
//		//.setFullScreenIntent(contentIntent, false)
//		.setContentIntent(contentIntentmain)
//		.setVibrate(tVibrate)
//		.setSound(Uri.parse(MyPreferences.mPreferences.getString("ringtonePref", getFilesDir().getAbsolutePath()+"/fallbackring.ogg")))
//		.build();
//		
//        NotificationManager nNotificationManager = 
//        		(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		nNotificationManager.notify(1, noti);

//		new AlertDialog.Builder(this)
//		.setMessage("收到訊息!")
//		.setPositiveButton("確定", new DialogInterface.OnClickListener(){
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		})
//		.show();

        //new CustomDialog(getApplicationContext()).show();
    }

}