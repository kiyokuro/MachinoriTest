package com.example.tama.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "MainActivity";

    private Button btn;
    private TextView tv;

    Thread scanBLE;


    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ビーコン端末を検索するためのクラスやらアダプター
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //5秒おきにスキャンするスレッド
        scanBLE = (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        //スキャンを開始
                                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                                        try {
                                            Thread.sleep(5000);
                                            //スキャンを停止
                                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }));

        //ボタンの定義
        btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);

        //テキストの定義
        tv = (TextView)findViewById(R.id.textView1);
        tv.setText("");


        mHandler =  new Handler(){
            //メッセージ受信
            public void handleMessage(Message message) {
                //メッセージの表示
                //tv.append((String) message.obj + "\n");
            };
        };

    }

    //コールバック関数の登録
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        //スキャンしてビーコン端末が見つかったとき
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,byte[] scanRecord) {
            getScanData(scanRecord);
            Log.d(TAG, "device address:"+device.getAddress() );

            Message msg = Message.obtain(); //推奨
            //Message msg = mHandler.obtainMessage(); //推奨
            msg.obj = new String("address:" + String.valueOf(device.getAddress()));

            //ハンドラへのメッセージ送信
            mHandler.sendMessage(msg);
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //データの整形
    private void getScanData(byte[] scanRecord ){
        if(scanRecord.length > 30)
        {
            if((scanRecord[5] == (byte)0x4c) && (scanRecord[6] == (byte)0x00) &&
                    (scanRecord[7] == (byte)0x02) && (scanRecord[8] == (byte)0x15))
            {
                String uuid = Integer.toHexString(scanRecord[9] & 0xff)
                        + Integer.toHexString(scanRecord[10] & 0xff)
                        + Integer.toHexString(scanRecord[11] & 0xff)
                        + Integer.toHexString(scanRecord[12] & 0xff)
                        + "-"
                        + Integer.toHexString(scanRecord[13] & 0xff)
                        + Integer.toHexString(scanRecord[14] & 0xff)
                        + "-"
                        + Integer.toHexString(scanRecord[15] & 0xff)
                        + Integer.toHexString(scanRecord[16] & 0xff)
                        + "-"
                        + Integer.toHexString(scanRecord[17] & 0xff)
                        + Integer.toHexString(scanRecord[18] & 0xff)
                        + "-"
                        + Integer.toHexString(scanRecord[19] & 0xff)
                        + Integer.toHexString(scanRecord[20] & 0xff)
                        + Integer.toHexString(scanRecord[21] & 0xff)
                        + Integer.toHexString(scanRecord[22] & 0xff)
                        + Integer.toHexString(scanRecord[23] & 0xff)
                        + Integer.toHexString(scanRecord[24] & 0xff);

                String major = Integer.toHexString(scanRecord[25] & 0xff) + Integer.toHexString(scanRecord[26] & 0xff);
                String minor = Integer.toHexString(scanRecord[27] & 0xff) + Integer.toHexString(scanRecord[28] & 0xff);

                Log.d(TAG, "UUID:"+uuid );
                Log.d(TAG, "major:"+major );
                Log.d(TAG, "minor:"+minor );
            }
        }
    }

    @Override
    public void onClick(View v) {
        // スレッド開始
        scanBLE.start();
    }
    //画面がら消えた時
    @Override
    public void onStop(){
        super.onStop();
        //スレッドをやめる
        scanBLE.interrupt();
    }
}
