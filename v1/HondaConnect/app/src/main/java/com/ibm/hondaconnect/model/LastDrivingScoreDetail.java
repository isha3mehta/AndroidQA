package com.ibm.hondaconnect.model;

/**
 * Created by IBM_ADMIN on 2/22/2017.
 */
public class LastDrivingScoreDetail {

    private String mLastDrivingParamName;
    private double mLaseDrivingParamValue;

    public String getDrivingParamName() {
        return mLastDrivingParamName;
    }

    public void setDrivingParamName(String drivingParamName) {
        this.mLastDrivingParamName = drivingParamName;
    }

    public double getDrivingParamValue() {
        return mLaseDrivingParamValue;
    }

    public void setDrivingParamValue(double drivingParamValue) {
        this.mLaseDrivingParamValue = drivingParamValue;
    }
}
