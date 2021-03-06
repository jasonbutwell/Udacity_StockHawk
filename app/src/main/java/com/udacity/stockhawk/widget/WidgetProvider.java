package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailActivity;

/**
 * Created by J on 19/03/2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    public static String EXTRA_SYMBOL  = "_SYMBOL";
    public static String EXTRA_HISTORY = "_HISTORY";

    public static String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int[] appWidgetIDs;

        if ( intent.getAction().equals(ACTION_UPDATE) ) {

            // Debugging
            //Log.i("WIDGETUPDATE","UPDATED!");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetIDs = appWidgetManager.getAppWidgetIds( new ComponentName( context, getClass() ) );

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.listView);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            Intent svcIntent = new Intent(context, WidgetService.class);
            svcIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            widgetViews.setRemoteAdapter(R.id.listView, svcIntent);
            widgetViews.setEmptyView(R.id.listView, R.id.listViewEmpty);

            // We have added the ability to launch the Stock Hawk app if the widget title bar is clicked

            Intent clickAppIntent = new Intent(context, MainActivity.class);
            Intent clickIntent = new Intent(context, StockDetailActivity.class);

            PendingIntent appPI = PendingIntent.getActivity(context, 0, clickAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            widgetViews.setOnClickPendingIntent(R.id.StockHawkTitle,appPI);
            widgetViews.setPendingIntentTemplate(R.id.listView, clickPI);

            appWidgetManager.updateAppWidget(appWidgetId, widgetViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
