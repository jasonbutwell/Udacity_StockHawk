package com.udacity.stockhawk.Utility;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by J on 21/03/2017.
 */

public class Utils {

    private Utils() {}  // private constructor - no instantiation

    public static int getColorFromResource(Context context, int colorResourceId ) {
        return ContextCompat.getColor(context, colorResourceId);
    }

}
