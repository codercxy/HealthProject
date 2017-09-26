package com.nju.android.health.views.fragments.data;


import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import lecho.lib.hellocharts.view.PreviewLineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataLineFragment extends Fragment implements View.OnClickListener{

    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";

    private TextView tab1, tab2, tab3;
    private List<Pressure> getByDay, getByWeek, getByMonth, currentData;
    private DbProvider mProvider;

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 2;
    private int numberOfPoints = 0;
//    private boolean isSetOffSet = false;


    public DataLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_data_line, container, false);

        initView(view);

        chart = (LineChartView) view.findViewById(R.id.data_line_chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        generateValues();

        clearChart();
        initLine(DAY);

        clearChart();
        initLine(DAY);
        //resetViewport();

        return view;
    }
    private void initView(View view) {
        tab1 = (TextView) view.findViewById(R.id.pressure_tab_p1);
        tab2 = (TextView) view.findViewById(R.id.pressure_tab_p2);
        tab3 = (TextView) view.findViewById(R.id.pressure_tab_p3);


        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
    }



    private void initLine(String datetime) {

        generateData(datetime);

        //可拖动
        if (!chart.isZoomEnabled()) {
            chart.setZoomEnabled(true);
        }

    }

    private void generateValues() {

        mProvider = new DbProvider();
        mProvider.init(getActivity().getApplicationContext());

        getByDay = mProvider.getPressure(DAY);
        getByWeek = mProvider.getPressure(WEEK);
        getByMonth = mProvider.getPressure(MONTH);

        mProvider.shutdown();
        currentData = new ArrayList<>();
        /*for (int i = 0; i < numberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }*/
    }

    private void generateData(String datetime) {


        List<Line> lines = new ArrayList<Line>();

        List<PointValue> values_high = new ArrayList<PointValue>();
        List<PointValue> values_low = new ArrayList<>();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        int previousDate = 0;


        switch (datetime) {
            case DAY:
                numberOfPoints = getByDay.size();
                currentData = getByDay;
                String axisValue_day;
                for (int j = 0; j < numberOfPoints; ++j) {
                    values_high.add(new PointValue(j, getByDay.get(j).getHigh()));
                    values_low.add((new PointValue(j, getByDay.get(j).getLow())));

                    axisValue_day = getAxisValueByDay(getByDay.get(j).getTime());
                    axisValues.add(new AxisValue(j).setLabel(axisValue_day));
                }
                break;
            case WEEK:
                numberOfPoints = getByWeek.size();
                currentData = getByWeek;
                String axisValue;
                if (numberOfPoints > 0) {
                    values_high.add(new PointValue(0, getByWeek.get(0).getHigh()));
                    values_low.add((new PointValue(0, getByWeek.get(0).getLow())));
                    previousDate = getDate(getByWeek.get(0).getTime());
                    axisValue = getAxisValue(getByWeek.get(0).getTime(), true);
                    axisValues.add(new AxisValue(0).setLabel(axisValue));
                }


                for (int j = 1; j < numberOfPoints; ++j) {
                    values_high.add(new PointValue(j, getByWeek.get(j).getHigh()));
                    values_low.add((new PointValue(j, getByWeek.get(j).getLow())));

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
                    values_high.add(new PointValue(0, getByMonth.get(0).getHigh()));
                    values_low.add((new PointValue(0, getByMonth.get(0).getLow())));
                    previousDate = getDate(getByMonth.get(0).getTime());
                    axisValue2 = getAxisValue(getByMonth.get(0).getTime(), true);
                    axisValues.add(new AxisValue(0).setLabel(axisValue2));
                }
                for (int j = 1; j < numberOfPoints; ++j) {
                    values_high.add(new PointValue(j, getByMonth.get(j).getHigh()));
                    values_low.add((new PointValue(j, getByMonth.get(j).getLow())));

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
        Line first = setUpLines(values_high, 0);
        Line second = setUpLines(values_low, 1);

        //filled Areas
        first.setFilled(true);
        second.setFilled(true);

        lines.add(first);
        lines.add(second);


        data = new LineChartData(lines);

        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data.setAxisYLeft(new Axis().setHasLines(false));

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

        chart.setViewportCalculationEnabled(false);

        //setWidth
        if (numberOfPoints > 9) {
            Viewport viewport = new Viewport(chart.getMaximumViewport());
            viewport.top = 180;
            viewport.bottom = 50;
            viewport.right = numberOfPoints - 0.7f;
            chart.setMaximumViewport(viewport);
            viewport.right = 9;
            chart.setCurrentViewport(viewport);

            chart.setZoomLevel(0, currentData.get(0).getHigh(), chart.getMaxZoom() / numberOfPoints);
        } else {
            resetViewport();
            if (numberOfPoints > 0) {
                chart.setZoomLevel(0, currentData.get(0).getHigh(), chart.getZoomLevel());
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



    private Line setUpLines(List<PointValue> values, int color) {
        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[color]);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);

//        line.setHasGradientToTransparent(hasGradientToTransparent);
        /*if (pointsHaveDifferentColor){
            line.setPointColor(ChartUtils.COLORS[(color + 1) % ChartUtils.COLORS.length]);
        }*/
        return line;
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "高压：" + String.valueOf(currentData.get(pointIndex).getHigh()) +
                    "，低压：" + String.valueOf(currentData.get(pointIndex).getLow()), Toast.LENGTH_SHORT).show();
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

        final Viewport v = new Viewport(chart.getCurrentViewport());
        v.bottom = 50;
        v.top = 180;
        v.left = 0;
        v.right = numberOfPoints - 0.8f;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setZoomEnabled(true);
    }

    /*private void setUpRange() {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        tempViewport.top = 130;
        tempViewport.bottom = 110;

        chart.setCurrentViewport(tempViewport);

        tempViewport.top = 80;
        tempViewport.bottom = 60;
        chart.setCurrentViewport(tempViewport);


    }*/

}
