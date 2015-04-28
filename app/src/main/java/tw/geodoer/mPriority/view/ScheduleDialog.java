package tw.geodoer.mPriority.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Reader;

/**
 * Created by MurasakiYoru on 2015/4/21.
 */
public class ScheduleDialog extends Dialog
{
    private Context mContext;
    private TextView title;


    public ScheduleDialog(Context context)
    {
        super(context);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.mContext = context;

        LinearLayout RR = new LinearLayout(mContext);
//----------------------------------------------------------------

        ScrollView SV = new ScrollView(mContext);
        RelativeLayout.LayoutParams SVP = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2000);
        SV.setBackgroundColor(Color.argb(255,255,0,0));

        RR.addView(SV, SVP);


        TextView TV = new TextView(mContext);
        TV.setTextSize(48f);
        TV.setText("TEXTVIEW");

        RR.addView(TV);

//-----------------------------------------------------------------
        setContentView(RR);
        getWindow().setLayout(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
    }


}
