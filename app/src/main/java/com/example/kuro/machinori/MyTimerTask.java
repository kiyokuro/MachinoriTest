package com.example.kuro.machinori;

import android.os.Handler;

import java.util.TimerTask;


/**
 * Created by keinon on 2015/09/19.
 */
public class MyTimerTask extends TimerTask {
    private Handler mHandler = new Handler();
    @Override
    public void run() {
        //�����ɒ�����Ŏ��s�������������L�q���܂�
        mHandler.post( new Runnable() {
            public void run() {
                SendGetTask sendGetTask = new SendGetTask();
                //sendGetTask.sendGetTask(latitude,longitude,accelerationX,accelerationY,accelerationZ,deviceId);
            }
        });
    }
}
