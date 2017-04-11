package com.ibm.hondaconnect;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibm.hondaconnect.util.AlertUtil;
import com.ibm.hondaconnect.view.MeterGauge;

public class DrivingAdviceScreen extends AppCompatActivity {

    private TextView more,less, advice2, advice3 ;

    private Menu mMenu;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driving_advice);
        /*android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.driving_advice);
        ab.setIcon(R.mipmap.back);*/
        //ActionToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.driving_advice));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        more = (TextView) findViewById(R.id.more);
        less = (TextView) findViewById(R.id.less);
        advice2 = (TextView) findViewById(R.id.advice2);
        advice3 = (TextView) findViewById(R.id.advice3);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.GONE);
                less.setVisibility(View.VISIBLE);
                advice2.setVisibility(View.VISIBLE);
                advice3.setVisibility(View.VISIBLE);
            }
        });
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.VISIBLE);
                less.setVisibility(View.GONE);
                advice2.setVisibility(View.GONE);
                advice3.setVisibility(View.GONE);
            }
        });
      //  setProgressBar();
        setMeterBar();
    }

    private void setMeterBar() {
        MeterGauge meter    = (MeterGauge) findViewById(R.id.meter);
            meter.setValue(getInterpretationValueInAngle(90));
        meter.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
    }

    private float getInterpretationValueInAngle(double value) {
        float val = (float)(-90 + (1.80 * value));//angle is 180...our value are between are in 0-100.
        return val;
    }

    /*private void setProgressBar() {

        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        if(android.os.Build.VERSION.SDK_INT >= 11){
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress()+80);
            progressAnimator.setDuration(1500);
            progressAnimator.setInterpolator(new LinearInterpolator());
            progressAnimator.start();
        }
        else
            progressBar.setProgress(progressBar.getProgress()+80);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        this.mMenu = menu;
        MenuItem action_info = mMenu.findItem(R.id.action_info).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        else if(menuItem.getItemId() == R.id.action_info) {
            AlertUtil.getScoreDialog(DrivingAdviceScreen.this).show();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
