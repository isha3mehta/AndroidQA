package com.ibm.hondaconnect;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ibm.hondaconnect.util.Constants;

public class AlertsDetailsScreen extends AppCompatActivity {

    private Menu mMenu;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_details);
        /*android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Performance Details");
        ab.setIcon(R.mipmap.back);*/
        //ActionToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        initUI();
    }

    private void initUI() {

        int alert_no =this.getIntent().getIntExtra(Constants.ALERTS_DETAIL, 0);
        imageView = (ImageView) findViewById(R.id.iv_screen);
        switch (alert_no) {
            case 0: imageView.setImageResource(R.mipmap.weather);
                    getSupportActionBar().setTitle(getString(R.string.weather_alert));
                    break;
            case 1: imageView.setImageResource(R.mipmap.traffic);
                    getSupportActionBar().setTitle(getString(R.string.traffic_alert));
                    break;
            case 2: imageView.setImageResource(R.mipmap.contextual_alert);
                    getSupportActionBar().setTitle(getString(R.string.contextual_alert));
                    break;
            default: imageView.setImageResource(R.mipmap.weather);
                     getSupportActionBar().setTitle(getString(R.string.weather_alert));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
