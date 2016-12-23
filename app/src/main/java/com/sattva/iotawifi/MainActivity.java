package com.sattva.iotawifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener
{

    private LineChart mChart;
    Thread testAlgoThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        define_chart();
        test_algo_plot(1);
        start_server();



        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            addEntry(intent.getFloatExtra("x", (float)1.0),intent.getFloatExtra("y", (float)130.0) );

        }
    };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("to_plot_data"));

    }


    public void start_server()
    {
        Intent startSocketServiceIntent = new Intent(MainActivity.this, SocketIntentService.class);
        startService(startSocketServiceIntent);

    }

    public void test_algo_plot(int sample_set)
    {



        csvread CSV = new csvread();
        CSV.csv(sample_set);

        Log.e("MainActivity", "Completed CSVread" + ApplicationUtils.test_input_array[0][1] + ", " + ApplicationUtils.test_input_array[14999][3]);



        //Uncomment this to run the algo and plotting when server is stopped.
        //ApplicationUtils.startMS = System.currentTimeMillis();


        //testAlgoThread = new Thread(new TestAlgoThread());
        //testAlgoThread.start();

    }


    class TestAlgoThread implements Runnable
    {

        public void run()
        {
            test_algorithm();
        }
    }




    public void test_algorithm()
    {


            if(ApplicationUtils.convert_flag == 1)
            {
                Intent startConvertIntent = new Intent(MainActivity.this, ConvertIntentService.class);
                startService(startConvertIntent);
                ApplicationUtils.convert_flag = 0;
            }


    }


    @Override
    protected void onStop()
    {
        super.onStop();

    }


    public void define_chart()
    {   mChart = (LineChart) findViewById(R.id.chartMain);

        mChart.setOnChartValueSelectedListener(this);

        // enable description text
        mChart.getDescription().setEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setAxisMaximum((float)15000);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(90f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    private void addEntry(float x_entry, float y_entry) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }


            Log.e("MainActivity", "x_entry = " + x_entry +", y_entry = " + y_entry );

            data.addEntry(new Entry(x_entry, y_entry), 0);
            data.notifyDataChanged();


            mChart.notifyDataSetChanged();


            mChart.setVisibleXRangeMaximum(15000);

            mChart.moveViewToX(data.getEntryCount());


        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
