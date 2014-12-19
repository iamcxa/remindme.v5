package me.iamcxa.remindme.editor;

import tw.remindme.database.contentProvider.TaskDbProvider;

/**
 * @author Kent
 * @version 20140930
 */
public class CommonEditorVar {

	// 顯示日期、時間對話方塊常數
	//public final int DATE_DIALOG_ID = 0;
	//public final int TIME_DIALOG_ID = 1;


	//切割分類
	public DateFields TaskDate = new DateFields();
	public LocationFields TaskLocation = new LocationFields();
	public TaskFields Task = new TaskFields();
	public AlertFields TaskAlert= new AlertFields();
	public TaskColorFields TaskCardColor= new TaskColorFields();
	public GetDBdata getDBdata=new GetDBdata();

	private CommonEditorVar(){}

	public static CommonEditorVar EditorVarInstance = new CommonEditorVar();

	public static CommonEditorVar GetInstance(){
		return EditorVarInstance;
	}

}

/*
 * 任務基本成員
 */
class TaskFields {
	public final int TASK_STATUS_UNFINISHED=0;
	public final int TASK_STATUS_FINISHED=1;
	
	// 1 - ID
	private int taskId=0;
	// 2 - 標題 
	private String title ="null";
	// 3 - 狀態  - 0未完成 - 1完成
	private int status=0;
	// 4 - 備註
	private String content ="null";
	// 5 - 到期日 - 毫秒
	private long due_date_millis=0;
	// 6 - 到期日  - 字串
	private String due_date_string="null";
	// 7 - 任務卡片顏色代號
	private int color=0;
	// 8 - 優先權
	private int priority=0;
	// 9 - 此任務新增時間 
	private long created=0;
	// 10- 分類id 
	private int category_id=0;
	// 11- 專案id
	private int project_id=0;
	// 12- 協作者uid 
	private String collaborator_id="null";
	// 13- 同步任務id
	private int sync_id=0;
	// 14- 任務地點id
	private int location_id=0;
	// 15- 標籤id - 分割字串
	private String tag_id="null";
	
	//---------------Getter/Setter-----------------//
	
	/**
	 * @return the taskId
	 */
	public int getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the due_date_millis
	 */
	public long getDue_date_millis() {
		return due_date_millis;
	}
	/**
	 * @param due_date_millis the due_date_millis to set
	 */
	public void setDue_date_millis(long due_date_millis) {
		this.due_date_millis = due_date_millis;
	}
	/**
	 * @return the due_date_string
	 */
	public String getDue_date_string() {
		return due_date_string;
	}
	/**
	 * @param due_date_string the due_date_string to set
	 */
	public void setDue_date_string(String due_date_string) {
		this.due_date_string = due_date_string;
	}
	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the created
	 */
	public long getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(long created) {
		this.created = created;
	}
	/**
	 * @return the category_id
	 */
	public int getCategory_id() {
		return category_id;
	}
	/**
	 * @param category_id the category_id to set
	 */
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	/**
	 * @return the project_id
	 */
	public int getProject_id() {
		return project_id;
	}
	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	/**
	 * @return the collaborator_id
	 */
	public String getCollaborator_id() {
		return collaborator_id;
	}
	/**
	 * @param collaborator_id the collaborator_id to set
	 */
	public void setCollaborator_id(String collaborator_id) {
		this.collaborator_id = collaborator_id;
	}
	/**
	 * @return the sync_id
	 */
	public int getSync_id() {
		return sync_id;
	}
	/**
	 * @param sync_id the sync_id to set
	 */
	public void setSync_id(int sync_id) {
		this.sync_id = sync_id;
	}
	/**
	 * @return the location_id
	 */
	public int getLocation_id() {
		return location_id;
	}
	/**
	 * @param location_id the location_id to set
	 */
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	/**
	 * @return the tag_id
	 */
	public String getTag_id() {
		return tag_id;
	}
	/**
	 * @param tag_id the tag_id to set
	 */
	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
}

/*
 * 任務地點成員
 */
class LocationFields {
	// 1 - 任務地點id
	private int locationId=0;
	// 2 - 地點名稱字串
	private String name ="null";
	// 3 - 4 - 經緯度
	private Double lat=0.0;
	private Double lon=0.0;
	// 5 - 與上次偵測地點之距離
	private Double distance=0.0;
	// 6 - 上次使用時間
	private long lastUsedTime=0;
	
	// 額外欄位  - 資料庫目前沒有存入
	// 是否有搜尋過地點
	private Boolean isSearched = false;
	private Boolean isDropped = false;
	// gps使用時間
	private int gpsUseTime = 0;
	
	//---------------Getter/Setter-----------------//

	/**
	 * @return the LocationId
	 */
	public int getLocationId() {
		return locationId;
	}
	/**
	 * @param LocationId the LocationId to set
	 */
	public void setLocationId(int _id) {
		this.locationId = _id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}
	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	/**
	 * @return the lastUsedTime
	 */
	public long getLastUsedTime() {
		return lastUsedTime;
	}
	/**
	 * @param lastUsedTime the lastUsedTime to set
	 */
	public void setLastUsedTime(long lastUsedTime) {
		this.lastUsedTime = lastUsedTime;
	}
	/**
	 * @return the isSearched
	 */
	public Boolean getIsSearched() {
		return isSearched;
	}
	/**
	 * @param isSearched the isSearched to set
	 */
	public void setIsSearched(Boolean isSearched) {
		this.isSearched = isSearched;
	}
	/**
	 * @return the isDropped
	 */
	public Boolean getIsDropped() {
		return isDropped;
	}
	/**
	 * @param isDropped the isDropped to set
	 */
	public void setIsDropped(Boolean isDropped) {
		this.isDropped = isDropped;
	}
	/**
	 * @return the gpsUseTime
	 */
	public int getGpsUseTime() {
		return gpsUseTime;
	}
	/**
	 * @param gpsUseTime the gpsUseTime to set
	 */
	public void setGpsUseTime(int gpsUseTime) {
		this.gpsUseTime = gpsUseTime;
	}
}

