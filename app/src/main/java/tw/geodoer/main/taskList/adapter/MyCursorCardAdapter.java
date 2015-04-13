/**
 *
 */
package tw.geodoer.main.taskList.adapter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import tw.geodoer.mDatabase.API.DBAlertHelper;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskEditor.controller.ActionSetAlarm;
import tw.geodoer.main.taskList.cardsui.CardThumbnailCircle;
import tw.geodoer.main.taskList.cardsui.MyCursorCard;
import tw.geodoer.main.taskList.controller.ActionOnClickCard;
import tw.geodoer.main.taskList.controller.ActionSetCardFromCursor;
import tw.geodoer.utils.MyDebug;
import tw.moretion.geodoer.R;

/**
 * @author Kent
 */

/**
 * Class MyCursorCardAdapter *
 */
public class MyCursorCardAdapter extends CardCursorAdapter {

    private static ActionSetCardFromCursor mActionSetCardFromCursor;
    private static ActionOnClickCard mReadCardOnClick;

    public MyCursorCardAdapter(Context context) {
        super(context);
    }

    public static MyCursorCardAdapter newInstance(Context context) {
        MyCursorCardAdapter adapter = new MyCursorCardAdapter(context);
        return adapter;
    }

    @Override
    protected Card getCardFromCursor(final Cursor cursor) {

        // 建立卡片物件
        MyCursorCard card = new MyCursorCard(getContext());

        // 設定卡片內容
        mActionSetCardFromCursor = new ActionSetCardFromCursor(getContext(), cursor, card);
        mActionSetCardFromCursor.setIt();
        mReadCardOnClick = new ActionOnClickCard(getContext(), cursor, card);
        mReadCardOnClick.setMyCursorCardAdapter(this);

        // Create a CardHeader
        CardHeader header = new CardHeader(getContext());

        // set color
        if (cursor.getString(ColumnTask.KEY.INDEX.status) == "0")
            card.setBackgroundResourceId(Color.WHITE);
        if (cursor.getString(ColumnTask.KEY.INDEX.status) == "1")
            card.setBackgroundResourceId(Color.GRAY);

        // Set the header title
        header.setTitle(card.mainHeader);

        // Add Header to card
        card.addCardHeader(header);

        //Set visible the expand/collapse button
        // card.setExpanded(true);
        header.setButtonExpandVisible(true);
        // This provides a simple (and useless) expand area
        CardExpand expand = new CardExpand(getContext());
        // Set inner title in Expand Area
        String aa = "dbId=" + cursor.getString(0) + ",w="
                + cursor.getString(ColumnTask.KEY.INDEX.priority)
                + "cardID=" + card.getId();
        expand.setTitle(aa);
        card.addCardExpand(expand);

        // Add Thumbnail to card
        // final MyCardThumbnail thumb = new MyCardThumbnail(getContext());
        // thumb.setDrawableResource(card.resourceIdThumb);
        CardThumbnailCircle thumb = new CardThumbnailCircle(getContext(), cursor.getInt(0));
        card.addCardThumbnail(thumb);

        //Set onClick listener
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //    Toast.makeText(getContext(), "Clickable card", Toast.LENGTH_LONG).show();
                mReadCardOnClick.readIt(card.getId());
            }
        });

        //card.setLongClickable(true);
        card.setOnLongClickListener(new Card.OnLongCardClickListener() {
            @Override
            public boolean onLongClick(Card card, View view) {
                cursor.moveToPosition(Integer.parseInt(card.getId()));
                ShowLongClickMenu(cursor.getInt(0), card.getId());
                return false;
            }
        });

        return card;
    }

    /*

     */
    private void removeCard(int task_id) {

        // Use this code to delete items on DB
        ContentResolver resolverTask = getContext().getContentResolver();

        long taskDeleted = 0;
        resolverTask.delete(ColumnTask.URI,
                ColumnTask.KEY._id + " = ? ",
                //new String[] { this.getCardFromCursor(getCursor()).getId() });
                new String[]{String.valueOf(task_id)});

        // Alert Part (New)
        try
        {
            DBAlertHelper mDBalerthelper = new DBAlertHelper(mContext);
            ArrayList<Integer> ids = mDBalerthelper.getIDArrayListOfUnFinishedTask();
            if(ids != null)
                for(int alert_id : ids)
                    if(mDBalerthelper.getItemInt(alert_id, ColumnAlert.KEY.task_id) == task_id)
                        if(mDBalerthelper.getItemInt(alert_id, ColumnAlert.KEY.state) == 0 )
                            mDBalerthelper.setItem(alert_id,ColumnAlert.KEY.state, 1);
        }
        catch (Exception e) { MyDebug.MakeLog(2,"ActionFinishTheAlert ERROR : "+e.toString()); }

        ActionSetAlarm AA = new ActionSetAlarm(mContext,task_id);
        AA.CancelIt();

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
        this.notifyDataSetChanged();
    }

    private AlertDialog ShowLongClickMenu(final int id, final String cardID) {
        return new AlertDialog.Builder(getContext())
                .setTitle("請選擇...")
                .setItems(R.array.Array_Task_List_Card_Long_Clcik_String,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:// 修改
                                        mReadCardOnClick.readIt(cardID);
                                        break;
                                    case 1:// 刪除
                                        removeCard(id);
                                        break;
                                }
                            }
                        }).show();
    }


}

