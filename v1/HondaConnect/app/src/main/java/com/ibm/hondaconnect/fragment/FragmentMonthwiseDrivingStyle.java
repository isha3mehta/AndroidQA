package com.ibm.hondaconnect.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.hondaconnect.BaseActivity;
import com.ibm.hondaconnect.R;
import com.ibm.hondaconnect.TripDetailsActivity;
import com.ibm.hondaconnect.TripScoreActivity;
import com.ibm.hondaconnect.model.LastDrivingScoreDetail;
import com.ibm.hondaconnect.util.AlertUtil;
import com.ibm.hondaconnect.view.ChartView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by IBM_ADMIN on 2/22/2017.
 */
public class FragmentMonthwiseDrivingStyle extends Fragment implements View.OnClickListener{

    //Graph part
    private TextView mTV_IndicatorExcellent, mTV_IndicatorStar, mTV_IndicatorPerformer, mTV_IndicatorFair, mTV_IndicatorNeeds;
    private RelativeLayout mRL_GraphHeader;
    private TextView mTV_GraphHeader;
    private ImageView mIV_GraphHeader;
    private LinearLayout mParentGraphContainer,mGraphContainer; //complete container of graph, indicator & error tv
    private LinearLayout mOnlyGraphAndIndicator; //ll that contains indicator and graph only
    private TextView mTV_ErrorLoadingScore; //error tv of graph
    private View mViewParent;

    private boolean mMonthChartOpened = true;

    private TextView mTV_SelectedDrivingMonth, mTV_SelectDrivingMonthLabel;
    private ImageView mSelectDrivingMonthIv;
    private String mSelectedDrivingMonth;
    private String mSelectedDrivingMonthNumber; //This is in format required for API usage "2016/0" 0 is Jan
    private int mYear, mMonth, mDay;
    private Calendar mCalendar;
    private Button mBtn_SearchMonthStyle;

    //Generics
    private BaseActivity mActivity;
    private Handler mUIHandler;
   private Dialog mAlertDialog;

