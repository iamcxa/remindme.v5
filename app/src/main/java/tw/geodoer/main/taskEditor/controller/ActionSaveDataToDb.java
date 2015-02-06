package tw.geodoer.main.taskEditor.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.util.Calendar;

import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mPriority.API.ServiceCaller;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.main.taskEditor.view.TaskEditorMainFragment;
import tw.geodoer.utils.MyCalendar;
import tw.geodoer.utils.MyDebug;

/**
 * @author Kent
 * @version 20141007
 */
public class ActionSaveDataToDb {
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();
    protected setTableTasks setTableTasks;
    protected setTableAlert setTableAlert;
    protected setTableLocation setTableLocation;
    private Uri mUri;
    private ActionSetAlarm mSetAlarm;
    private Context context;
    private ContentValues values = new ContentValues();
    private int taskId = 0, alertId = 0, locId = 0, alertSelected = 0, locSelected = 0;
    private int lastTaskID, lastLocID;
    //private readDB readDB;

    public ActionSaveDataToDb(Context context, int thisTaskID, int lastTaskID, int lastLocID) {
        super();
        this.context = context;
        this.lastTaskID = lastTaskID;
        this.lastLocID = lastLocID;

        // 取得日期與時間選擇器數值加總後的毫秒值
        getTaskDueDateTime();

        // 寫入或更新資料庫
        saveTableTasks();
        saveTableAlert();
        //saveTableLocation();
        //----------------------------------------------------------------------------------//
        //call out service position                                                         //
        ServiceCaller.call(context,ServiceCaller.KEY_POSITION);                             //
        //----------------------------------------------------------------------------------//

    }

    // 取得日期與時間加總的到期日毫秒
    private static long getTaskDueDateTime() {
        // 初始化
        long taskDueDateTime = 0;

        //
        int mYear = mEditorVar.TaskDate.getmYear();
        int mMonth = mEditorVar.TaskDate.getmMonth();
        int mDay = mEditorVar.TaskDate.getmDay();
        int mHour = mEditorVar.TaskDate.getmHour();
        int mMinute = mEditorVar.TaskDate.getmMinute();
        MyDebug.MakeLog(2, "@selected Date plus time=" + mYear + "/" + mMonth + "/" + mDay + "/" + mHour + ":" + mMinute);

        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(mYear, mMonth - 1, mDay, mHour, mMinute);

        taskDueDateTime = c.getTimeInMillis();

        mEditorVar.TaskDate.setmDatePulsTimeMillis(taskDueDateTime);

        return taskDueDateTime;
    }

    private void saveTableTasks() {
        values.clear();
        // 設定對應 URI, 執行 SQL 命令
        mUri = ColumnTask.URI;
        setTableTasks = new setTableTasks(values);
        isSaveOrUpdate(values, taskId);
    }

    private void saveTableAlert() {
        values.clear();
        // 設定對應 URI, 執行 SQL 命令
        mUri = ColumnAlert.URI;
        setTableAlert = new setTableAlert(values, lastTaskID, lastLocID);
        if (isSaveOrUpdate(values, alertId)) {
            mSetAlarm = new ActionSetAlarm(context
                    , mEditorVar.TaskDate.getmDatePulsTimeMillis()
                    , lastTaskID + 1
            );
            mSetAlarm.SetIt();
        }
    }

    private void saveTableLocation() {
        values.clear();
        // 設定對應 URI, 執行 SQL 命令
        mUri = ColumnLocation.URI;
        setTableLocation = new setTableLocation(values);
        isSaveOrUpdate(values, locId);
    }

    // 判斷本次操作是寫入新資料或更新已存在資料
    private boolean isSaveOrUpdate(ContentValues values, int taskId) {
        if (taskId != 0) {
            return UpdateIt(values, taskId);
        } else {
            return SaveIt(values);
        }
    }

    // 寫入新資料
    private boolean SaveIt(ContentValues values) {
        try {


            context.getContentResolver().insert(mUri, values);
            Toast.makeText(context, "新事項已經儲存", Toast.LENGTH_SHORT).show();


            return true;
        } catch (Exception e) {
            Toast.makeText(context, "儲存出錯！", Toast.LENGTH_SHORT).show();
            MyDebug.MakeLog(2, "SaveOrUpdate SaveIt error=" + e);
            return false;
        }
    }

    // 更新已存在資料
    private boolean UpdateIt(ContentValues values, int taskId) {
        try {
            Uri uri = ContentUris.withAppendedId(mUri,
                    taskId);
            context.getContentResolver().update(uri, values, null, null);
            Toast.makeText(context, "事項更新成功！", Toast.LENGTH_SHORT).show();
            //if(alertSelected==1) mSetAlarm.SetIt(true);
            mEditorVar.Task.setTaskId(0);
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "儲存出錯！", Toast.LENGTH_SHORT).show();
            MyDebug.MakeLog(2, "SaveOrUpdate UpdateIt error=" + e);
            return false;
        }
    }
    // 結束 //
}

/*
 * 
 */
class setTableTasks {
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();

