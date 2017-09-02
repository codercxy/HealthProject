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
import com.nju.android.health.model.data.Step;
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
public class DataStepFragment extends Fragment{

    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";

    private LineChartView chart;
    private LineChartData data;
    private int numberOfPoints = 0;
//    private boolean isSetOffSet = false;


    private List<Step> getByMonth;
    private DbProvider mProvider;
    public DataStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_step, container, false);

        initView(view);

        generateData();

        initLine();
        return view;
    }

    private void initView(View view) {
        chart = (LineChartView) view.findViewById(R.id.data_step_chart);
        chart.setOnValueTouchListener(new DataStepFragment.ValueTouchListener());
    }

    private void generateData() {

        mProvider = new DbProvider();
        mProvider.init(getActivity());

        //getStep data
        getByMonth = mProvider.getStepByMonth();

    }
    private void initLine() {
        chart.offsetLeftAndRight(10);
        if (!chart.isZoomEnabled()) {
            chart.setZoomEnabled(true);
        }


        List<Line> lines = new ArrayList<Line>();

        List<PointValue> values = new ArrayList<>();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        int previousMonth = 0;
        numberOfPoints = getByMonth.size();
        String axisValue;

        if (numberOfPoints > 0) {
            values.add(new PointValue(0, getByMonth.get(0).getNumber() * 1.0f / 1000));
            previousMonth = getMonth(getByMonth.get(0).getDate());
            axisValue = getAxisValue(getByMonth.get(0).getDate(), true);
            axisValues.add(new AxisValue(0).setLabel(axisValue));
        }

        for (int j = 1; j < numberOfPoints; ++j) {
            values.add(new PointValue(j, getByMonth.get(j).getNumber() * 1.0f / 1000));

            if (previousMonth == getMonth(getByMonth.get(j).getDate())) {
                axisValue = getAxisValue(getByMonth.get(j).getDate(), false);
            } else {
                axisValue = getAxisValue(getByMonth.get(j).getDate(), true);
            }
            axisValues.add(new AxisValue(j).setLabel(axisValue));
            previousMonth = getMonth(getByMonth.get(j).getDate());
        }

        Line line = setUpLines(values);

        lines.add(line);

        data = new LineChartData(lines);

        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));

        data.setAxisYLeft(new Axis().setHasLines(false));

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);



        chart.setViewportCalculationEnabled(true);

        //setWidth
        if (numberOfPoints > 9) {
            Viewport viewport = new Viewport(chart.getMaximumViewport());
            viewport.top = 20;
            viewport.bottom = 0;
            viewport.right = numberOfPoints - 0.7f;
            chart.setMaximumViewport(viewport);
            viewport.right = 9;
            chart.setCurrentViewport(viewport);

            chart.setZoomLevel(1, getByMonth.get(0).getNumber(), chart.getMaxZoom() / numberOfPoints);
        } else {
            resetViewport();
            chart.setZoomLevel(1, getByMonth.get(0).getNumber(), chart.getZoomLevel());
        }

        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
    }

    private int getMonth(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date strToDate = sdf.parse(datetime, new ParsePosition(0));
        return strToDate.getMonth();
    }

    private String getAxisValue(String previousValue, boolean isFirst) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date strToDate = sdf.parse(previousValue, new ParsePosition(0));

        SimpleDateFormat newSdf;

        if (isFirst) {
            newSdf = new SimpleDateFormat("MM/dd");
        } else {
            newSdf = new SimpleDateFormat("dd");
        }

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
            Toast.makeText(getActivity(),  String.valueOf((int) (value.getY() * 1000)) + "步", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void clearChart() {
        chart.destroyDrawingCache();
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)

        final Viewport v = new Viewport(chart.getCurrentViewport());
        v.bottom = 0;
        v.top = 20;
        v.left = 0;
        v.right = numberOfPoints - 0.8f;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setZoomEnabled(false);

    }




}
