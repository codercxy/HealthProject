package com.nju.android.health.views.activities.next;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nju.android.health.R;
import com.nju.android.health.utils.DemoBase;
import com.nju.android.health.utils.MyMarkerView;
import com.nju.android.health.utils.RadarMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Chart_1_Activity extends DemoBase{


    private LineChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_1_);

        mChart = (LineChart) findViewById(R.id.num_1_chart);
        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // add data
        setData(100, 30);


        mChart.invalidate();
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
//        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setTextColor(Color.rgb(0, 0, 0));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setAxisMaximum(getTimesnight());
        xAxis.setAxisMinimum(getTimesmorning());
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
//        leftAxis.setTextColor(Color.rgb(255, 192, 56));
        leftAxis.setTextColor(Color.rgb(0, 0, 0));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setData(int count, float range) {

        // now in hours
        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        Log.e("currentTime", String.valueOf(now));

        ArrayList<Entry> values = new ArrayList<Entry>();

        float from = now;

        // count = hours
        float to = now + count;

        float y = getRandom(range, 50);
        values.add(new Entry(from, y));

        y = getRandom(range, 50);
        values.add(new Entry(from+4, y));

        y = getRandom(range, 50);
        values.add(new Entry(from+6, y));

        // increment by 1 hour
        /*for (float x = from; x < to; x++) {

            float y = getRandom(range, 50);
            values.add(new Entry(x, y)); // add one entry per hour
        }*/
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);


        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2.5f);
        set1.setDrawCircles(true);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(ColorTemplate.getHoloBlue());
        set1.setDrawCircleHole(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // create a data object with the datasets
        dataSets.add(set1);
        dataSets.add(setDataStandard());

        LineData data = new LineData(dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
        ;
    }
    public LineDataSet setDataStandard() {
        float from = getTimesmorning();
        ArrayList<Entry> values = new ArrayList<Entry>();
        float to = from+24;

        int index = 0;
        for (float x = from; x <= to; x++) {
            float y =  30 * (float)Math.sin(Math.PI * index / 4) + 80;
            values.add(new Entry(x, y)); // add one entry per hour
            index++;
        }
        LineDataSet set2 = new LineDataSet(values, "DataSet 2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.rgb(255, 0, 0));
        set2.setValueTextColor(Color.rgb(255, 0, 0));
        set2.setLineWidth(2.5f);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.rgb(255, 0, 0));
        set2.setHighLightColor(Color.rgb(255, 0, 0));
        set2.setDrawCircleHole(false);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        return set2;
    }

    public static float getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
//        return (float) (cal.getTimeInMillis()/1000);
        return TimeUnit.MILLISECONDS.toHours(cal.getTimeInMillis());
    }
    public static float getTimesnight(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
//        return (float) (cal.getTimeInMillis()/1000);
        return TimeUnit.MILLISECONDS.toHours(cal.getTimeInMillis());
    }

}
