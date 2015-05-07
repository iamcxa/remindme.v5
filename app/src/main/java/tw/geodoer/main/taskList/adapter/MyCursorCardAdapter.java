/**
 *
 */
package tw.geodoer.main.taskList.adapter;

import android.content.Context;
import android.database.Cursor;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;
import tw.geodoer.main.taskList.cardsui.CardThumbnailCircle;
import tw.geodoer.main.taskList.cardsui.MyCursorCard;
import tw.geodoer.main.taskList.controller.ActionSetCardFromCursor;

/**
 * @author Kent
 */

/**
 * Class MyCursorCardAdapter *
 */
public class MyCursorCardAdapter extends CardCursorAdapter {

    int position;
    CardThumbnailCircle thumb = null;
    ActionSetCardFromCursor mActionSetCardFromCursor = null;

    public MyCursorCardAdapter(Context context, int position) {
        super(context);
        this.position = position;
    }

    @Override
    protected Card getCardFromCursor(final Cursor cursor) {

        // 建立卡片物件
        MyCursorCard card = new MyCursorCard(super.getContext());
        thumb = new CardThumbnailCircle(super.getContext(), cursor.getInt(0), position);
        card.addCardThumbnail(thumb);

        // 設定卡片內容
        mActionSetCardFromCursor =
                new ActionSetCardFromCursor(super.getContext(), cursor, card, position);

        return card;
    }
}