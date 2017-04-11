package com.ibm.hondaconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by IBM_ADMIN on 2/20/2017.
 */
public class DashboardActivity extends Activity implements View.OnClickListener{

    private ImageView mPerformanceDetailsIv,mPeerComparisonIv,mDrivingAdviceIv,mYourRatingIv,mTripDetailsIv,mContextualAlertsIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_dashboard);
        initializeUI();

    }

    private void initializeUI() {

        mPerformanceDetailsIv = (ImageView) findViewById(R.id.dashboard_iv_performance_details);
        mPeerComparisonIv = (ImageView) findViewById(R.id.dashboard_iv_peer_comparison);
        mDrivingAdviceIv = (ImageView) findViewById(R.id.dashboard_iv_driving_advice);
        mYourRatingIv = (ImageView) findViewById(R.id.dashboard_iv_your_rating);
        mTripDetailsIv = (ImageView) findViewById(R.id.dashboard_iv_trip_details);
        mContextualAlertsIv = (ImageView) findViewById(R.id.dashboard_iv_contextual_alerts);

        mPerformanceDetailsIv.setOnClickListener(this);
        mPeerComparisonIv.setOnClickListener(this);
        mDrivingAdviceIv.setOnClickListener(this);
        mYourRatingIv.setOnClickListener(this);
        mTripDetailsIv.setOnClickListener(this);
        mContextualAlertsIv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dashboard_iv_performance_details:
                Intent intentPerformanceDetails = new Intent(DashboardActivity.this, PerformanceDetailsScreen.class);
                startActivity(intentPerformanceDetails);
                break;
            case R.id.dashboard_iv_peer_comparison:
                Intent intentPeerComparison = new Intent(DashboardActivity.this, PeerComparisonScreen.class);
                startActivity(intentPeerComparison);
                break;
            case R.id.dashboard_iv_driving_advice:
                Intent intentDrivingAdvice = new Intent(DashboardActivity.this, DrivingAdviceScreen.class);
                startActivity(intentDrivingAdvice);
                break;
            case R.id.dashboard_iv_your_rating:
                Intent intentRating = new Intent(DashboardActivity.this, RatingScreen.class);
                startActivity(intentRating);
                break;
            case R.id.dashboard_iv_trip_details:
                Intent intent = new Intent(DashboardActivity.this, TripDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.dashboard_iv_contextual_alerts:
                Intent intentContextualAlerts = new Intent(DashboardActivity.this, ContextualAlertsScreen.class);
                startActivity(intentContextualAlerts);
                break;
        }
    }
}
