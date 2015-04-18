package tw.geodoer.main.taskEditor.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Build;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.geodoer.geotodo.R;

import java.lang.reflect.Field;
import java.util.Calendar;

import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.utils.MyCalendar;
import tw.geodoer.utils.MyDebug;

/**
 * This is a custom dialog class that will hold a tab view with 2 tabs.
 * Tab 1 will be a list view. Tab 2 will be a list view.
 */
public class DueDateCustomDialog extends AlertDialog
        implements
        DialogInterface.OnClickListener,
        OnDateChangeListener,
        OnTimeChangedListener,
        OnShowListener,
        OnTabChangeListener

{
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();
    /**
     * Our custom list view adapter for tab 1 listView (listView01).
     */
    //ListView01Adapter listView01Adapter = null;
    private String selectedDate = "";

    //private ViewGroup viewGroup=(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
    private String selectedTime = "";
    private LayoutInflater inflater = getWindow().getLayoutInflater();
    @SuppressLint("InflateParams")
    private View dialogLayout = inflater.inflate(R.layout.custom_dialog_duedate, null);
    private TabHost tabs = (TabHost) dialogLayout.findViewById(R.id.TabHost01);
    /**
     * Time Picker On-Time-Changed-Listener
     */
//    private OnTimeChangedListener timeChangedListener = new OnTimeChangedListener() {
//        @Override
//        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//            // TODO Auto-generated method stub
//            //			// get millisecond from calendar selected.
//            //			mEditorVar.TaskDate.setmHour(view.getCurrentHour());
//            //			mEditorVar.TaskDate.setmMinute(view.getCurrentMinute());
//            //
//            //			// give a new title with selected date.
//            //			String newTab2Title=view.getCurrentHour()+":"+view.getCurrentMinute();
//            //			setTab2Title(newTab2Title);
//            //
//            //			selectedTime=mEditorVar.TaskDate.getmHour()+":"+mEditorVar.TaskDate.getmMinute();
//            //			MyDebug.MakeLog(0, "The time you Selected="+selectedTime);
//            //
//        }
//    };


    public DueDateCustomDialog(Context context) {
        super(context);

        // remove window title
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // get this window's layout parameters so we can change the position
        WindowManager.LayoutParams params = getWindow().getAttributes();

        // change the position. 0,0 is center
        params.x = 0;
        params.y = 50;
        params.height = -2;
        params.width = -2;
        this.getWindow().setAttributes(params);

        // set custom dialog layout
        setView(dialogLayout);

        // get our tabHost from the xml
        tabs.setup();

        // create tab 1 - calendar - a date picker
        String tab1_Title =
                MyCalendar.getThisMonth()
                        + getContext().getResources().getString(R.string.String_Task_Editor_Date_Month)
                        + MyCalendar.getThisDay()
                        + getContext().getResources().getString(R.string.String_Task_Editor_Date_Day);
        TabHost.TabSpec tab1 = tabs.newTabSpec("tab1");
        tab1.setContent(R.id.calendarView01);
        tab1.setIndicator(tab1_Title);
        tabs.addTab(tab1);

        // create tab 2	- time picker
        String tab2_Title = getContext().getResources().getString(R.string.String_Task_Editor_Dialog_Pick_A_Time);
        TabHost.TabSpec tab2 = tabs.newTabSpec("tab2");
        tab2.setContent(R.id.timePicker01);
        tab2.setIndicator(tab2_Title);
        tabs.addTab(tab2);

        // set listView and tab3- disable
        //setListViews(context);
        ListView listView01 = (ListView) dialogLayout.findViewById(R.id.listView01);
        listView01.setVisibility(View.GONE);

        // set tab host Tab Changed Listener - to add/remove button dynamically.
        tabs.setOnTabChangedListener(this);

        // set dialog Buttons
        this.setButton(BUTTON_POSITIVE,
                getContext().getResources()
                        .getString(R.string.String_Task_Editor_Dialog_BUTTON_POSITIVE), this);
        this.setButton(BUTTON_NEUTRAL,
                getContext().getResources()
                        .getString(R.string.String_Task_Editor_Dialog_BUTTON_NEUTRAL), this);
        this.setButton(BUTTON_NEGATIVE,
                getContext().getResources()
                        .getString(R.string.String_Task_Editor_Dialog_BUTTON_NEGATIVE), this);
        this.setCanceledOnTouchOutside(false);

        // set Show Listener - in case to hide BUTTON_NEUTRAL.
        this.setOnShowListener(this);

        // Calendar - data picker
        CalendarView cal = (CalendarView) dialogLayout.findViewById(R.id.calendarView01);
        cal.setOnDateChangeListener(this);

        // TimePicker
        TimePicker tPicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker01);
        tPicker.setOnTimeChangedListener(this);
        tPicker.setIs24HourView(true);
    }

