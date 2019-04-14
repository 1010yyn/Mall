package com.example.yangy.mall;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CreateData extends Activity {

    private String TAG = "MYTAG";

    public ArrayList<String> post_m(JSONObject req) {
        String strUrl = "http://192.168.43.110:8080/MyServer/server_servlet";//手机
        //String strUrl = "http://172.22.70.245:8080/MyServer/server_servlet";//图书馆
        //String strUrl = "http://172.17.104.159:8080/MyServer/server_servlet";//宿舍
        String result = "";
        ArrayList<String> rst = new ArrayList<>();
        URL url;
        try {
            url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);//设置输入流采用字节流
            urlConn.setDoOutput(true);//设置输出流采用字节流
            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);//设置缓存
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置meta参数
            urlConn.setRequestProperty("Charset", "utf-8");

            Log.i(TAG, "尝试连接");
            urlConn.connect();//连接，往服务端发送消息

            DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
            dop.writeBytes("param=" + URLEncoder.encode(req.toString(), "utf-8"));//发送参数
            dop.flush();//发送，清空缓存
            dop.close();//关闭

            //接收返回数据
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String readLine;

            while ((readLine = bufferReader.readLine()) != null) {
                result += readLine;
                rst.add(URLDecoder.decode(readLine, "UTF-8"));
            }
            bufferReader.close();
            urlConn.disconnect();//关闭连接
            result = URLDecoder.decode(result, "UTF-8");//解码数据
            Log.i(TAG, "result:" + result);
        } catch (Exception e) {
            Log.e(TAG, "连接失败" + e.getMessage());
            rst.add("false");
        }
        return rst;
    }

    //TODO——get图片
}