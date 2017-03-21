package com.udacity.stockhawk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.Utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.udacity.stockhawk.widget.WidgetProvider.EXTRA_HISTORY;
import static com.udacity.stockhawk.widget.WidgetProvider.EXTRA_SYMBOL;

/**
 * Created by J on 11/03/2017.
 */

public class StockDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_details);

        // Obtain the bundle from the Intent passed in
        Bundle extras = getIntent().getExtras();

        String symbol = "", historyData = "";

        // Check to see if the Bundle is null first, before attempting to retrieve data from it
        if ( extras != null ) {

            historyData = extras.getString(EXTRA_HISTORY);
            symbol = extras.getString(EXTRA_SYMBOL);

            displayChart(historyData, symbol);

            // Debug
            //Log.i("HISTORY",historyData);
        }
        else
            finish();
            // If we didn't get the data to display in the chart, close the activity
    }

    private void displayChart(String historyData, String stockSymbol) {

        LineChart chart = (LineChart) findViewById(R.id.historyChart);  // the UI id of line chart
        List<Entry> entries = new ArrayList<>();    // entries for our X, and Y to plot the chart

        final ArrayList<String> stockDates = new ArrayList<>(); // used for stock price dates

        String[] stockHistory = historyData.split("\n");    // break up entries by the newline

        int timeCount = 0;  // used for our X axis and as a way to access the dates

        for ( String historyDataItem : stockHistory ) {
            String[] data = historyDataItem.split(", ");    // split into 2 items for time and value

            // add entries for chart, in this case - add stock price over time
            entries.add(new Entry( timeCount++, Float.parseFloat(data[1])) );   // add 2nd part as the stock prices
            stockDates.add(data[0]);    // add the first part of array to our dates
        }

        Context context = getApplicationContext();  // Required for our color resource Utility method

        // Build line chart
        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.legend_chart_stock_price));
        LineData lineData = new LineData(dataSet);

        Legend legend = chart.getLegend();
        legend.setTextColor( Utils.getColorFromResource(context,R.color.chart_legend_color) );
        dataSet.setColor( Utils.getColorFromResource(context,R.color.chart_dataset_color) );
        dataSet.setCircleColor( Utils.getColorFromResource(context,R.color.chart_dataset_circlecolor) );
        dataSet.setLineWidth(1f);
        dataSet.setFillAlpha(10);
        dataSet.setFillColor( Utils.getColorFromResource(context,R.color.chart_dataset_fillcolor) );
        dataSet.setDrawCircles(true);

        chart.setData(lineData);

        Description description = new Description();
        description.setText(stockSymbol + " - " + getString(R.string.label_chart_stock_price));
        description.setTextColor( Utils.getColorFromResource(context,R.color.chart_description_color) );
        description.setTextSize(10f);
        chart.setDescription(description);

        XAxis XAxis = chart.getXAxis();
        XAxis.setValueFormatter(new IAxisValueFormatter() {

            Calendar calendar = Calendar.getInstance();

            // Here we are using a workaround to display dates for our X time axis

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                // We use the calendar to convert the millis into dates

                calendar.setTimeInMillis((long) Float.parseFloat(stockDates.get((int)Math.floor(value))));

                int mYear = calendar.get(Calendar.YEAR) - 2000; // Just want the last 2 digits
                int mMonth = calendar.get(Calendar.MONTH) + 1;  // add 1 as Jan = 0
                int mDay = calendar.get(Calendar.DAY_OF_MONTH); // obtain day of month

                return mDay+"/"+mMonth+"/"+mYear;   // build the date - i.e: 1/3/16
            }
        });

        // change the X, and Y left and Right axis colours

        XAxis.setTextColor ( Utils.getColorFromResource(context,R.color.chart_xaxis_color) );

        YAxis YAxisL = chart.getAxis(YAxis.AxisDependency.LEFT);
        YAxis YAxisR = chart.getAxis(YAxis.AxisDependency.RIGHT);
        YAxisL.setTextColor ( Utils.getColorFromResource(context,R.color.chart_yaxis_left_color) );
        YAxisR.setTextColor ( Utils.getColorFromResource(context,R.color.chart_yaxisright_color) );

        // refresh the chart
        chart.invalidate();
    }
}
