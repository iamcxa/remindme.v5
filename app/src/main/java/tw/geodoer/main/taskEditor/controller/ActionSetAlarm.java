package tw.geodoer.main.taskEditor.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import tw.geodoer.mPriority.receiver.GeoBroadcastReceiver_TaskAlert;
import tw.geodoer.utils.MyDebug;

public class ActionSetAlarm {
    Context context;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    long alertTime, taskID;

    public ActionSetAlarm(Context context, long alertTime, int taskID) {
        // TODO Auto-generated constructor stub
        super();
        this.context = context;
        this.alertTime = alertTime;
        this.taskID = taskID;
    }

    // 設定通知提示
    public void SetIt() {
        MyDebug.MakeLog(2, "@SetAlarm");
        MyDebug.MakeLog(2, "@SetAlarm check taskID=" + taskID);

        // 保存內容、日期與時間字串
        //String content = null;

        final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";

        // 取得AlarmManager實例
        final AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // 實例化Intent
        Intent intent = new Intent(context, GeoBroadcastReceiver_TaskAlert.class);

        // 設定Intent action屬性
        intent.setAction(BC_ACTION);
        intent.putExtra("msg", "me.iamcxa.remindme.alarm");
        intent.putExtra("taskID", taskID);

        // 實例化PendingIntent
        final PendingIntent pi =
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);


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

    }

}



