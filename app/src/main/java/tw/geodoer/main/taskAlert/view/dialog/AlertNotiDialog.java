package tw.geodoer.main.taskAlert.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geodoer.geotodo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;


/**
 * @author iamcxa 提醒方法
 */
public class AlertNotiDialog extends Activity
{

    private int taskID ;
    private Context mContext;

    private DBTasksHelper DBT;
    private DBLocationHelper DBL;

    private String title;
    private String content;

    private Calendar mCalendar;
    private final String format = "yyyy-MM-dd kk:mm";
    private long millis_due_time;

    private int locID;
    private String loctitle;

    public AlertNotiDialog()
    {

        this.mContext = this;

        if(mCalendar == null) mCalendar = Calendar.getInstance();

        DBT = new DBTasksHelper(mContext);
        DBL = new DBLocationHelper(mContext);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent it = getIntent();
        Bundle b = it.getExtras();
        String ID = b.getString("taskID");
        this.taskID = Integer.valueOf(ID);
        title = DBT.getItemString(taskID,ColumnTask.KEY.title);
        content = DBT.getItemString(taskID,ColumnTask.KEY.content);

        millis_due_time = DBT.getItemLong(taskID,ColumnTask.KEY.due_date_millis);

        locID = DBT.getItemInt(taskID,ColumnTask.KEY.location_id);
        if(locID != 0)loctitle = DBL.getItemString(locID, ColumnLocation.KEY.name);


        this.setTitle("Fast Check");

        LinearLayout LL = new LinearLayout(mContext);
        LL.setOrientation(LinearLayout.VERTICAL);
        LL.setBackgroundColor(Color.argb(255,255,255,255));
        LinearLayout.LayoutParams LLP = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //--------------------------------------------------------------
        TextView text_title = new TextView(mContext);
        text_title.setText("任務: "+title);
        text_title.setTextSize(30f);
        text_title.setTextColor(Color.argb(255,0,0,0));
        LL.addView(text_title);
        //--------------------------------------------------------------
        TextView text_content = new TextView(mContext);
        text_content.setText("備註: " + content);
        text_content.setTextSize(24f);
        text_content.setTextColor(Color.argb(255, 0, 0, 0));
        LL.addView(text_content);
        //--------------------------------------------------------------
        TextView text_time = new TextView(mContext);
        if (millis_due_time != 0)
        {
            mCalendar.setTimeInMillis(millis_due_time);
            SimpleDateFormat SD = new SimpleDateFormat(format);
            text_time.setText("時間: " + SD.format(mCalendar.getTime()));
        }
        else text_time.setText("時間: 未定");
        text_time.setTextSize(24f);
        text_time.setTextColor(Color.argb(255, 0, 0, 0));
        LL.addView(text_time);
        //--------------------------------------------------------------
        TextView text_loc = new TextView(mContext);
        if (locID != 0)text_loc.setText("地點: " + loctitle);
        else text_loc.setText("地點: 未定");
        text_loc.setTextSize(24f);
        text_loc.setTextColor(Color.argb(255, 0, 0, 0));
        LL.addView(text_loc);
        //---------------------------------------------------------------
        setContentView(LL,LLP);
    }

}