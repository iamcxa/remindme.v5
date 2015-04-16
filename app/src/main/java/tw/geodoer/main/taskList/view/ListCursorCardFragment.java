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

package tw.geodoer.main.taskList.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geodoer.geotodo.R;
import com.shamanland.fab.ShowHideOnScroll;

import it.gmariotti.cardslib.library.view.CardListView;
import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mPriority.controller.PriorityUpdater;
import tw.geodoer.main.taskEditor.view.TaskEditorTabFragment;
import tw.geodoer.main.taskList.adapter.MyCursorCardAdapter;
import tw.geodoer.utils.MyCalendar;
import tw.geodoer.utils.MyDebug;

/**
 * List with Cursor Example
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ListCursorCardFragment extends MyBaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static TextView txtIfTaskListIsEmpty;

    // getArguments().getInt(FILTER_STRING)
    public static final String FILTER_STRING = "FILTER_STRING";
    private static int position;
    private static MyCursorCardAdapter mAdapter;
    private static CardListView mListView;
    private static String[] projectionTask = ColumnTask.PROJECTION;
    private static String[] projectionAlert = ColumnAlert.PROJECTION;
    private static String[] projectionLoc = ColumnLocation.PROJECTION;
    private static String taskSelection = null;
    //    private static String taskSortOrder = ColumnTask.DEFAULT_SORT_ORDER;
    private static String taskSortOrder ="priority DESC";
    //
    private static String alertSelection = null;
    private static String alertSortOrder = ColumnAlert.DEFAULT_SORT_ORDER;
    //
    private static String LocSelection = null;
    private static String LocSortOrder = ColumnLocation.DEFAULT_SORT_ORDER;
    private static String[] selectionArgs;
    private static String todayString = MyCalendar.getTodayString(0);
    private Handler mHandler;
    private Runnable newInit = new Runnable() {

        @Override
        public void run() {
            //setContentShown(true);

            init();

        }

        ;
    };
    private Loader<Cursor> loader;
    private Cursor data;


    /********************/
    /** Initialization **/

    public static ListCursorCardFragment newInstance() {
        ListCursorCardFragment fragment = new ListCursorCardFragment();
        return fragment;
    }

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

    /**
     * ****************
     */
    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        //int filter = getArguments().getInt(FILTER_STRING);
        int filter = position;

        MyDebug.MakeLog(0, "ListCursorCardFragment-init");
        switch (filter) {
            case 0:// 智慧待辦清單
                setTaskSelection(ColumnTask.KEY.status+" == 0");
                taskSortOrder = ColumnTask.KEY.priority+" DESC";
                break;
            case 1:// 按照時間
                setTaskSelection(ColumnTask.KEY.location_id+" == 0" +
                        " AND " + ColumnTask.KEY.status + " == 0");
                taskSortOrder = ColumnTask.KEY.priority+" DESC";
                break;
            case 2:// 按照地點
                //MyPreferences.mPreferences = PreferenceManager
                //        .getDefaultSharedPreferences(getActivity());
                //int val_radiated_distance = MyPreferences.getValueOfRadiatedDistance();
                setTaskSelection(ColumnTask.KEY.location_id+" != 0" +
                        " AND " + ColumnTask.KEY.status + " == 0");
                taskSortOrder = ColumnTask.KEY.priority+" DESC";
                break;
            case 3:// 地圖檢視
                // 切換fragment
                break;
            case 4:// 已完成
                //setProjection(projection)
                setTaskSelection(ColumnTask.KEY.status+" == 1");
                taskSortOrder = ColumnTask.KEY.checked+" DESC";
                break;
            case 5:// 垃圾桶
                setTaskSelection(ColumnTask.KEY.status+" == 2");
                taskSortOrder = ColumnTask.KEY.due_date_millis+" DESC";
                break;
            case 6:// 測試功能
                // 切換fragment
                break;
            default:
                break;
        }

        //
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
                    intent.setClass(getActivity(), TaskEditorTabFragment.class);
                    startActivity(intent);
                    //fab_add.setBackgroundColor(getResources().getColor(R.color.demo_card_background_color2));

                    //Toast.makeText(v.getContext(), R.string.TaskEditor_Field_Inbox, Toast.LENGTH_SHORT).show();
                }
            });


            mListView.setOnTouchListener(new ShowHideOnScroll(fab_add));

            //
            txtIfTaskListIsEmpty=(TextView) getActivity().findViewById(R.id.txtIfListEmpty);

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
        return R.string.app_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStat) {
        return inflater.inflate(R.layout.card_fragment_list_cursor,
                container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(101, savedInstanceState, this);
        //getLoaderManager().initLoader(201, savedInstanceState, this);
        //getLoaderManager().initLoader(301, savedInstanceState, this);
        init();

        //----------------------------------------------------------//
        PriorityUpdater PrU = new PriorityUpdater(getActivity());
        PrU.PirorityUpdate();
        //----------------------------------------------------------//
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
                        projectionAlert, alertSelection, selectionArgs, taskSortOrder);
                break;
            case 301:
                loader = new CursorLoader(getActivity(), ColumnLocation.URI,
                        projectionLoc, LocSelection, selectionArgs, taskSortOrder);
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
/*
        if (data.moveToFirst())
        {
            do
            {
                String str = data.getString(ColumnTask.KEY.INDEX.due_date_millis);
                MyDebug.MakeLog(2,str);
            }while(data.moveToNext());
        }
*/
        mAdapter.swapCursor(data);

        if(data.getCount()==0){
            txtIfTaskListIsEmpty.setVisibility(View.VISIBLE);
            txtIfTaskListIsEmpty.setText(getView().getContext().
                    getResources().
                    getString(R.string.String_If_Task_List_Is_Empty));
            txtIfTaskListIsEmpty.setShadowLayer(10, 0, 0, Color.BLUE);
        }else {
            txtIfTaskListIsEmpty.setVisibility(View.INVISIBLE);
            txtIfTaskListIsEmpty.setText("");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}

