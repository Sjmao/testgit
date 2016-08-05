package com.example.administrator.hf;

import android.content.Context;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
//import org.apache.http.protocol.HttpContext;

import java.util.List;

import cz.msebera.android.httpclient.protocol.HttpContext;

public class HttpUtils {
	private static AsyncHttpClient client = new AsyncHttpClient();

	private static void setcoockie() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(
				HFApplication.newInstance());
		client.setCookieStore(myCookieStore);
	}

	public static String getcoockies(){
		try {
		HttpContext httpContext = client.getHttpContext();
		CookieStore cookies = (CookieStore) httpContext
				.getAttribute(ClientContext.COOKIE_STORE);
		List<Cookie> s= cookies.getCookies();
		for(Cookie c:s){
			if("KKGUANSESSIONID".equals(c.getName())){
				if(Constant.requestUrl.contains(c.getDomain())){
					return "KKGUANSESSIONID="+c.getValue();
//					return c.getValue();
				}
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}
	public static void initcoockie() {
		HttpContext httpContext = client.getHttpContext();
		CookieStore cookies = (CookieStore) httpContext
				.getAttribute(ClientContext.COOKIE_STORE);
		if (cookies == null) {
			setcoockie();
		}
	}

	private HttpUtils() {

	}

	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		HttpContext httpContext = client.getHttpContext();
		CookieStore cookies = (CookieStore) httpContext
				.getAttribute(ClientContext.COOKIE_STORE);
		// if (cookies != null) {
		// for (Cookie c : cookies.getCookies()) {
		// }
		// }
		client.get(context, url, params, handler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.post(url, params, handler);
	}

	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler handler) {

		client.setTimeout(1*1000*20);
		client.post(context, url, params, handler);
	}

//	public static void post(Context context, String url, HttpEntity entity,
//			String contentType, AsyncHttpResponseHandler handler) {
//		client.post(context, url, entity, contentType, handler);
//	}

	public static void cancel(Context context, boolean mayInterruptIfRunning) {
		client.cancelRequests(context, mayInterruptIfRunning);
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{
		client.get(urlString, res);
	}

	public static AsyncHttpClient getAsyncHttpClient() {
		return client;
	}

}
