package com.ibm.hondaconnect;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.ibm.hondaconnect.util.Constants;

public class ContextualAlertsScreen extends AppCompatActivity {

    private Menu mMenu;
    Intent alertIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contextual_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.manage_alert));
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
        Switch weather_switch = (Switch) findViewById(R.id.weather_switch);

        //set the switch to ON
        weather_switch.setChecked(true);
        //attach a listener to check for changes in state
        weather_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(ContextualAlertsScreen.this , "Weather Alerts is currently ON" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContextualAlertsScreen.this , "Weather Alerts is currently OFF" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch contextual_switch = (Switch) findViewById(R.id.contextual_switch);

        //set the switch to ON
        contextual_switch.setChecked(true);
        //attach a listener to check for changes in state
        contextual_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(ContextualAlertsScreen.this , "Contextual Alerts is currently ON" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContextualAlertsScreen.this , "Contextual Alerts is currently OFF" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch traffic_switch = (Switch) findViewById(R.id.traffic_switch);

        //set the switch to ON
        traffic_switch.setChecked(true);
        //attach a listener to check for changes in state
        traffic_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(ContextualAlertsScreen.this , "Traffic Alerts is currently ON" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContextualAlertsScreen.this , "Traffic Alerts is currently OFF" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout ll_weather = (LinearLayout) findViewById(R.id.ll_weather);
        ll_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertIntent = new Intent(ContextualAlertsScreen.this , AlertsDetailsScreen.class);
                alertIntent.putExtra(Constants.ALERTS_DETAIL,Constants.ALERTS_WEATHER);
                startActivity(alertIntent);
            }
        });

        LinearLayout ll_traffic = (LinearLayout) findViewById(R.id.ll_traffic);
        ll_traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertIntent = new Intent(ContextualAlertsScreen.this , AlertsDetailsScreen.class);
                alertIntent.putExtra(Constants.ALERTS_DETAIL,Constants.ALERTS_TRAFFIC);
                startActivity(alertIntent);
            }
        });

        LinearLayout ll_contextual = (LinearLayout) findViewById(R.id.ll_contextual);
        ll_contextual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertIntent = new Intent(ContextualAlertsScreen.this , AlertsDetailsScreen.class);
                alertIntent.putExtra(Constants.ALERTS_DETAIL,Constants.ALERTS_CONTEXTUAL);
                startActivity(alertIntent);
            }
        });
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
