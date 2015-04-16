package tw.geodoer.main.taskList.controller;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.geodoer.geotodo.R;

import java.util.ArrayList;
import java.util.Locale;

import it.gmariotti.cardslib.library.internal.Card;
import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskEditor.controller.ActionSetAlarm;
import tw.geodoer.main.taskEditor.controller.ActionSetLocationAlarm;
import tw.geodoer.main.taskList.view.ListCursorCardFragment;
import tw.geodoer.utils.MyDebug;

/**
 * Created by Kent on 2015/4/16. 卡片長按動作
 */
public class ActionOnCardLongClicked implements  Card.OnLongCardClickListener {

    private static Context context;
    private static Cursor cursor;
    //private static Card card;
    //private static MyCursorCardAdapter myAdapter;

    public ActionOnCardLongClicked(Context context,Cursor cursor) {
        ActionOnCardLongClicked.context = context;
        ActionOnCardLongClicked.context = context;
        ActionOnCardLongClicked.cursor = cursor;
        //ActionOnCardLongClicked.card = card;
        //ActionOnCardLongClicked.myAdapter = myAdapter;
    }

    private void cancelAlert(int task_id){
        // Alert Part (New)
        try
        {
            DBAlertHelper mDBalerthelper = new DBAlertHelper(context);
            ArrayList<Integer> ids = mDBalerthelper.getIDArrayListOfUnFinishedTask();
            if(ids != null)
                for(int alert_id : ids)
                    if(mDBalerthelper.getItemInt(alert_id, ColumnAlert.KEY.task_id) == task_id)
                        if(mDBalerthelper.getItemInt(alert_id, ColumnAlert.KEY.state) == 0 )
                            mDBalerthelper.setItem(alert_id,ColumnAlert.KEY.state, 1);
        }
        catch (Exception e) { MyDebug.MakeLog(2, "ActionFinishTheAlert ERROR : " + e.toString()); }

        ActionSetAlarm AA = new ActionSetAlarm(context,task_id);
        AA.CancelIt();

        ActionSetLocationAlarm ALA = new ActionSetLocationAlarm(context,task_id);
        ALA.CancelIt();
    }

    /**
     * 完成卡片
     * @param task_id 資料庫中之任務ID
     */
    private void finishedCard(int task_id){
        DBTasksHelper dbTasksHelper=new DBTasksHelper(context);
        dbTasksHelper.setItem(task_id,ColumnTask.KEY.status,ColumnTask.TASK_STATUS_FINISHED);
        cancelAlert(task_id);
    }

    /*
        丟掉卡片
     */
    private void trashedCard(int task_id) {
        DBTasksHelper dbTasksHelper=new DBTasksHelper(context);
        dbTasksHelper.setItem(task_id, ColumnTask.KEY.status, ColumnTask.TASK_STATUS_TRASHED);
        cancelAlert(task_id);
    }

    /*
        恢復卡片
    */
    private void restoreCard(int task_id){
        DBTasksHelper dbTasksHelper=new DBTasksHelper(context);
        dbTasksHelper.setItem(task_id, ColumnTask.KEY.status, ColumnTask.TASK_STATUS_NORMAL);

//        ActionSetAlarm AA = new ActionSetAlarm(context,task_id);
//        AA.SetIt();
//        ActionSetLocationAlarm ALA = new ActionSetLocationAlarm(context,task_id);
//        ALA.SetIt();
    }

