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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PeerComparisonScreen extends AppCompatActivity {

    LinearLayout ll_indication;
    ImageView indication_text, iv_plus;
    boolean expanded = true;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_comparison);
        //ActionToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.peer_comparison));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        /*android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.peer_comparison);
        ab.setIcon(R.mipmap.back);*/
        initUI();
    }

    private void initUI() {

        indication_text = (ImageView) findViewById(R.id.indication_text);
        ll_indication = (LinearLayout) findViewById(R.id.ll_indication);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        ll_indication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expanded) {
                    iv_plus.setImageResource(R.mipmap.plus);
                    indication_text.setVisibility(View.INVISIBLE);
                    expanded = false;
                } else {
                    iv_plus.setImageResource(R.mipmap.minus);
                    indication_text.setVisibility(View.VISIBLE);
                    expanded = true;
                }
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
