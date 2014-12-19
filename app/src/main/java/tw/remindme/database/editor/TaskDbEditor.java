package tw.remindme.database.editor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tw.remindme.database.contentProvider.TaskDbProvider.DatabaseHelper;
import tw.remindme.database.columns.ColumnLocation;

public class TaskDbEditor {
	private Context context;
	private DatabaseHelper DH = null;
	private SQLiteDatabase db;
	public TaskDbEditor(Context context){
		this.context=context;
	}
	
	public void openDB(){
    	DH = new DatabaseHelper(context);
    }
	
    public void closeDB(){
    	DH.close();    	
    }
    

	public Cursor query(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = DH.getWritableDatabase();
		return db.query(ColumnLocation.TABLE_NAME,		//資料表名稱
				projection,	//欄位名稱
				selection, // WHERE
				selectionArgs, // WHERE 的參數
				sortOrder, // GROUP BY
				null, // HAVING
				null  // ORDOR BY
		);
	}

	public void update(ContentValues values, Object object,
			Object object2) {
		db = DH.getWritableDatabase();
		db.update(ColumnLocation.TABLE_NAME, values, null	, null);
	}
    
}
