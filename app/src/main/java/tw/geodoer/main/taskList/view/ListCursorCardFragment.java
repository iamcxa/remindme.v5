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

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geodoer.geotodo.R;
import com.melnykov.fab.FloatingActionButton;

import it.gmariotti.cardslib.library.view.CardListView;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.mPriority.controller.PriorityUpdater;
import tw.geodoer.main.taskEditor.view.TaskEditorTabFragment;
import tw.geodoer.main.taskList.adapter.MyCursorCardAdapter;
import tw.geodoer.utils.CommonVar;

/**
 * List with Cursor Example
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ListCursorCardFragment
        extends
        MyBaseFragment
        implements
        LoaderManager.LoaderCallbacks<Cursor> {

    CardListView mListView;
    MyCursorCardAdapter mAdapter;
    TextView txtIfTaskListIsEmpty;
    FloatingActionButton faBtn_add;
    Loader<Cursor> loader;

    int position;
    String taskSelection;
    String taskSortOrder;

    public static ListCursorCardFragment newInstance(int position) {
        ListCursorCardFragment fragment = new ListCursorCardFragment();
        Bundle args = new Bundle();
        args.putInt(CommonVar.STRING_DRAWER_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        //int filter = getArguments().getInt(FILTER_STRING);
        switch (position) {
            case 0:// 智慧待辦清單
                this.taskSelection=(ColumnTask.KEY.status+" == 0");
                taskSortOrder = ColumnTask.KEY.priority+" DESC";
                break;
            case 1:// 按照時間
                this.taskSelection=(ColumnTask.KEY.location_id+" == 0" +
                        " AND " + ColumnTask.KEY.status + " == 0");
                taskSortOrder = ColumnTask.KEY.priority+" DESC";
                break;
            case 2:// 按照地點
                //MyPreferences.mPreferences = PreferenceManager
                //        .getDefaultSharedPreferences(getActivity());
                //int val_radiated_distance = MyPreferences.getValueOfRadiatedDistance();
                taskSelection=(ColumnTask.KEY.location_id+" != 0" +
                        " AND " + ColumnTask.KEY.status + " == 0");
                taskSortOrder = ColumnTask.KEY.priority+" DESC";
                break;
            case 3:// 地圖檢視
                // 切換fragment
                break;
            case 4:// 已完成
                this.taskSelection=(ColumnTask.KEY.status+" == 1");
                taskSortOrder = ColumnTask.KEY.checked+" DESC";
                break;
            case 5:// 垃圾桶
                this.taskSelection=(ColumnTask.KEY.status+" == 2");
                taskSortOrder = ColumnTask.KEY.due_date_millis+" DESC";
                break;
            case 6:// 測試功能
                // 切換fragment
                break;
            default:
                break;
        }

        //
        txtIfTaskListIsEmpty =
                (TextView) getActivity().findViewById(R.id.txtIfListEmpty);
        mAdapter = new MyCursorCardAdapter(getActivity(),position);
        mListView = (CardListView) getActivity().findViewById(
                R.id.carddemo_list_cursor);

        if (mListView != null){
            mListView.setAdapter(mAdapter);
            setFaBtnAdd();
        }

        // Force start background query to load sessions
        getLoaderManager().restartLoader(101, null, this);
        LoaderManager.enableDebugLogging(true);
    }

    private void setFaBtnAdd(){
        faBtn_add
                = (FloatingActionButton) getActivity().findViewById(R.id.faBtn_mainView_add);
        faBtn_add.attachToListView(mListView);
        faBtn_add.setType(FloatingActionButton.TYPE_NORMAL);
        faBtn_add.setColorNormalResId(R.color.card_background_color2);
        faBtn_add.setColorPressedResId(R.color.card_background_color_red_light);
        faBtn_add.setColorRipple(Color.BLUE);
        faBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TaskEditorTabFragment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_fragment_list_cursor, container, false);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.app_name;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt(CommonVar.STRING_DRAWER_POSITION);
        }

        init();

        //----------------------------------------------------------//
        PriorityUpdater PrU = new PriorityUpdater(getActivity());
        PrU.PirorityUpdate();
        //----------------------------------------------------------//
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        loader = null;
        switch (id){
            case 101:
                loader = new CursorLoader(getActivity(), ColumnTask.URI,
                        ColumnTask.PROJECTION, taskSelection, null, taskSortOrder);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getActivity() == null) {
            return;
        }

        if(data.getCount()==0){
            txtIfTaskListIsEmpty.setVisibility(View.VISIBLE);
            txtIfTaskListIsEmpty.setText(
                    getActivity().
                            getResources().
                            getString(R.string.String_If_Task_List_Is_Empty));
            txtIfTaskListIsEmpty.setShadowLayer(10, 0, 0, Color.BLUE);
        }else {
            txtIfTaskListIsEmpty.setVisibility(View.GONE);
            txtIfTaskListIsEmpty.setText("");
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
