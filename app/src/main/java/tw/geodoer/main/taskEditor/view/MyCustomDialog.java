package tw.geodoer.main.taskEditor.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.utils.MyCalendar;
import tw.moretion.geodoer.R;

/**
 * This is a custom dialog class that will hold a tab view with 2 tabs.
 * Tab 1 will be a list view. Tab 2 will be a list view.
 */
public class MyCustomDialog extends AlertDialog {
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();

    /**
     * Our custom list view adapter for tab 1 listView (listView01).
     */
    ListView01Adapter listView01Adapter = null;

    /**
     * Our custom list view adapter for tab2 listView (listView02).
     */
    ListView02Adapter listView02Adapter = null;
    /**
     * 建立三個按鈕的監聽式
     */
    private DialogInterface.OnClickListener ClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //which可以用來分辨是按下哪一個按鈕
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //快顯訊息
                    Toast.makeText(getContext(), "左邊按鈕",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    //快顯訊息
                    Toast.makeText(getContext(), "中間按鈕",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //快顯訊息
                    Toast.makeText(getContext(), "右邊按鈕",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    /**
     * TabHost On Tab ChangeListener
     */
    private TabHost.OnTabChangeListener tabsChangedListener = new OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            // TODO Auto-generated method stub
            TabHost tabs = (TabHost) getWindow().findViewById(R.id.TabHost01);
            String tag = tabs.getCurrentTabTag();
            Toast.makeText(getContext(), tag, Toast.LENGTH_SHORT).show();
            Button positiveButton = getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = getButton(DialogInterface.BUTTON_NEGATIVE);
            Button nutralButton = getButton(DialogInterface.BUTTON_NEUTRAL);
            nutralButton.setVisibility(View.GONE);

            if (tag == "tab1") nutralButton.setVisibility(View.GONE);
            if (tag == "tab2") nutralButton.setVisibility(View.VISIBLE);
        }
    };

    /**
     * Default constructor.
     *
     * @param context
     */
    public MyCustomDialog(Context context) {
        super(context);

        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // get this window's layout parameters so we can change the position
        WindowManager.LayoutParams params = getWindow().getAttributes();

        // change the position. 0,0 is center
        params.x = 0;
        params.y = -150;
        params.height = -2;
        params.width = -2;
        this.getWindow().setAttributes(params);

        //ViewGroup viewGroup=(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        LayoutInflater inflater = getWindow().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.custom_dialog_duedate, null);
        setView(dialoglayout);

        // instantiate our list views for each tab
        ListView listView01 = (ListView) dialoglayout.findViewById(R.id.listView01);
        ListView listView02 = (ListView) dialoglayout.findViewById(R.id.listView02);

        // register a context menu for all our listView02 items
        registerForContextMenu(listView01);

        // instantiate and set our custom list view adapters
        listView01Adapter = new ListView01Adapter(context);
        listView01.setAdapter(listView01Adapter);

        //listView02Adapter = new ListView02Adapter(context);
        //listView02.setAdapter(listView02Adapter);

        // bind a click listener to the listView01 list
        listView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                // will dismiss the dialog
                dismiss();
            }
        });

        // bind a click listener to the listView02 list