    ArrayList<LastDrivingScoreDetail> lastDrivingScoreDetailArrayList = new ArrayList<>();
    String[] arDrivingScoreParameterName = {"Month-1", "Month-2", "Month-3", "Month-4", "Month-5"};
    String[] arDrivingScoreParameterName1 = {"Month-1", "Month-2", "Month-3", "Month-4", "Month-5"};
    double[] arDrivingScoreValue = {20,40,60,80,100};
    double[] arDrivingScoreValue1 = {100,80,60,40,20};




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TripDetailsActivity activity = (TripDetailsActivity) getActivity();
        mSelectedDrivingMonth = activity.mSelectedDrivingMonthUI;
        mMonthChartOpened = activity.mMonthChartOpened;
        mSelectedDrivingMonthNumber = activity.mSelectedDrivingMonthServer;
        mActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monthwise_driving_style, container, false);
        initializeUI(v);
        initializeDatePicker();
        mViewParent = v;
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(null != mSelectedDrivingMonth){
            mTV_SelectedDrivingMonth.setText(mSelectedDrivingMonth);
        }
        showHideLastScores();
    }



    private void initializeUI(View view) {

        //Graph Part
        mRL_GraphHeader = (RelativeLayout) view.findViewById(R.id.rl_show_hide_graph_month);
        mRL_GraphHeader.setOnClickListener(this);
        mIV_GraphHeader = (ImageView) view.findViewById(R.id.iv_last_trips_month);
        mParentGraphContainer = (LinearLayout) view.findViewById(R.id.container_graph_month);
        mOnlyGraphAndIndicator = (LinearLayout) view.findViewById(R.id.ll_chart_month_indicator);
        mGraphContainer = (LinearLayout) view.findViewById(R.id.chart_monthwise);
        mGraphContainer.setOnClickListener(this);


        mTV_ErrorLoadingScore = (TextView) view.findViewById(R.id.no_month_chart_found);
        mTV_GraphHeader = (TextView) view.findViewById(R.id.tv_header_last_trips_month);
        mTV_IndicatorExcellent = (TextView) view.findViewById(R.id.indicator_excellent_month);
        mTV_IndicatorStar = (TextView) view.findViewById(R.id.indicator_star_performer_month);
        mTV_IndicatorPerformer = (TextView) view.findViewById(R.id.indicator_performer_month);
        mTV_IndicatorFair = (TextView) view.findViewById(R.id.indicator_fair_month);
        mTV_IndicatorNeeds = (TextView) view.findViewById(R.id.indicator_needs_improvement_month);


        //Date selection part
        mTV_SelectedDrivingMonth = (TextView) view.findViewById(R.id.tv_drivingstyle_selectdate_month);
        mTV_SelectDrivingMonthLabel = (TextView) view.findViewById(R.id.drivingstyle_tv_selectdateLabel_month);

        mSelectDrivingMonthIv = (ImageView) view.findViewById(R.id.iv_drivingstyle_selectdate_month);
        mSelectDrivingMonthIv.setOnClickListener(this);

        mBtn_SearchMonthStyle = (Button) view.findViewById(R.id.btn_drivingstyle_search_month);
        mBtn_SearchMonthStyle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRL_GraphHeader) {
            if (mParentGraphContainer.getVisibility() == View.VISIBLE) {
                mParentGraphContainer.setVisibility(View.GONE);
                mMonthChartOpened = false;
                mIV_GraphHeader.setImageResource(R.mipmap.arrow_right);
            } else {
                showHideLastScores();
                mParentGraphContainer.setVisibility(View.VISIBLE);
                mMonthChartOpened = true;
                mIV_GraphHeader.setImageResource(R.mipmap.arrow_down);
            }
        } else if (v == mSelectDrivingMonthIv) {
            showMonthPicker();
        } else if (v == mBtn_SearchMonthStyle) {
            validateSelectedDateAndGetTrips();
        }else if (v == mGraphContainer){
            Intent intentTripScore = new Intent(mActivity, TripScoreActivity.class);
            startActivity(intentTripScore);
        }
    }

    //++++++START View Utilities++++++++
    private void showMessage(final String msg, final boolean popBackOneStep){
        mAlertDialog = AlertUtil.getSingleButtonDialog(mActivity, msg, "OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                if(popBackOneStep){
                    mActivity.finish();
                }
            }
        });
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }



    // ------------------- START MONTH PICKER CODE ------------------//

    private void initializeDatePicker() {
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    private void showMonthPicker() {
        DatePickerDialog monthPicker = new DatePickerDialog(mActivity, AlertDialog.THEME_HOLO_LIGHT, mMonthListener, mYear, mMonth, mDay);
        monthPicker.show();
        hideDayFromMonthPicker(monthPicker);
    }

    private DatePickerDialog.OnDateSetListener mMonthListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setMonthOnUI(year, monthOfYear);
        }
    };

    private void setMonthOnUI(int year, int month) {
        mSelectedDrivingMonthNumber = year+"/"+month;
        String monthName = getMonthName(month+1);
        mSelectedDrivingMonth = monthName + "-" + year;
        mTV_SelectedDrivingMonth.setText(mSelectedDrivingMonth);
    }

    public static String getMonthName(int month) {
        String monthName = "";
        switch (month) {
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
        }
        return monthName;
    }


    public void hideDayFromMonthPicker(DatePickerDialog mMonthPicker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = mMonthPicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }

            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            if (monthSpinnerId != 0) {
                View monthSpinner = mMonthPicker.findViewById(monthSpinnerId);
                if (monthSpinner != null) {
                    monthSpinner.setVisibility(View.VISIBLE);
                }
            }

            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
            if (yearSpinnerId != 0) {
                View yearSpinner = mMonthPicker.findViewById(yearSpinnerId);
                if (yearSpinner != null) {
                    yearSpinner.setVisibility(View.VISIBLE);
                }
            }
        } else { //Older SDK versions
            Field f[] = mMonthPicker.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                    field.setAccessible(true);
                    Object dayPicker = null;
                    try {
                        dayPicker = field.get(mMonthPicker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) dayPicker).setVisibility(View.GONE);
                }

                if (field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner")) {
                    field.setAccessible(true);
                    Object monthPicker = null;
                    try {
                        monthPicker = field.get(mMonthPicker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) monthPicker).setVisibility(View.VISIBLE);
                }

                if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) {
                    field.setAccessible(true);
                    Object yearPicker = null;
                    try {
                        yearPicker = field.get(mMonthPicker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) yearPicker).setVisibility(View.GONE);
                }
            }
        }
    }
    //++++++END View Utilities++++++++


    //++++++START processing methods+++++++++++
    private void showHideLastScores() {
        mParentGraphContainer.setVisibility(View.VISIBLE);
        mIV_GraphHeader.setImageResource(R.mipmap.arrow_down);
        lastDrivingScoreDetailArrayList.clear();
        for (int i = 0; i < arDrivingScoreParameterName.length; i++) {

            LastDrivingScoreDetail lastDrivingScoreDetail = new LastDrivingScoreDetail();
            lastDrivingScoreDetail.setDrivingParamValue(arDrivingScoreValue[i]);
            lastDrivingScoreDetail.setDrivingParamName(arDrivingScoreParameterName[i].toString());

            lastDrivingScoreDetailArrayList.add(i,lastDrivingScoreDetail);
        }

        ChartView.loadChart(mActivity, mViewParent, lastDrivingScoreDetailArrayList, R.id.chart_monthwise);

    }

    private void callScoreDetailsActivity(){
        /*Intent scoreDetailsIntent = new Intent(mActivity, DrivingStyleDetailsActivity.class);
        scoreDetailsIntent.putExtra(iRequestParams.KEY_ANALYTICS_JOB_DETAILS, mJobScoreDetails);
        startActivity(scoreDetailsIntent);*/
    }

    private void validateSelectedDateAndGetTrips(){
        String selectedTripDate = mTV_SelectedDrivingMonth.getText().toString();
        if(null != selectedTripDate && selectedTripDate.trim().length() > 0){
            lastDrivingScoreDetailArrayList.clear();
            for (int i = 0; i < arDrivingScoreParameterName1.length; i++) {

                LastDrivingScoreDetail lastDrivingScoreDetail = new LastDrivingScoreDetail();
                lastDrivingScoreDetail.setDrivingParamValue(arDrivingScoreValue1[i]);
                lastDrivingScoreDetail.setDrivingParamName(arDrivingScoreParameterName1[i].toString());

                lastDrivingScoreDetailArrayList.add(i,lastDrivingScoreDetail);
            }

            ChartView.loadChart(mActivity, mViewParent, lastDrivingScoreDetailArrayList, R.id.chart_monthwise);

        }else{
            showMessage("Please select a date to get trips.", false);
        }
    }
    //---------END processing methods-----------



    @Override
    public void onDestroyView() {
        TripDetailsActivity activity = (TripDetailsActivity) getActivity();
        activity.mSelectedDrivingMonthUI = mSelectedDrivingMonth;
        activity.mSelectedDrivingMonthServer = mSelectedDrivingMonthNumber;
        activity.mMonthChartOpened = mMonthChartOpened;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mSelectedDrivingMonth = null;
        mSelectedDrivingMonthNumber = null;
        super.onDetach();
    }
}
