package tw.geodoer.main.taskList.cardsui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageButton;

import it.gmariotti.cardslib.library.view.component.CardHeaderView;

/**
 * *******************
 */
// implment the clickable card thumbnail.
public class MyCardHeader extends CardHeaderView {

    @Override
    @SuppressLint("NewApi")
    protected void setupButtons() {
        // TODO Auto-generated method stub
        super.setupButtons();
    }

    @Override
    protected void setupInnerView() {
        // TODO Auto-generated method stub
        super.setupInnerView();

    }

    @Override
    public ImageButton getImageButtonOverflow() {
        // TODO Auto-generated method stub
        return super.getImageButtonOverflow();
    }

    @Override
    public ImageButton getImageButtonExpand() {
        // TODO Auto-generated method stub
        return super.getImageButtonExpand();
    }

    @Override
    public ImageButton getImageButtonOther() {
        // TODO Auto-generated method stub
        return super.getImageButtonOther();
    }

    public MyCardHeader(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

}
