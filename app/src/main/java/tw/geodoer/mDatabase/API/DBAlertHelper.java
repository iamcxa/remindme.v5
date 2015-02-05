package tw.geodoer.mDatabase.API;

/**
 * @author Kent
 *
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.utils.MyDebug;

/**
 * @version 0.2
 * @since 20150204
 */
public class DBAlertHelper {

    private Context context;
    private Uri mUri = ColumnAlert.URI;
    private String className = this.getClass().getName().getClass().toString();


    /**
     * 主要結構
     *
     * @param context => getContext(),getActivity(),getApplicationContext(),...
     */
    public DBAlertHelper(Context context) {

        this.context = context;
    }

    /**
     * 此方法為 API 內部統一 Log 輸出規格之用。
     *
     * @param methodName method名稱
     * @param msg        錯誤訊息
     */
    private void msgOut(String methodName, String msg) {
        String newMsg = className + "." + methodName + ":" + msg;
        MyDebug.MakeLog(2, newMsg);
    }

    /**
     * 檢查傳入的 Cursor 是否已經關閉；如果尚未關閉則關閉它。
     *
     * @param cursor 傳入一個 Cursor。
     * @return true, if the Cursor is closed.
     * <br> false, if any error was occurred.
     */
    public boolean closeCursor(Cursor cursor) {
        try {
            if (!cursor.isClosed()) cursor.close();
            return true;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 此方法用來設定準備查詢的資料範圍。使用完記得使用 closeCursor() 關閉此cursor。
     * 不傳入參數即為預設選取模式：<br> 1.全選資料表task_alerts所有欄位.<br>
     *                             2.selection和selectionArgs為null.<br>
     *                             3.sortOrder為"created DESC".
     * @return 回傳一個可以套進各式Adapter的Cursor資料物件。理論上輸出應該不會有為null的情況。
     */
    public Cursor getCursor() {
        return context.getContentResolver().
                query(mUri,
                        ColumnAlert.PROJECTION, null, null,
                        ColumnAlert.DEFAULT_SORT_ORDER);
    }


    /**
     * 此方法用來設定準備查詢的資料範圍。
     *
     * @param projection    (String[]) <br>
     *                      表示資料表中欲選擇的欄位--傳入null可回傳全部--不過如需回傳全部建議不傳用我的預設值，或匯入"ColumnAlert.PROJECTION"這組我預先定義的全選陣列。
     * @param selection     (String) 即SQL之(where=..)語句，如直接傳入5即等於(where==5)；可傳入＝?搭配selectionArgs使用。例如: <br>
     *                      selection="City=高雄";
     * @param selectionArgs (String[]) <br>
     *                      搭配selection與?使用，表示欲查詢的元素(s)。例如：<br>
     *                      selection="City=?"; <br>
     *                      selectionArgs={高雄,台北};
     * @param shortOrder    (String) <br>
     *                      表示欲排序的依據與順序。shortOrder="欄位名 [ASC,DESC]";
     * @return 回傳一個可以套進各式Adapter的Cursor資料物件。記得檢查輸出是否為null。
     * <br> null, if any error was occurred.
     */
    public Cursor getCursor(String[] projection,
                            String selection,
                            String[] selectionArgs,
                            String shortOrder) {
        try {
            return context.getContentResolver().
                    query(mUri,
                            projection,
                            selection,
                            selectionArgs,
                            shortOrder);
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return null;
        }
    }


    /**
     * 此方法可取得資料表 task_alerts 之任務總數量。
     *
     * @return (int) a count of table "task_alerts".
     * <br> (int) -1, if any error was occurred.
     */
    public int getCount() {
        Cursor thisCursor = getCursor();
        int thisCount = thisCursor.getCount();
        if (closeCursor(thisCursor))
            return thisCount;
        else
            return -1;
    }


    /**
     * 此方法可取得資料表 task_alerts 之"未完成"任務總數量。
     *
     * @return (int) a count of "UnFinished" Task of the table "task_alerts"..
     * <br> (int) -1, if any error was occurred.
     */
    public int getCountByUnFinishedTask() {
        String[] projection = {"_id", "state"};
        // 0-> 未完成 1->已完成
        try {
            Cursor thisCursor = getCursor(projection, "state == 0", null, "_id DESC");
            int thisCount = thisCursor.getCount();
            if (closeCursor(thisCursor))
                return thisCount;
            else
                return -1;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 此方法可取得資料表 task_alerts 之"已完成"任務總數量。
     *
     * @return (int) a count of "Finished" Task of the table "task_alerts"..
     * <br> (int) -1, if any error was occurred.
     */
    public int getCountByFinishedTask() {
        String[] projection = {"_id", "state"};
        // 0-> 未完成 1->已完成
        try {
            Cursor thisCursor = getCursor(projection, "state == 1", null, "_id DESC");
            int thisCount = thisCursor.getCount();
            if (closeCursor(thisCursor))
                return thisCount;
            else
                return -1;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 此方法可取得資料表 task_alerts 之"到期"提醒類型任務總數量。
     *
     * @return (int) a count of "Time Depends" Task of the table "task_alerts".
     * <br> (int) -1, if any error was occurred.
     */
    public int getCountByTimeAlertTask() {
        String[] projection = {"_id", "type"};
        // 0-> 無提醒 1->到期提醒 2->提醒 3->到期+靠近提醒
        try {
            Cursor thisCursor = getCursor(projection, "type == 1", null, "_id DESC");
            int thisCount = thisCursor.getCount();
            if (closeCursor(thisCursor))
                return thisCount;
            else
                return -1;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 此方法可取得資料表 task_alerts 之"靠近"提醒類型任務總數量。
     *
     * @return (int) a count of "Location Depends" Task of the table "task_alerts".
     * <br> (int) -1, if any error was occurred.
     */
    public int getCountByLocationAlertTask() {
        String[] projection = {"_id", "type"};
        // 0-> 無提醒 1->到期提醒 2->提醒 3->到期+靠近提醒
        try {
            Cursor thisCursor = getCursor(projection, "type == 2", null, "_id DESC");
            int thisCount = thisCursor.getCount();
            if (closeCursor(thisCursor))
                return thisCount;
            else
                return -1;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 此方法可取得資料表 task_alerts 之"靠近且到期"提醒類型任務總數量。
     *
     * @return (int) a count of "Smart(loc+time)" Task of the table "task_alerts".
     * <br> (int) -1, if any error was occurred.
     */
    public int getCountBySmartAlertTask() {
        String[] projection = {"_id", "type"};
        // 0-> 無提醒 1->到期提醒 2->提醒 3->到期+靠近提醒
        try {
            Cursor thisCursor = getCursor(projection, "type == 3", null, "_id DESC");
            int thisCount = thisCursor.getCount();
            if (closeCursor(thisCursor))
                return thisCount;
            else
                return -1;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的字串。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) 欄位名稱由ColumnAlert.KEY方法提供。例如：<br>
     *                   getItemString(10, ColumnAlert.KEY.title) 可取得資料表 id=10 的 title 欄數值。
     * @return (String) Target's String.
     * <br> (String) String -> "error".
     */
    public String getItemString(int itemId, String columnName) {
        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(itemId)};
        try {
            Cursor thisCursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
            thisCursor.moveToFirst();
            if (thisCursor.getCount() > 0) {
                // columns => {"_id", columnName}
                // column0->"_id"
                // column1->"columnName"->target.
                return thisCursor.getString(1);
            } else {
                return "error";
            }
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return "error";
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) <br>
     *                   欄位名稱由 ColumnAlert.KEY 方法提供。例如：<br>
     *                   getItemInt(10, ColumnAlert.KEY._id) 可取得資料表 id=10 的 _id 欄數值。
     * @return (int) target's value.
     * <br> (int) -1, if any error was occurred.
     */
    protected int getItemInt(int itemId, String columnName) {
        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(itemId)};
        try {
            Cursor thisCursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
            thisCursor.moveToFirst();
            if (thisCursor.getCount() > 0) {
                // columns => {"_id", columnName}
                // column 0->"_id"
                // column 1->"columnName"->target.
                return thisCursor.getInt(1);
            } else {
                return -1;
            }
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) <br>
     *                   欄位名稱由 ColumnAlert.KEY 方法提供。例如：<br>
     *                   getItemInt(10, ColumnAlert.KEY.due_date_millis) 可取得資料表 id=10 的 due_date_millis 數值。
     * @return (double) target's value.
     * <br> (double) -1(d), if any error was occurred.
     */
    public Double getItemDouble(int itemId, String columnName) {
        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(itemId)};
        try {
            Cursor thisCursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
            thisCursor.moveToFirst();
            if (thisCursor.getCount() > 0) {
                // columns => {"_id", columnName}
                // column 0->"_id"
                // column 1->"columnName"->target.
                return thisCursor.getDouble(1);
            } else {
                return -1d;
            }
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1d;
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) <br>
     *                   欄位名稱由 ColumnAlert.KEY 方法提供。例如：<br>
     *                   getItemInt(10, ColumnAlert.KEY.due_date_millis) 可取得資料表 id=10 的 due_date_millis 欄位數值。
     * @return (long) target's value.
     * <br> (long) -1(l), if any error was occurred.
     */
    public long getItemLong(int itemId, String columnName) {
        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(itemId)};
        try {
            Cursor thisCursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
            thisCursor.moveToFirst();
            if (thisCursor.getCount() > 0) {
                // columns => {"_id", columnName}
                // column 0->"_id"
                // column 1->"columnName"->target.
                return thisCursor.getLong(1);
            } else {
                return -1l;
            }
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return -1l;
        }
    }


    //todo :需找時間改寫成將預計存入資料封裝成物件，再解開物件分別存入的方式，不然現在太亂了
    /**
     * 本方法可新增一筆資料到資料表 task_alerts。<br>
     * @return true.
     * <br> false , also logcat will output className+" additem method error=..."。
     */
    public boolean addItem(AlertItem anAlertItem) {

        ContentValues values = new ContentValues();
        // 對應task id
//        values.put(ColumnAlert.KEY.task_id, task_id);
//        // 此alert之類型（0=unfinished, 1=finished.）
//        values.put(ColumnAlert.KEY.type, type);
//
//        // 間隔
//        values.put(ColumnAlert.KEY.interval, interval);
//        // 時間偏移量
//        values.put(ColumnAlert.KEY.time_offset, time_offset);
//        // duedate之millis
//        values.put(ColumnAlert.KEY.due_date_millis, due_date_millis);
//
//        // 對應地點id
//        values.put(ColumnAlert.KEY.loc_id, loc_id);
//        // 是否啟用地點靠近提醒
//        values.put(ColumnAlert.KEY.loc_on, loc_on);
//        // 地點提醒半徑
//        values.put(ColumnAlert.KEY.loc_radius, loc_radius);


        try {
            context.getContentResolver().insert(mUri, values);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            MyDebug.MakeLog(2, className + " additem method error=" + e.toString());
            return false;
        }
    }


    /**
     * 本方法可更新一筆資料資料表 task_alerts 中的 (String) 類型數值資料。<br>
     *
     * @param itemId    (int) Item's id.
     * @param targetKey (String) 目標欄位名稱，由 ColumnAlert.Key 提供。
     * @param newValue  (String) 目標欄位的新值。
     * @return true.
     * <br> false.
     */
    public boolean setItem(int itemId, String targetKey, String newValue) {
        try {
            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);
            ContentValues values = new ContentValues();
            values.put(targetKey, newValue);
            context.getContentResolver().update(thisUri, values, null, null);
            return true;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 本方法可更新一筆資料資料表 task_alerts 中的 (Double) 類型數值資料。<br>
     *
     * @param itemId    (int) Item's id.
     * @param targetKey (String) 目標欄位名稱，由 ColumnAlert.Key 提供。
     * @param newValue  (Double) 目標欄位的新值。
     * @return True.
     * <br> false.
     */
    public boolean setItem(int itemId, String targetKey, Double newValue) {
        try {
            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);
            ContentValues values = new ContentValues();
            values.put(targetKey, newValue);
            context.getContentResolver().update(thisUri, values, null, null);
            return true;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 本方法可更新一筆資料資料表 task_alerts 中的 (long) 類型數值資料。<br>
     *
     * @param itemId    (int) Item's id.
     * @param targetKey (String) 目標欄位名稱，由 ColumnAlert.Key 提供。
     * @param newValue  (long) 目標欄位的新值。
     * @return True.
     * <br> false.
     */
    public boolean setItem(int itemId, String targetKey, long newValue) {
        try {
            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);
            ContentValues values = new ContentValues();
            values.put(targetKey, newValue);
            context.getContentResolver().update(thisUri, values, null, null);
            return true;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 本方法可更新一筆資料資料表 task_alerts 中的 (int) 類型數值資料。<br>
     *
     * @param itemId    (int) Item's id.
     * @param targetKey (String) 目標欄位名稱，由 ColumnAlert.Key 提供。
     * @param newValue  (int) 目標欄位的新值。
     * @return True.
     * <br> false.
     */
    public boolean setItem(int itemId, String targetKey, int newValue) {
        try {
            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);
            ContentValues values = new ContentValues();
            values.put(targetKey, newValue);
            context.getContentResolver().update(thisUri, values, null, null);
            return true;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 本方法可刪除一筆資料資料表 task_alerts 中的資料。<br>
     *
     * @param itemId (String) Target item's id。
     * @return True.
     * <br> false, also logcat will output className+" deleteItem method error=..."。
     */
    public boolean deleteItem(int itemId) {

        try {
            context
                    .getContentResolver()
                    .delete(mUri, "_id = ?", new String[]{String.valueOf(itemId)});
            return true;
        } catch (Exception e) {
            msgOut(Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
            return false;
        }
    }

    /**
     * 表 task_alerts 之封裝資料物件
     */
    public static class AlertItem {
        private int key;

    }
}