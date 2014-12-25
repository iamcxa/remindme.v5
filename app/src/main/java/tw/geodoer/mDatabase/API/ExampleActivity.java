/**
 *
 */
package tw.geodoer.mDatabase.API;

import android.app.Activity;
import android.database.Cursor;

import tw.geodoer.utils.MyDebug;
import tw.geodoer.mDatabase.columns.ColumnLocation;

/**
 * @author Kent
 * @version 0.1
 * @since 20141203
 */
public class ExampleActivity extends Activity {


    //-------------------------------------------------------//
    //                     									 //
    //                 DBLocationHelper 範例					 //
    //                     									 //
    //-------------------------------------------------------//
	/*
	 * 注意：sqlite中資料只有兩種類型 TEXT/INTEGER，請詳照addItem方法
	 * 此版本中預設執行TaskEditorMain.java中之第154行，請視情況註解掉
	 */
    private void exampleOfDBLocationHelper(){
        DBLocationHelper dbLocationHelper=new DBLocationHelper(getApplicationContext());

        // 增加物件
        dbLocationHelper.addItem("測試", "1.0", "2.0", 333.0, 444.0, 5,0,"null");
        dbLocationHelper.addItem("測試2", "1.0", "2.0", 333.0, 444.0, 5,0,"null");
        dbLocationHelper.addItem("測試3", "1.0", "2.0", 333.0, 444.0, 5,0,"null");

        // 刪除物件
        dbLocationHelper.deleteItem(3);

        // 修改物件
        dbLocationHelper.setItem(2, ColumnLocation.KEY.distance,123123.2);

        // 取得物件
        // 預設cursor
        Cursor defaultDbLocCursor=dbLocationHelper.getCursor();
        // 特殊指定cursor
        String[] projection = null,selectionArgs = null;
        String selection = null,shortOrder = null;
        Cursor customDbLocCursor=dbLocationHelper.getCursor(projection, selection, selectionArgs, shortOrder);

        // 取得id=0欄位之地點名稱
        String locNameString=dbLocationHelper.getItemString(0, ColumnLocation.KEY.name);
        // 取得id=0欄位之地點距離
        Double locDistance=dbLocationHelper.getItemDouble(0, ColumnLocation.KEY.distance);
        // 取得id=0欄位之地點類型
        int locType=dbLocationHelper.getItemInt(0, ColumnLocation.KEY.type);

        // 取得總數量
        int count=dbLocationHelper.getCount();
        MyDebug.MakeLog(2, "dbLocationHelper.getCount="+count);
    }
}
