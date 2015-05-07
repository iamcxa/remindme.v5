package tw.geodoer.main.taskEditor.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.geodoer.geotodo.R;

import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mGeoInfo.view.LocationCustomDialog;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.utils.CommonVar;
import tw.geodoer.utils.MyDebug;

public class TaskEditorMainFragment extends Fragment implements
        OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    static class ViewHolder{
        MultiAutoCompleteTextView taskTitle;    // 任務標題

        // 任務日期
        EditText taskDueDate;                    // 任務到期日
        ImageButton taskBtnDueDate;

        // 設定地點相關
        Spinner spinnerTaskLocation;
        ImageButton taskBtnSetLocation;
        ImageButton taskBtnUnSetLocation;

        //
        EditText taskContent;

        // 其他
        Spinner taskCategory;                       // 類別
        Spinner taskPriority;                       // 優先
        Spinner taskTag;                            // tag
        Spinner taskProject;                        // 專案
        Button btnMore;                             // more按鈕
    }

    // flag - 被地圖dialog呼叫
    public static boolean calledByDialog = false;
    private static Loader<Cursor> loader;
    private static ViewHolder vH = new ViewHolder();

    //
    private static String nullString = "null";
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();

    @Override
    public void onResume() {
        super.onResume();
    }

    private OnItemSelectedListener test = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String aa[] = {getTaskLocation().getSelectedItem().toString()};

            // 以名稱撈資料
            Cursor c = getActivity().getContentResolver().
                    query(ColumnLocation.URI, ColumnLocation.PROJECTION, "name = ?", aa,
                            ColumnLocation.SORT_BY_LASTUSEDTIME);
            // 如果有資料
            if (c.moveToFirst()) {
                String locName=c.getString(1);
                int locId=c.getInt(0);
                double locLon=c.getDouble(3);
                double locLat=c.getDouble(2);
                long locLST=c.getLong(5);

                // 傳值給共用變數
                mEditorVar.Task.setLocation_id(locId);
                mEditorVar.TaskLocation.setName(locName);
                mEditorVar.TaskLocation.setLon(locLon);
                mEditorVar.TaskLocation.setLat(locLat);
                mEditorVar.TaskLocation.setLastUsedTime(locLST);

                // 輸出logs
                MyDebug.MakeLog(2, "地點id=" + locId);
                MyDebug.MakeLog(2, "地點名稱=" + locName);
                Toast.makeText(getActivity(),
                        "spinner ID="+ id +
                                "\nspinner 位置="+position+
                                "\n地點id=" + locId +
                                "\n地點名稱=" + locName
                        , Toast.LENGTH_SHORT).show();
                // 關閉
                c.close();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    };

    public static TaskEditorMainFragment newInstance() {
        return new TaskEditorMainFragment();
    }

    //------------------------------------- 由資料庫初始化變數
    public void init(Intent intent) {
        Bundle b = intent.getBundleExtra(CommonVar.BundleName);
        if (b != null) {
            //參照 底部之TaskFieldContents/RemindmeVar.class等處, 確保變數欄位與順序都相同
            mEditorVar.Task.setTaskId(b.getInt(ColumnTask.KEY._id));
            mEditorVar.Task.setTitle(b.getString(ColumnTask.KEY.title));
            mEditorVar.Task.setContent(b.getString(ColumnTask.KEY.content));
            mEditorVar.Task.setCreated(b.getLong(ColumnTask.KEY.created));
            //------------------------------------
            //fixed
            mEditorVar.Task.setDue_date_millis(  Long.parseLong(b.getString(ColumnTask.KEY.due_date_millis ))  );
            //------------------------------------
            mEditorVar.Task.setDue_date_string(b.getString(ColumnTask.KEY.due_date_string));

            mEditorVar.Task.setCategory_id(b.getInt(ColumnTask.KEY.category_id));
            mEditorVar.Task.setPriority(b.getInt(ColumnTask.KEY.priority));
            mEditorVar.Task.setTag_id(b.getString(ColumnTask.KEY.tag_id));
            //mEditorVar.Task.setTaskId(b.getInt(ColumnTask.KEY._id));
            MyDebug.MakeLog(2, "@edit main id=" + b.getInt(ColumnTask.KEY._id));
            TaskEditorMainFragment.setTaskTitle(b.getString(ColumnTask.KEY.title));
            TaskEditorMainFragment.setTaskDueDate(b.getString(ColumnTask.KEY.due_date_string));
            if (!b.getString(ColumnTask.KEY.content).equalsIgnoreCase("null")) {
                TaskEditorMainFragment.setTaskContent(b.getString(ColumnTask.KEY.content));
            }

        }
        //--------------------------------------------------
        //added by Murakumo
        else
        {
            mEditorVar.Task.setDue_date_millis(0);
            mEditorVar.Task.setTaskId(0);
            mEditorVar.Task.setLocation_id(0);
        }
        //--------------------------------------------------
    }

    //-----------------TaskTitle------------------//
    public static String getTaskTitle() {
        String TaskTitleString = nullString;
        // 如果欄位不為空則放入使用者輸入數值
        if (!(vH.taskTitle.getText().toString().isEmpty())) {
            TaskTitleString = vH.taskTitle.getText().toString().trim();
        }
        //MyDebug.MakeLog(0,"getTaskTitle:"+ TaskTitleString+"" +",TaskTitle.len="+TaskTitleString.length());
        return TaskTitleString;
    }

    public static void setTaskTitle(String taskTitle) {
        vH.taskTitle.setText(taskTitle);
    }

    //-----------------TaskDueDate------------------//
    // 取得文字
    public static String getTaskDueDateString() {
        String taskDueDateString = nullString;
        // 如果欄位不為空則放入使用者輸入數值
        if (!(vH.taskDueDate.getText().toString().isEmpty())) {
            taskDueDateString = vH.taskDueDate.getText().toString().trim();
        }
        return taskDueDateString;
    }

    // 取得長度
    public static int getTaskDueDateStringLength() {
        int taskDueDateStringLength = 0;
        // 如果欄位不為空則放入使用者輸入數值
        if (!(vH.taskDueDate.getText().toString().isEmpty())) {
            taskDueDateStringLength = vH.taskDueDate.getText().length();
        }
        return taskDueDateStringLength;
    }

    // 設定文字
    public static void setTaskDueDate(String taskDueDateString) {
        vH.taskDueDate.setText(taskDueDateString);
    }

    //-----------------TaskContent------------------//
    public static String getTaskContent() {
        String taskContentString = nullString;
        // 如果欄位不為空則放入使用者輸入數值
        if (!(vH.taskContent.getText().toString().isEmpty())) {
            taskContentString = vH.taskContent.getText().toString().trim();
        }
        return taskContentString;
    }

    public static void setTaskContent(String taskContentString) {
        vH.taskContent.setText(taskContentString);
    }

    //-----------------TaskCategory------------------//
    //TODO 處理spinner對應資料
    public static Spinner getTaskCategory() {
        return vH.taskCategory;
    }

    public static void setTaskCategory(Spinner taskCategory) {
        vH.taskCategory = taskCategory;
    }

    //-----------------TaskTag------------------//
    //TODO 處理spinner對應資料
    public static Spinner getTaskTag() {
        return vH.taskTag;
    }

    public static void setTaskTag(Spinner taskTag) {
        vH.taskTag = taskTag;
    }

    //-----------------TaskPriority------------------//
    //TODO 處理spinner對應資料
    public static Spinner getTaskPriority() {
        return vH.taskPriority;
    }

    public static void setTaskPriority(Spinner taskPriority) {
        vH.taskPriority = taskPriority;
    }

    //-----------------TaskLocation------------------//
    //TODO
    public static Spinner getTaskLocation() {
        return vH.spinnerTaskLocation;
    }

    public static void setTaskLocation(Spinner taskLocation) {
        vH.spinnerTaskLocation = taskLocation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_task_editor_tab_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        obtainData();
        if (savedInstanceState == null) {

        }
    }

    private void setupStringArray() {
        String[] BasicStringArray =
                getResources().getStringArray(R.array.Array_Task_Editor_Date_Basic_Meaning_String);
        String[] RepeatStringArray =
                getResources().getStringArray(R.array.Array_Task_Editor_Date_Repeat_Meaning_String);
        CommonVar.TASKEDITOR_DUEDATE_BASIC_STRING_ARRAY = BasicStringArray;
        CommonVar.TASKEDITOR_DUEDATE_EXTRA_STRING_ARRAY = RepeatStringArray;
    }

    private void setupViewComponent() {
        // 標題 - 輸入框
        vH.taskTitle = (MultiAutoCompleteTextView) getActivity().
                findViewById(R.id.multiAutoCompleteTextViewTitle);
        vH.taskTitle.setHint(getResources().getString(R.string.TaskEditor_Field_Title_Hint));
        //taskTitle.setText(mEditorVar.Task.getTitle());

        // 期限 - 輸入框
        vH.taskDueDate = (EditText) getActivity().findViewById(R.id.editTextDueDate);
        vH.taskDueDate.setHint(getResources().getString(R.string.TaskEditor_Field_DueDate_Hint));
        //taskDueDate.setText(mEditorVar.Task.getDueDate());
        //taskDueDate.setEnabled(false);  // 關閉欄位暫時避免輸入偵測判斷
        //taskDueDate.setClickable(false);// 關閉選取暫時避免輸入偵測判斷

        // 期限 - 選擇按鈕
        vH.taskBtnDueDate = (ImageButton) getActivity().findViewById(R.id.imageButtonResetDate);
        vH.taskBtnDueDate.setOnClickListener(this);

        // taskContent
        vH.taskContent = (EditText) getActivity().findViewById(R.id.editTextContent);
        vH.taskContent.setHint(getResources().getString(R.string.TaskEditor_Field_Content_Hint));

        // 地點選擇
        Cursor c = getActivity().getContentResolver().
                query(ColumnLocation.URI, ColumnLocation.PROJECTION, null, null,
                        ColumnLocation.DEFAULT_SORT_ORDER);

        //
        vH.spinnerTaskLocation = (Spinner) getActivity().findViewById(R.id.spinnerTextLocation);
        vH.spinnerTaskLocation.setAdapter(setLocationArray(c));
        vH.spinnerTaskLocation.setOnItemSelectedListener(test);
        vH.spinnerTaskLocation.setEnabled(true);

        // 設定地點按鈕
        vH.taskBtnSetLocation = (ImageButton) getActivity().findViewById(R.id.imageButtonSetLocation);
        vH.taskBtnSetLocation.setOnClickListener(this);
        vH.taskBtnSetLocation.setEnabled(true);

        // 取消設定地點
        vH.taskBtnUnSetLocation=(ImageButton) getActivity().findViewById(R.id.imageButtonUnSetLocation);
        vH.taskBtnUnSetLocation.setOnClickListener(this);
        vH.taskBtnUnSetLocation.setEnabled(true);

        // btnMore
        vH.btnMore = (Button) getActivity().findViewById(R.id.btnMore);
        vH.btnMore.setOnClickListener(this);
        vH.btnMore.setEnabled(true);

        // 類別選擇框
        vH.taskCategory = (Spinner) getActivity().findViewById(R.id.spinnerCategory);
        vH.taskCategory.setPrompt(getResources().getString(R.string.TaskEditor_Field_Category_Hint));
        vH.taskCategory.setVisibility(View.GONE);

        // 優先度選擇
        vH.taskPriority = (Spinner) getActivity().findViewById(R.id.spinnerPriority);
        vH.taskPriority.setPrompt(getResources().getString(R.string.TaskEditor_Field_Priority_Hint));
        vH.taskPriority.setVisibility(View.GONE);

        // tag
        vH.taskTag = (Spinner) getActivity().findViewById(R.id.spinnerTag);
        vH.taskTag.setPrompt(getResources().getString(R.string.TaskEditor_Field_Tag_Hint));
        vH.taskTag.setVisibility(View.GONE);

        // project
        vH.taskProject = (Spinner) getActivity().findViewById(R.id.spinnerProject);
        vH.taskProject.setPrompt(getResources().getString(R.string.TaskEditor_Field_Project_Hint));
        vH.taskProject.setVisibility(View.GONE);
    }

    //-----------------obtainData------------------//
    private void obtainData() {
        // Show indeterminate progress
        //mHandler = new Handler();
        //mHandler.postDelayed(mShowContentRunnable, 5);

        getLoaderManager().initLoader(1, null, this);

        setupViewComponent();
        setupStringArray();
        init(getActivity().getIntent());
    }

    //----------------- onClick ------------------//
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imageButtonResetDate:
                new DueDateCustomDialog(this.getActivity()).show();
                break;

            case R.id.imageButtonSetLocation:
                new LocationCustomDialog().show(getFragmentManager(), "dialog");
                break;

            case R.id.imageButtonUnSetLocation:
                vH.spinnerTaskLocation.setSelection(0);
                mEditorVar.TaskLocation.locationId=0;
                mEditorVar.Task.setLocation_id(0);
                break;

            case R.id.btnMore:
                if (vH.taskCategory.getVisibility() == View.GONE) {
                    vH.taskCategory.setVisibility(View.VISIBLE);
                    vH.taskPriority.setVisibility(View.VISIBLE);
                    vH.taskProject.setVisibility(View.VISIBLE);
                    vH.taskTag.setVisibility(View.VISIBLE);
                    vH.btnMore.setText(getResources().getString(R.string.btnMore_Less));
                } else {
                    vH.taskCategory.setVisibility(View.GONE);
                    vH.taskPriority.setVisibility(View.GONE);
                    vH.taskProject.setVisibility(View.GONE);
                    vH.taskTag.setVisibility(View.GONE);
                    vH.btnMore.setText(getResources().getString(R.string.btnMore_More));
                }
                break;

            default:
                break;
        }
    }

    //-----------------設定任務地點陣列------------------//
    private ArrayAdapter<String> setLocationArray(Cursor data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (data != null) {
            if (data.getCount() > 0) {
                data.moveToFirst();
                adapter.add(getResources().getString(R.string.TaskEditor_Field_Location_Spinner_Hint));
                do {
                    adapter.add(data.getString(data.getColumnIndex("name")));
                } while (data.moveToNext());
                if (vH.spinnerTaskLocation != null) vH.spinnerTaskLocation.setEnabled(true);
            } else {
                adapter.add(getResources().getString(R.string.TaskEditor_Field_Location_Is_Empty));
                if (vH.spinnerTaskLocation != null) vH.spinnerTaskLocation.setEnabled(false);
            }
        }
        return adapter;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 1:
                loader= new CursorLoader(getActivity(),
                        ColumnLocation.URI,
                        ColumnLocation.PROJECTION, null, null,
                        ColumnLocation.DEFAULT_SORT_ORDER);
                break;
        }
        return loader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        vH. spinnerTaskLocation.setAdapter(setLocationArray(data));
        vH. spinnerTaskLocation.setOnItemSelectedListener(test);

        if (calledByDialog) {
            vH.spinnerTaskLocation.setSelection(vH.spinnerTaskLocation.getCount() - 1);
            calledByDialog = false;
        }

        if (mEditorVar.Task.getTaskId() != 0) {
            Bundle b = getActivity().getIntent().getBundleExtra(CommonVar.BundleName);
            if (b != null) {
                MyDebug.MakeLog(2, "location_id=" + b.getString(ColumnTask.KEY.location_id));
                mEditorVar.Task.setLocation_id(Integer.valueOf(b.getString(ColumnTask.KEY.location_id)));
                MyDebug.MakeLog(2, "location_id@mEditorVar=" + mEditorVar.Task.getLocation_id());
                MyDebug.MakeLog(2, "spinnerTaskLocation count=" + vH.spinnerTaskLocation.getCount());
                vH. spinnerTaskLocation.setSelection(mEditorVar.Task.getLocation_id());
                MyDebug.MakeLog(2, "spinnerTaskLocation selected=" + vH.spinnerTaskLocation.getSelectedItemPosition());
            }
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}