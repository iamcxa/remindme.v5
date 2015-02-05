/**
 *
 */
package tw.geodoer.mDatabase.API;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.utils.MyDebug;

/**
 * @author Kent
 * @version 0.1
 * @since 20141203
 */
public class Example_DBLocationHelper implements View.OnClickListener {


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
    //                 DBLocationHelper 範例					 //
    //                     									 //
    //-------------------------------------------------------//
    /*
	 * 注意：sqlite中資料只有兩種類型 TEXT/INTEGER，請詳照addItem方法
	 * 此版本中預設執行TaskEditorMain.java中之第154行，請視情況註解掉
	 */
    private void exampleHelper(Context context) {
        DBLocationHelper dbLocationHelper = new DBLocationHelper(context);

        // 增加物件
        dbLocationHelper.addItem("測試", "1.0", "2.0", 333.0, 444.0, 5, 0, "null");
        dbLocationHelper.addItem("測試2", "1.0", "2.0", 333.0, 444.0, 5, 0, "null");
        dbLocationHelper.addItem("測試3", "1.0", "2.0", 333.0, 444.0, 5, 0, "null");

        // 刪除物件
        dbLocationHelper.deleteItem(3);

        // 修改物件
        dbLocationHelper.setItem(2, ColumnLocation.KEY.distance, 123123.2);

        // 取得物件 - 預設cursor
        Cursor defaultDbLocCursor = dbLocationHelper.getCursor();
        try {
            defaultDbLocCursor.moveToFirst();
        } catch (Exception e) {
            MyDebug.MakeLog(2, e.toString());
        }

        // 特殊指定cursor
        String[] projection = {}, selectionArgs = {};
        String selection = "", shortOrder = "";
        Cursor customDbLocCursor =
                dbLocationHelper.getCursor(projection, selection, selectionArgs, shortOrder);
        try {
            customDbLocCursor.moveToFirst();
        } catch (Exception e) {
            MyDebug.MakeLog(2, e.toString());
        }

        // 取得string
        String test_string = dbLocationHelper.getItemString(1, ColumnLocation.KEY.name);
        MyDebug.MakeLog(2, "dbLocationHelper" +
                ".getItemString(id=1,ColumnLocation.KEY.name)=" + test_string);

        // 取得double
        Double test_double = dbLocationHelper.getItemDouble(1, ColumnLocation.KEY.distance);
        MyDebug.MakeLog(2, "dbLocationHelper" +
                ".getItemDouble(id=1, ColumnLocation.KEY.distance)=" + test_double);

        // 取得int
        int test_int = dbLocationHelper.getItemInt(1, ColumnLocation.KEY.type);
        MyDebug.MakeLog(2, "dbLocationHelper" +
                ".getItemInt(id=1, ColumnLocation.KEY.type)=" + test_int);

        // 取得LONG數值
        long test_long = dbLocationHelper.getItemLong(1, ColumnLocation.KEY.lon);
        MyDebug.MakeLog(2, "dbLocationHelper" +
                ".getItemLong(id=1,ColumnLocation.KEY.lon)=" + test_long);

        // 取得總數量
        int count = dbLocationHelper.getCount();
        MyDebug.MakeLog(2, "dbLocationHelper.getCount=" + count);

        // 設定新值
        dbLocationHelper.setItem(1, ColumnLocation.KEY.name, "這是經由API設定過的值");


        Toast.makeText(context, "this.getClass().getName()=\n" +
                this.getClass().getName(), Toast.LENGTH_LONG).show();

    }
}