    public setTableTasks(ContentValues values) {
        super();
        getTaskDataFields();
        setTasksValues(values);
    }

    //TODO　由view物件取得輸入資訊
    private static void getTaskDataFields() {

        //---------------------------- 字串 -------------------------//
        // 標題字串
        mEditorVar.Task.setTitle(TaskEditorMainFragment.getTaskTitle());
        // 確保任務狀態為"未完成"
        mEditorVar.Task.setStatus(mEditorVar.Task.TASK_STATUS_UNFINISHED);
        // 註解字串（任務說明）
        mEditorVar.Task.setContent(TaskEditorMainFragment.getTaskContent());

        //---------------------------- 時間 -------------------------//
        // 設定該任務建立時間
        mEditorVar.Task.setCreated(MyCalendar.getNow());
        // 設定資料庫日期(字串)欄位
        mEditorVar.Task.setDue_date_string(TaskEditorMainFragment.getTaskDueDateString());
        // 設定資料庫日期(毫秒)欄位
        mEditorVar.Task.setDue_date_millis(mEditorVar.TaskDate.getmDatePulsTimeMillis());

        //---------------------------- IDs -------------------------//
        mEditorVar.Task.setPriority(TaskEditorMainFragment.getTaskPriority().getSelectedItemPosition());

        mEditorVar.Task.setCategory_id(TaskEditorMainFragment.getTaskCategory().getSelectedItemPosition());

        mEditorVar.Task.setPriority(TaskEditorMainFragment.getTaskPriority().getSelectedItemPosition());

        mEditorVar.Task.setColor(mEditorVar.TaskCardColor.getTaskDefaultColor());

        mEditorVar.Task.setTag_id("null");

        mEditorVar.Task.setCollaborator_id("null");

        mEditorVar.Task.setSync_id(0);

        mEditorVar.Task.setChecked(0);
    }


    // 存入資料表 - tasks
    public void setTasksValues(ContentValues values) {
        // 1 - ID
        //values.put(ColumnTask.KEY._id, mEditorVar.Task.getTaskId());
        // 2 - 標題
        values.put(ColumnTask.KEY.title, mEditorVar.Task.getTitle());
        // 3 - 狀態  - 0未完成 - 1完成
        values.put(ColumnTask.KEY.status, mEditorVar.Task.getStatus());
        // 4 - 備註
        values.put(ColumnTask.KEY.content, mEditorVar.Task.getContent());
        // 5 - 到期日 - 毫秒
        values.put(ColumnTask.KEY.due_date_millis, mEditorVar.Task.getDue_date_millis());
        // 6 - 到期日  - 字串
        values.put(ColumnTask.KEY.due_date_string, mEditorVar.Task.getDue_date_string());
        // 7 - 任務卡片顏色代號
        values.put(ColumnTask.KEY.color, mEditorVar.Task.getColor());
        // 8 - 優先權
        values.put(ColumnTask.KEY.priority, mEditorVar.Task.getPriority());
        // 9 - 此任務新增時間
        values.put(ColumnTask.KEY.created, mEditorVar.Task.getCreated());
        // 10- 分類id
        values.put(ColumnTask.KEY.category_id, mEditorVar.Task.getCategory_id());
        // 11- 專案id
        values.put(ColumnTask.KEY.project_id, mEditorVar.Task.getProject_id());
        // 12- 協作者uid
        values.put(ColumnTask.KEY.collaborator_id, mEditorVar.Task.getCollaborator_id());
        // 13- 同步任務id
        values.put(ColumnTask.KEY.sync_id, mEditorVar.Task.getSync_id());
        // 14- 任務地點id
        values.put(ColumnTask.KEY.location_id, mEditorVar.Task.getLocation_id());
        // 15- 標籤id
        values.put(ColumnTask.KEY.tag_id, mEditorVar.Task.getTag_id());
        // checked
        values.put(ColumnTask.KEY.checked, mEditorVar.Task.getChecked());
    }
}

/*
 * TODO
 */
class setTableLocation {
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();

    public setTableLocation(ContentValues values) {
        super();
        getLocationFields();
        setTaskLocation(values);
    }

    private void getLocationFields() {
        // TODO 地點判斷與取得相關資料部分仍未完成

        if (TaskEditorMainFragment.getTaskLocation().getSelectedItemPosition() != -1) {
            // 設定地點名稱
            mEditorVar.TaskLocation.setName("null");

            // 設定經緯度
            mEditorVar.TaskLocation.setLat(0.0);
            mEditorVar.TaskLocation.setLon(0.0);

            // 設定目的地與現在地點距離
            mEditorVar.TaskLocation.setDistance(0.0);

            // 設定該地點最近使用時間為現在
            mEditorVar.TaskLocation.setLastUsedTime(MyCalendar.getNow());
        }
    }


