package me.iamcxa.remindme.editor;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

import me.iamcxa.remindme.R;
import tw.remindme.database.columns.ColumnLocation;
import tw.remindme.database.columns.ColumnTask;
import tw.remindme.common.function.MyDebug;
import tw.remindme.common.view.SlidingTabLayout;
import tw.remindme.taskEditor.adapter.TaskEditorFragmentPagerAdapter;

public class TaskEditorTab extends ActionBarActivity 
implements
OnMenuItemClickListener{

	private static CommonEditorVar mEditorVar=CommonEditorVar.GetInstance();
	protected static Act_SaveToDb mSaveOrUpdate;
	private ReadDB_BeforeSaveDB readDB;

	private static Toolbar toolbar;
	private static SlidingTabLayout slidingTabLayout;
	private static ViewPager viewPager;

	private ArrayList<Fragment> fragments;
	private TaskEditorFragmentPagerAdapter viewPager_Adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor_tab);
		setupViewComponent();
		//init(this.getIntent());
	}


	// This is the action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 抓取editor_activity_actionbar.xml內容
		getMenuInflater().inflate(R.menu.editor_activity_actionbar, menu);

		// 啟用action bar返回首頁箭頭
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.TaskEditor_ActionBar_Title));

		// actionAdd
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		actionAdd.setOnMenuItemClickListener(this);

		return true;
	}

	// actionbar箭頭返回首頁動作
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

	//設定tab
	private void setupViewComponent() {

		// 實例化介面物件
		toolbar = (Toolbar) findViewById(R.id.taskeditor_toolbar);
		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.taskeditor_tab);
		viewPager = (ViewPager) findViewById(R.id.taskeditor_viewpage);

		// 設置 ViewPager
		fragments = new ArrayList<Fragment>();
		fragments.add(new TaskEditorMain());
		fragments.add(new TaskEditorLocation());
		viewPager_Adapter = new TaskEditorFragmentPagerAdapter(
				getSupportFragmentManager()
				,fragments);
		viewPager.setOffscreenPageLimit(fragments.size());
		viewPager.setAdapter(viewPager_Adapter);
		// 設置SlidingTab
		slidingTabLayout.setViewPager(viewPager);
		setSupportActionBar(toolbar);  



		//	final ActionBar actBar = getSupportActionBar();
		//	actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		//final ActionBar toolbar = getSupportActionBar();

		//ReadDB_BeforeSaveDB readDB=new ReadDB_BeforeSaveDB(getApplicationContext());
		//getLoaderManager().initLoader(readDB.LOADER_ID_TASKS, null, readDB.getDBTaskID);
		//getLoaderManager().initLoader(readDB.LOADER_ID_LOCATION, null, readDB.getDBLocID);

		//Fragment fragMarriSug = TaskEditorMain.newInstance();


		//		actBar.addTab(actBar.newTab()
		//				.setText("")
		//				.setIcon(getResources().getDrawable(R.drawable.tear_of_calendar)));
		//.setTabListener(new MyTabListener(fragMarriSug)));
		//.setTabListener(new MyTabListener(fragMarriSug)));

		//		Fragment fragGame =  TaskEditorLocation.newInstance();
		//		actBar.addTab(actBar.newTab()
		//				.setText("")
		//				.setIcon(getResources().getDrawable(R.drawable.map_marker))
		//				.setTabListener(new MyTabListener(fragGame)));
		//
		//		Fragment fragVideo = new TaskEditorMain();
		//		actBar.addTab(actBar.newTab()
		//				.setText("")
		//				.setIcon(getResources().getDrawable(android.R.drawable.ic_media_play))
		//				.setTabListener(new MyTabListener(fragVideo)));

	}

	private void btnActionAdd(){
		//檢查title是否為空
		boolean isEmpty=(TaskEditorMain.getTaskTitle().contentEquals("null"));
		if(!isEmpty){
			try {

				// 取得最

				mSaveOrUpdate=new Act_SaveToDb(
						getApplicationContext()
						,mEditorVar.Task.getTaskId()
						,lastTaskID(),lastLocID());

				finish();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),"ERROR:"+e.toString() , Toast.LENGTH_SHORT).show();
			}
		}else {
			String EmptyMsg=getString(R.string.TaskEditor_Title_Is_Empty);
			Toast.makeText(getApplicationContext(),EmptyMsg , Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub

		String itemName=String.valueOf(item.getTitle());

		if (itemName.contentEquals( "action_add")){

			btnActionAdd();

			lastTaskID();
			lastLocID();

		}else if (itemName.contentEquals("action_refresh")) {
			//btnActionCancel();//暫時取消此功能

		}

		return false;
	}

	// 抓地點id
	private int lastLocID(){
		Cursor c= getApplicationContext().getContentResolver().
				query(ColumnLocation.URI, ColumnLocation.PROJECTION, null, null, 
						ColumnLocation.DEFAULT_SORT_ORDER);
		int data=0;
		if (c != null) {
			if(c.moveToLast()){
				data = c.getInt(0);
				MyDebug.MakeLog(2,"lastLocID="+data);
			}
			c.close();
		}
		return data;
	}

	// 抓任務id
	private int lastTaskID(){	
		Cursor c= getApplicationContext().getContentResolver().
				query(ColumnTask.URI, ColumnTask.PROJECTION, null, null, 
						ColumnTask.DEFAULT_SORT_ORDER);		
		int data = 0;
		if (c != null) {
			if(c.moveToLast()){
				if(!c.isNull(0)) data = c.getInt(0);
				MyDebug.MakeLog(2,"lastTaskID="+data);
			}
			c.close();
		}

		return data;
	}

	// 將讀出資料轉換為  ArrayList 後輸出
	private ArrayList<String> getContents(Cursor data) {

		data.moveToFirst();

		ArrayList<String> contents = new ArrayList<String>();

		while(!data.isAfterLast()) {
			contents.add(data.getString(data.getColumnIndex("_id")));
			//contents.add(data.getString(data.getColumnIndex("title")));
			//contents.add(data.getString(data.getColumnIndex("title")));
			data.moveToLast();


			String content =  contents.toArray().toString();
			MyDebug.MakeLog(2, "content="+contents);

			return contents;
		}

		data.close();

		return null;

	}


}
