package com.ibm.hondaconnect.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by IBM_ADMIN on 2/21/2017.
 */
public class DrivingStylePageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs = 3;

    public DrivingStylePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentTripwiseDrivingStyle tabTripWise = new FragmentTripwiseDrivingStyle();
                return tabTripWise;
            case 1:
                FragmentDaywiseDrivingStyle tabDaywise = new FragmentDaywiseDrivingStyle();
                return tabDaywise;
            case 2:
                FragmentMonthwiseDrivingStyle tabMonthwise = new FragmentMonthwiseDrivingStyle();
                return tabMonthwise;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
