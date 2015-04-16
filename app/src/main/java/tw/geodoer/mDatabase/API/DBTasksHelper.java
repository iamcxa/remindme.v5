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

import java.util.ArrayList;

import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.utils.MyDebug;

/**
 * @version 0.1
 * @since 20150206
 */
public class DBTasksHelper {

    private Context context;
    private Uri mUri = ColumnTask.URI;


    /**
     * 此方法為 API 內部統一 Log 輸出規格之用。
     * @param methodName 發出訊息的 Method 名稱
     * @param msg        訊息
     */
    private void logOut(String methodName,String msg) {
        String newMsg = Thread.currentThread().getStackTrace()[1].getMethodName()
                +"." + methodName + ":" + msg;
        MyDebug.MakeLog(2, newMsg);

    }


    /**
     * 主要結構
     *
     * @param context => getContext(),getActivity(),getApplicationContext(),...
     */
    public DBTasksHelper(Context context) {

        this.context = context;
    }


    /**
     * 檢查傳入的 Cursor 是否已經關閉；如果尚未關閉則關閉它。
     *
     * @param cursor 傳入一個 Cursor 物件。
     * @return true, if the Cursor is closed.<br>
     * false, if any error was occurred.
     */
    public boolean closeCursor(Cursor cursor) {
        try {
            if (!cursor.isClosed()) cursor.close();
            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 此方法用來設定準備查詢的資料範圍。
     * 不傳入參數即為預設選取模式：<br> 1.全選資料表task所有欄位.<br>
     * 2.selection和selectionArgs為null.<br>
     * 3.sortOrder為"created DESC".
     *
     * @return 回傳一個可以套進各式Adapter的Cursor資料物件。理論上輸出應該不會有為null的情況。
     */
    public Cursor getCursor() {
        return context.getContentResolver().
                query(mUri,
                        ColumnTask.PROJECTION, null, null,
                        ColumnTask.DEFAULT_SORT_ORDER);
    }

    /**
     * 此方法用來設定準備查詢的資料範圍。
     *
     * @param projection    (String[]) <br>
     *                      表示資料表中欲選擇的欄位--傳入null可回傳全部--不過如需回傳全部建議不傳用我的預設值，或匯入"ColumnTask.PROJECTION"這組我預先定義的全選陣列。
     * @param selection     (String) <br>
     *                      即SQL之(where=..)語句，如直接傳入5即等於(where==5) 可傳入＝?搭配selectionArgs使用。例如: <br>
     *                      selection="City=高雄";
     * @param selectionArgs (String[]) <br>
     *                      搭配selection與?使用，表示欲查詢的元素(s)。例如：<br>
     *                      selection="City=?"; <br>
     *                      selectionArgs={高雄,台北};
     * @param shortOrder    (String) <br>
     *                      表示欲排序的依據與順序。shortOrder="欄位名 [ASC,DESC]";
     * @return (Cursor)a Cursor, 可以套進各式Adapter的資料物件。<br>
     * null, if any error was occurred.
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
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return null;
        }
    }


    /**
     * 此方法可取得資料表 task 之資料總數量。
     *
     * @return (int) a count of table "task".<br>
     * (int) -1, if any error was occurred.
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
     * 本方法可取得特定編號資料行之特定欄位的字串。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) 欄位名稱由 ColumnTask.KEY 方法提供。例如：<br>
     *                   getItemString(10, ColumnTask.KEY.title) 可取得資料表 id=10 的 task_id 欄數值。
     * @return (String) Target's String.<br>
     * (String) String -> "error".
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
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return "error";
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) <br>
     *                   欄位名稱由 ColumnTask.KEY 方法提供。例如：<br>
     *                   getItemInt(10, ColumnTask.KEY._id) 可取得資料表 id=10 的 _id 欄數值。
     * @return (int) target's value.<br>
     * (int)-1, if any error was occurred.
     */
    public int getItemInt(int itemId, String columnName) {
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
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return -1;
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) <br>
     *                   欄位名稱由 ColumnTask.KEY 方法提供。例如：<br>
     *                   getItemInt(10, ColumnTask.KEY.due_date_millis) 可取得資料表 id=10 的 due_date_millis 數值。
     * @return (double) target's value.<br>
     * (double) -1(d), if any error was occurred.
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
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return -1d;
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     (int) 物件位於表格之索引ID。
     * @param columnName (String) <br>
     *                   欄位名稱由 ColumnTask.KEY 方法提供。例如：<br>
     *                   getItemInt(10, ColumnTask.KEY.due_date_millis) 可取得資料表 id=10 的 due_date_millis 欄位數值。
     * @return (long) target's value.<br>
     * (long) -1(l), if any error was occurred.
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
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return -1l;
        }
    }


