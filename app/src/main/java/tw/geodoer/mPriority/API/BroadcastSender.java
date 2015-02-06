package tw.geodoer.mPriority.API;

import android.content.Context;
import android.content.Intent;

import tw.geodoer.mPriority.receiver.GeoBroadcastReceiver_TaskAlert;

/**
 * Created by MurasakiYoru on 2015/2/6.
 */
public class BroadcastSender
{
    public final static int KEY_POSITION = 1;
    public final static int KEY_WEIGHT = 2;

    public static void send(Context context,int key)
    {

        //sending a broadcast to call out service
        final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
        Intent intent = new Intent(context,GeoBroadcastReceiver_TaskAlert.class);

        // 設定Intent action屬性
        intent.setAction(BC_ACTION);

        if(key == KEY_POSITION)
            intent.putExtra("msg", "me.iamcxa.remindme.position");
        else if(key == KEY_WEIGHT)
            intent.putExtra("msg", "me.iamcxa.remindme.weight");

        context.sendBroadcast(intent);

    }



}
