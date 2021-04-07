package com.dvlpr.findme.classes;

import android.content.Context;
import android.content.res.Resources;

public class ScreenInformation {

    Context context;

    public ScreenInformation(Context context) {
        this.context = context;
    }

    public int GetStatusBarHeight(){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public int GetNavigationBarHeight(){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }

        return 0;
    }

}
