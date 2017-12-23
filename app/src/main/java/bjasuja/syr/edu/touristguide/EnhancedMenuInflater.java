package bjasuja.syr.edu.touristguide;

import android.graphics.Color;
import android.support.v4.internal.view.SupportMenuItem;
;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EnhancedMenuInflater {

    public static void inflate(MenuInflater inflater, Menu menu, boolean forceVisible) {
        inflater.inflate(R.menu.bottom_toolbar, menu);

        if (!forceVisible) {
            return;
        }

        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            // check if app:showAsAction = "ifRoom"
            if (((MenuItemImpl) item).requestsActionButton()) {
                item.setShowAsAction(SupportMenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
    }
}
