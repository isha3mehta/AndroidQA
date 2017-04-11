package com.ibm.hondaconnect.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by IBM_ADMIN on 2/22/2017.
 */
public class FragmentDaywiseDrivingStyle extends Fragment implements View.OnClickListener {


    //Graph part
    private TextView mTV_IndicatorExcellent, mTV_IndicatorStar, mTV_IndicatorPerformer, mTV_IndicatorFair, mTV_IndicatorNeeds;
    private RelativeLayout mRL_GraphHeader; //Header like last 5 days score
    private TextView mTV_GraphHeader; //text line last 5 days score
    private ImageView mIV_GraphHeader; //Arrow on header
    private LinearLayout mParentGraphContainer,mGraphContainer; //complete container of graph, indicator & error tv
    private LinearLayout mOnlyGraphAndIndicator; //ll that contains indicator and graph only
    private TextView mTV_ErrorLoadingScore; //error tv of graph
    private View mViewParent;


    private TextView mTV_SelectedDrivingDate, mTV_SelectDrivingDateLabel;
    private ImageView mSelectDrivingDateIv;
    private String mSelectedDrivingDate;
    private int mYear, mMonth, mDay;
    private Calendar mCalendar;
    private Button mBtn_SearchDayStyle;

    //Generics
    private BaseActivity mActivity;
    private Handler mUIHandler;
    private Dialog mAlertDialog;

    ArrayList<LastDrivingScoreDetail> lastDrivingScoreDetailArrayList = new ArrayList<>();
    String[] arDrivingScoreParameterName = {"Day-1", "Day-2", "Day-3", "Day-4", "Day-5"};
    String[] arDrivingScoreParameterName1 = {"Day-1", "Day-2", "Day-3", "Day-4", "Day-5"};
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
        mSelectedDrivingDate = activity.mSelectedDrivingDate;
        mActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daywise_driving_style, container, false);
        initializeUI(v);
        initializeDatePicker();
        mViewParent = v;
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(null != mSelectedDrivingDate){
            mTV_SelectedDrivingDate.setText(mSelectedDrivingDate);
        }
        showHideLastScores();
    }



    private void initializeUI(View view) {

        //Graph Part
        mRL_GraphHeader = (RelativeLayout) view.findViewById(R.id.rl_show_hide_graph_day);
        mRL_GraphHeader.setOnClickListener(this);
        mIV_GraphHeader = (ImageView) view.findViewById(R.id.iv_last_trips_day);
        mParentGraphContainer = (LinearLayout) view.findViewById(R.id.container_graph_day);
        mOnlyGraphAndIndicator = (LinearLayout) view.findViewById(R.id.ll_chart_indicator);
        mGraphContainer = (LinearLayout) view.findViewById(R.id.chart_daywise);
        mGraphContainer.setOnClickListener(this);


        mTV_ErrorLoadingScore = (TextView) view.findViewById(R.id.no_day_chart_found);
        mTV_GraphHeader = (TextView) view.findViewById(R.id.tv_header_last_trips_day);
        mTV_IndicatorExcellent = (TextView) view.findViewById(R.id.indicator_excellent_day);
        mTV_IndicatorStar = (TextView) view.findViewById(R.id.indicator_star_performer_day);
        mTV_IndicatorPerformer = (TextView) view.findViewById(R.id.indicator_performer_day);
        mTV_IndicatorFair = (TextView) view.findViewById(R.id.indicator_fair_day);
        mTV_IndicatorNeeds = (TextView) view.findViewById(R.id.indicator_needs_improvement_day);


        //Date selection part
        mTV_SelectedDrivingDate = (TextView) view.findViewById(R.id.tv_drivingstyle_selectdate_day);
        mTV_SelectDrivingDateLabel = (TextView) view.findViewById(R.id.drivingstyle_tv_selectdateLabel_day);

        mSelectDrivingDateIv = (ImageView) view.findViewById(R.id.iv_drivingstyle_selectdate_day);
        mSelectDrivingDateIv.setOnClickListener(this);

        mBtn_SearchDayStyle = (Button) view.findViewById(R.id.btn_drivingstyle_search_day);
        mBtn_SearchDayStyle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRL_GraphHeader) {
            if (mParentGraphContainer.getVisibility() == View.VISIBLE) {
                mParentGraphContainer.setVisibility(View.GONE);
                mIV_GraphHeader.setImageResource(R.mipmap.arrow_right);
            } else {
                showHideLastScores();
                mParentGraphContainer.setVisibility(View.VISIBLE);
                mIV_GraphHeader.setImageResource(R.mipmap.arrow_down);
            }
        } else if (v == mSelectDrivingDateIv) {
            showDrivingDatePicker();
        } else if (v == mBtn_SearchDayStyle) {
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


    // ------------------- START TRIP DATE PICKER CODE ------------------//

    private void initializeDatePicker() {
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    private void showDrivingDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(mActivity, myDrivingDateListener, mYear, mMonth, mDay);
        datePicker.show();
    }

    private DatePickerDialog.OnDateSetListener myDrivingDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDrivingDate(year, monthOfYear + 1, dayOfMonth);
        }
    };

    private void setDrivingDate(int year, int month, int day) {
        String date = year + "-"+ (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) ;
        mTV_SelectedDrivingDate.setText(date);
        mSelectedDrivingDate = date;
    }
    // ------------------- END DATE PICKER CODE ------------------//

    //++++++END View Utilities++++++++



    //++++++START processing methods+++++++++++
    private void callScoreDetailsActivity(){

        /*Intent scoreDetailsIntent = new Intent(mActivity, DrivingStyleDetailsActivity.class);
        scoreDetailsIntent.putExtra(iRequestParams.KEY_ANALYTICS_JOB_DETAILS, mJobScoreDetails);
        startActivity(scoreDetailsIntent);*/
    }

    private void validateSelectedDateAndGetTrips(){
        String selectedTripDate = mTV_SelectedDrivingDate.getText().toString();
        if(null != selectedTripDate && selectedTripDate.trim().length() > 0){
            lastDrivingScoreDetailArrayList.clear();
            for (int i = 0; i < arDrivingScoreParameterName1.length; i++) {

                LastDrivingScoreDetail lastDrivingScoreDetail = new LastDrivingScoreDetail();
                lastDrivingScoreDetail.setDrivingParamValue(arDrivingScoreValue1[i]);
                lastDrivingScoreDetail.setDrivingParamName(arDrivingScoreParameterName1[i].toString());

                lastDrivingScoreDetailArrayList.add(i,lastDrivingScoreDetail);
            }

            ChartView.loadChart(mActivity, mViewParent, lastDrivingScoreDetailArrayList, R.id.chart_daywise);

        }else{
            showMessage("Please select a date to get trips.", false);
        }
    }

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

        ChartView.loadChart(mActivity, mViewParent, lastDrivingScoreDetailArrayList, R.id.chart_daywise);

    }

    //---------END processing methods-----------



    @Override
    public void onDestroyView() {
        TripDetailsActivity activity = (TripDetailsActivity) getActivity();
        activity.mSelectedDrivingDate = mSelectedDrivingDate;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mSelectedDrivingDate = null;
         super.onDetach();
    }

}
