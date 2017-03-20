package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.StockDetailActivity;

/**
 * Created by J on 19/03/2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    public static String EXTRA_HISTORY = "_HISTORY";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            Intent svcIntent = new Intent(context, WidgetService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            widgetViews.setRemoteAdapter(R.id.listView, svcIntent);

            Intent clickIntent = new Intent(context, StockDetailActivity.class);
            PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            widgetViews.setPendingIntentTemplate(R.id.listView, clickPI);

            appWidgetManager.updateAppWidget(appWidgetId, widgetViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