//		listView02.setOnItemClickListener(new AdapterView.OnItemClickListener()
//		{
//			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id)
//			{
//				// will dismiss the dialog
//				dismiss();
//			}
//		});

        // get our tabHost from the xml
        TabHost tabs = (TabHost) dialoglayout.findViewById(R.id.TabHost01);
        tabs.setup();

        // create tab 1 - calendar
        String tab1_Title =
                MyCalendar.getThisMonth()
                        + getContext().getResources().getString(R.string.String_Task_Editor_Date_Month).toString()
                        + MyCalendar.getThisDay()
                        + getContext().getResources().getString(R.string.String_Task_Editor_Date_Day).toString();
        TabHost.TabSpec tab1 = tabs.newTabSpec("tab1");
        tab1.setContent(R.id.calendarView01);
        tab1.setIndicator(tab1_Title);
        tabs.addTab(tab1);

        // create tab 2
        String tab2_Title = getContext().getResources().getString(R.string.String_Task_Editor_Dialog_Pick_A_Time).toString();
        TabHost.TabSpec tab2 = tabs.newTabSpec("tab2");
        tab2.setContent(R.id.timePicker01);
        tab2.setIndicator(tab2_Title);
        tabs.addTab(tab2);

        // create tab 3
        TabHost.TabSpec tab3 = tabs.newTabSpec("tab3");
        tab3.setContent(R.id.listView01);
        tab3.setIndicator("jj");
        tabs.addTab(tab3);

        // create tab 4
        TabHost.TabSpec tab4 = tabs.newTabSpec("tab4");
        tab4.setContent(R.id.listView02);
        tab4.setIndicator("time");
        tabs.addTab(tab4);

        // Calendar - data picker
        CalendarView cal = (CalendarView) dialoglayout.findViewById(R.id.calendarView01);

        // set Buttons
        this.setButton(BUTTON_NEGATIVE, "no", ClickListener);
        this.setButton(BUTTON_NEUTRAL, "oo", ClickListener);
        this.setButton(BUTTON_POSITIVE, "yes", ClickListener);

        // tab host Tab Changed Listener
        tabs.setOnTabChangedListener(tabsChangedListener);
    }

    /**
     * This is called when a long press occurs on our listView02 items.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Delete");
    }

    /**
     * This is called when an item in our context menu is clicked.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete") {

        } else {
            return false;
        }

        return true;
    }

    /**
     * A custom list adapter for the listView01
     */
    private class ListView01Adapter extends BaseAdapter {
        public ListView01Adapter(Context context) {

        }

        /**
         * This is used to return how many rows are in the list view
         */
        @Override
        public int getCount() {
            // add code here to determine how many results we have, hard coded for now

            return 2;
        }

        /**
         * Should return whatever object represents one row in the
         * list.
         */
        @Override
        public Object getItem(int position) {
            return position;
        }

        /**
         * Used to return the id of any custom data object.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * This is used to define each row in the list view.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            // our custom holder will represent the view on each row. See class below.
            ListView01Holder holder = null;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();

                // inflate our row from xml
                row = inflater.inflate(R.layout.custom_dialog_list_view_01_row, parent, false);

                // instantiate our holder
                holder = new ListView01Holder(row);

                // set our holder to the row
                row.setTag(holder);

            } else {
                holder = (ListView01Holder) row.getTag();
            }

            return row;
        }

        // our custom holder
        class ListView01Holder {
            // text view
            private TextView text = null;

            // image view
            private ImageView image = null;

            ListView01Holder(View row) {
                // get out text view from xml
                text = (TextView) row.findViewById(R.id.list_view_01_row_text_view);

                // add code here to set the text
                text.setText("ddd");

                // get our image view from xml
                image = (ImageView) row.findViewById(R.id.list_view_01_row_image_view);

                // add code here to determine which image to load, hard coded for now
                image.setImageResource(R.drawable.map_marker);
            }
        }
    }

    /**
     * A custom list adapter for listView02
     */
    private class ListView02Adapter extends BaseAdapter {
        public ListView02Adapter(Context context) {

        }

        /**
         * This is used to return how many rows are in the list view
         */
        @Override
        public int getCount() {
            // add code here to determine how many results we have, hard coded for now

            return 3;
        }

        /**
         * Should return whatever object represents one row in the
         * list.
         */
        @Override
        public Object getItem(int position) {
            return position;
        }

        /**
         * Used to return the id of any custom data object.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * This is used to define each row in the list view.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ListView02Holder holder = null;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.custom_dialog_list_view_02_row, parent, false);
                holder = new ListView02Holder(row);
                row.setTag(holder);
            } else {
                holder = (ListView02Holder) row.getTag();
            }

            return row;
        }

        class ListView02Holder {
            private TextView text = null;

            ListView02Holder(View row) {
                text = (TextView) row.findViewById(R.id.list_view_02_row_text_view);
                text.setText("sdsda");
            }
        }
    }
}