package tw.geodoer.main.taskEditor.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import tw.geodoer.mGeoInfo.view.LocationCustomDialog;
import tw.geodoer.utils.CommonVar;
import tw.geodoer.utils.MyDebug;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.moretion.geodoer.R;

public class TaskEditorMainFragment extends Fragment implements
OnClickListener
{
	private static MultiAutoCompleteTextView taskTitle; 	// 任務標題
	private static EditText taskDueDate;					// 任務到期日
	private static ImageButton taskBtnDueDate;

	private static Spinner tasklocation;		
	private static ImageButton taskBtnLocation;

	private static EditText taskContent;
	private static Spinner taskCategory;					// 類別
	private static Spinner taskPriority;					// 優先
	private static Spinner taskTag;							// tag
	private static Spinner taskProject;					  	// 專案
	private static Button btnMore;							// more按鈕
	private Handler mHandler;

	private static String nullString="null";

	public static TaskEditorMainFragment newInstance() {
		TaskEditorMainFragment fragment = new TaskEditorMainFragment();
		return fragment;
	}

	private static CommonEditorVar mEditorVar=CommonEditorVar.GetInstance();

	private Runnable mShowContentRunnable = new Runnable() {
		@Override
		public void run() {
			setupViewComponent();
			setupStringArray();
			init(getActivity().getIntent());
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.activity_task_editor_tab_main, container,false);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		obtainData();

		if(savedInstanceState==null){

		}
	}

	private void setupStringArray(){
		String[] BasicStringArray =
				getResources().getStringArray(R.array.Array_Task_Editor_Date_Basic_Meaning_String);
		String[] RepeatStringArray =
				getResources().getStringArray(R.array.Array_Task_Editor_Date_Repeat_Meaning_String);
		CommonVar.TASKEDITOR_DUEDATE_BASIC_STRING_ARRAY=BasicStringArray;
		CommonVar.TASKEDITOR_DUEDATE_EXTRA_STRING_ARRAY=RepeatStringArray;
	}

	private void setupViewComponent(){
		// 標題 - 輸入框
		taskTitle =(MultiAutoCompleteTextView)getView().
				findViewById(R.id.multiAutoCompleteTextViewTitle);
		taskTitle.setHint(getResources().getString(R.string.TaskEditor_Field_Title_Hint));
		//taskTitle.setText(mEditorVar.Task.getTitle());

		// 期限 - 輸入框
		taskDueDate =(EditText)getView().findViewById(R.id.editTextDueDate);
		taskDueDate.setHint(getResources().getString(R.string.TaskEditor_Field_DueDate_Hint));
		//taskDueDate.setText(mEditorVar.Task.getDueDate());
		//taskDueDate.setEnabled(false);  // 關閉欄位暫時避免輸入偵測判斷
		//taskDueDate.setClickable(false);// 關閉選取暫時避免輸入偵測判斷
		// 期限 - 選擇按鈕
		taskBtnDueDate=(ImageButton)getView().findViewById(R.id.imageButtonResetDate);
		taskBtnDueDate.setOnClickListener(this);

		// taskContent
		taskContent=(EditText)getView().findViewById(R.id.editTextContent);
		taskContent.setHint(getResources().getString(R.string.TaskEditor_Field_Content_Hint));

		// 地點 
		Cursor c= getActivity().getContentResolver().
				query(ColumnLocation.URI, ColumnLocation.PROJECTION, null, null, 
						ColumnLocation.DEFAULT_SORT_ORDER);
		tasklocation=(Spinner)getView().findViewById(R.id.spinnerTextLocation);
		tasklocation.setAdapter(setLocationArray(c));
		tasklocation.setOnItemSelectedListener(test);
		tasklocation.setEnabled(true);

		taskBtnLocation=(ImageButton)getView().findViewById(R.id.imageButtonSetLocation);
		taskBtnLocation.setOnClickListener(this);
		taskBtnLocation.setEnabled(true);

		// btnMore 
		btnMore=(Button)getView().findViewById(R.id.btnMore);
		btnMore.setOnClickListener(this);
		btnMore.setEnabled(true);

		// 類別選擇框
		taskCategory=(Spinner)getActivity().findViewById(R.id.spinnerCategory);
		taskCategory.setPrompt(getResources().getString(R.string.TaskEditor_Field_Category_Hint));
		taskCategory.setVisibility(View.GONE);

		// 優先度選擇
		taskPriority=(Spinner)getView().findViewById(R.id.spinnerPriority);
		taskPriority.setPrompt(getResources().getString(R.string.TaskEditor_Field_Priority_Hint));
		taskPriority.setVisibility(View.GONE);

		// tag
		taskTag=(Spinner)getView().findViewById(R.id.spinnerTag);
		taskTag.setPrompt(getResources().getString(R.string.TaskEditor_Field_Tag_Hint));
		taskTag.setVisibility(View.GONE);

		// project
		taskProject=(Spinner)getView().findViewById(R.id.spinnerProject);
		taskProject.setPrompt(getResources().getString(R.string.TaskEditor_Field_Project_Hint));
		taskProject.setVisibility(View.GONE);

	}


	private OnItemSelectedListener test=new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String aa[]={ getTaskLocation().getSelectedItem().toString() };

			Cursor c= getActivity().getContentResolver().
					query(ColumnLocation.URI, ColumnLocation.PROJECTION, "name = ?",aa, 
							ColumnLocation.DEFAULT_SORT_ORDER);

			if (c.moveToFirst()) {
				MyDebug.MakeLog(2,"地點id="+c.getInt(0));
				MyDebug.MakeLog(2,"地點名稱="+c.getString(1));
				Toast.makeText(getActivity()
						,"地點id="+c.getInt(0)+"\n地點名稱="+c.getString(1)

						, Toast.LENGTH_SHORT).show();


				c.close();
			}



		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	//------------------------------------- 由資料庫初始化變數
	public static void init(Intent intent) {
		Bundle b = intent.getBundleExtra(CommonVar.BundleName);
		if (b != null) {
			//參照 底部之TaskFieldContents/RemindmeVar.class等處, 確保變數欄位與順序都相同
			mEditorVar.Task.setTaskId(b.getInt(ColumnTask.KEY._id));
			mEditorVar.Task.setTitle(b.getString(ColumnTask.KEY.title));
			mEditorVar.Task.setContent(b.getString(ColumnTask.KEY.content));
			mEditorVar.Task.setCreated(b.getLong(ColumnTask.KEY.created));
			mEditorVar.Task.setDue_date_millis(b.getLong(ColumnTask.KEY.due_date_millis));
			mEditorVar.Task.setDue_date_string(b.getString(ColumnTask.KEY.due_date_string));

			mEditorVar.Task.setCategory_id(b.getInt(ColumnTask.KEY.category_id));
			mEditorVar.Task.setPriority(b.getInt(ColumnTask.KEY.priority));
			mEditorVar.Task.setTag_id(b.getString(ColumnTask.KEY.tag_id));



			mEditorVar.Task.setTaskId(b.getInt(ColumnTask.KEY._id));
			MyDebug.MakeLog(2, "@edit main id="+b.getInt(ColumnTask.KEY._id));
			TaskEditorMainFragment.setTaskTitle(b.getString(ColumnTask.KEY.title));
			TaskEditorMainFragment.setTaskDueDate(b.getString(ColumnTask.KEY.due_date_string));
			if(!b.getString(ColumnTask.KEY.content).equalsIgnoreCase("null")){
				TaskEditorMainFragment.setTaskContent(b.getString(ColumnTask.KEY.content));
			}
		}
	}

	//--------------任務地點選擇器---------------//
	//	@SuppressLint("InflateParams")
	//	private TaskEditorMain ShowTaskLocationSelectMenu() {
	//		LayoutInflater inflater = LayoutInflater.from(getActivity());
	//		View mview = inflater.inflate(
	//				R.layout.activity_task_editor_tab_location,	null);
	//		new AlertDialog.Builder(getActivity())
	//		.setTitle(getResources().getString(R.string.TaskEditor_Field_Location_Tittle))
	//		.setView(mview)
	//
	//		//		.setItems(R.array.Array_TaskEditor_btnTaskDueDate_String,
	//		//				new DialogInterface.OnClickListener() {
	//		//			public void onClick(DialogInterface dialog,
	//		//					int which) {
	//		//				
	//		//			}})
	//
	//		.show();
	//		return this;
	//	}

	//-----------------obtainData------------------//
	private void obtainData() {
		// Show indeterminate progress
		mHandler = new Handler();
		mHandler.postDelayed(mShowContentRunnable, 5);
		//getLoaderManager().initLoader(0, null, this);
	}

	//-----------------TaskTitle------------------//
	public static String getTaskTitle() {
		String TaskTitleString = nullString;
		// 如果欄位不為空則放入使用者輸入數值
		if (!(taskTitle.getText().toString().isEmpty())){
			TaskTitleString= taskTitle.getText().toString().trim();
		}
		//MyDebug.MakeLog(0,"getTaskTitle:"+ TaskTitleString+"" +",TaskTitle.len="+TaskTitleString.length());
		return TaskTitleString;
	}
	public static void setTaskTitle(String taskTitle) {
		TaskEditorMainFragment.taskTitle.setText(taskTitle);
	}

	//-----------------TaskDueDate------------------//
	// 取得文字
	public static String getTaskDueDateString() {
		String taskDueDateString=nullString;
		// 如果欄位不為空則放入使用者輸入數值
		if (!(taskDueDate.getText().toString().isEmpty())){
			taskDueDateString= taskDueDate.getText().toString().trim();
		}
		return taskDueDateString;
	}	
	// 取得長度
	public static int getTaskDueDateStringLength() {
		int taskDueDateStringLength=0;
		// 如果欄位不為空則放入使用者輸入數值
		if (!(taskDueDate.getText().toString().isEmpty())){
			taskDueDateStringLength= taskDueDate.getText().length();
		}
		return taskDueDateStringLength;
	}
	// 設定文字
	public static void setTaskDueDate(String taskDueDateString) {
		TaskEditorMainFragment.taskDueDate.setText(taskDueDateString);
	}

	//-----------------TaskContent------------------//
	public static String getTaskContent() {
		String taskContentString=nullString;
		// 如果欄位不為空則放入使用者輸入數值
		if (!(taskContent.getText().toString().isEmpty())){
			taskContentString= taskContent.getText().toString().trim();
		}
		return taskContentString;
	}	
	public static void setTaskContent(String taskContentString) {
		TaskEditorMainFragment.taskContent.setText(taskContentString);
	}

	//-----------------TaskCategory------------------//
	//TODO 處理spinner對應資料
	public static Spinner getTaskCategory() {
		return taskCategory;
	}
	public static void setTaskCategory(Spinner taskCategory) {
		TaskEditorMainFragment.taskCategory = taskCategory;
	}	

	//-----------------TaskTag------------------//
	//TODO 處理spinner對應資料
	public static Spinner getTaskTag() {
		return taskTag;
	}
	public static void setTaskTag(Spinner taskTag) {
		TaskEditorMainFragment.taskTag = taskTag;
	}

	//-----------------TaskPriority------------------//
	//TODO 處理spinner對應資料
	public static Spinner getTaskPriority() {
		return taskPriority;
	}
	public static void setTaskPriority(Spinner taskPriority) {
		TaskEditorMainFragment.taskPriority = taskPriority;
	}

	//-----------------TaskLocation------------------//
	//TODO 
	public static Spinner getTaskLocation() {
		return tasklocation;
	}
	public static void setTaskLocation(Spinner taskLocation) {
		TaskEditorMainFragment.tasklocation = taskLocation;
	}


	//----------------- onClick ------------------//
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageButtonResetDate:
			new DueDateCustomDialog(getView().getContext()).show();
			break;

		case R.id.imageButtonSetLocation:
			new LocationCustomDialog().show(getFragmentManager() , "dialog");
			break;

		case R.id.btnMore:
			if(taskCategory.getVisibility()==View.GONE){
				taskCategory.setVisibility(View.VISIBLE);
				taskPriority.setVisibility(View.VISIBLE);
				taskProject.setVisibility(View.VISIBLE);
				taskTag.setVisibility(View.VISIBLE);
				btnMore.setText(getResources().getString(R.string.btnMore_Less));
			}else {
				taskCategory.setVisibility(View.GONE);
				taskPriority.setVisibility(View.GONE);
				taskProject.setVisibility(View.GONE);
				taskTag.setVisibility(View.GONE);
				btnMore.setText(getResources().getString(R.string.btnMore_More));
			}
			break;

		default:
			break;
		}
	}


	//-----------------設定任務地點陣列------------------//
	private ArrayAdapter<String> setLocationArray(Cursor data){
		ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(data!=null){
			if (data.getCount()>0){
				data.moveToFirst();
				adapter.add(getResources().getString(R.string.TaskEditor_Field_Location_Spinner_Hint).toString());
				do{
					adapter.add(data.getString(data.getColumnIndex("name")));
				}while (data.moveToNext());
				if(tasklocation!=null) tasklocation.setEnabled(true);
			}else{
				adapter.add(getResources().getString(R.string.TaskEditor_Field_Location_Is_Empty).toString());
				if(tasklocation!=null) tasklocation.setEnabled(false);
			}
		}
		return adapter;
	}

}