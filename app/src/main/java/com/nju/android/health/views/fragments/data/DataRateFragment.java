package com.nju.android.health.views.fragments.data;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.nju.android.health.R;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.providers.DbProvider;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRateFragment extends Fragment implements View.OnClickListener{
    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";


    private TextView tab1, tab2, tab3;
    private List<Pressure> getByDay, getByWeek, getByMonth, currentData;
    private DbProvider mProvider;

    private LineChartView chart;
    private LineChartData data;
    private int numberOfPoints = 0;
//    private boolean isSetOffSet = false;

    public DataRateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_rate, container, false);

        initView(view);

        generateValues();

        initLine(DAY);

        return view;
    }

    private void initView(View view) {
        tab1 = (TextView) view.findViewById(R.id.pressure_tab_p1);
        tab2 = (TextView) view.findViewById(R.id.pressure_tab_p2);
        tab3 = (TextView) view.findViewById(R.id.pressure_tab_p3);


        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);

        chart = (LineChartView) view.findViewById(R.id.data_rate_chart);
        chart.setOnValueTouchListener(new DataRateFragment.ValueTouchListener());
    }

    private void generateValues() {
        mProvider = new DbProvider();
        mProvider.init(getActivity());

        getByDay = mProvider.getPressure(DAY);
        getByWeek = mProvider.getPressure(WEEK);
        getByMonth = mProvider.getPressure(MONTH);

        currentData = new ArrayList<>();
    }

    private void initLine(String datetime) {

        generateData(datetime);

        //可拖动
        if (!chart.isZoomEnabled()) {
            chart.setZoomEnabled(true);
        }

    }

    private void generateData(String datetime) {


        List<Line> lines = new ArrayList<Line>();

        List<PointValue> values = new ArrayList<>();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        int previousDate = 0;

        switch (datetime) {
            case DAY:
                numberOfPoints = getByDay.size();
                currentData = getByDay;
                for (int j = 0; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, getByDay.get(j).getRate()));

                    String axisValue = getAxisValueByDay(getByDay.get(j).getTime());
                    axisValues.add(new AxisValue(j).setLabel(axisValue));
                }
                break;
            case WEEK:
                numberOfPoints = getByWeek.size();
                currentData = getByWeek;
                String axisValue;
                if (numberOfPoints > 0) {
                    values.add(new PointValue(0, getByWeek.get(0).getRate()));
                    previousDate = getDate(getByWeek.get(0).getTime());
                    axisValue = getAxisValue(getByWeek.get(0).getTime(), true);
                    axisValues.add(new AxisValue(0).setLabel(axisValue));
                }


                for (int j = 1; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, getByWeek.get(j).getRate()));

                    if (previousDate == getDate(getByWeek.get(j).getTime())) {
                        axisValue = getAxisValue(getByWeek.get(j).getTime(), false);
                    } else {
                        axisValue = getAxisValue(getByMonth.get(j).getTime(), true);
                    }
                    axisValues.add(new AxisValue(j).setLabel(axisValue));
                    previousDate = getDate(getByWeek.get(j).getTime());
                }
                break;
            case MONTH:
                numberOfPoints = getByMonth.size();
                currentData = getByMonth;
                String axisValue2;
                if (numberOfPoints > 0) {
                    values.add(new PointValue(0, getByMonth.get(0).getRate()));
                    previousDate = getDate(getByMonth.get(0).getTime());
                    axisValue2 = getAxisValue(getByMonth.get(0).getTime(), true);
                    axisValues.add(new AxisValue(0).setLabel(axisValue2));
                }
                for (int j = 1; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, getByMonth.get(j).getRate()));

                    if (previousDate == getDate(getByMonth.get(j).getTime())) {
                        axisValue2 = getAxisValue(getByMonth.get(j).getTime(), false);
                    } else {
                        axisValue2 = getAxisValue(getByMonth.get(j).getTime(), true);
                    }
                    axisValues.add(new AxisValue(j).setLabel(axisValue2));
                    previousDate = getDate(getByMonth.get(j).getTime());
                }
                break;
        }
        Line line = setUpLines(values);

        lines.add(line);

        data = new LineChartData(lines);

        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data.setAxisYLeft(new Axis().setHasLines(false));

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

        chart.setViewportCalculationEnabled(false);

        //setWidth
        if (numberOfPoints > 0) {
            Viewport viewport = new Viewport(chart.getMaximumViewport());
            viewport.top = 160;
            viewport.bottom = 50;
            viewport.right = numberOfPoints - 0.7f;
            chart.setMaximumViewport(viewport);
            viewport.right = 9;
            chart.setCurrentViewport(viewport);

            chart.setZoomLevel(0, currentData.get(0).getHigh(), chart.getMaxZoom() / numberOfPoints);
        } else {
            resetViewport();
            if (numberOfPoints > 0) {
                chart.setZoomLevel(0, currentData.get(0).getHigh(), chart.getMaxZoom() / numberOfPoints);
            }

        }

        /*setUpRange();*/


        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);



    }

    private int getDate(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date strToDate = sdf.parse(datetime, new ParsePosition(0));
        return strToDate.getDate();
    }

    private String getAxisValue(String previousValue, boolean isFirst) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        Date strToDate = sdf.parse(previousValue, new ParsePosition(0));

        SimpleDateFormat newSdf;

        if (isFirst) {
            newSdf = new SimpleDateFormat("dd日HH时");
        } else {
            newSdf = new SimpleDateFormat("HH时");
        }

        return newSdf.format(strToDate);
    }

    private String getAxisValueByDay(String previousValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        Date strToDate = sdf.parse(previousValue, new ParsePosition(0));

        SimpleDateFormat newSdf = new SimpleDateFormat("HH:mm");


        return newSdf.format(strToDate);
    }



    private Line setUpLines(List<PointValue> values) {
        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setFilled(true);

//        line.setHasGradientToTransparent(hasGradientToTransparent);
        /*if (pointsHaveDifferentColor){
            line.setPointColor(ChartUtils.COLORS[(color + 1) % ChartUtils.COLORS.length]);
        }*/
        return line;
    }



    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "心率：" + String.valueOf(value.getY()), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pressure_tab_p1:
                clearChart();
                initLine(DAY);
                break;
            case R.id.pressure_tab_p2:
                clearChart();
                initLine(WEEK);

                break;
            case R.id.pressure_tab_p3:
                clearChart();
                initLine(MONTH);

                break;
            default:
                break;
        }
    }

    private void clearChart() {
        chart.destroyDrawingCache();
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)

        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 50;
        v.top = 160;
        v.left = 0;
        v.right = numberOfPoints - 0.8f;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setZoomEnabled(false);
    }


}