    // 存入資料到資料表 - task_location
    public void setTaskLocation(ContentValues values) {
        // 1 - 任務地點id
        //values.put(ColumnLocation.KEY._id,mEditorVar.TaskLocation.getLocationId());
        // 2 - 地點名稱字串
        values.put(ColumnLocation.KEY.name, mEditorVar.TaskLocation.getName());
        // 3 - 4 - 經緯度
        values.put(ColumnLocation.KEY.lat, mEditorVar.TaskLocation.getLat());
        values.put(ColumnLocation.KEY.lon, mEditorVar.TaskLocation.getLon());
        // 5 - 與上次偵測地點之距離
        values.put(ColumnLocation.KEY.distance, mEditorVar.TaskLocation.getDistance());
        // 6 - 上次使用時間
        values.put(ColumnLocation.KEY.lastUsedTime, mEditorVar.TaskLocation.getLastUsedTime());

    }
}

/*
 * TODO
 */
class setTableAlert {
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();

    int newTaskId = 0;
    int newLocId = 0;

    public setTableAlert(ContentValues values, int lastTaskID, int lastLocID) {
        super();
        this.newTaskId = lastTaskID + 1;
        this.newLocId = lastLocID;
        MyDebug.MakeLog(2, "@newTaskId=" + newTaskId + ", selectedLocId=" + newLocId);
        getAlertFields();
        setTaskAlert(values);
    }

    private void getAlertFields() {
        // 設定提醒時間毫秒數
        mEditorVar.TaskAlert.setDue_date_millis(mEditorVar.TaskDate.getmDatePulsTimeMillis());

        // 設定（同步）提醒時間字串
        mEditorVar.TaskAlert.setDue_date_string(mEditorVar.Task.getDue_date_string());

        // 設定提醒間隔
        mEditorVar.TaskAlert.setInterval(0);

        // 設定時間偏移
        mEditorVar.TaskAlert.setTime_offset(0);

        // 設定提醒類型 - 0-> 無提醒 1->到期提醒 2->靠近提醒 3->到期+靠近提醒
        if (mEditorVar.TaskDate.getmDatePulsTimeMillis() == 0) {
            mEditorVar.TaskAlert.setType(1);
        } else if ((mEditorVar.TaskDate.getmDatePulsTimeMillis() != 0) &&
                (mEditorVar.TaskLocation.getName() == "null")) {
            mEditorVar.TaskAlert.setType(2);
        } else if ((mEditorVar.TaskDate.getmDatePulsTimeMillis() != 0) &&
                (mEditorVar.TaskLocation.getName() != "null")) {
            mEditorVar.TaskAlert.setType(3);
        }

        // 設定對應任務ID
        mEditorVar.TaskAlert.setTask_id(newTaskId);

        // 設定對應地點ID
        mEditorVar.TaskAlert.setLoc_id(newLocId);

        // 設定任務提醒狀態 0-> 未完成
        mEditorVar.TaskAlert.setState(0);

        // 設定是否使用靠近地點提醒 - 取得地點id/地點開關/地點半徑
        setAlertByLocation();
    }

    private void setAlertByLocation() {
        //		if(loc){
        //
        //		}else{
        //			// 設定偵測地點ID
        //			mEditorVar.TaskAlert.setLoc_id(newLocId);
        //			mEditorVar.TaskAlert.setLoc_on(0);
        //			mEditorVar.TaskAlert.setLoc_radius(0);
        //		}
    }

    // 存入資料到資料表 - task_alerts
    public void setTaskAlert(ContentValues values) {
        // 1 - 提醒id
        //values.put(ColumnAlert.KEY._id,mEditorVar.TaskAlert.getAlertID());
        // 2 - 到期日 - 毫秒
        values.put(ColumnAlert.KEY.due_date_millis, mEditorVar.TaskAlert.getDue_date_millis());
        // 3 - 到期日 - 字串
        values.put(ColumnAlert.KEY.due_date_string, mEditorVar.TaskAlert.getDue_date_string());
        // 4 - 觸發間隔
        values.put(ColumnAlert.KEY.interval, mEditorVar.TaskAlert.getInterval());
        // 5 - 提醒事件包含的地點id
        values.put(ColumnAlert.KEY.loc_id, mEditorVar.TaskAlert.getLoc_id());
        MyDebug.MakeLog(2, "@set newTaskId=" + mEditorVar.TaskAlert.getLoc_id());

        // 6 - 是否開啟靠近地點提醒
        values.put(ColumnAlert.KEY.loc_on, mEditorVar.TaskAlert.getLoc_on());
        // 7 - 地點靠近提醒半徑
        values.put(ColumnAlert.KEY.loc_radius, mEditorVar.TaskAlert.getLoc_radius());
        // 8 - 備用欄位
        values.put(ColumnAlert.KEY.other, mEditorVar.TaskAlert.getOther());
        // 9 - 對應任務id
        values.put(ColumnAlert.KEY.task_id, mEditorVar.TaskAlert.getTask_id());
        // 10- 時間修正
        values.put(ColumnAlert.KEY.time_offset, mEditorVar.TaskAlert.getTime_offset());
        // 11- 提醒類型
        values.put(ColumnAlert.KEY.type, mEditorVar.TaskAlert.getType());
        // 12- 提醒狀態
        values.put(ColumnAlert.KEY.state, mEditorVar.TaskAlert.getState());
    }
}
