package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import static com.udacity.stockhawk.R.id.symbol;
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

        TextView symbolTV = (TextView) findViewById(symbol);
        TextView historyTV = (TextView) findViewById(R.id.history);

        // Obtain the bundle from the Intent passed in
        Bundle extras = getIntent().getExtras();

        // Show the history data obtained in a list view for now.

        // Check to see if the Bundle is null first, before attempting to retrieve data from it
        if ( extras != null ) {

            String symbol = "", historyData = "";

            historyData = extras.getString(EXTRA_HISTORY);
            symbol = extras.getString(EXTRA_SYMBOL);

            symbolTV.setText(symbol);
            historyTV.setText(historyData);
        }
    }
}
