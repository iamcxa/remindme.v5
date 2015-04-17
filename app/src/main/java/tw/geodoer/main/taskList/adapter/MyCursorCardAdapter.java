/**
 *
 */
package tw.geodoer.main.taskList.adapter;

import android.content.Context;
import android.database.Cursor;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;
import it.gmariotti.cardslib.library.view.component.CardShadowView;
import tw.geodoer.main.taskList.cardsui.CardThumbnailCircle;
import tw.geodoer.main.taskList.cardsui.MyCursorCard;
import tw.geodoer.main.taskList.controller.ActionOnCardClicked;
import tw.geodoer.main.taskList.controller.ActionOnCardLongClicked;
import tw.geodoer.main.taskList.controller.ActionSetCardFromCursor;

/**
 * @author Kent
 */

/**
 * Class MyCursorCardAdapter *
 */
public class MyCursorCardAdapter extends CardCursorAdapter {


 Context context;
    private int position = 0;

    public MyCursorCardAdapter(Context context, int position) {
        super(context);
        this.context=context;
        this.position=position;
    }

    public static MyCursorCardAdapter newInstance(Context context, int position) {
        return new MyCursorCardAdapter(context,position);
    }

    @Override
    protected Card getCardFromCursor(final Cursor cursor) {
        ViewHolder viewHolder;

         viewHolder=new ViewHolder();

        // 建立卡片物件
        viewHolder.card = new MyCursorCard(getContext());
        viewHolder.thumb = new CardThumbnailCircle(getContext(), cursor.getInt(0), position);
        viewHolder.card.addCardThumbnail(viewHolder.thumb);

        //Set onClick listener
        viewHolder.card.setOnClickListener(new ActionOnCardClicked(getContext(),cursor));

        // set onLongClick listener;
        viewHolder.card.setOnLongClickListener(
                new ActionOnCardLongClicked(getContext(),cursor,position));

        // 設定卡片內容
        ActionSetCardFromCursor mActionSetCardFromCursor =
                new ActionSetCardFromCursor(getContext(), cursor, viewHolder.card);
        mActionSetCardFromCursor.setIt();

        return viewHolder.card;
    }

    static class ViewHolder{
         MyCursorCard card;
         CardThumbnailCircle thumb;
        CardShadowView cardShadowView;
    }
}

