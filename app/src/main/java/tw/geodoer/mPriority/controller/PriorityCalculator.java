/**
 * 
 */
package tw.geodoer.mPriority.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.text.DecimalFormat;

import tw.geodoer.mDatabase.controller.TaskDbEditor;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.common.controller.MyCalendar;
import tw.geodoer.common.controller.MyDebug;
import tw.geodoer.mGeoInfo.controller.DistanceCalculator;

/**
 * @author cxa
 * 
 */
public class PriorityCalculator {

	private double Lat;
	private double Lon;
	public static String[] projection = ColumnLocation.PROJECTION;
	public static String selection = ColumnLocation.KEY.lat
			+ " <> \"\" AND " + ColumnLocation.KEY.lon
			+ " NOT LIKE \"null%\"";
	public static String sortOrder = ColumnLocation.DEFAULT_SORT_ORDER;
	public static String[] selectionArgs;
	// private ArrayList<String> coordinatesList = new ArrayList<String>();
	// private ArrayList<String> distanceList = new ArrayList<String>();
	// private ArrayList<String> newdistanceList = new ArrayList<String>();
	// private ArrayList<String> idList = new ArrayList<String>();
	// private ArrayList<String> WeightsList = new ArrayList<String>();

	private int newPriorityWeight;
	private String endDate, endTime;
	private Context context;
    private static TaskDbEditor load;
	
	public PriorityCalculator(Context context){
		this.context=context;
		load = new TaskDbEditor(context);
	}
	
	public Cursor loadData() {
		load.openDB();
		Cursor Data = load.query(projection,
				selection, selectionArgs, sortOrder);
		return Data;
		
	}

	public void ProcessData(Cursor data) {

		MyDebug.MakeLog(0, "GetDistance onLoadFinished");
		int i = 0;
		double Distance;
		long dayLeft;
		data.moveToFirst();
		for (i = 0; i < data.getCount(); i++) {
			MyDebug.MakeLog(0, "GetDistance data.move@" + i);
			// idList.add("" +
			// data.getInt(ColumnTask.IndexColumns.KEY_ID));
			// WeightsList.add("" + data.getInt(4));

			MyDebug.MakeLog(0, "GetDistance LatLon1=" + data.getString(2)
					+ " / LatLon2=" + Lat + "," + Lon);
			Distance = DistanceCalculator.Distance(data.getString(2), Lat, Lon);
			endDate = data.getString(5);
			endTime = data.getString(6);
			dayLeft = MyCalendar.getDaysLeft(endDate + " " + endTime, 1);

			MyDebug.MakeLog(0, "GetDistance dayLeft org=" + dayLeft);
			MyDebug.MakeLog(0, "GetDistance endDate=" + endDate);
			MyDebug.MakeLog(0, "GetDistance endTime=" + endTime);
			MyDebug.MakeLog(0, "GetDistance Distance=" + Distance);
//			saveOrUpdate(
//					Integer.valueOf(data.getInt(0)),
//					getNewWeight(i, Integer.valueOf(data.getInt(4)), Distance,
//							dayLeft), Distance);
			if (!data.isLast())
				data.moveToNext();

		}
	}

	// if(10km> 距離 >3km) 權重每少 1km+150.。
	// if(4km> 距離 >1km) 權重每少 1km+250
	// if(2km> 距離 >1km) 權重每少 1km+400。
	// if(0.5km> 距離 >0.1km) 權重每少 1km+800
	// if(48> 時間 >23) 權重每少一小時 +200
	// if(24> 時間 >11) 權重每少一小時 +250
	// if(12> 時間 >6) 權重每少一小時 +300
	// if(7> 時間 >3) 權重每少一小時 +350
	// if(4> 時間 >2) 權重每少一小時 +550
	// if(3> 時間 >0) 權重每少一小時 +1000

	public int getNewWeight(int i, int oldWeights, Double Distance, long dayLeft) {
		int newPriorityWeight = oldWeights;
		dayLeft = (dayLeft) * 24;
		//Distance *= 10;
		MyDebug.MakeLog(0, "GetDistance dayLeft=" + dayLeft);
		// 時間
//		if ((dayLeft>168)) {
//			newPriorityWeight *= 0.25;
//		} else if ((168 > dayLeft) && (dayLeft > 119)) {
//			newPriorityWeight *= 0.5;
//		} else if ((120 > dayLeft) && (dayLeft > 47)) {
//			newPriorityWeight -= 50;
//		} else if ((48 > dayLeft) && (dayLeft > 23)) {
//			newPriorityWeight -= 20;
//		} else if ((24 > dayLeft) && (dayLeft > 11)) {
//			newPriorityWeight += 5;
//		} else if ((12 > dayLeft) && (dayLeft > 6)) {
//			newPriorityWeight += 30;
//		}

		// 距離
		if ((Distance >= 24)) {
			newPriorityWeight *= 0.78;
		} else if ((24 > Distance) && (Distance > 4)) {
			newPriorityWeight *= 0.92;
		} else if ((5 > Distance) && (Distance > 1.9)) {
			newPriorityWeight *= 0.95;
		} else if ((2 > Distance) && (Distance > 0.4)) {
			newPriorityWeight *= 1.01;
		} else if ((0.5 > Distance) && (Distance > 0.1)) {
			newPriorityWeight *= 1.15;
		}
//		if ((Distance >= 15)) {
//			newPriorityWeight *= 0.88;
//		} else {
//			newPriorityWeight *= 1.15;
//	}
//		
		//newPriorityWeight = newPriorityWeight + oldWeights;
//		
	//	oldWeights+=newPriorityWeight ;
		MyDebug.MakeLog(0, "GetDistance getNewWeight=" + newPriorityWeight);
		return newPriorityWeight+1;   //+1防止歸0
	}

	// 儲存或修改備忘錄資訊
	public boolean saveOrUpdate(int ID, int newPriorityWeight, Double Distance) {
		try {
			ContentValues values = new ContentValues();

			DecimalFormat df = new DecimalFormat("##.00");
			Distance = Double.parseDouble(df.format(Distance));

			values.clear();

			values.put(ColumnLocation.KEY.dintance,
					((Distance)));
			values.put(ColumnTask.KEY.priority,
					newPriorityWeight);

			// 修改
			Uri uri = ContentUris.withAppendedId(ColumnLocation.URI, ID);
			context.getContentResolver().update(uri, values, null, null);
			//load.update(values, null, null);
			// Toast.makeText(this, "事項更新成功！" + curDate.toString(),
			// Toast.LENGTH_SHORT).show();

			MyDebug.MakeLog(0, "GetDistance newPriorityWeight更新成功！");
			load.closeDB();
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "GetDistance 儲存出錯！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
	}
	
	public void SetLatLng(double Lat,Double Lon){
		this.Lat=Lat;
		this.Lon=Lon;
	}
}
