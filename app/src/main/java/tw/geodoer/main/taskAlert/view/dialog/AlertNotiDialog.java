package tw.geodoer.main.taskAlert.view.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertNotiDialog extends Activity
{
    public AlertNotiDialog()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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