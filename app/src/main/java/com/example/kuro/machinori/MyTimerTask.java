package com.example.kuro.machinori;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.TimerTask;


/**
 * Created by keinon on 2015/09/19.
 */
public class MyTimerTask extends TimerTask {
    private Handler mHandler;
    private Context context;
    MyTimerTask(Context context){
        mHandler = new Handler();
        this.context = context;
    }
    @Override
    public void run() {
        mHandler.post( new Runnable() {
            public void run() {
                ((GpsActivity)context).send();
                Log.v("タイマー来たよ★","");
            }
        });
    }
}
