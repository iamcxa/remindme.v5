package tw.geodoer.mPriority.API;

import android.content.Context;
import android.content.Intent;

import tw.geodoer.mPriority.service.GeoServiceEventUpdater;

/**
 * Created by MurasakiYoru on 2015/2/6.
 */
public class ServiceCaller
{
    public final static int KEY_POSITION = 1;
    //public final static int KEY_WEIGHT = 2;

    public static void call(Context context,int key)
    {
        Intent it = new Intent();

        if(key == KEY_POSITION)
            it.setClass(context, GeoServiceEventUpdater.class);

        context.startService(it);
    }

}