    //todo: 需找時間一併改為存入封裝物件之方式
    /**
     * 本方法可新增一筆資料到資料表tasks。<br>
     *
     * @param locName      (String) 地點名稱。
     * @param lat          (String) 緯度。
     * @param lon          (String) 經度。
     * @param distance     (Double) 距離。
     * @param lastUsedTime (Double) 上次使用時間。
     * @param weight       (int) 使用者造訪偏好(地點權重)。
     * @param type         (int) 地點類型。
     * @param tag          (String) 地點tag。
     * @return true.<br>
     * false , also logcat will output "... additem method error=..."。
     */
    public boolean addItem(String locName, String lat, String lon,
                           Double distance, Double lastUsedTime,
                           int weight, int type,
                           String tag) {

        ContentValues values = new ContentValues();
        // 0 - 任務地點id
        // 1 - 地點名稱字串
//        values.put(ColumnTask.KEY.name, locName);
//        // 2 - 3 - 經緯度
//        values.put(ColumnTask.KEY.lat, lat);
//        values.put(ColumnTask.KEY.lon, lon);
//        // 4 - 與上次偵測地點之距離
//        values.put(ColumnTask.KEY.distance, distance);
//        // 5 - 上次使用時間
//        values.put(ColumnTask.KEY.lastUsedTime, lastUsedTime);
//        // 6 - 上次使用時間
//        values.put(ColumnTask.KEY.weight, weight);
//        // 7 - 地點類型
//        values.put(ColumnTask.KEY.type, type);
//        // 8 - 地點關鍵字
//        values.put(ColumnTask.KEY.tag, tag);

        try {
            context.getContentResolver().insert(mUri, values);
            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 本方法可更新一筆資料資料表tasks中的字串資料。<br>
     *
     * @param itemId    (String) 地點ID。
     * @param targetKey (String) 目標欄位名稱，由ColumnTask.Key提供。
     * @param newValue  (String) 目標欄位的新值。
     * @return true. <br>
     * false, also logcat will output "DBLocationHelpr setItem method error=..."。
     */
    public boolean setItem(int itemId, String targetKey, String newValue) {

        try {

            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);

            ContentValues values = new ContentValues();

            values.put(targetKey, newValue);

            context.getContentResolver().update(thisUri, values, null, null);

            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }


    /**
     * 本方法可更新一筆資料資料表tasks中的數值資料。<br>
     *
     * @param itemId    (String) 地點ID。
     * @param targetKey (String) 目標欄位名稱，由ColumnTask.Key提供。
     * @param newValue  (int) 目標欄位的新值。
     * @return True. <br>
     * false, also logcat will output "DBLocationHelpr setItem method error=..."。
     */
    public boolean setItem(int itemId, String targetKey, int newValue) {

        try {

            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);

            ContentValues values = new ContentValues();

            values.put(targetKey, newValue);

            context.getContentResolver().update(thisUri, values, null, null);

            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }

    /**
     * 本方法可更新一筆資料資料表tasks中的數值資料。<br>
     *
     * @param itemId    (String) 地點ID。
     * @param targetKey (String) 目標欄位名稱，由ColumnTask.Key提供。
     * @param newValue  (long) 目標欄位的新值。
     * @return True. <br>
     * false, also logcat will output "DBLocationHelpr setItem method error=..."。
     */
    public boolean setItem(int itemId, String targetKey, long newValue) {

        try {

            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);

            ContentValues values = new ContentValues();

            values.put(targetKey, newValue);

            context.getContentResolver().update(thisUri, values, null, null);

            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }

    /**
     * 本方法可更新一筆資料資料表tasks中的數值資料。<br>
     *
     * @param itemId    (String) 地點ID。
     * @param targetKey (String) 目標欄位名稱，由ColumnTask.Key提供。
     * @param newValue  (Double) 目標欄位的新值。
     * @return True. <br>
     * false, also logcat will output "DBLocationHelpr setItem method error=..."。
     */
    public boolean setItem(int itemId, String targetKey, Double newValue) {

        try {

            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);

            ContentValues values = new ContentValues();

            values.put(targetKey, newValue);

            context.getContentResolver().update(thisUri, values, null, null);

            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }

    /**
     * 此方法可取得資料表 task 所有還存在的任務 ID 陣列。
     *
     * @return (ArrayList)
     */
    public ArrayList<Integer> getIDArrayListOfTask() {
        String[] projection = {"_id"};
        try {
            Cursor thisCursor = getCursor(projection, "_id > 0", null, "_id DESC");
            if(thisCursor.getCount()==0) return null;
            else
            {
                ArrayList<Integer> thisArray =new ArrayList<>();
                thisArray.clear();
                while(thisCursor.moveToNext())
                    thisArray.add(thisCursor.getInt(0));
                thisCursor.close();
                return thisArray;
            }
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(),e.toString());
            return null;
        }
    }

    /**
     * 此方法可取得資料表 task 該id 是否存在
     * 使用此式的前提是 ID 不能有重複存在
     * @return boolean
     */
    public boolean isIDExist(int id)
    {
        boolean check = false;
        String[] projection = {"_id"};
        try {
            Cursor thisCursor = getCursor(projection, "_id == "+id, null, "_id DESC");

            if(thisCursor.getCount()==0)check = false;
            else check =true;

            thisCursor.close();
            return check;

        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(),e.toString());
            return false;
        }

    }


    /**
     * 本方法可刪除一筆資料資料表tasks中的資料。<br>
     *
     * @param itemId (String) 地點ID。
     * @return True. <br>
     * false, also logcat will output "DBLocationHelpr deleteItem method error=..."。
     */
    public boolean deleteItem(int itemId) {

        try {

            context.getContentResolver().delete(mUri, "_id = ?", new String[]{String.valueOf(itemId)});

            return true;
        } catch (Exception e) {
            logOut(Thread.currentThread().getStackTrace()[2].getMethodName(), e.toString());
            return false;
        }
    }
}