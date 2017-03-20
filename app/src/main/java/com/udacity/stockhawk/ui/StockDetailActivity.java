package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import static com.udacity.stockhawk.widget.WidgetProvider.EXTRA_HISTORY;

/**
 * Created by J on 11/03/2017.
 */

public class StockDetailActivity extends AppCompatActivity {

    private String historyData = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_details);

        TextView tv = (TextView) findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();

        // Show the history data obtained in a list view for now.

        if ( extras != null ) {
            historyData = getIntent().getExtras().getString(EXTRA_HISTORY);

            tv.setText(historyData);
        }
    }
}
