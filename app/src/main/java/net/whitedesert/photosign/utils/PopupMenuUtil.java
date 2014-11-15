package net.whitedesert.photosign.utils;

import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.PopupMenu;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 11/14/14.
 */
public final class PopupMenuUtil {

    private PopupMenuUtil() {
        throw new AssertionError();
    }

    public static PopupMenu initMenu(View anchor, int menuId) {
        final ContextThemeWrapper themeWrapper = new ContextThemeWrapper(anchor.getContext(), R.style.PopupMenu);

        final PopupMenu menu = new PopupMenu(themeWrapper, anchor);

        menu.getMenuInflater().inflate(menuId, menu.getMenu());

        return menu;

    }
}
