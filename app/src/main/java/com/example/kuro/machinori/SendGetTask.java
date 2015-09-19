package com.example.kuro.machinori;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by keinon on 2015/09/19.
 */
public class SendGetTask extends Activity{
    TextView respons;
    public void sendGetTask(final String latitude, final String longitude, final String accelerationX,final String accelerationY,final String accelerationZ, final String deviceId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://133.242.234.254:9999/?latitude="+latitude+"&longitude="+longitude+"&accelerationX="+
                        accelerationX+"&accelerationY="+accelerationY+"&accelerationZ="+accelerationZ+"&deviceId="+deviceId);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    String str = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    //respons = (TextView)findViewById(R.id.response);
                    //respons.setText(str);
                    Log.d("ÅöHTTP", str);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }
}
