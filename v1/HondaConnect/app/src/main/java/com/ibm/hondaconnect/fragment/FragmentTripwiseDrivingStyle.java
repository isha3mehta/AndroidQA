package com.ibm.hondaconnect.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class FragmentTripwiseDrivingStyle extends Fragment implements View.OnClickListener {

    //Graph part
    private TextView mTV_IndicatorExcellent, mTV_IndicatorStar, mTV_IndicatorPerformer, mTV_IndicatorFair, mTV_IndicatorNeeds;
    private RelativeLayout mRL_GraphHeader;
    private TextView mTV_GraphHeader;
    private ImageView mIV_GraphHeader;
    private LinearLayout mParentGraphContainer,mGraphContainer;//complete container of graph, indicator & error tv

    private LinearLayout mOnlyGraphAndIndicator; //ll that contains indicator and graph only
    private TextView mTV_ErrorLoadingScore; //error tv of graph
    private View mViewParent;

    private boolean mTripChartOpened = true;


    //TripList part
    private TextView mTV_Indicator_SNo, mTV_IndicatorStartTime, mTV_IndicatorDuration, mTV_IndicatorDistance;
    private TextView mTV_NoTripFound;
    private RelativeLayout mRL_TripListHeader,mRl_DateContainer;
    private TextView mTV_TripListHeader;
    private ImageView mIV_TripListHeader;
    private LinearLayout mTripListParentContainer; //This contains TV no trip found also
    private LinearLayout mTripListContainer; //This contains List View and header only


    //Date Selection part
    private TextView mTV_SelectedTripDate, mTV_SelectTripDateLabel;
    private ImageView mSelectTripDateIv;
    private String mSelectedTripDate;
    private int mYear, mMonth, mDay;
    private Calendar mCalendar;
    private Button mBtn_GetTrips;

    //Generics
    private TripDetailsActivity mActivity;

    private ProgressDialog mProgressDialog;
    private Dialog mAlertDialog;

    ArrayList<LastDrivingScoreDetail> lastDrivingScoreDetailArrayList = new ArrayList<>();
    String[] arDrivingScoreParameterName = {"Trip-1", "Trip-2", "Trip-3", "Trip-4", "Trip-5"};
    String[] arDrivingScoreParameterName1 = {"Trip-1", "Trip-2", "Trip-3", "Trip-4", "Trip-5"};
    double[] arDrivingScoreValue = {20,40,60,80,100};
    double[] arDrivingScoreValue1 = {100,80,60,40,20};




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (TripDetailsActivity) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TripDetailsActivity activity = (TripDetailsActivity) getActivity();
        mSelectedTripDate = activity.mSelectedTripDate;
        mTripChartOpened = activity.mTripChartOpened;
        mActivity = (TripDetailsActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tripwise_driving_style, container, false);
        initializeUI(v);
        initializeDatePicker();
        mViewParent = v;
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(null != mSelectedTripDate){
            mTV_SelectedTripDate.setText(mSelectedTripDate);
        }
        showHideLastScores();

    }



    private void initializeUI(View view) {

        //Graph Part
        mRL_GraphHeader = (RelativeLayout) view.findViewById(R.id.rl_show_hide_graph);
        mRL_GraphHeader.setOnClickListener(this);
        mIV_GraphHeader = (ImageView) view.findViewById(R.id.iv_last_trips);
        mParentGraphContainer = (LinearLayout) view.findViewById(R.id.container_graph);
        mOnlyGraphAndIndicator = (LinearLayout) view.findViewById(R.id.ll_chart_trip_indicator);
        mGraphContainer = (LinearLayout) view.findViewById(R.id.chart_tripwise);
        mGraphContainer.setOnClickListener(this);

        mTV_ErrorLoadingScore = (TextView) view.findViewById(R.id.no_trip_chart_found);
        mTV_GraphHeader = (TextView) view.findViewById(R.id.tv_header_last_trips);
        mTV_IndicatorExcellent = (TextView) view.findViewById(R.id.indicator_excellent);
        mTV_IndicatorStar = (TextView) view.findViewById(R.id.indicator_star_performer);
        mTV_IndicatorPerformer = (TextView) view.findViewById(R.id.indicator_performer);
        mTV_IndicatorFair = (TextView) view.findViewById(R.id.indicator_fair);
        mTV_IndicatorNeeds = (TextView) view.findViewById(R.id.indicator_needs_improvement);


        //Trip List Part
        mRL_TripListHeader = (RelativeLayout) view.findViewById(R.id.rl_show_hide_triplist);
        mRL_TripListHeader.setOnClickListener(this);
        mRl_DateContainer = (RelativeLayout) view.findViewById(R.id.rl_drivingstyle_selectdate_parent);
        mIV_TripListHeader = (ImageView) view.findViewById(R.id.iv_trip_list);
        mTripListParentContainer = (LinearLayout) view.findViewById(R.id.parentContainerTripList);
        mTripListContainer = (LinearLayout) view.findViewById(R.id.ll_drivingstyle_list_parent);

        mTV_TripListHeader = (TextView) view.findViewById(R.id.tv_header_triplist);
        mTV_Indicator_SNo = (TextView) view.findViewById(R.id.tv_drivingstyle_sno);
        mTV_IndicatorStartTime = (TextView) view.findViewById(R.id.tv_drivingstyle_starttime);
        mTV_IndicatorDuration = (TextView) view.findViewById(R.id.tv_drivingstyle_duration);
        mTV_IndicatorDistance = (TextView) view.findViewById(R.id.tv_drivingstyle_distance);
        mTV_NoTripFound = (TextView) view.findViewById(R.id.no_drivingstyle_found);


        /*mRV_TripList = (RecyclerView) view.findViewById(R.id.rv_drivingstyle_list);
        mRV_TripList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRV_TripList.setLayoutManager(mLayoutManager);
        mRV_TripList.addItemDecoration(new RecyclerViewDevider(mActivity, LinearLayoutManager.VERTICAL));
*/
        //Date selection part

        mTV_SelectedTripDate = (TextView) view.findViewById(R.id.tv_drivingstyle_selectdate);
        mTV_SelectTripDateLabel = (TextView) view.findViewById(R.id.drivingstyle_tv_selectdateLabel);

        mSelectTripDateIv = (ImageView) view.findViewById(R.id.iv_drivingstyle_selectdate);
        mSelectTripDateIv.setOnClickListener(this);

        mBtn_GetTrips = (Button) view.findViewById(R.id.btn_drivingstyle_search);
        mBtn_GetTrips.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRL_GraphHeader) {
            if (mParentGraphContainer.getVisibility() == View.VISIBLE) {
                mParentGraphContainer.setVisibility(View.GONE);
                mTripChartOpened = false;
                mIV_GraphHeader.setImageResource(R.mipmap.arrow_right);
            } else {
                showHideLastScores();
                mParentGraphContainer.setVisibility(View.VISIBLE);
                mTripChartOpened = true;
                mIV_GraphHeader.setImageResource(R.mipmap.arrow_down);
            }
        } else if (v == mRL_TripListHeader) {
            if (mRl_DateContainer.getVisibility() == View.VISIBLE && mBtn_GetTrips.getVisibility() == View.VISIBLE) {
                mRl_DateContainer.setVisibility(View.GONE);
                mIV_TripListHeader.setImageResource(R.mipmap.arrow_right);
                mBtn_GetTrips.setVisibility(View.GONE);
            } else {
                mRl_DateContainer.setVisibility(View.VISIBLE);
                mBtn_GetTrips.setVisibility(View.VISIBLE);
                mIV_TripListHeader.setImageResource(R.mipmap.arrow_down);
            }
        } else if (v == mSelectTripDateIv) {
            showTripDatePicker();
        } else if (v == mBtn_GetTrips) {
            validateSelectedDateAndGetTrips();
        } else if (v == mGraphContainer){
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

    private void showTripDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(mActivity, myTripDateListener, mYear, mMonth, mDay);
        datePicker.show();
    }

    private DatePickerDialog.OnDateSetListener myTripDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setTripwiseDate(year, monthOfYear + 1, dayOfMonth);
        }
    };

    private void setTripwiseDate(int year, int month, int day) {
        String date = year + "-"+ (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) ;
        mTV_SelectedTripDate.setText(date);
        mSelectedTripDate = date;
    }
    // ------------------- END DATE PICKER CODE ------------------//

    //++++++END View Utilities++++++++

    // +++++++START web requests+++++++++


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

        ChartView.loadChart(mActivity, mViewParent, lastDrivingScoreDetailArrayList, R.id.chart_tripwise);


    }

    private void callScoreDetailsActivity(){
      /*  Intent scoreDetailsIntent = new Intent(mActivity, DrivingStyleDetailsActivity.class);
        scoreDetailsIntent.putExtra(iRequestParams.KEY_ANALYTICS_JOB_DETAILS, mJobScoreDetails);
        startActivity(scoreDetailsIntent);*/
    }

    private void validateSelectedDateAndGetTrips(){
        String selectedTripDate = mTV_SelectedTripDate.getText().toString();
        if(null != selectedTripDate && selectedTripDate.trim().length() > 0){
            lastDrivingScoreDetailArrayList.clear();
            for (int i = 0; i < arDrivingScoreParameterName1.length; i++) {

                LastDrivingScoreDetail lastDrivingScoreDetail = new LastDrivingScoreDetail();
                lastDrivingScoreDetail.setDrivingParamValue(arDrivingScoreValue1[i]);
                lastDrivingScoreDetail.setDrivingParamName(arDrivingScoreParameterName1[i].toString());

                lastDrivingScoreDetailArrayList.add(i,lastDrivingScoreDetail);
            }

            ChartView.loadChart(mActivity, mViewParent, lastDrivingScoreDetailArrayList, R.id.chart_tripwise);

        }else{
            showMessage("Please select a date to get trips.", false);
        }
    }

    /*private void showHideTripList(boolean fromSaved) {
        mTripListParentContainer.setVisibility(View.VISIBLE);
        mIV_TripListHeader.setImageResource(R.drawable.arrow_down);
        if (null == mTripListServerResponse) {
            mTripListContainer.setVisibility(View.GONE);
            mTV_NoTripFound.setVisibility(View.VISIBLE);
        } else {
            ArrayList<TripDetails> tripDetailsList = mTripListServerResponse.getTripDetailsArray();
            if (tripDetailsList == null || tripDetailsList.size() < 1) {
                mTripListContainer.setVisibility(View.GONE);
                mTV_NoTripFound.setVisibility(View.VISIBLE);
                dismissProgress();
            } else {

                if(!fromSaved){
                    processTripListForDuration();
                }

                mTripListContainer.setVisibility(View.VISIBLE);
                mTV_NoTripFound.setVisibility(View.GONE);
                mTripListAdapter = (AdapterDrivingStyleTripList) mRV_TripList.getAdapter();

                if (tripDetailsList.size() >= 5) {
                    mRV_TripList.getLayoutParams().height = 700;
                } else {
                    mRV_TripList.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }

                if (null == mTripListAdapter) {
                    mTripListAdapter = new AdapterDrivingStyleTripList(mActivity, tripDetailsList);
                    mTripListAdapter.setOnTripItemClickListener(FragmentTripwiseDrivingStyle.this);
                    mRV_TripList.setAdapter(mTripListAdapter);
                } else {
                    mTripListAdapter.resetTripList(tripDetailsList);
                }
                dismissProgress();
            }
        }
    }*/


    @Override
    public void onDestroyView() {
        TripDetailsActivity activity = (TripDetailsActivity) getActivity();
         activity.mSelectedTripDate = mSelectedTripDate;
        activity.mTripChartOpened = mTripChartOpened;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
