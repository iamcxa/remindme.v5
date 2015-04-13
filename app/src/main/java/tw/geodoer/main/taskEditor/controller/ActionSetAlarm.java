package tw.geodoer.main.taskEditor.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import tw.geodoer.mPriority.receiver.BroadcastReceiver_TaskAlert;
import tw.geodoer.utils.MyDebug;

public class ActionSetAlarm {

    private final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    private final String MSG = "me.iamcxa.remindme.alarm";
    private final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;
    private final int TYPE = AlarmManager.RTC_WAKEUP;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

    private Context context;
    //private long alertTime;
    private long taskID;

    private AlarmManager am;
    private Intent intent;
    private PendingIntent pi;

    public ActionSetAlarm(Context context, int taskID)
    {
        // TODO Auto-generated constructor stub
        super();
        this.context = context;
        //this.alertTime = alertTime;
        this.taskID = taskID;

        // 取得AlarmManager實例
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 實例化Intent
        this.intent = new Intent(this.context, BroadcastReceiver_TaskAlert.class);

        // 設定Intent action屬性
        this.intent.setAction(BC_ACTION);
        this.intent.putExtra("msg", MSG);
        this.intent.putExtra("taskID", taskID);

        // 實例化PendingIntent
        this.pi = PendingIntent.getBroadcast(context, 1, intent, FLAG);

    }

    /** 設定通知提示**/
    /****************************************************************************
    *當 AlarmManager 執行 set() 時，
    * Android 系統會比對已註冊的其他 Intent 的 action、data、type、class、category，
    * 如果這幾個屬性完全相同，則系統會將這兩個 Intent 視為一樣，
    * 這時系統會視 PendingIntent.FLAG???? 參數以決定如何處理這個新註冊的 Intent，
    *****************************************************************************/
    public void SetIt(long alertTime)
    {
        MyDebug.MakeLog(2, "@SetAlarm SeiIt taskID=" + taskID);
        am.set( this.TYPE ,alertTime , this.pi );

        /*
        Calendar cal = Calendar.getInstance();
        // 設定於 3 分鐘後執行
        cal.add(Calendar.SECOND, 10);
        // 取得系統時間
        final long time1 = System.currentTimeMillis();
        //Calendar c = Calendar.getInstance();
        //c.set(mYear, mMonth, mDay, mHour, mMinute);
        //long time2 = c.getTimeInMillis();
        if ((alertTime - time1) > 0) {

            am.set(AlarmManager.RTC_WAKEUP, alertTime, pi);

            //MyDebug.MakeLog(2, "@SetAlarm set="+alertTime);
            MyDebug.MakeLog(2, "@SetAlarm alertTime=" + alertTime);
            MyDebug.MakeLog(2, "@SetAlarm cal 1min=" + cal.getTimeInMillis());
        } else {
            MyDebug.MakeLog(2, "@SetAlarm set failed");
            am.cancel(pi);
        }
        */
    }
    public void CancelIt()
    {
        MyDebug.MakeLog(2, "@SetAlarm CancelIt taskID=" + taskID);
        am.cancel(this.pi);
    }

}



