/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package me.iamcxa.remindme.cardfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shamanland.fab.ShowHideOnScroll;

import it.gmariotti.cardslib.library.view.CardListView;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.database.columns.ColumnAlert;
import me.iamcxa.remindme.database.columns.ColumnLocation;
import me.iamcxa.remindme.database.columns.ColumnTask;
import me.iamcxa.remindme.editor.TaskEditorTab;
import tw.remindme.common.function.MyCalendar;
import tw.remindme.common.function.MyDebug;

/**
 * List with Cursor Example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ListCursorCardFragment extends BaseFragment implements
LoaderManager.LoaderCallbacks<Cursor> {
	// getArguments().getInt(FILTER_STRING)
	public static final String FILTER_STRING="FILTER_STRING";
	private static int position;
	private Handler mHandler ;
	private static MyCursorCardAdapter mAdapter;
	private static CardListView mListView;
	private static String[] projectionTask = ColumnTask.PROJECTION;
	private static String[] projectionAlert = ColumnAlert.PROJECTION;
	private static String[] projectionLoc = ColumnLocation.PROJECTION;
	private static String taskSelection = null;
	private static String taskSortOrder = ColumnTask.DEFAULT_SORT_ORDER;
	//
	private static String alertSelection = null;
	private static String alertSortOrder = ColumnAlert.DEFAULT_SORT_ORDER;
	//
	private static String LocSelection = null;
	private static String LocSortOrder = ColumnLocation.DEFAULT_SORT_ORDER;
	private static String[] selectionArgs;
	private static String todayString=MyCalendar.getTodayString(0);

	public static ListCursorCardFragment newInstance() {
		ListCursorCardFragment fragment = new ListCursorCardFragment();
		return fragment;
	}


	/********************/
	/** Initialization **/
	/********************/
	@SuppressLint("ClickableViewAccessibility")
	private void init() {

		//int filter = getArguments().getInt(FILTER_STRING);
		int filter =position;

		MyDebug.MakeLog(0, "init被執行");

		switch (filter) {
		case 0:// 任務盒
			setTaskSelection("due_date_string = 'null'");
			break;
		case 1:// 今天
			Toast.makeText(getActivity(), 
					"Today="+todayString, Toast.LENGTH_SHORT).show();
			setTaskSelection("due_date_string = '"+todayString+"'");
			break;
		case 2://未來七天
			setTaskSelection("due_date_string IS NOT 'null'");
			break;
		case 3://專案

			break;
		case 4://距離檢視
			//setProjection(projection)
			setTaskSelection("distance IS NOT '0'");
			break;
		case 5://地圖檢視

			break;
		case 6://標籤
			setTaskSelection("TAG IS NOT 'null'");
			break;
		default:
			break;
		}



		mAdapter = MyCursorCardAdapter.newInstance(getActivity());
		mListView = (CardListView) getActivity().findViewById(
				R.id.carddemo_list_cursor);

		if (mListView != null) {
			mListView.setAdapter(mAdapter);

			final View fab_add = getActivity().findViewById(R.id.fab_add);
			fab_add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					
					Intent intent = new Intent();
					intent.setClass(getActivity(), TaskEditorTab.class);
					startActivity(intent);
					//fab_add.setBackgroundColor(getResources().getColor(R.color.demo_card_background_color2));

					Toast.makeText(v.getContext(), R.string.TaskEditor_Field_Inbox, Toast.LENGTH_SHORT).show();
				}
			});
			
			

			mListView.setOnTouchListener(new ShowHideOnScroll(fab_add));

		}



		// Force start background query to load sessions
		getLoaderManager();
		getLoaderManager().restartLoader(101, null, this);
		//getLoaderManager().restartLoader(201, null, this);
		//getLoaderManager().restartLoader(301, null, this);
		LoaderManager.enableDebugLogging(true);
	}



	@Override
	public int getTitleResourceId() {
		return  R.string.app_name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceStat) {
		View root = inflater.inflate(R.layout.card_fragment_list_cursor,
				container, false);

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(101, savedInstanceState, this);
		//getLoaderManager().initLoader(201, null, this);
		//getLoaderManager().initLoader(301, null, this);
		init();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		Loader<Cursor> loader = null;

		switch (id) {
		case 101:
			loader = new CursorLoader(getActivity(), ColumnTask.URI,
					projectionTask, taskSelection, selectionArgs, taskSortOrder);
			break;
		case 201:
			loader = new CursorLoader(getActivity(), ColumnAlert.URI,
					projectionAlert, alertSelection, selectionArgs, alertSortOrder);
			break;
		case 301:
			loader = new CursorLoader(getActivity(), ColumnLocation.URI,
					projectionLoc, LocSelection, selectionArgs, LocSortOrder);
			break;

		default:
			break;
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (getActivity() == null) {
			return;
		}

		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	private Runnable newInit = new Runnable() {

		@Override
		public void run() {
			//setContentShown(true);

			init();

		};
	};
	//-------------------------------------------------//
	public static MyCursorCardAdapter getmAdapter() {
		return mAdapter;
	}

	public static void setmAdapter(MyCursorCardAdapter mAdapter) {
		ListCursorCardFragment.mAdapter = mAdapter;
	}

	public static String getTaskSelection() {
		return taskSelection;
	}

	public static void setTaskSelection(String taskSelection) {
		ListCursorCardFragment.taskSelection = taskSelection;
	}

	public static String getAlertSelection() {
		return alertSelection;
	}

	public static void setAlertSelection(String alertSelection) {
		ListCursorCardFragment.alertSelection = alertSelection;
	}

	public static String getLocSelection() {
		return LocSelection;
	}

	public static void setLocSelection(String locSelection) {
		LocSelection = locSelection;
	}


	/**
	 * @return the position
	 */
	public static int getPosition() {
		return position;
	}


	/**
	 * @param position the position to set
	 */
	public static void setPosition(int position) {
		ListCursorCardFragment.position = position;
	}


}

