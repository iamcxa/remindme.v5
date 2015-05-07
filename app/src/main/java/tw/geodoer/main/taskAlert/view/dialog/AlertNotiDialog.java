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
    public AlertNotiDialog()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.VERTICAL);
        LL.setBackgroundColor(Color.argb(255,255,255,255));
        LinearLayout.LayoutParams LLP = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView text_title = new TextView(this);
        text_title.setText("任務dialog");
        text_title.setTextSize(30f);
        text_title.setTextColor(Color.argb(255,0,0,0));
        LL.addView(text_title);

        setContentView(LL,LLP);
    }

}