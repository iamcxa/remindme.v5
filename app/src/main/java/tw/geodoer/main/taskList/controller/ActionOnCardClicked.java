package tw.geodoer.main.taskList.controller;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import it.gmariotti.cardslib.library.internal.Card;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskEditor.view.TaskEditorTabFragment;
import tw.geodoer.utils.CommonVar;
import tw.geodoer.utils.MyDebug;
/**
 * Created by Kent on 2015/4/16. 卡片短按動作
 */
public class ActionOnCardClicked implements  Card.OnCardClickListener{
    private static Context context;
   // private static Card card;
    private static Cursor cursor;
    //private static MyCursorCardAdapter mMyCursorCardAdapter;
    //private static MyCursorCardAdapter myAdapter;

    public ActionOnCardClicked(Context context, Cursor cursor) {
        ActionOnCardClicked.context = context;
        ActionOnCardClicked.cursor = cursor;
       // ActionOnCardClicked.card = card;
       // ActionOnCardClicked.myAdapter = myAdapter;
    }

//    public MyCursorCardAdapter getMyCursorCardAdapter() {
//        return mMyCursorCardAdapter;
//    }

    //public void setMyCursorCardAdapter(MyCursorCardAdapter mMyCursorCardAdapter) {
       // ActionOnCardClicked.mMyCursorCardAdapter = mMyCursorCardAdapter;
    //}

    protected void readIt(String cardPosition) {
        try {

            // 取得該卡片任務在tasks表之id
            //Integer cardID=(int) mMyCursorCardAdapter.getItemId(Integer.parseInt(cardPosition));

            cursor.moveToPosition(Integer.parseInt(cardPosition));

            ReadDatafromCursor();

        } catch (Exception e) {

            MyDebug.MakeLog(0, "ReadCardonClick error=" + e.toString());
        }
    }

    private void ReadDatafromCursor() {
        int taskId = cursor.getInt(0);
        //		String[] DatefromCursor={""};
        //		int i=0;
        //		for (String element : ColumnTask.PROJECTION) {
        //			DatefromCursor[i]=element;
        //			i+=1;
        //		}

        Bundle b = new Bundle();
        //主要內容
        b.putInt(ColumnTask.KEY._id, taskId);
        b.putString(ColumnTask.KEY.title, cursor.getString(ColumnTask.KEY.INDEX.title));
        b.putString(ColumnTask.KEY.content, cursor.getString(ColumnTask.KEY.INDEX.content));
        b.putString(ColumnTask.KEY.due_date_millis, cursor.getString(ColumnTask.KEY.INDEX.due_date_millis));
        b.putString(ColumnTask.KEY.due_date_string, cursor.getString(ColumnTask.KEY.INDEX.due_date_string));
        b.putString(ColumnTask.KEY.location_id,cursor.getString(ColumnTask.KEY.INDEX.location_id));

        // 將備忘錄資訊添加到Intent
        Intent intent = new Intent();
        intent.putExtra(CommonVar.BundleName, b);
        // 啟動備忘錄詳細資訊Activity
        intent.setClass(context, TaskEditorTabFragment.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(Card card, View view) {
        readIt(card.getId());
card.setBackgroundResourceId(Color.CYAN);
    }
}