//    private void setListViews(Context context) {
//        // TODO Auto-generated method stub
//        // instantiate our list views for each tab
//        ListView listView01 = (ListView) dialogLayout.findViewById(R.id.listView01);
//
//        // register a context menu for all our listView02 items
//        registerForContextMenu(listView01);
//
//        // instantiate and set our custom list view adapters
//        listView01Adapter = new ListView01Adapter(context);
//        listView01.setAdapter(listView01Adapter);
//
//        // bind a click listener to the listView01 list
//        listView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
//                // will dismiss the dialog
//                dismiss();
//            }
//        });
//
//        // create tab 3
//        TabHost.TabSpec tab3 = tabs.newTabSpec("tab3");
//        tab3.setContent(R.id.listView01);
//        tab3.setIndicator("jj");
//        tabs.addTab(tab3);
//    }

    /**
     *
     */
    private void getSelectedDate() {
        // get millisecond from calendar selected.
        CommonEditorVar.TaskDate.setmOnlyDateMillis(getDatePicker().getDate());

        // transform Millisecond to MMYYDD
        String YYMMDD = MyCalendar.getDate_From_TimeMillis(false, CommonEditorVar.TaskDate.getmOnlyDateMillis());
        String YYMMDD_Array[] = YYMMDD.split("/");

        int mYear = Integer.valueOf(YYMMDD_Array[0]);
        int mMonth = Integer.valueOf(YYMMDD_Array[1]);
        int mDay = Integer.valueOf(YYMMDD_Array[2]);

        // 設定calendar view的年/月/日/毫秒到mEditorVar中保存
        CommonEditorVar.TaskDate.setmYear(mYear);
        CommonEditorVar.TaskDate.setmMonth(mMonth);
        CommonEditorVar.TaskDate.setmDay(mDay);
        CommonEditorVar.TaskDate.setmOnlyDateMillis(getDatePicker().getDate());

        selectedDate = YYMMDD;

        MyDebug.MakeLog(0, this.toString() + " The date you Selected=" + YYMMDD);
    }

    /**
     *
     */
    private void getSelectedTime() {
        if ((getBtnNutral().getVisibility()) == (View.VISIBLE)) {

            selectedTime =
                    getTimePicker().getCurrentHour()
                            + ":"
                            + getTimePicker().getCurrentMinute();

            // get millisecond from calendar selected.
            CommonEditorVar.TaskDate.setmHour(getTimePicker().getCurrentHour());
            CommonEditorVar.TaskDate.setmMinute(getTimePicker().getCurrentMinute());

            MyDebug.MakeLog(0,
                    "The time you Selected="
                            + CommonEditorVar.TaskDate.getmHour() + ":"
                            + CommonEditorVar.TaskDate.getmMinute());
        }
    }

    private TimePicker getTimePicker() {
        final TimePicker tPicker;
        tPicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker01);
        return tPicker;
    }

    private CalendarView getDatePicker() {
        final CalendarView cal;
        cal = (CalendarView) dialogLayout.findViewById(R.id.calendarView01);
        return cal;
    }

    private TextView getTab1Title() {
        return (TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
    }

    private TextView getTab2Title() {
        return (TextView) tabs.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
    }

    private void setTab2Title(String newTab2Title) {
        // give a new title with selected time.
        getTab2Title().setText(newTab2Title);
    }

    private Button getBtnNutral() {
        return getButton(DialogInterface.BUTTON_NEUTRAL);
    }

    private Button getBtnPositive() {
        //Button negativeButton = getButton(AlertDialog.BUTTON_NEGATIVE);
        return getButton(DialogInterface.BUTTON_POSITIVE);
    }

    private Button getBtnNegative() {
        return getButton(DialogInterface.BUTTON_NEGATIVE);
    }

    private void setTab1Title(int year, int month, int dayOfMonth) {
        // give a new title with selected date.
        String optionalYear = "";

        if ((year) != Integer.valueOf(MyCalendar.getThisYear()))
            optionalYear = String.valueOf(year)
                    + getContext().getResources().
                    getString(R.string.String_Task_Editor_Date_Year);

        String newTab1Title =
                optionalYear
                        + month
                        + getContext().getResources().getString(R.string.String_Task_Editor_Date_Month)
                        + dayOfMonth
                        + getContext().getResources().getString(R.string.String_Task_Editor_Date_Day);

        getTab1Title().setText(newTab1Title);
    }

    private void setDialogShowing(DialogInterface dialog) {
        try {
            //不關閉
            Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
            MyDebug.MakeLog(1, "setDialogShowing");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDialogDismiss(DialogInterface dialog) {
        try {
            //不關閉
            Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
            MyDebug.MakeLog(1, "setDialogDismiss");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO
    private void setBtnAction_Positive(DialogInterface dialog) {
        // 清空 TaskDueDate 欄位
        TaskEditorMainFragment.setTaskDueDate(null);
        try {
            // 取得所選日期
            getSelectedDate();

            // 取得所選時間
            getSelectedTime();

            // 如果放棄時間按鈕"不"存在 -> 只把日期放入TaskDueDate欄位
            if ((getBtnNutral().getVisibility()) == (View.GONE))
                TaskEditorMainFragment.setTaskDueDate(selectedDate);

            if ((getBtnNutral().getVisibility()) == (View.VISIBLE))
                TaskEditorMainFragment.setTaskDueDate(selectedDate + ";" + selectedTime);

            setDialogDismiss(dialog);
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "error msg="
                            + e.toString(), Toast.LENGTH_SHORT).show();
            MyDebug.MakeLog(2, this.toString() + " error msg=" + e.toString());
        }
    }

    private void setBtnAction_Neutral() {
        selectedTime = "";

        setTab2Title(getContext().getResources()
                .getString(R.string.String_Task_Editor_Dialog_Pick_A_Time));

        // 隱藏按鈕
        getBtnNutral().setVisibility(View.GONE);

        // 切換tabHost到日期
        tabs.setCurrentTab(0);

        // mEditorVar
        CommonEditorVar.TaskDate.setmDatePulsTimeMillis(0);
        CommonEditorVar.TaskDate.setmHour(0);
        CommonEditorVar.TaskDate.setmMinute(0);
    }

    /**
     *  Data Picker On-Click-Listener
     */
    //	private OnDateChangeListener dateChangeListener=new OnDateChangeListener() {
    //		@Override
    //		public void onSelectedDayChange(CalendarView view, int year, int month,
    //				int dayOfMonth) {
    //			// TODO Auto-generated method stub
    //
    //			//Calendar calendar= Calendar.getInstance();
    //			//fixUpDatePickerCalendarView(calendar);
    //
    //			int mMonth=month+1;
    //			setTab1Title(year, mMonth, dayOfMonth);
    //			selectedDate=year+"/"+mMonth+"/"+dayOfMonth;
    //			MyDebug.MakeLog(0, "The date you Selected="+selectedDate);
    //		}
    //	};


    private void fixUpDatePickerCalendarView(Calendar date) {
        // Workaround for CalendarView bug relating to setMinDate():
        // https://code.google.com/p/android/issues/detail?id=42750
        // Set then reset the date on the calendar so that it properly
        // shows today's date. The choice of 24 months is arbitrary.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final CalendarView cal = getDatePicker();
            if (cal != null) {
                date.add(Calendar.MONTH, 24);
                cal.setDate(date.getTimeInMillis(), false, true);
                date.add(Calendar.MONTH, -24);
                cal.setDate(date.getTimeInMillis(), false, true);
            }
        }
    }

    /**
     * 建立三個按鈕的監聽式
     */
    //	private DialogInterface.OnClickListener btnClickListener = new DialogInterface.OnClickListener()
    //	{
    //		@Override
    //		public void onClick(DialogInterface dialog, int which) {
    //			//which可以用來分辨是按下哪一個按鈕
    //			switch (which) {
    //			case Dialog.BUTTON_POSITIVE:	// save selected date/time
    //
    //				setBtnAction_Positive(dialog);
    //
    //				break;
    //			case Dialog.BUTTON_NEUTRAL:		// 取消時間
    //				setDialogShowing(dialog);
    //				setBtnAction_Neutral();
    //
    //				break;
    //			case Dialog.BUTTON_NEGATIVE:	// 取消全部
    //
    //				setDialogDismiss(dialog);
    //
    //				break;
    //			}
    //		}
    //	};

    /**
     * Dialog On-Show-Listener
     * 從 TaskEditorMain 讀取日期時間  / 隱藏放棄時間按鈕
     */
    //	private OnShowListener dialogShowListener=new OnShowListener() {
    //		@Override
    //		public void onShow(DialogInterface dialog) {
    //			// 啟動先隱藏放棄時間按鈕
    //			getBtnNutral().setVisibility(ViewGroup.GONE);
    //
    //			// 檢查TaskEditorMain中的TaskDueDate欄位長度
    //			if(TaskEditorMain.getTaskDueDateStringLength()>0){
    //				// 如果有長度才讀出該欄位
    //				String existDueDate=TaskEditorMain.getTaskDueDate();
    //
    //				// 判斷 TaskEditorMain.getTaskDueDate() 是否有"/"符號
    //				if(existDueDate.contains("/")){
    //					// 以";"符號分隔日期與時間 - YYYY/MM/DD;HH:MM - 以[0]確保一定是抓到日期
    //					String[] arrayExistDueDate=existDueDate.split(";");
    //
    //					// 以"/"符號分隔年月日 - YYYY/MM/DD
    //					String[] arrayExistDueDateDetail=arrayExistDueDate[0].split("/");
    //
    //					// 把YYYY/MM/DD部分放入變數selectedDate.
    //					selectedDate=arrayExistDueDate[0];
    //
    //					//  由mEditorVar讀出所選擇日期之毫秒資訊
    //					long dueDateMillis=mEditorVar.TaskDate.getmOnlyDateMillis();
    //
    //					// 將換算完成後的毫秒塞入calendar view中.
    //					getDatePicker().setDate(dueDateMillis);
    //
    //					// 將日期資料放到 tab1標題上
    //					setTab1Title(
    //							Integer.valueOf(arrayExistDueDateDetail[0]),
    //							Integer.valueOf(arrayExistDueDateDetail[1]),
    //							Integer.valueOf(arrayExistDueDateDetail[2]));
    //
    //
    //					// 讀時間資料到 tab 2 title.
    //					// 如果有該欄位有時間部分, 則將其放入selectedTime.
    //					if(existDueDate.contains(";")) {
    //						getBtnNutral().setVisibility(View.VISIBLE);
    //						selectedTime=arrayExistDueDate[1];
    //						setTab2Title(selectedTime);
    //
    //						// 設定所選擇時間到time picker
    //						String[] arrayTimeStrings=arrayExistDueDate[1].split(":");
    //						getTimePicker().setCurrentHour(Integer.valueOf(arrayTimeStrings[0]));
    //						getTimePicker().setCurrentMinute(Integer.valueOf(arrayTimeStrings[1]));
    //
    //					}
    //
    //
    //					// log
    //					MyDebug.MakeLog(0, "arrayExistDueDate[0]="+arrayExistDueDate[0]);
    //					MyDebug.MakeLog(0, "existDueDate="+existDueDate);
    //					if(existDueDate.contains(";")) MyDebug.MakeLog(0, "arrayExistDueDate[1]="+arrayExistDueDate[1]);
    //					MyDebug.MakeLog(0, "dueDateMillis="+dueDateMillis);
    //
    //				}else {
    //					// 預設選擇今天
    //					selectedDate=String.valueOf(MyCalendar.getTodayString(0));
    //					getDatePicker().setDate(MyCalendar.getNextFewDays(0));
    //				}
    //			}
    //		}
    //	};

    /**
     * TabHost On-Tab-ChangeListener
     */
    //	private TabHost.OnTabChangeListener tabsChangedListener = new OnTabChangeListener() {
    //		@Override
    //		public void onTabChanged(String tabId) {
    //			// tab2=時間
    //			if(tabId=="tab2") {
    //				selectedTime=getTimePicker().getCurrentHour()+":"+getTimePicker().getCurrentMinute();
    //
    //				getBtnNutral().setVisibility(View.VISIBLE);
    //
    //				setTab2Title(selectedTime);
    //			}
    //		}
    //	};

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
     * TabHost On-Tab-ChangeListener
     */
    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        // tab2=時間
        if (tabId.equals("tab2")) {
            selectedTime = getTimePicker().getCurrentHour() + ":" + getTimePicker().getCurrentMinute();

            getBtnNutral().setVisibility(View.VISIBLE);

            setTab2Title(selectedTime);
        }
    }

    /**
     * Dialog On-Show-Listener
     * 從 TaskEditorMain 讀取日期時間  / 隱藏放棄時間按鈕
     */
    @Override
    public void onShow(DialogInterface dialog) {
        // TODO Auto-generated method stub
        // 啟動先隱藏放棄時間按鈕
        getBtnNutral().setVisibility(View.GONE);

        // 檢查TaskEditorMain中的TaskDueDate欄位長度
        if (TaskEditorMainFragment.getTaskDueDateStringLength() > 0) {
            // 如果有長度才讀出該欄位
            String existDueDate = TaskEditorMainFragment.getTaskDueDateString();

            // 判斷 TaskEditorMain.getTaskDueDate() 是否有"/"符號
            if (existDueDate.contains("/")) {
                // 以";"符號分隔日期與時間 - YYYY/MM/DD;HH:MM - 以[0]確保一定是抓到日期
                String[] arrayExistDueDate = existDueDate.split(";");

                // 以"/"符號分隔年月日 - YYYY/MM/DD
                String[] arrayExistDueDateDetail = arrayExistDueDate[0].split("/");

                // 把YYYY/MM/DD部分放入變數selectedDate.
                selectedDate = arrayExistDueDate[0];

                int year = Integer.valueOf(arrayExistDueDateDetail[0]);
                int month = Integer.valueOf(arrayExistDueDateDetail[1]);
                int day = Integer.valueOf(arrayExistDueDateDetail[2]);

                //  由mEditorVar讀出所選擇日期之毫秒資訊
                //long dueDateMillis=mEditorVar.TaskDate.getmOnlyDateMillis();
                // 設定讀出年月日給calender, 月份從0開始故需-1
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, (month - 1), day);

                // 將換算完成後的毫秒塞入calendar view中.
                getDatePicker().setDate(calendar.getTimeInMillis());

                // 將日期資料放到 tab1標題上
                setTab1Title(year, month, day);

                // 讀時間資料到 tab 2 title.
                // 如果有該欄位有時間部分, 則將其放入selectedTime.
                if (existDueDate.contains(";")) {
                    getBtnNutral().setVisibility(View.VISIBLE);
                    selectedTime = arrayExistDueDate[1];
                    setTab2Title(selectedTime);

                    // 設定所選擇時間到time picker
                    String[] arrayTimeStrings = arrayExistDueDate[1].split(":");
                    getTimePicker().setCurrentHour(Integer.valueOf(arrayTimeStrings[0]));
                    getTimePicker().setCurrentMinute(Integer.valueOf(arrayTimeStrings[1]));

                }


                // log
                MyDebug.MakeLog(0, "arrayExistDueDate[0]=" + arrayExistDueDate[0]);
                MyDebug.MakeLog(0, "existDueDate=" + existDueDate);
                if (existDueDate.contains(";"))
                    MyDebug.MakeLog(0, "arrayExistDueDate[1]=" + arrayExistDueDate[1]);
                MyDebug.MakeLog(0, "dueDateMillis=" + calendar.getTimeInMillis());

            } else {
                // 預設選擇今天
                selectedDate = String.valueOf(MyCalendar.getTodayString(0));
                getDatePicker().setDate(MyCalendar.getNextFewDays(0));
            }
        }
    }

    /**
     * Time Picker On-Time-Changed-Listener
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub
        // get millisecond from calendar selected.
        CommonEditorVar.TaskDate.setmHour(view.getCurrentHour());
        CommonEditorVar.TaskDate.setmMinute(view.getCurrentMinute());

        // give a new title with selected date.
        String newTab2Title = view.getCurrentHour() + ":" + view.getCurrentMinute();
        setTab2Title(newTab2Title);

        selectedTime = CommonEditorVar.TaskDate.getmHour() + ":" + CommonEditorVar.TaskDate.getmMinute();
        MyDebug.MakeLog(2, "The time you Selected=" + selectedTime);
    }

    /**
     * Data Picker On-Click-Listener
     */
    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month,
                                    int dayOfMonth) {
        // TODO Auto-generated method stub
        //Calendar calendar= Calendar.getInstance();
        //fixUpDatePickerCalendarView(calendar);


        setTab1Title(year, month + 1, dayOfMonth);

        CommonEditorVar.TaskDate.setmYear(year);
        CommonEditorVar.TaskDate.setmMonth(month + 1);
        CommonEditorVar.TaskDate.setmDay(dayOfMonth);

        selectedDate = CommonEditorVar.TaskDate.getmYear() + "/"
                + CommonEditorVar.TaskDate.getmMonth() + "/" +
                CommonEditorVar.TaskDate.getmDay();
        MyDebug.MakeLog(2, "The date you Selected=" + selectedDate);
    }

    /**
     * 建立三個按鈕的監聽式
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        //which可以用來分辨是按下哪一個按鈕
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:    // save selected date/time

                setBtnAction_Positive(dialog);

                break;
            case DialogInterface.BUTTON_NEUTRAL:        // 取消時間
                setDialogShowing(dialog);
                setBtnAction_Neutral();

                break;
            case DialogInterface.BUTTON_NEGATIVE:    // 取消全部

                setDialogDismiss(dialog);

                break;
        }
    }
}