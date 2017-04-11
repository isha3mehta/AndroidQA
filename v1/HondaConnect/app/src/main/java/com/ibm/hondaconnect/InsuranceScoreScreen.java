package com.ibm.hondaconnect;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ibm.hondaconnect.util.AlertUtil;
import com.ibm.hondaconnect.view.MeterGauge;

public class InsuranceScoreScreen extends AppCompatActivity {

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_score);
        /*android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.insurance_score);
        ab.setIcon(R.mipmap.back);*/
        //ActionToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.insurance_score));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
            AlertUtil.getScoreDialog(InsuranceScoreScreen.this).show();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
