package com.example.earthquakeapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private String mUrl;

    private long mTimeInMilliseconds;

    public Earthquake(double magnitude,String location,long timeInMilliseconds,String url)
    {
        mMagnitude=magnitude;
        mLocation=location;

       mTimeInMilliseconds = timeInMilliseconds;
       mUrl = url;
    }

    public double getMagnitude()
    {
        return mMagnitude;
    }
    public String getLocation()
    {
        return mLocation;
    }



    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getUrl() {
        return mUrl;
    }


}
