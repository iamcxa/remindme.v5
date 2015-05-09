package tw.geodoer.main.taskAlert.Neocontroller;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskEditor.controller.ActionSetAlarm;
import tw.geodoer.main.taskEditor.controller.ActionSetLocationAlarm;

/**
 * Created by MurasakiYoru on 2015/5/8.
 */
public class BootResetAlarm extends IntentService
{
    private DBTasksHelper mDBT = null;
    private DBLocationHelper mDBL = null;

    private ActionSetAlarm ASA = null;
    private ActionSetLocationAlarm ASLA = null;

    public BootResetAlarm()
    {
        super(null);
        mDBT = new DBTasksHelper(this);
        mDBL = new DBLocationHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int count = mDBT.getCount();
        if(count == 0) return;

        //------------------------------
        int loc_id;
        long due_date_millis;
        double lat,lon;
        ArrayList<Integer> ids = mDBT.getIDArrayListOfTask();
        if(ids != null)
            for(int task_id : ids)
            {
                try {
                    if(mDBT.getItemInt(task_id,ColumnTask.KEY.status)!= 0) continue;

                    due_date_millis = mDBT.getItemLong(task_id, ColumnTask.KEY.due_date_millis);
                    ASA = new ActionSetAlarm(this, task_id);
                    ASA.SetIt(due_date_millis);

                    loc_id = mDBT.getItemInt(task_id, ColumnTask.KEY.location_id);
                    if (loc_id != 0) {
                        lat = mDBL.getItemDouble(loc_id, ColumnLocation.KEY.lat);
                        lon = mDBL.getItemDouble(loc_id, ColumnLocation.KEY.lon);

                        ASLA = new ActionSetLocationAlarm(this, task_id);
                        ASLA.SetIt(due_date_millis, lat, lon);
                    }
                }
                catch (Exception e)
                {
                    continue;
                }
            }

    }
}
