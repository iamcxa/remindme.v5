package tw.geodoer.main.taskList.view;/*
 *
 */

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import tw.geodoer.utils.MyDebug;
import tw.geodoer.main.taskPreference.controller.MyPreferences;
import tw.geodoer.mGeoInfo.controller.LocationGetter;
import tw.geodoer.mPriority.service.TaskSortingService;
import tw.geodoer.main.taskEditor.controller.ActionSetAlarm;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.main.taskEditor.view.TaskEditorTabFragment;
import tw.geodoer.main.taskPreference.view.AppPreferenceActivity;
import tw.moretion.geodoer.R;


/**
 * @author cxa Main Activity
 */
public class AppMainActivity extends ActionBarActivity {
    /**********************/
    /** Variables LOCALE **/
    /**
     * ******************
     */
    // Used in savedInstanceState
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();
    private static ProgressDialog psDialog;
    private static int lastPosition = 9999;
    private static Bundle args;
    private static Fragment fragment;
    private static DrawerLayout mDrawerLayout;
    private static ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;

    CharSequence mDrawerTitle;
    CharSequence mTitle;
    String[] mPlanetTitles;

    static FrameLayout loading_Frame;
    static FrameLayout content_Frame;
    static FragmentManager fragmentManager;
    static Fragment fragmentLoading;

    /*********************/
    /** onCreate LOCALE **/
    /**
     * *****************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MyPreferences.mPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        fragmentManager = getSupportFragmentManager();
        loading_Frame = (FrameLayout) findViewById(R.id.loading_frame);
        content_Frame = (FrameLayout) findViewById(R.id.content_frame);
        fragmentLoading = new MyProgressFragment();


        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setNavigationDrawer(savedInstanceState);
        setViewComponent();

    }

    /*********************/
    /** onResume LOCALE **/
    /**
     * *****************
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    /*************************/
    /** onSaveInstanceState **/
    /**
     * *********************
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /*************************/
    /** setNavigationDrawer **/
    /**
     * *********************
     */
    private void setNavigationDrawer(Bundle savedInstanceState) {
        mPlanetTitles = getResources().getStringArray(R.array.drawer_array_CHT);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //mTitle =mPlanetTitles[mDrawerIndex];
        mDrawerTitle = getTitle();

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.activity_main_drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open//,  /* "open drawer" description for accessibility */
                //R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!(lastPosition == position)) {
                selectItem(position);
                MyDebug.MakeLog(0, "navagation drawer切換頁面");
            } else {
                drawerActions(position);
            }
        }
    }

    // 按下 Navigation Drawer 後的動作
    private void selectItem(int position) {
        // setLoadingStart();

        // update the main content by replacing fragments
        fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        //if(fragment==null){
        fragment = MyProgressFragment.newInstance();
        args = new Bundle();
        //}

        // 傳遞所按位置索引
        //args.clear();
        //args.putInt(RemindmeFragment.FILTER_STRING,(position));
        //fragment.getArguments()
        //fragment.setArguments(args);
        ListCursorCardFragment.setPosition(position);

        MyDebug.MakeLog(0, "position@MainActivity=" + position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "RemindmeFragment").commit();

        drawerActions(position);
    }

    private void drawerActions(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        this.setTitle(mPlanetTitles[position]);
        this.getSupportActionBar().setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        mTitle = mPlanetTitles[position];

        lastPosition = position;
    }

    /**********************/
    /** StartService LOCALE **/
    /**
     * ******************
     */
    public void StartService() {
        MyPreferences.mPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        if (MyPreferences.IS_SERVICE_ON()) {
            Intent intent = new Intent(this, TaskSortingService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(intent);
        }
    }

    /*************************/
    /** onCreateOptionsMenu **/
    /**
     * *********************
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 由main_activity_actionbar.xml讀取按鈕資訊
        getMenuInflater().inflate(R.menu.main_activity_actionbar, menu);

        // 定義：搜尋按鈕
        MenuItem actionSearch = menu.findItem(R.id.action_search);
        // 設定：點擊接收器
        // mSearchView = (SearchView) actionSearch.getActionView();
        // setSearchView(actionSearch);

        // 定義：新增按鈕
        MenuItem actionAdd = menu.findItem(R.id.action_add);
        // 設定：點擊接收器
        actionAdd.setOnMenuItemClickListener(btnActionAddClick);

        // 定義：重整按鈕
        MenuItem actionRefresh = menu.findItem(R.id.action_refresh);

        // 設定：點擊接收器
        actionRefresh.setOnMenuItemClickListener(btnRefreshClick);

        // 定義：設置按鈕
        MenuItem actionPref = menu.findItem(R.id.action_settings);
        // 設定：點擊接收器
        actionPref.setOnMenuItemClickListener(btnPrefClick);

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);


        //-------------------------------------------------------//
        //                     									 //
        //                     上架暫時關閉地點						 //
        //                     									 //
        //-------------------------------------------------------//
        actionRefresh.setEnabled(false);
        actionRefresh.setVisible(false);
        actionSearch.setEnabled(false);
        actionSearch.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**********************/
    /** setViewComponent **/
    /**
     * ******************
     */
    private void setViewComponent() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                StartService();
            }
        }).start();
    }

    /**********************/
    /** btnActionAddClick **/
    /**
     * ******************
     */
    private MenuItem.OnMenuItemClickListener btnActionAddClick = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // Toast.makeText(getApplication(), item.getTitle(),
            // Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(getApplication(), TaskEditorTabFragment.class);
            startActivity(intent);
            return false;
        }
    };

    /************************/
    /** btnRefreshAddClick **/
    /**
     * ********************
     */
    private MenuItem.OnMenuItemClickListener btnRefreshClick = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Toast.makeText(getApplication(), item.getTitle(),
                    Toast.LENGTH_SHORT).show();

            LocationGetter UpdataLocation = new LocationGetter(getApplicationContext());
            UpdataLocation.UpdateOncePriority();

            ListCursorCardFragment.getmAdapter().notifyDataSetChanged();


            ActionSetAlarm mSetAlarm = new ActionSetAlarm(getApplicationContext(), System.currentTimeMillis() + 1000, 10);
            mSetAlarm.SetIt();

            return false;
        }
    };

    /**********************/
    /** menuActionPrefClick **/
    /**
     * ******************
     */
    private MenuItem.OnMenuItemClickListener btnPrefClick = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // Display the fragment as the main content.

            Toast.makeText(getApplication(), item.getTitle(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AppPreferenceActivity.class);
            startActivity(intent);

            return false;
        }
    };

    public static void setLoadingEnd() {

        AppMainActivity.loading_Frame.setVisibility(View.GONE);
        AppMainActivity.content_Frame.setVisibility(View.VISIBLE);
    }

    public static void setLoadingStart() {


        AppMainActivity.content_Frame.setVisibility(View.GONE);
        AppMainActivity.loading_Frame.setVisibility(View.VISIBLE);


    }


}