
/**
 * @author Kent
 *
 */
package me.iamcxa.remindme.database.helper;

import tw.remindme.common.function.MyDebug;
import me.iamcxa.remindme.database.columns.ColumnLocation;
import me.iamcxa.remindme.database.columns.ColumnTask;
import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
/**
 *@version 0.0
 *@since 20141203
 */
public class DBTasksHelper {

	//	private Cursor cursor;
	private Context context;

	//
	private String[] projection, selectionArgs;
	private String shortOrder, selection;

	/**
	 * 主要結構
	 *@param Context => getActivity()等即可取得
	 *@return 無
	 *@throws 無
	 */
	public DBTasksHelper(Context context){

		this.context=context;
	}

	/**
	 * 此方法用來設定準備查詢的資料範圍。
	 *@param 不傳入參數即為預設選取模式：<br>
	 *1.全選資料表Tasks所有欄位.<br>
	 *2.selection和selectionArgs為null.<br>
	 *3.sortOrder為"created DESC".
	 *@return 回傳一個可以套進各式Adapter的Cursor資料物件。
	 *@throws null
	 */
	public Cursor getCursor(){

		return context.getContentResolver().
				query(ColumnTask.URI, 
						ColumnTask.PROJECTION, null, null, 
						ColumnTask.DEFAULT_SORT_ORDER);
	}

	/**
	 * 此方法用來設定準備查詢的資料範圍。
	 *@param projection 
	 *類型為String[]。表示資料表中欲選擇的欄位--傳入null可回傳全部--不過如需回傳全部建議不傳用我的預設值，或匯入"ColumnTask.PROJECTION"這組我預先定義的全選陣列。
	 *@param selection 
	 *類型為String。即SQL之(where=..)語句，如直接傳入5即等於(where==5)；可傳入＝?搭配selectionArgs使用。例如: <br>
	 *selection="City=高雄";
	 *@param selectionArgs 
	 *類型為String[]。搭配selection與?使用，表示欲查詢的元素(s)。例如：<br>
	 *selection="City=?"; <br>
	 *selectionArgs={高雄,台北};
	 *@param shortOrder 
	 *類型為String。表示欲排序的依據與順序。shortOrder="欄位名 [ASC,DESC]";
	 *@return 回傳一個可以套進各式Adapter的Cursor資料物件。
	 *@throws null
	 */
	public Cursor getCursor(String[] projection,
			String selection,
			String[] selectionArgs, 
			String shortOrder){

		return context.getContentResolver().
				query(ColumnTask.URI, 
						projection, 
						selection, 
						selectionArgs, 
						shortOrder);
	}


	/**
	 * 本方法可取得特定編號資料行之特定欄位的字串。需傳入索引編號與欄位名稱。<br>
	 * 注意：索引編號可能會隨著資料庫修改而被移除。
	 *@param objId 物件位於表格之索引ID
	 *@param columnName 類型是String。欄位名稱由ColumnTask.KEY方法提供。例如：<br>
	 *getItemString(10, ColumnTask.KEY.title)可取得id=10資料的title字串。
	 *@return String
	 *@throws Exception e.toString() or String "null".
	 */
	public String getItemString(int objId, String columnName){
		
		String[] projection={ "_id",columnName };
		String[] argStrings={ String.valueOf(objId) };
		Cursor cursor = getCursor(projection,"_id=?",argStrings,"_id DESC");
		cursor.moveToFirst();
		if (cursor.getCount()>0) {
			try {
	
				return cursor.getString(1).toString();
			} catch (Exception e) {
				// TODO: handle exception
				return e.toString();
			}
		}else {
			return "null";
		}
	}


	/**
	 * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
	 * 注意：索引編號可能會隨著資料庫修改而被移除。
	 *@param objId 物件位於表格之索引ID
	 *@param columnName 類型是String。欄位名稱由ColumnTask.KEY方法提供。例如：<br>
	 *getItemInt(10, ColumnTask.KEY.title)可取得id=10資料的title字串。
	 *@return String
	 *@throws "-1".
	 */
	public int getItemInt(int objId, String columnName){
		
		String[] projection={ "_id",columnName };
		String[] argStrings={ String.valueOf(objId) };
		Cursor cursor = getCursor(projection,"_id=?",argStrings,"_id DESC");
		cursor.moveToFirst();
		if (cursor.getCount()>0) {
			
			return cursor.getInt(1);
		}else {
			
			return -1;
		}
	}
}
