package tw.geodoer.main.taskList.cardsui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geodoer.geotodo.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Class MyCursorCard *
 */
public class MyCursorCard extends Card {

    TextView mTitleTextView;
    TextView mSecondaryTitleTextView;
    TextView mThirdTitleTextView;

    public String dueDate;
    public String LocationName;
    public String Notifications;
    public String mainHeader;
    public int resourceIdThumb;

    public MyCursorCard(Context context) {
        super(context, R.layout.card_cursor_inner_content);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        // Retrieve elements
        mTitleTextView = (TextView) parent
                .findViewById(R.id.card_cursor_main_inner_title);
        mSecondaryTitleTextView = (TextView) parent
                .findViewById(R.id.card_cursor_main_inner_subtitle);
        mThirdTitleTextView = (TextView) parent
                .findViewById(R.id.card_cursor_main_inner_thirdtitle);

        if (mTitleTextView != null)
            mTitleTextView.setText(dueDate);

        if (mSecondaryTitleTextView != null)
            mSecondaryTitleTextView.setText(LocationName);

        if (mThirdTitleTextView != null)
            mThirdTitleTextView.setText(Notifications);
    }
}
