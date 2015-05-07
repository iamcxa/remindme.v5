package tw.geodoer.main.taskEditor.fields;

/**
 * @author Kent
 * @version 20140930
 */
public class CommonEditorVar {

    // 顯示日期、時間對話方塊常數
    //public final int DATE_DIALOG_ID = 0;
    //public final int TIME_DIALOG_ID = 1;

    //切割分類
    public  DateFields TaskDate = new DateFields();
    public  LocationFields TaskLocation = new LocationFields();
    public  TaskFields Task = new TaskFields();
    public  AlertFields TaskAlert = new AlertFields();
    public  TaskColorFields TaskCardColor = new TaskColorFields();
    public  GetDBdata getDBdata = new GetDBdata();
    public  static CommonEditorVar EditorVarInstance = new CommonEditorVar();

    public CommonEditorVar() {
    }

    public static CommonEditorVar GetInstance() {
        return EditorVarInstance;
    }

}

