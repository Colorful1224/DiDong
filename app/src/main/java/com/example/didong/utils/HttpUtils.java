package com.example.didong.utils;

import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.didong.Common;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtils {
    public JSONObject requests(String method, String Api, JSONObject data) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        JSONObject response = new JSONObject();
        int code = 0;
        String body = "";
        try {
            //try-catch捕捉异常
            Common cmn = new Common();
            //首先获取一个要获取数据的URL
            URL url = new URL(cmn.serverAddress + Api);
            //设置一个连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接时间
            connection.setConnectTimeout(10000);
            //这一步很重要，设置连接方式（有八种连接方式）
            connection.setRequestMethod(method);
            //设置语言和时区
            connection.setRequestProperty("Accept-language", "zh-CN,zh;q=0.9");
            if (method.equals("GET")) {
                //建立连接
                connection.connect();
                //结果码
                code = connection.getResponseCode();
                //如果结果码是200，说明正常访问
                if (code == 200) {
                    Map<String, List<String>> headerFields = connection.getHeaderFields();
                    Set<Map.Entry<String, List<String>>> entries = headerFields.entrySet();
                    //foreach输出获取到的内容
                    if (cmn.debugMode) {
                        for (Map.Entry<String, List<String>> entry : entries) {
                            Log.d("TAG", entry.getKey() + " == " + entry.getValue());
                        }
                    }
                    //使用输入流来读取获取到json文件的内容
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    body = bufferedReader.readLine();
                    if (cmn.debugMode) {
                        Log.d("TAG", "run: " + body);
                    }
                }

            } else {
                //outputstream流需要写入的时候 要传一个bytes类型的数组
                byte[] bytes;
                if (data==null) {
                    bytes = "".getBytes("UTF-8");
                } else {
                    bytes = data.toString().getBytes("UTF-8");
                }
                connection.setRequestProperty("Content-length", String.valueOf(bytes.length));
                //建立连接
                connection.connect();
                //把数据给到服务器
                outputStream = connection.getOutputStream();
                //写入输出流
                outputStream.write(bytes);
                outputStream.flush();
                //拿到状态码
                code = connection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    //使用输入流来读取内容
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    body = bufferedReader.readLine();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            body = "";
        } finally {
            //最后要关闭流
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            response.put("code", code);
            response.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}