/*
 * 任務提醒成員
 */
class AlertFields {
	// 1 - 提醒id
	private int alertID=0;
	// 2 - 到期日 - 毫秒
	private long due_date_millis=0;
	// 3 - 到期日 - 字串
	private String due_date_string="null";
	// 4 - 觸發間隔
	private long interval=0;
	// 5 - 提醒事件包含的地點id
	private int loc_id=-1;
	// 6 - 是否開啟靠近地點提醒
	private int loc_on=0;
	// 7 - 地點靠近提醒半徑
	private int loc_radius=0;
	// 8 - 備用欄位
	private String other="null";
	// 9 - 對應任務id
	private int task_id=0;
	// 10- 時間修正
	private int time_offset=0;
	// 11- 提醒類型
	private int type=0;
	
	//---------------Getter/Setter-----------------//
	/**
	 * @return the alertID
	 */
	public int getAlertID() {
		return alertID;
	}
	/**
	 * @param alertID the alertID to set
	 */
	public void setAlertID(int alertID) {
		this.alertID = alertID;
	}
	/**
	 * @return the due_date_millis
	 */
	public long getDue_date_millis() {
		return due_date_millis;
	}
	/**
	 * @param due_date_millis the due_date_millis to set
	 */
	public void setDue_date_millis(long due_date_millis) {
		this.due_date_millis = due_date_millis;
	}
	/**
	 * @return the due_date_string
	 */
	public String getDue_date_string() {
		return due_date_string;
	}
	/**
	 * @param due_date_string the due_date_string to set
	 */
	public void setDue_date_string(String due_date_string) {
		this.due_date_string = due_date_string;
	}
	/**
	 * @return the interval
	 */
	public long getInterval() {
		return interval;
	}
	/**
	 * @param interval the interval to set
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}
	/**
	 * @return the loc_id
	 */
	public int getLoc_id() {
		return loc_id;
	}
	/**
	 * @param loc_id the loc_id to set
	 */
	public void setLoc_id(int loc_id) {
		this.loc_id = loc_id;
	}
	/**
	 * @return the loc_on
	 */
	public int getLoc_on() {
		return loc_on;
	}
	/**
	 * @param loc_on the loc_on to set
	 */
	public void setLoc_on(int loc_on) {
		this.loc_on = loc_on;
	}
	/**
	 * @return the loc_radius
	 */
	public int getLoc_radius() {
		return loc_radius;
	}
	/**
	 * @param loc_radius the loc_radius to set
	 */
	public void setLoc_radius(int loc_radius) {
		this.loc_radius = loc_radius;
	}
	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}
	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}
	/**
	 * @return the task_id
	 */
	public int getTask_id() {
		return task_id;
	}
	/**
	 * @param task_id the task_id to set
	 */
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	/**
	 * @return the time_offset
	 */
	public int getTime_offset() {
		return time_offset;
	}
	/**
	 * @param time_offset the time_offset to set
	 */
	public void setTime_offset(int time_offset) {
		this.time_offset = time_offset;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}

//日期成員
class DateFields {

	// 日期
	protected int mYear=0;
	protected int mMonth=0;
	protected int mDay=0;
	// 時間
	protected int mHour=0;
	protected int mMinute=0;
	//private int target;
	// 毫秒
	protected long mOnlyDateMillis=0;
	protected long mDatePulsTimeMillis=0;

	//---------------Getter/Setter-----------------//
	
	public int getmYear() {
		return mYear;
	}
	public void setmYear(int mYear) {
		this.mYear = mYear;
	}
	public int getmMonth() {
		return mMonth;
	}
	public void setmMonth(int mMonth) {
		this.mMonth = mMonth;
	}
	public int getmDay() {
		return mDay;
	}
	public void setmDay(int mDay) {
		this.mDay = mDay;
	}
	public int getmHour() {
		return mHour;
	}
	public void setmHour(int mHour) {
		this.mHour = mHour;
	}
	public int getmMinute() {
		return mMinute;
	}
	public void setmMinute(int mMinute) {
		this.mMinute = mMinute;
	}
	public long getmOnlyDateMillis() {
		return mOnlyDateMillis;
	}
	public void setmOnlyDateMillis(long mOnlyDateMillis) {
		this.mOnlyDateMillis = mOnlyDateMillis;
	}
	public long getmDatePulsTimeMillis() {
		return mDatePulsTimeMillis;
	}
	public void setmDatePulsTimeMillis(long mDatePulsTimeMillis) {
		this.mDatePulsTimeMillis = mDatePulsTimeMillis;
	}

}

class TaskColorFields{
	private int taskDefaultColor=0;
	
	//---------------Getter/Setter-----------------//

	/**
	 * @return the taskDefault
	 */
	public int getTaskDefaultColor() {
		return taskDefaultColor;
	}
	/**
	 * @param taskDefault the taskDefault to set
	 */
	public void setTaskDefaultColor(int taskDefaultColor) {
		this.taskDefaultColor = taskDefaultColor;
	}
	
	
}

class GetDBdata{
	
	private TaskDbProvider taskDbProvider=new TaskDbProvider();
	
	
	

	public TaskDbProvider getTaskDbProvider() {
		return taskDbProvider;
	}

}