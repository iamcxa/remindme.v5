package tw.geodoer.main.taskAlert.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;

import tw.geodoer.main.taskAlert.Neocontroller.BootResetAlarm;
import tw.geodoer.main.taskAlert.Neocontroller.NeoAlertHandler;
import tw.geodoer.main.taskAlert.view.dialog.AlertNotiDialog;
import tw.geodoer.utils.MyDebug;

/**
 * @author iamcxa 定時提醒廣播
 */
public class BroadcastReceiver_TaskAlert extends BroadcastReceiver
{
    private static String BC_action = "me.iamcxa.remindme.TaskReceiver";
    public static boolean lock = false;
    public static int lock_time = 3000;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle b = intent.getExtras();
        String msg = (b.getString("msg") == null) ? "no msg" : b.getString("msg");
        String action = intent.getAction();

//        MyDebug.MakeLog(2, "@ Receiver onReceive: "+msg);

        Bundle newB = new Bundle();
        Intent it = new Intent();

        if (action.equals(BC_action))
        {
//            switch (msg)
//            {
//                case "me.iamcxa.remindme.alarm":
//
//                    it.setClass(context, AlertHandler.class);
//                    newB.putString("taskID", b.get("taskID").toString());
//                    it.putExtras(newB);
//                    context.startService(it);
//                    break;
//                case "me.iamcxa.remindme.location":
//
//                    it.setClass(context, LocationAlertHandler.class);
//                    newB.putString("taskID", b.get("taskID").toString());
//                    it.putExtras(newB);
//                    context.startService(it);
//                    break;
//                default:
//                    break;
//            }
            if(!lock)
            {
                lock = true;
                Handler mHandler = new Handler();
                //Log.wtf("receiver", "lock");

                //wake up screen
                mHandler.post(new wake_screen(context));

                //noti
                Intent intentNeoAlert = new Intent(context, NeoAlertHandler.class);
                intentNeoAlert.putExtras(b);
                context.startService(intentNeoAlert);

                //check dialog
                Intent intentFastCheck = new Intent(context, AlertNotiDialog.class);
                intentFastCheck.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intentFastCheck.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intentFastCheck);

                //unlock
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BroadcastReceiver_TaskAlert.lock = false;
                        //Log.wtf("receiver", "unlock");
                    }
                }, lock_time);
            }
        }
        else if(action.equals("android.intent.action.BOOT_COMPLETED"))
        {
            //MyDebug.MakeLog(2, "智慧提醒＠開機啟動完成！");
            Intent intentBootResetAlarm = new Intent(context, BootResetAlarm.class);
            context.startService(intentBootResetAlarm);
        }
        else if(action.equals("android.net.wifi.supplicant.STATE_CHANGE"))
        {
            MyDebug.MakeLog(2,  "智慧提醒＠wifi狀態改變！");
        }
        else if(action.equals("android.net.nsd.STATE_CHANGED"))
        {
            MyDebug.MakeLog(2,  "智慧提醒＠網路連線狀態改變！");
        }


    }

    private static class wake_screen implements Runnable
    {
        private Context mContext;
        public  wake_screen(Context context)
        {
            mContext = context;
        }
        @Override
        public void run()
        {
            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock WL= pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK|
                    PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP),"TAG");
            WL.acquire();
        }
    }


}
