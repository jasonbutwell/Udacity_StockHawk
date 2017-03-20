package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.Locale;

/**
 * Created by J on 19/03/2017.
 */

public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    // cursor for obtaining data from content provider

    private Cursor cursor;

    // The projection fields we want to get from our query

    private static final String[] STOCK_COLUMN_PROJECTION = {
            Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
            Contract.Quote.COLUMN_HISTORY,
    };

    private Context context = null;
    private int appWidgetId;

    public WidgetViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    // Used to query the content provider to obtain the stock info for the Widget

    private void queryCP() {

        // Needed otherwise we get a permission exception when calling the content provider

        final long token = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(
                Contract.Quote.URI,
                STOCK_COLUMN_PROJECTION,
                null,
                null,
                Contract.Quote.COLUMN_SYMBOL + Contract.SORTORDER_ASCENDING );

        Binder.restoreCallingIdentity(token);
    }

    // Used to set the stock percent colour - Set to green for + and red for - accordingly

    private void setPercentColour(RemoteViews row, String percent) {

        // Required to change the background of the price component (if up or down)
        String priceBackground =  "setBackgroundResource";

        if ( checkValuePositive(percent) )
            row.setInt(R.id.change, priceBackground, R.color.stock_price_up);
        else
            row.setInt(R.id.change, priceBackground, R.color.stock_price_down);
    }

    // Adjust the stock percent string for position value - Insert a plus at the start if positive

    private String adjustPercentageString(String percent) {

        // We need String builder to manipulate
        StringBuilder newPercent = new StringBuilder(percent);

        // If the value is greater than zero it's a +
        if ( checkValuePositive(percent) )
            // Insert the + at the start of the string
            newPercent.insert( 0, "+" );

        // Return string value of the StringBuffer
        return newPercent.toString();
    }

    // Used to check percentage string as a float to determine positive or negative
    private boolean checkValuePositive(String value) {
        return Float.parseFloat(value) > 0;
    }

    // Used to get data from the cursor

    private String getCursorValue(int stringIndex) {
        return cursor.getString(stringIndex);
    }

    @Override
    public void onCreate() {
        queryCP();
    }

    @Override
    public void onDataSetChanged() {
        queryCP();
    }

    @Override
    public void onDestroy() {
        // Releases the cursor when we are done with it
        if ( cursor != null ) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        // returns the count of the cursor or 0 if cursor is null
        return cursor != null ? cursor.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        String currencySymbolUS = "$ ";

        // Check we have data at this view position - If we don't then exit with null
        if (!cursor.moveToPosition(position))
            return null;

        // Grab the Symbol, price and percentage change from the cursor
        String symbol  = getCursorValue(Contract.Quote.POSITION_SYMBOL);
        String price   = getCursorValue(Contract.Quote.POSITION_PRICE);
        String percent = getCursorValue(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        // Obtain the History information
        String history = getCursorValue(Contract.Quote.POSITION_HISTORY);

        // Debugging purposes
        //Log.i("HISTORY",history);

        // Initialise our new view Row - Reusing the layout list_item_quote.xml
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);

        // Format the percentage change to 2 places
        String newPercent = String.format(Locale.US,"%.2f",Float.parseFloat(percent));

        // Set the view fields to the data obtained from the Cursor
        row.setTextViewText(R.id.symbol, symbol);
        row.setTextViewText(R.id.price, currencySymbolUS + price);
        row.setTextViewText(R.id.change, adjustPercentageString( newPercent ));

        // Set the percent change background depending on + or -
        setPercentColour( row, percent );

        // What we do when a row is clicked
        Intent intent = new Intent();
        Bundle extras = new Bundle();

        // Pass in the Symbol and History information here into the Bundle
        extras.putString(WidgetProvider.EXTRA_SYMBOL, symbol);
        extras.putString(WidgetProvider.EXTRA_HISTORY, history);

        // Put the extras into the intent
        intent.putExtras(extras);

        // Set click behaviour
        row.setOnClickFillInIntent(android.R.id.text1, intent);

        // Return the remoteViews row element
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}