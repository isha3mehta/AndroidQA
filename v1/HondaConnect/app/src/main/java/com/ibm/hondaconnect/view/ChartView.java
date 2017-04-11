package com.ibm.hondaconnect.view;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.ibm.hondaconnect.R;
import com.ibm.hondaconnect.model.LastDrivingScoreDetail;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

/**
 * Created by IBM_ADMIN on 2/22/2017.
 */
public class ChartView {

    public static void loadChart(Activity activityCtx, View view, ArrayList<LastDrivingScoreDetail> lastDrivingScoreDetailsArray, int resource){

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Trip Series to the dataset
        for (int i = 0; i <lastDrivingScoreDetailsArray.size(); i++) {
            XYSeries trip1Series = new XYSeries("");
            trip1Series.add(i, lastDrivingScoreDetailsArray.get(i).getDrivingParamValue());
            dataset.addSeries(trip1Series);
        }

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);
        multiRenderer.setXLabels(0);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(100);
        //multiRenderer.setYAxisMax(6);
        multiRenderer.setShowLabels(true, false);
        //multiRenderer.setYLabels(0);
        /***
         * Customizing graphs
         */
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setXLabelsColor(activityCtx.getResources().getColor(R.color.White));
        //setting zoom buttons visiblity
        multiRenderer.setZoomButtonsVisible(false);
        //setting pan enablity which uses graph to move on both axis
        multiRenderer.setPanEnabled(false, false);
        //setting click false on graphnhi
        multiRenderer.setClickEnabled(false);
        //setting zoom to false on both axis
        multiRenderer.setZoomEnabled(false, false);
        //setting lines to display on y axis
        multiRenderer.setShowGridY(false);
        //setting lines to display on x axis
        multiRenderer.setShowGridX(true);
        //setting legend to fit the screen sizegraph ka screen shot
        // multiRenderer.setFitLegend(true);
        //setting displaying line on grid
        multiRenderer.setShowGrid(true);
        multiRenderer.setXAxisColor(activityCtx.getResources().getColor(R.color.White));
        multiRenderer.setYAxisColor(activityCtx.getResources().getColor(R.color.Black));
        multiRenderer.setGridColor(activityCtx.getResources().getColor(R.color.Black));
        //setting zoom to false
        multiRenderer.setZoomEnabled(false);
        //setting external zoom functions to false
        multiRenderer.setExternalZoomEnabled(false);
        //setting displaying lines on graph to be formatted(like using graphics)
        multiRenderer.setAntialiasing(true);
        //setting to in scroll to false
        multiRenderer.setInScroll(false);
        //setting to set legend height of the graph
        //multiRenderer.setLegendHeight(30);
        //setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        //setting y axis label to align
        //multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        //setting text style
         multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
       // multiRenderer.setTextTypeface(FontUtils.getRoboTypeFace(activityCtx, Typeface.NORMAL));
        //setting no of values to display in y axis
        // multiRenderer.setYLabels(10);
        // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here
        // multiRenderer.setYAxisMax(4000);
        //setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(-0.9);
        //setting max values to be display in x axis
        multiRenderer.setXAxisMax(5);
        //setting bar size or space between two bars
        multiRenderer.setBarSpacing(-0.65);
        //Setting background color of the graph to transparent
        multiRenderer.setBackgroundColor(activityCtx.getResources().getColor(R.color.Black));
        //Setting margin color of the graph to transparent
        multiRenderer.setMarginsColor(activityCtx.getResources().getColor(R.color.Black));
        //multiRenderer.setApplyBackgroundColor(true);

        //setting the margin size for the graph in the order right, top, left, bottom
        multiRenderer.setMargins(new int[]{15, 60, 160, 15});
        multiRenderer.setXLabelsPadding(16);
        /*if(resource == R.id.chart_daywise){
            multiRenderer.setMargins(new int[]{15, 60, 160, 15});
            multiRenderer.setXLabelsPadding(44);
        }else if(resource == R.id.chart_monthwise){
            multiRenderer.setMargins(new int[]{15, 60, 120, 15});
            multiRenderer.setXLabelsPadding(25);
        }else*/ if(resource == R.id.chart_monthwise){
            multiRenderer.setXLabelsPadding(28);
        }

        for (int i = 0; i < lastDrivingScoreDetailsArray.size(); i++) {
            multiRenderer.addXTextLabel(i, lastDrivingScoreDetailsArray.get(i).getDrivingParamName());
        }

        for (int i = 0; i < lastDrivingScoreDetailsArray.size(); i++) {
            XYSeriesRenderer tripRenderer = new XYSeriesRenderer();
            setColor(activityCtx, lastDrivingScoreDetailsArray.get(i).getDrivingParamValue(), tripRenderer);
            tripRenderer.setFillPoints(true);
            tripRenderer.setLineWidth(2);

            multiRenderer.addSeriesRenderer(tripRenderer);
        }

        multiRenderer.setShowGrid(true);
        multiRenderer.setShowLegend(false);
        //this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) view.findViewById(resource);
        //remove any views before u paint the chart
        chartContainer.removeAllViews();
        //drawing bar chart
        view = ChartFactory.getBarChartView(activityCtx, dataset, multiRenderer, BarChart.Type.STACKED);

        //adding the view to the linearlayout
        chartContainer.addView(view);
    }

    private static void setColor(Activity activityCtx,double percentage, XYSeriesRenderer tripRenderer) {
        if (percentage <= 20) {
            tripRenderer.setColor(ContextCompat.getColor(activityCtx, R.color.driving_performance_red));
        } else if (percentage <= 40) {
            tripRenderer.setColor(ContextCompat.getColor(activityCtx, R.color.driving_performance_orange));
        } else if (percentage <= 60) {
            tripRenderer.setColor(ContextCompat.getColor(activityCtx, R.color.driving_performance_yellow));
        } else if (percentage <= 80) {
            tripRenderer.setColor(ContextCompat.getColor(activityCtx, R.color.driving_performance_parrot));
        } else if (percentage <= 100) {
            tripRenderer.setColor(ContextCompat.getColor(activityCtx, R.color.msilgreen));
        }
    }
}
