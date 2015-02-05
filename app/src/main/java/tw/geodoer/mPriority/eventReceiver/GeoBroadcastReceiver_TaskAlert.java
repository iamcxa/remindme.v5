package tw.geodoer.mPriority.eventReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import tw.geodoer.mPriority.service.GeoServiceNotification;
import tw.geodoer.mPriority.service.GeoServicePosition;
import tw.geodoer.mPriority.service.GeoServiceWeight;
import tw.geodoer.utils.MyDebug;
import tw.geodoer.main.taskAlert.controller.AlertHandler;

/**
 * @author iamcxa 定時提醒廣播
 */
public class GeoBroadcastReceiver_TaskAlert extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle b = intent.getExtras();
        String msg = (b.getString("msg") == null) ? "no msg" : b.getString("msg");
        String action = intent.getAction().toString();

        Bundle newB = new Bundle();
        Intent it;
        MyDebug.MakeLog(2, "@RemindmeReceiver_TaskAlert onReceive");
        MyDebug.MakeLog(2, "get action " + intent.getAction().toString());
        MyDebug.MakeLog(2, "get msg: " + msg);


        if (action.equals("me.iamcxa.remindme.TaskReceiver")) {
            switch (msg)

            {
                case "me.iamcxa.remindme.alarm":
                    //if(msg.equals("me.iamcxa.remindme.alarm"))
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //

                    intent.setClass(context, AlertHandler.class);
                    //

                    newB.putString("taskID", b.get("taskID").toString());

                    intent.putExtras(newB);

                    context.startService(intent);
                    break;

                case "me.iamcxa.remindme.location":
                    //if(msg.equals("me.iamcxa.remindme.location"))

                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //------------------------------------------------------------------------
                    //set location handler
                    intent.setClass(context, GeoServiceNotification.class);

                    //------------------------------------------------------------------------

                    newB.putString("taskID", b.get("taskID").toString());
                    intent.putExtra(GeoServiceNotification.MESSAGE, "12331321546");
                    intent.putExtras(newB);

                    context.startService(intent);
                    break;

                case "me.iamcxa.remindme.position":
                    //if(msg.equals("me.iamcxa.remindme.position"))
                    it = new Intent();
                    it.setClass(context, GeoServicePosition.class);
                    context.startService(it);
                    break;
                case "me.iamcxa.remindme.weight":
                    //if(msg.equals("me.iamcxa.remindme.position"))
                    it = new Intent();
                    it.setClass(context, GeoServiceWeight.class);
                    context.startService(it);
                    break;
                default:
                    break;
            }
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
