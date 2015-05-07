package tw.geodoer.main.taskAlert.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import tw.geodoer.main.taskAlert.controller.AlertHandler;
import tw.geodoer.main.taskAlert.controller.LocationAlertHandler;
import tw.geodoer.main.taskAlert.view.dialog.AlertNotiDialog;
import tw.geodoer.utils.MyDebug;

/**
 * @author iamcxa 定時提醒廣播
 */
public class BroadcastReceiver_TaskAlert extends BroadcastReceiver
{
    private static String BC_action = "me.iamcxa.remindme.TaskReceiver";
    private static boolean lock = false;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle b = intent.getExtras();
        String msg = (b.getString("msg") == null) ? "no msg" : b.getString("msg");
        String action = intent.getAction();

        MyDebug.MakeLog(2, "@ Receiver onReceive: "+msg);

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
//
//                    break;
//
//                case "me.iamcxa.remindme.location":
//
//                    it.setClass(context, LocationAlertHandler.class);
//                    newB.putString("taskID", b.get("taskID").toString());
//                    it.putExtras(newB);
//                    context.startService(it);
//
//                    break;
//
//                default:
//                    break;
//            }

            Intent new_it = new Intent(context,AlertNotiDialog.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(new_it);
        }
        else if(action.equals("android.intent.action.BOOT_COMPLETED"))
        {
            MyDebug.MakeLog(2, "智慧提醒＠開機啟動完成！");
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
}
