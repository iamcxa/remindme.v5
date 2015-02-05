package tw.geodoer.main.taskList.controller;

import android.content.Context;
import android.database.Cursor;

import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskList.cardsui.MyCursorCard;
import tw.geodoer.utils.MyCalendar;

public class ActionSetCardFromCursor {

    public MyCursorCard card;
    private Context context;
    private Cursor cursor;

    public ActionSetCardFromCursor(Context context, Cursor cursor, MyCursorCard card) {
        this.context = context;
        this.cursor = cursor;
        this.card = card;
    }

    public void setIt() {
        // 準備常數
        boolean Extrainfo = cursor
                .isNull(ColumnTask.KEY.INDEX.tag_id);
        int CID = cursor.getInt(ColumnTask.KEY.INDEX._id);
        //主要內容
        String title = cursor.getString(ColumnTask.KEY.INDEX.title);
        String status = cursor.getString(ColumnTask.KEY.INDEX.status);
        String content = cursor.getString(ColumnTask.KEY.INDEX.content);
        int due_date_millis = cursor.getInt(ColumnTask.KEY.INDEX.due_date_millis);
        String due_date_string = cursor.getString(ColumnTask.KEY.INDEX.due_date_string);
        int color = cursor.getInt(ColumnTask.KEY.INDEX.color);
        int priority = cursor.getInt(ColumnTask.KEY.INDEX.priority);
        int created = cursor.getInt(ColumnTask.KEY.INDEX.created);
        //分類,標籤與優先
        int category_id = cursor.getInt(ColumnTask.KEY.INDEX.category_id);
        int tag_id = cursor.getInt(ColumnTask.KEY.INDEX.tag_id);
        int project_id = cursor.getInt(ColumnTask.KEY.INDEX.project_id);
        int collaborator_id = cursor.getInt(ColumnTask.KEY.INDEX.collaborator_id);
        int sync_id = cursor.getInt(ColumnTask.KEY.INDEX.sync_id);
        int location_id = cursor.getInt(ColumnTask.KEY.INDEX.location_id);

        long dayLeft = MyCalendar.getDaysLeft(due_date_string, 2);
        // int dayLeft = Integer.parseInt("" + dayLeftLong);

        // give a ID.
        card.setId(String.valueOf(cursor.getPosition()));

        // 卡片標題 - first line
        // MyDebug.MakeLog(0, "cardID="+CID + " set Tittle="+title);
        card.mainHeader = title;

        // 時間日期 - sec line
        // MyDebug.MakeLog(0, CID + " set Date/Time...");
        card.dueDate = due_date_string;

        // MyDebug.MakeLog(0, CID + " dayleft=" + dayLeft);
//		if ((180 > dayLeft) && (dayLeft > 14)) {
//			card.DateTime = "再 " + (int) Math.floor(dayLeft) / 30 + " 個月 - "
//					+ due_date_string + " - " + due_date_millis;
//		} else if ((14 > dayLeft) && (dayLeft > 0)) {
//			card.DateTime = "再 " + dayLeft + " 天 - " + due_date_string + " - "
//					+ due_date_string;
//		} else if ((2 > dayLeft) && (dayLeft > 0)) {
//			card.DateTime = "再 " + (int) Math.floor(dayLeft * 24) + "小時後 - "
//					+ due_date_string + " - " + due_date_millis;
//		} else if (dayLeft == 0) {
//			card.DateTime = "今天 - " + due_date_string + " - " + due_date_millis;
//		} else {
//			card.DateTime = due_date_string + " - " + due_date_millis;
//		}
//
//		// 小圖標顯示 - 判斷是否存有地點資訊
//		MyDebug.MakeLog(0, "Location=\"" + location_id + "\"");
//
//		if (location_id != 0) {
//			card.resourceIdThumb = R.drawable.map_marker;
//		} else {
//			card.resourceIdThumb = R.drawable.tear_of_calendar;
//			card.LocationName = "沒有任務地點";
//		}
//
//		// set color
//		if(cursor.getString(ColumnTask.KEY.INDEX.status).equalsIgnoreCase("0"))
//			card.setBackgroundResourceId(R.drawable.card_background);
//		if(cursor.getString(ColumnTask.KEY.INDEX.status).equalsIgnoreCase("1"))
//			card.setBackgroundResourceId(R.drawable.card_background_gray);
//
//
//		// 距離與地點資訊
//		ContentResolver resolverLocation =context.getContentResolver();

//		Cursor cursorLocation=resolverLocation.query(ColumnLocation.URI,
//				null,
//				ColumnLocation.KEY._id+" = ? ",
//				new String[] { ColumnTask.KEY.location_id },
//				ColumnLocation.DEFAULT_SORT_ORDER);
//		String dintence;
//		if(cursorLocation.getCount()>0){
//			 dintence=String.valueOf(cursorLocation.getLong(ColumnLocation.KEY.INDEX.distance));
//		}else {
//			 dintence="null";
//		}
        // 	MyDebug.MakeLog(0, "dintence=" +dintence);
//		if (dintence == "") {
//			card.LocationName = cursorLocation.getString(ColumnLocation.KEY.INDEX.name);
//		} else {
//			//			if (Double.valueOf(dintence) < 1) {
//			//				card.LocationName = LocationName + " - 距離 "
//			//						+ Double.valueOf(dintence) * 1000 + " 公尺";
//			//			} else {
//			card.LocationName = location_id + " - 距離 " + dintence + " 公里";
//
//			//			}
//		}

        // 可展開額外資訊欄位
//		MyDebug.MakeLog(0, "isExtrainfo=" + Extrainfo);
//		 card.Notifications = "dbId="
//		 + cursor.getString(0)
//		 + ",priority="
//		 + cursor.getString(ColumnTask.KEY.INDEX.priority);
//		if (!Extrainfo) {
//			card.resourceIdThumb = R.drawable.outline_star_act;
//			// 額外資訊提示 - 第四行
//
//		}
//		card.Notifications = cursor.getString(0);

//		// 依照權重給予卡片顏色
//		if (cursor.getInt(ColumnTask.KEY.INDEX.priority) > 6000) {
//			card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
//		} else if (cursor.getInt(ColumnTask.KEY.INDEX.priority) > 3000) {
//			card.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
//		}
    }

}
