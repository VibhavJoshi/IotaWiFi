package com.sattva.iotawifi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


public class MainActivity extends AppCompatActivity
{
    public static final int SERVERPORT = 8080;
    private static double DELTA = 0.001;
    private static long sampleCount = -1L;
    private static double mean = 0.0;

    //TextView tvTest;
    private LineChart mChart;
    double inputToGraph;
    Button btnSetDelta, btnSetChannel;
    EditText etFeedDelta, etFeedChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        mChart = (LineChart) findViewById(R.id.chartMain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tvTest = (TextView)findViewById(R.id.tvTextTest);
        etFeedDelta = (EditText)findViewById(R.id.etSetDelta);
        etFeedChannel = (EditText)findViewById(R.id.etSetChannel);

        btnSetDelta = (Button)findViewById(R.id.btnSetDelta);
        btnSetChannel = (Button)findViewById(R.id.btnSetChannel);


        btnSetDelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DELTA = Double.parseDouble(etFeedDelta.getText().toString());

            }
        });

        btnSetChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApplicationUtils.chan_select = Integer.parseInt(etFeedChannel.getText().toString()) - 1;

            }
        });



        mChart = (LineChart) findViewById(R.id.chartMain);

        mChart.setTouchEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.BLACK);


        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
      //add empty data
        mChart.setData(data);
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = mChart.getAxisLeft();

        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(1);
        leftAxis.setAxisMinimum(-1);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


        csvread CSV = new csvread();
        CSV.csv();

        Log.e("MainActivity", "Completed CSVread" + ApplicationUtils.test_input_array[0][0] + ", " + ApplicationUtils.test_input_array[14999][3]);

       Intent startSocketServiceIntent = new Intent(MainActivity.this, SocketIntentService.class);
       startService(startSocketServiceIntent);

        //Intent startConvertServiceIntent = new Intent(MainActivity.this, ConvertIntentService.class);
        //startService(startConvertServiceIntent);




    }






    @Override
    protected void onStop()
    {
        super.onStop();

    }


    private void addEntry(double value) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float)value), 0);
            data.notifyDataChanged();
            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            sampleCount++;
            Log.d("mean", "sampleCount: " + sampleCount);


            if(sampleCount % 500 < 5)
            {
                mean += value;
                Log.d("mean", "< 5 mean: " + mean);


            } else if(sampleCount % 500 == 5) {
                mean /= 5;
                Log.d("mean", "==5 mean: " + mean);
                mChart.getAxisLeft().setAxisMaximum((float)(mean + DELTA));
                mChart.getAxisLeft().setAxisMinimum((float)(mean - DELTA));
            }

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(100);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());


        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.WHITE);
        set.setDrawCircles(false);
        set.setLineWidth(5f);
        set.setDrawValues(false);
        return set;
    }





}
