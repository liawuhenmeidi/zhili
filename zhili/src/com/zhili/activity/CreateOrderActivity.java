package com.zhili.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zhili.R;
import com.zhili.manager.DataManager;
import com.zhili.manager.NetDataManager;
import com.zhili.manager.UpdateManager;

public class CreateOrderActivity extends ActionBarActivity {

	WebView webview;
	DataManager dataManager = DataManager.getInstance();
	
	// 响应
    private HttpResponse mHttpResponse;
    // 实体
    private HttpEntity mHttpEntity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.createorder);
        //webview = (WebView)findViewById(R.id.createorderactivity_webview);
        
        //start();
        webview = new WebView(this); 
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.loadUrl(DataManager.getBaseURL() + "meidiserver/androidSupport/order.jsp" + (String)dataManager.get("urlParam"));
        setContentView(webview);
    }

    private void start(){
        // 生成一个请求对象
        HttpGet httpGet = new HttpGet("http://www.baidu.com/");
        // 生成一个Http客户端对象
        HttpClient httpClient = new DefaultHttpClient();

        // 下面使用Http客户端发送请求，并获取响应内容

        InputStream inputStream = null;
        try
        {
            // 发送请求并获得响应对象
            mHttpResponse = httpClient.execute(httpGet);
            // 获得响应的消息实体
            mHttpEntity = mHttpResponse.getEntity();

            // 获取一个输入流
            inputStream = mHttpEntity.getContent();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));

            String result = "";
            String line = "";

            while (null != (line = bufferedReader.readLine()))
            {
                result += line;
            }

            // 将结果打印出来，可以在LogCat查看
            System.out.println(result);

            // 将内容载入WebView显示
            webview.getSettings().setDefaultTextEncodingName("UTF-8");
            // 直接使用mWebView.loadData(result, "text/html", "utf-8");会显示找不到网页

            // 换成下面的方式可以正常显示（但是比较宽，拖动可见百度logo）
            webview.loadDataWithBaseURL(null, result, "text/html",
                    "utf-8", null);
            
            // 直接载入URL也可以显示页面（但是此例子主要是为了验证响应返回的字符串是否正确，所以不用下面这行代码）
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }   
    }
    
   
}