package tw.geodoer.main.taskEditor.fields;

/**
 * Created by Kent on 2014/12/24.
 */ /*
 * 任務基本成員
 */
public class TaskFields {

  //  public TaskFields(){}

    public final int TASK_STATUS_UNFINISHED=0;
    public final int TASK_STATUS_FINISHED=1;

    // 1 - ID
    private static int taskId=0;
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
    // 16 checked
    private int checked=0;

    //---------------Getter/Setter-----------------//

    public int getChecked() {
        return checked;
    }
    public void setChecked(int checked) {
        this.checked = checked;
    }

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
