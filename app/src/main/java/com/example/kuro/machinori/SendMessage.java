package com.example.kuro.machinori;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by keinon on 2015/09/19.
 */
public class SendMessage extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... contents) {
        // URL指定
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://133.242.234.254:9999/aaa");
        // BODYに登録、設定
        ArrayList<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("Latitude", contents[0]));
        /*value.add(new BasicNameValuePair("Latitude", contents[0]));
        value.add(new BasicNameValuePair("Longitude", contents[1]));
        value.add(new BasicNameValuePair("X", contents[2]));
        value.add(new BasicNameValuePair("Y", contents[3]));
        value.add(new BasicNameValuePair("Z", contents[4]));
        value.add( new BasicNameValuePair("DeviceId", contents[5]));
*/
        String body = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(value, "UTF-8"));
            // リクエスト送信
            HttpResponse response = client.execute(post);
            // 取得
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity, "UTF-8");

        } catch(IOException e) {
            e.printStackTrace();
        }
        client.getConnectionManager().shutdown();
        return body;
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        // 取得した結果をテキストビューに入れちゃったり
        Log.v("respons★",result);
    }
}
