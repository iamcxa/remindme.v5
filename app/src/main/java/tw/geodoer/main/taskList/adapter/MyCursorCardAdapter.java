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

    private MyCursorCard card;
    private CardShadowView cardShadowView;
    private CardThumbnailCircle thumb;

    public MyCursorCardAdapter(Context context) {
        super(context);
    }

    public static MyCursorCardAdapter newInstance(Context context) {
        return new MyCursorCardAdapter(context);
    }

    @Override
    protected Card getCardFromCursor(final Cursor cursor) {

        // 建立卡片物件
        card = new MyCursorCard(getContext());
       // card.setShadow(true);

        // Add Thumbnail to card
        thumb = new CardThumbnailCircle(getContext(), cursor.getInt(0));
        card.addCardThumbnail(thumb);

        //Set onClick listener
        card.setOnClickListener(new ActionOnCardClicked(getContext(),cursor));

        // set onLongClick listener;
        card.setOnLongClickListener(new ActionOnCardLongClicked(getContext(),cursor));

        // 設定卡片內容
        ActionSetCardFromCursor mActionSetCardFromCursor =
                new ActionSetCardFromCursor(getContext(), cursor, card);
        mActionSetCardFromCursor.setIt();

        return card;
    }
}

