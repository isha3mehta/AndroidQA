package com.ibm.hondaconnect;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibm.hondaconnect.fragment.DrivingStylePageAdapter;


/**
 * Created by IBM_ADMIN on 2/20/2017.
 */
public class TripDetailsActivity extends BaseActivity {


    private static final String _TAG = TripDetailsActivity.class.getSimpleName();
    public String mSelectedTripDate;
    public String mSelectedDrivingDate;
    public String mSelectedDrivingMonthUI;
    public String mSelectedDrivingMonthServer;
    public boolean mMonthChartOpened = false;
    public boolean mTripChartOpened = false;
    private Menu mMenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trip Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        setUpTabs();
    }

    private void setUpTabs(){
        //Set up and design tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

        TextView tabTrip = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTrip.setText("TRIP");
        tabLayout.getTabAt(0).setCustomView(tabTrip);

        TextView tabDay = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabDay.setText("DAY");
        tabLayout.getTabAt(1).setCustomView(tabDay);

        TextView tabMonth = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabMonth.setText("MONTH");
        tabLayout.getTabAt(2).setCustomView(tabMonth);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final DrivingStylePageAdapter adapter = new DrivingStylePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
