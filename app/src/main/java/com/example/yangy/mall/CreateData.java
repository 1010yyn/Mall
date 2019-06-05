package com.example.yangy.mall;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.provider.Telephony.Mms.Part.CHARSET;

public class CreateData extends Activity {

    private String TAG = "MYTAG";

    private String strUrl = "http://172.17.104.159:8080/MyServer/server_servlet";//宿舍
    //private String strUrl = "http://192.168.43.110:8080/MyServer/server_servlet";//手机
    // private String strUrl = "http://172.22.76.50:8080/MyServer/server_servlet";//图书馆
    //设置访问目标网页url

    public ArrayList<String> post_m(JSONObject req) {
        String result = "";
        ArrayList<String> rst = new ArrayList<>();
        URL url;
        try {
            url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();//得到网络访问对象java.net.HttpURLConnection
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
                rst.add(URLDecoder.decode(readLine, "UTF-8"));//解码数据，并加入结果list
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

    public void post_p(Drawable file, Map<String, String> param, String imageName) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        FormatTools tools = new FormatTools();
        // 显示进度框
        //      showProgressDialog();
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("X-bocang-Authorization", "token"); //token可以是用户登录后的token等等......
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                Log.v("520it", "触发到");
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();

                String params = "";
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
                        sb = null;
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"")
                                .append(key)
                                .append("\"")
                                .append(LINE_END)
                                .append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        dos.write(params.getBytes());
                    }
                }
                sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"")
                        .append("avatar")
                        .append("\"")
                        .append(";filename=\"")
                        .append(imageName)
                        .append("\"\n");
                sb.append("Content-Type: image/png");
                sb.append(LINE_END).append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = tools.Drawable2InputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */

                int res = conn.getResponseCode();
                System.out.println("res=========" + res);
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                } else {
                }
            } else {
                Log.v("520it", "触发不到");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     *
     * @param url
     * @param imagePath 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    public String uploadImage(String url, String imagePath, File photo) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d("imagePath", imagePath);
        File file = new File(imagePath);
        url = strUrl;
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody image1 = RequestBody.create(MediaType.parse("image/png"), photo);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, image1)
                .build();
        Request request = new Request.Builder()
                .url(url + "/uploadImage")
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        return jsonObject.optString("image");
    }

}