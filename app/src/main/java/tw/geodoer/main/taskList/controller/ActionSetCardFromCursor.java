package tw.geodoer.main.taskList.controller;

import android.content.Context;
import android.database.Cursor;

import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskList.cardsui.MyCursorCard;
import tw.geodoer.utils.MyCalendar;

public class ActionSetCardFromCursor {

    public ActionSetCardFromCursor(final Context context,
                                   final Cursor cursor,
                                   final MyCursorCard card,
                                   final int position) {

        // 準備常數
        boolean itHasTime = false;
        int location_id = cursor.getInt(ColumnTask.KEY.INDEX.location_id);

        // give a ID.
        card.setId(String.valueOf(cursor.getPosition()));

        // 卡片標題 - first line
        card.mainHeader = cursor.getString(ColumnTask.KEY.INDEX.title);

        // 距離與地點資訊
        DBLocationHelper dbLocationHelper = new DBLocationHelper(context);
        String LocationName=dbLocationHelper.getItemString(location_id, ColumnLocation.KEY.name);
        double distance=dbLocationHelper.getItemDouble(location_id, ColumnLocation.KEY.distance);
        if(location_id==0){
            card.LocationName ="無地點";
        }else {
            if (LocationName.length() > 14) {
                String tmpStr = LocationName.substring(0, 12);
                LocationName = tmpStr + "..";
            }
            if (distance > 0.0) {
                if (distance < 1.0) {
                    card.LocationName = LocationName + " - 距離 "
                            + (int) Math.floor((distance) * 1000) + " m";
                } else {
                    card.LocationName = LocationName + " - 距離 "
                            + (int) Math.floor(distance) + " km";
                }
            } else {
                card.LocationName = LocationName;
            }
        }

        //時間日期 - sec line
        long due_date_millis = cursor.getLong(ColumnTask.KEY.INDEX.due_date_millis);
        String due_date_string = cursor.getString(ColumnTask.KEY.INDEX.due_date_string);
        long dayLeft = MyCalendar.getDaysLeftByLong(due_date_millis, 2);
        //MyDebug.MakeLog(2, "id=" + CID + " dayleft=" + dayLeft);
        if(due_date_string.length()==4){
            card.dueDate = "無日期";
        }else {
            // 去掉日期
            String duedateStr[]=due_date_string.split(";");
            if(duedateStr.length>1){
                due_date_string=duedateStr[1];
                itHasTime = true;
            }

            // 轉換成人類易讀
            boolean sign = dayLeft < 0;  // 1 = +    0 = -
            dayLeft = Math.abs(dayLeft);

            if(dayLeft == 0)
            {
                due_date_string = "今天" + (itHasTime ? "的" + due_date_string : "");

                long left_millis = due_date_millis - System.currentTimeMillis();
                long left_min = left_millis / (1000*60);

                if( (left_min>0) && left_min<60)
                    due_date_string = due_date_string + "(約"  + left_min + "分鐘後)";
                else if( (left_min>0) && left_min<360 )
                    due_date_string = due_date_string + "(約"  + left_min/60 + "個小時後)";

                card.dueDate= due_date_string;
            }
            else if (dayLeft == 1) card.dueDate = (sign?"明天":"昨天") + due_date_string;
            else if (dayLeft < 7 ) card.dueDate = "約" + dayLeft   + "天"  + (sign?"後的":"前的")+due_date_string;
            else if (dayLeft < 30) card.dueDate = "約" + dayLeft/7 + "週"  + (sign?"後":"前");
            else if (dayLeft <365) card.dueDate = "約" + dayLeft/30 +"個月"+ (sign?"後":"前");
            else card.dueDate = dayLeft/365 +"年" +(sign?"後":"前");

            if(due_date_millis < System.currentTimeMillis() ) card.dueDate = card.dueDate + "(到期)";

        }

        // 標題物件
        CardHeader header = new CardHeader(context);
        header.setTitle(card.mainHeader);
        card.addCardHeader(header);

        // 可展開view
        String expandTitle = "dbId=" + cursor.getString(0) +
                ",w=" + cursor.getString(ColumnTask.KEY.INDEX.priority)
                + ",cardID=" + card.getId();

        //Set visible the expand/collapse button
        header.setButtonExpandVisible(true);
        CardExpand expand = new CardExpand(context);
        expand.setTitle(expandTitle);
        card.addCardExpand(expand);

        //Set onClick listener
        card.setOnClickListener(new ActionOnCardClicked(context,cursor));

        // set onLongClick listener;
        card.setOnLongClickListener(
                new ActionOnCardLongClicked(context, cursor, position));

    }

}
