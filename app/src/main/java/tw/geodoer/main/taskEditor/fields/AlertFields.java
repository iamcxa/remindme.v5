package tw.geodoer.main.taskEditor.fields;

/**
 * Created by Kent on 2014/12/24.
 */ /*
 * 任務提醒成員
 */
public class AlertFields {

    // 1 - 提醒id
    private int alertID = 0;
    // 2 - 到期日 - 毫秒
    private long due_date_millis = 0;
    // 3 - 到期日 - 字串
    private String due_date_string = "null";
    // 4 - 觸發間隔
    private long interval = 0;
    // 5 - 提醒事件包含的地點id
    private int loc_id = -1;
    // 6 - 是否開啟靠近地點提醒
    private int loc_on = 0;
    // 7 - 地點靠近提醒半徑
    private int loc_radius = 0;
    // 8 - 備用欄位
    private String other = "null";
    // 9 - 對應任務id
    private int task_id = 0;
    // 10- 時間修正
    private int time_offset = 0;
    // 11- 提醒類型
    // 0->無提醒 1->到期提醒 2->提醒 3->到期+靠近提醒
    private int type = 0;
    // 12- 提醒狀態
    // 0->未完成 1->已完成
    private int state = 0;

    public AlertFields() {
    }

    //---------------Getter/Setter-----------------//

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

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
