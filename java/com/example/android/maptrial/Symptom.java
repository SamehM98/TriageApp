package com.example.android.maptrial;

public class Symptom {

    private int mDegree;
    private String mName;

    public Symptom(String name, int deg){
        mDegree = deg;
        mName = name;
    }

    public int getDegree() {
        return mDegree;
    }

    public String getName() {
        return mName;
    }
}