    /*
        刪除卡片 - 由資料庫
     */
    private void removeCard(int task_id){

        // Use this code to delete items on DB
        ContentResolver resolverTask = context.getContentResolver();

        // long taskDeleted = 0;
        resolverTask.delete(ColumnTask.URI,
                ColumnTask.KEY._id + " = ? ",
                //new String[] { this.getCardFromCursor(getCursor()).getId() });
                new String[]{String.valueOf(task_id)});

        cancelAlert(task_id);

        // Alert PArt
//        ContentResolver resolverAlert = getContext().getContentResolver();
//        Cursor rowIDs = resolverAlert.query(ColumnAlert.URI,
//                null,
//                ColumnAlert.KEY.task_id + " = ? ",
//                new String[]{String.valueOf(id)},
//                ColumnAlert.DEFAULT_SORT_ORDER);
//        int rowCounter = rowIDs.getCount();
//        rowIDs.moveToFirst();
//        String[] IDs = {""};
//        for (int i = 0; i < rowIDs.getCount(); i++) {
//            IDs[i] = rowIDs.getString(i).toString();
//            MyDebug.MakeLog(0, "rowOrder=" + i +
//                    ",rowID=" + IDs);
//            rowIDs.moveToNext();
//        }

//        MyDebug.MakeLog(0, "task_id=" + ColumnAlert.KEY.task_id +
//                ",ColumnAlert rows=" + rowCounter);
//        long alertDeleted = 0;
//        if (rowCounter > 0) {
//            alertDeleted = resolverAlert.delete(ColumnAlert.URI,
//                    ColumnAlert.KEY.task_id + " = ? ",
//                    IDs);
//        }
//
//        MyDebug.MakeLog(0, "taskDeleted=" + taskDeleted +
//                ",alertDeleted=" + alertDeleted);
        // this.notifyDataSetChanged();
    }


    /**
     *
     * @param task_id 資料庫中之任務ID
     * @return #AlertDialog
     */
    private AlertDialog ShowLongClickMenuTrashed(final int task_id) {
        return new AlertDialog.Builder(context)
                .setTitle("請選擇...")
                .setItems(R.array.Array_Task_List_Card_Long_Clcik_String_TrashCan,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:// 恢復
                                        restoreCard(task_id);
                                        break;

                                    case 1:// 刪除
                                        removeCard(task_id);
                                        break;
                                }
                            }
                        }).show();
    }

    /*

     */
    private AlertDialog ShowLongClickMenuNormal(final int task_id, final String cardID) {
        return new AlertDialog.Builder(context)
                .setTitle("請選擇...")
                .setItems(R.array.Array_Task_List_Card_Long_Clcik_String_Normal,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:// 完成
                                        finishedCard(task_id);
                                        break;

                                    case 1:// 修改
                                        ActionOnCardClicked mReadCardOnClick;
                                        mReadCardOnClick = new ActionOnCardClicked(context, cursor);
                                        //mReadCardOnClick.setMyCursorCardAdapter(myAdapter);
                                        mReadCardOnClick.readIt(cardID);
                                        break;

                                    case 2:// 帶我去 - 導航
                                        //  呼叫導航
                                        DBLocationHelper dbLocationHelper = new DBLocationHelper(context);
                                        callGoogleMapNavigation(
                                                dbLocationHelper.getItemDouble(Integer.valueOf(cardID), ColumnLocation.KEY.lat),
                                                dbLocationHelper.getItemDouble(Integer.valueOf(cardID), ColumnLocation.KEY.lon),
                                                dbLocationHelper.getItemString(Integer.valueOf(cardID), ColumnLocation.KEY.name));
                                        break;

                                    case 3://丟掉
                                        trashedCard(task_id);
                                        break;
                                }
                            }
                        }).show();
    }

    /*
        呼叫導航
     */
    private void callGoogleMapNavigation(double lat, double lon, String name) {
        String uri = String.format(Locale.ENGLISH,
                "http://maps.google.com/maps?&daddr=%f,%f (%s)",
                lat, lon, name);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onLongClick(Card card, View view) {
        cursor.moveToPosition(Integer.parseInt(card.getId()));
        switch (ListCursorCardFragment.getPosition()){
            case 5:
                ShowLongClickMenuTrashed(cursor.getInt(0));
                break;

            default:
                ShowLongClickMenuNormal(cursor.getInt(0), card.getId());
                break;
        }
        return true;
    }
}
