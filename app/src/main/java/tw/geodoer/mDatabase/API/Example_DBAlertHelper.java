/**
 *
 */
package tw.geodoer.mDatabase.API;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.utils.MyCalendar;
import tw.geodoer.utils.MyDebug;

/**
 * @author Kent
 * @version 0.1
 * @since 20141203
 */
public class Example_DBAlertHelper implements View.OnClickListener {


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        exampleHelper(v.getContext());
    }


    //-------------------------------------------------------//
    //                     									 //
    //                 dbAlertHelper 範例					 //
    //                     									 //
    //-------------------------------------------------------//
    /*
	 * 注意：sqlite中資料只有兩種類型 TEXT/INTEGER，請詳照addItem方法
	 * 此版本中預設執行TaskEditorMain.java中之第154行，請視情況註解掉
	 */
    private void exampleHelper(Context context) {
        DBAlertHelper dbAlertHelper = new DBAlertHelper(context);

        // 增加物件
        // dbAlertHelper.addItem("測試", "1.0", "2.0", 333.0, 444.0, 5,0,"null");
        // dbAlertHelper.addItem("測試2", "1.0", "2.0", 333.0, 444.0, 5,0,"null");
        // dbAlertHelper.addItem("測試3", "1.0", "2.0", 333.0, 444.0, 5,0,"null");

        // 刪除物件
        dbAlertHelper.deleteItem(0);

        // 修改物件 - string
        dbAlertHelper.setItem(2, ColumnAlert.KEY.other, "這是經由API設定過的值");

        // 修改物件 - int
        dbAlertHelper.setItem(2, ColumnAlert.KEY.loc_id, 1);

        // 修改物件 - long
        dbAlertHelper.setItem(2, ColumnAlert.KEY.time_offset, MyCalendar.getNow());

        // 修改物件 - double
        dbAlertHelper.setItem(2, ColumnAlert.KEY.due_date_millis, (double)MyCalendar.getNow());

        // 取得物件 - 預設cursor
        Cursor defaultDbLocCursor = dbAlertHelper.getCursor();
        try {
            defaultDbLocCursor.moveToFirst();
        } catch (Exception e) {
            MyDebug.MakeLog(2, e.toString());
        }

        // 特殊指定cursor
        String[] projection = {}, selectionArgs = {};
        String selection = "", shortOrder = "";
        Cursor customDbLocCursor =
                dbAlertHelper.getCursor(projection, selection, selectionArgs, shortOrder);
        try {
            customDbLocCursor.moveToFirst();
        } catch (Exception e) {
            MyDebug.MakeLog(2, e.toString());
        }

        // 取得string
        String test_string = dbAlertHelper.getItemString(1, ColumnAlert.KEY.due_date_string);
        MyDebug.MakeLog(2, "dbAlertHelper" +
                ".getItemString(id=1,ColumnAlert.KEY.due_date_string)=" + test_string);

        // 取得double
        Double test_double = dbAlertHelper.getItemDouble(1, ColumnAlert.KEY.interval);
        MyDebug.MakeLog(2, "dbAlertHelper" +
                ".getItemDouble(id=1, ColumnAlert.KEY.interval)=" + test_double);

        // 取得int
        int test_int = dbAlertHelper.getItemInt(1, ColumnAlert.KEY.loc_id);
        MyDebug.MakeLog(2, "dbAlertHelper" +
                ".getItemInt(id=1, ColumnAlert.KEY.loc_id)=" + test_int);

        // 取得LONG數值
        long test_long = dbAlertHelper.getItemLong(1, ColumnAlert.KEY.due_date_millis);
        MyDebug.MakeLog(2, "dbAlertHelper" +
                ".getItemLong(id=1,ColumnAlert.KEY.due_date_millis)=" + test_long);

        // 取得總數量
        int count = dbAlertHelper.getCount();
        MyDebug.MakeLog(2, "dbAlertHelper.getCount=" + count);

        // 取得未完成的任務總數量
        int countUunfinished=dbAlertHelper.getCountByUnFinishedTask();
        MyDebug.MakeLog(2, "dbAlertHelper.getCountByUnFinishedTask=" + countUunfinished);

        // 取得已完成的任務總數量
        int countFinished=dbAlertHelper.getCountByFinishedTask();
        MyDebug.MakeLog(2, "dbAlertHelper.getCountByFinishedTask=" + countFinished);

        // 取得地點提醒的任務總數量
        int countLocationAlertTask=dbAlertHelper.getCountByLocationAlertTask();
        MyDebug.MakeLog(2, "dbAlertHelper.getCountByLocationAlertTask=" + countLocationAlertTask);

        // 取得到期提醒的任務總數量
        int countTimeAlertTask=dbAlertHelper.getCountByTimeAlertTask();
        MyDebug.MakeLog(2, "dbAlertHelper.getCountByTimeAlertTask=" + countTimeAlertTask);

        // 取得啟用智慧提醒的任務總數量
        int smartCount=dbAlertHelper.getCountBySmartAlertTask();
        MyDebug.MakeLog(2, "dbAlertHelper.getCountBySmartAlertTask=" + smartCount);

        Toast.makeText(context, "this.getClass().getName()=\n" +
                this.getClass().getName(), Toast.LENGTH_LONG).show();
    }
}
