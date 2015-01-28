package tw.geodoer.mPriority.eventReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tw.geodoer.mPriority.service.GeoServiceNotification;
import tw.geodoer.mPriority.service.GeoServicePosition;
import tw.geodoer.mPriority.service.GeoServiceWeight;
import tw.geodoer.utils.MyDebug;


/**
 * Created by MurasakiYoru on 2015/1/20.
 */
public class GeoBroadcastReceiver extends BroadcastReceiver
{

    public static final int BROADCAST_COMMAND_NOTI = 10;
    public static final int BROADCAST_COMMAND_POSITON = 5;
    public static final int BROADCAST_COMMAND_DISTANCE = 3;
    public static final int BROADCAST_COMMAND_WEIGHT = 1;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Intent i;
        String action = intent.getAction().toString();
        Integer Command = intent.getExtras().getInt("Command");

        if (action.equals("tw.geodoer.mPriority.service.RemainBroadcast"))
        {
            //MyDebug.MakeLog(2,"remain command =:"+Command.toString());
            switch (Command)
            {
                case BROADCAST_COMMAND_NOTI:
                    i = new Intent(context, GeoServiceNotification.class);
                    if(intent.getExtras().getString("content")!=null)
                        i.putExtra(GeoServiceNotification.MESSAGE, intent.getExtras().getString(GeoServiceNotification.MESSAGE));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(i);
                    break;

                case BROADCAST_COMMAND_POSITON:
                    i = new Intent(context, GeoServicePosition.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(i);
                    break;

                case BROADCAST_COMMAND_WEIGHT:
                    i = new Intent(context, GeoServiceWeight.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(i);
                    break;
                default:
                    break;
            }


            //BROADCAST_COMMAND_DISTANCE)



        }

    }

}