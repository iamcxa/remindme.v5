package tw.geodoer.main.taskEditor.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mPriority.receiver.BroadcastReceiver_TaskAlert;

public class ActionSetAlarm {
    private final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    private final String MSG = "me.iamcxa.remindme.alarm";
    private final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;
    private final int TYPE = AlarmManager.RTC_WAKEUP;

    private Context context;
    private int taskID;

    private DBTasksHelper mDBT;

    private AlarmManager AM;
    private Intent intent;
    private PendingIntent pi;

    public ActionSetAlarm(Context cont, int taskID)
    {
        // TODO Auto-generated constructor stub
        super();

        this.context = cont;
        this.taskID = taskID;

        this.mDBT = new DBTasksHelper(context);

        this.AM = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.intent = new Intent(this.context, BroadcastReceiver_TaskAlert.class);
        this.intent.setAction(BC_ACTION);
        this.intent.putExtra("msg", MSG);
        this.intent.putExtra("taskID", this.taskID);
        this.pi = PendingIntent.getBroadcast(context, 1, intent, FLAG);

    }

    /** 設定通知提示**/
    /****************************************************************************
    *當 AlarmManager 執行 set() 時，
    * Android 系統會比對已註冊的其他 Intent 的 action、data、type、class、category，
    * 如果這幾個屬性完全相同，則系統會將這兩個 Intent 視為一樣，
    * 這時系統會視 PendingIntent.FLAG???? 參數以決定如何處理這個新註冊的 Intent，
    *****************************************************************************/
    public void SetIt()
    {
        long due_date_millis = mDBT.getItemLong(taskID, ColumnTask.KEY.due_date_millis);
        if(due_date_millis == 0)
        {
            this.CancelIt();
            Log.wtf("Alarm","canacel :"+taskID);
        }
        else
        {
            AM = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            AM.set(this.TYPE, due_date_millis, pi);
            Log.wtf("Alarm","set ID:" +taskID +"    " + due_date_millis);
        }
    }

    public void ReSetIt(long alertTime)
    {
        this.AM.set(this.TYPE, alertTime, this.pi);
    }
    public void CancelIt()
    {
        //MyDebug.MakeLog(2, "@SetAlarm CancelIt taskID=" + taskID);
        this.AM.cancel(this.pi);
    }

}



