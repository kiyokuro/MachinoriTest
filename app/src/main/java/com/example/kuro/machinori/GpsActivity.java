package com.example.kuro.machinori;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Timer;


public class GpsActivity extends Activity implements LocationListener, SensorEventListener {
    //private String latitude;//緯度
    //private String longitude;//経度
    //private String accelerationX;//加速度x軸
    //private String accelerationY;//加速度y軸
    //private String accelerationZ;//加速度z軸
    //private String deviceId;//デバイスID
    private SensorManager manager;
    private TextView values;//加速度をセット
    private TextView tvDevicdId;//デバイスIDをセット
    private Timer timer;
    Data data = new Data();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // LocationManagerを取得
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationProvider provider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);

        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();

        // Accuracyを指定(MEDIUMにするとネットワークから位置情報取得。FINEにするとGPSから位置情報取得)
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        // PowerRequirementを指定(消費電力)
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

        // ロケーションプロバイダの取得
        String providerName = mLocationManager.getBestProvider(criteria, true);

        // 取得したロケーションプロバイダを表示
        TextView tv_provider = (TextView) findViewById(R.id.Provider);
        tv_provider.setText("Provider: "+providerName);

        // LocationListenerを登録 1秒経つか2メートル移動したら位置情報を再取得
        mLocationManager.requestLocationUpdates(providerName,
                1000,//1秒
                2,//2メートル
                this);


        //SwnsorManegerのインスタンスを取得
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        values = (TextView)findViewById(R.id.value);


        //デバイスIDを取得
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        tvDevicdId = (TextView)findViewById(R.id.device_id);
        data.setDeviceId(telephonyManager.getDeviceId());
        tvDevicdId.setText("デバイスID"+ data.getDeviceId());

        //タイマーインスタンス生成
        timer = new Timer();
        //タスククラスインスタンス生成
        MyTimerTask timerTask = new MyTimerTask(this);
        //タイマースケジュール設定＆開始
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    @Override
    public void onStop(){
        super.onStop();
        // Listenerの登録解除
        manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Listenerの登録
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // 緯度の表示
        TextView tv_lat = (TextView) findViewById(R.id.Latitude);
        data.setLatitude(String.valueOf(location.getLatitude()));
        tv_lat.setText("緯度:" + data.getLatitude());

        // 経度の表示
        TextView tv_lng = (TextView) findViewById(R.id.Longitude);
        data.setLongitude(String.valueOf(location.getLongitude()));
        tv_lng.setText("経度:" + data.getLongitude());

        //緯度経度が更新されるタイミングで文字列のバイト数を表示
        //int detaSize = getByte(data.getLatitude() + "," + data.getLongitude() + "," + data.getAccelerationX() + "," + data.getAccelerationY() + ","
        //        + data.getAccelerationZ()+ "," + data.getDeviceId(), "UTF-8");
        //Log.v("★文字列バイト数", String.valueOf(detaSize));

        //データをサーバに送信する
        //SendMessage post = new SendMessage();
        //post.execute(latitude,longitude,accelerationX,accelerationY,accelerationZ,deviceId);
        //SendGetTask sendGetTask = new SendGetTask();
        //sendGetTask.sendGetTask(data.getLatitude(),data.getLongitude(),data.getAccelerationX(),data.getAccelerationY(),data.getAccelerationZ(),data.getAccelerationZ());

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            data.setAccelerationX(String.valueOf(sensorEvent.values[SensorManager.DATA_X]));
            data.setAccelerationY(String.valueOf(sensorEvent.values[SensorManager.DATA_Y]));
            data.setAccelerationZ(String.valueOf(sensorEvent.values[SensorManager.DATA_Z]));
            String str = "加速度センサー値:"
                    + "\nX軸:" + data.getAccelerationX()
                    + "\nY軸:" + data.getAccelerationY()
                    + "\nZ軸:" + data.getAccelerationZ();
            values.setText(str);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 渡したエンコードでの文字列のバイト数を返す
     * @param value 文字列
     * @param enc エンドードUTF-8とか
     * @return バイト数
     */
    public static int getByte(String value, String enc) {
        if ( value == null || value.length() == 0 )
        return 0;
        int ret = 0;
        try {
            ret = value.getBytes(enc).length;
        } catch ( UnsupportedEncodingException e ) {
            ret = 0;
        }
        return ret;
    }

    /**
     * センサで取得したデータをサーバーに送信
     */
    public void send(){
        SendGetTask sendGetTask = new SendGetTask();
        sendGetTask.sendGetTask(data.getLatitude(),data.getLongitude(),data.getAccelerationX(),data.getAccelerationY(),
                data.getAccelerationZ(),data.getDeviceId());
    }
}
