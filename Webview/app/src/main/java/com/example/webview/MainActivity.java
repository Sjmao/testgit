package com.example.webview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.webview.dbutil.WebDao;
import android.os.Bundle;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.view.GestureDetector;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private ArrayAdapter<String> adapter;
	private WebDao mWebDao;
	private ProgressDialog progressDialog;
	private WebView mWebView;
	private Spinner mSpinner;
	private Button but1, but2;
	private EditText editText1, editText2;
	private String url = null,resetText=null;
	private String[] str1, str2;
	private List<String> list, listSpinnerText;
	private List<Integer> listSpinnerPosition;
	private Resources mresources;
	private Map<String, String> map;
	private AutoScrollTextView autoScrollTextView;
	private static boolean mIsBack = false;
	private GestureDetector mDetector;
	private static boolean mIsDelete = false;
	private TextView mDesTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = new ArrayList<String>();
		listSpinnerText = new ArrayList<String>();
		listSpinnerPosition = new ArrayList<Integer>();
		mWebView = (WebView) findViewById(R.id.myweb);
		mWebDao = new WebDao(MainActivity.this, 1);
		// mDetector=new GestureDetector(this);
		but1 = (Button) findViewById(R.id.but1);
		but2 = (Button) findViewById(R.id.but2);
		editText1 = (EditText) findViewById(R.id.edit1);
		editText2 = (EditText) findViewById(R.id.edit2);
		mDesTextView=(TextView)findViewById(R.id.describe);
		this.mWebView.setWebViewClient(new webViewClient());

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings()
				.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mSpinner = (Spinner) findViewById(R.id.myspinner);
		autoScrollTextView = (AutoScrollTextView) findViewById(R.id.TextViewNotice);
		map = new HashMap<String, String>();
		mresources = this.getResources();
		str1 = mresources.getStringArray(R.array.web);
		str2 = mresources.getStringArray(R.array.weburl);

		for (int i = 0; i < str1.length; i++) {
			map.put(str1[i], str2[i]);
			list.add(str1[i]);
		}
		Map<String, String> addMap = mWebDao.check();
		for (String key : addMap.keySet()) {
			list.add(key);
			map.put(key, addMap.get(key));
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, list);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (mSpinner.getSelectedItemPosition() >= str1.length) {
					mIsDelete = true;
					Builder builder = new Builder(MainActivity.this);
					builder.setMessage("确认删除此项吗？");

					builder.setTitle("提示");

					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									// TODO Auto-generated method stub
									String[] strArray = { mSpinner
											.getSelectedItem().toString() };
									mWebDao.delete(strArray);
									list.remove(mSpinner
											.getSelectedItemPosition());
									adapter.notifyDataSetChanged();
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}

							});

					builder.create().show();
				}
				return true;
			}
		});
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				if (mSpinner.getSelectedItemPosition() >= str1.length) {
					mDesTextView.setVisibility(View.VISIBLE);
//					Toast toast = Toast.makeText(MainActivity.this, "长按此项可删除",
//							2000);
//					toast.setGravity(Gravity.TOP, 0, 150);
//					toast.show();
				}else {
					mDesTextView.setVisibility(View.GONE);
				}
				if (!mIsBack) {
					url = map.get(mSpinner.getSelectedItem());
					if (url != null)
						mWebView.loadUrl(url);
					else {

					}
					if (mIsDelete) {
						listSpinnerPosition.remove(listSpinnerPosition.size() - 1);
						listSpinnerText.remove(listSpinnerText.size() - 1);

					}
					listSpinnerText.add(mSpinner.getSelectedItem().toString());
					listSpinnerPosition.add(mSpinner.getSelectedItemPosition());
					autoScrollTextView.setText(mSpinner.getSelectedItem()
							.toString());
					autoScrollTextView.init(getWindowManager());
					autoScrollTextView.startScroll();
				}
				mIsBack = false;
				mIsDelete = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		autoScrollTextView.init(getWindowManager());
		autoScrollTextView.startScroll();

	}

	class webViewClient extends WebViewClient {

		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {


			//view.loadUrl(url);

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false

			return false;

		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("数据加载中，请稍后。。。");
				progressDialog.show();
				mWebView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {// 网页加载结束的时候
			// super.onPageFinished(view, url);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
				mWebView.setEnabled(true);
			}
			//	view.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('audio'); for(var i=0;i<videos.length;i++){videos[i].play();}})()"); }

		}
	}

	public boolean onTouchEvent() {

		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mWebView.canGoBack()){
				mWebView.goBack();
			}else {
				finish();
			}


//
//			// 返回前一个页面
//			if (listSpinnerText.size() - 1 > 0) {
//				if (!mWebView.getUrl().equals(
//						map.get(mSpinner.getSelectedItem()))) {
//					if (!mWebView.getUrl().equals(url)) {
//						url=mWebView.getUrl();
//						mWebView.loadUrl(map.get(mSpinner.getSelectedItem()));
//					}else {
//						mIsBack = true;
//						listSpinnerText.remove(listSpinnerText.size() - 1);
//						listSpinnerPosition.remove(listSpinnerPosition.size() - 1);
//						mSpinner.setSelection(listSpinnerPosition
//								.get(listSpinnerPosition.size() - 1));
//						mWebView.loadUrl(map.get(listSpinnerText
//								.get(listSpinnerText.size() - 1)));
//						autoScrollTextView.setText(listSpinnerText
//								.get(listSpinnerText.size() - 1));
//						autoScrollTextView.init(getWindowManager());
//						autoScrollTextView.startScroll();
//						url=map.get(listSpinnerText
//								.get(listSpinnerText.size() - 1));
//					}
//
//
//				} else {
//					mIsBack = true;
//					listSpinnerText.remove(listSpinnerText.size() - 1);
//					listSpinnerPosition.remove(listSpinnerPosition.size() - 1);
//					mSpinner.setSelection(listSpinnerPosition
//							.get(listSpinnerPosition.size() - 1));
//					mWebView.loadUrl(map.get(listSpinnerText
//							.get(listSpinnerText.size() - 1)));
//					autoScrollTextView.setText(listSpinnerText
//							.get(listSpinnerText.size() - 1));
//					autoScrollTextView.init(getWindowManager());
//					autoScrollTextView.startScroll();
//				}
//				return true;
//
//			} else {
//				listSpinnerText.clear();
//				listSpinnerPosition.clear();
//				mWebView.clearHistory();
//				Intent intent = new Intent();
//				intent.setAction(intent.ACTION_MAIN);
//				intent.addCategory(intent.CATEGORY_HOME);
//				startActivity(intent);
//				// finish();
//			}
		}
		return false;
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.but1:
				editText1.setVisibility(View.VISIBLE);
				editText2.setVisibility(View.VISIBLE);
				mSpinner.setVisibility(View.GONE);
				but1.setVisibility(View.GONE);
				but2.setVisibility(View.VISIBLE);

				break;
			case R.id.but2:
				editText1.setVisibility(View.INVISIBLE);
				editText2.setVisibility(View.INVISIBLE);
				mSpinner.setVisibility(View.VISIBLE);
				but1.setVisibility(View.VISIBLE);
				but2.setVisibility(View.GONE);
				if (!(editText1.getText().toString().isEmpty())
						&& !(editText2.getText().toString().isEmpty())) {
					Map<String, String> map = new HashMap<String, String>();
					String text1 = editText1.getText().toString();
					String text2 = editText2.getText().toString();
					if (text2.indexOf("http://") == -1)
						text2 = "http://" + text2;
					mWebDao.insert(text1, text2);
					map = mWebDao.check();
					for (String key : map.keySet()) {
						list.add(key);
						this.map.put(key, map.get(key));
					}

					adapter.notifyDataSetChanged();
				}
				editText1.setText("");
				editText2.setText("");

			default:
				break;
		}

	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 /*
         * 
         * add()方法的四个参数，依次是：
         * 
         * 1、组别，如果不分组的话就写Menu.NONE,
         * 
         * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
         * 
         * 3、顺序，那个菜单现在在前面由这个参数的大小决定
         * 
         * 4、文本，菜单的显示文本
         */

		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "分享当前链接");
		menu.add(Menu.NONE,Menu.FIRST+2,6,"保存浏览状态退出");

		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的

//        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "保存");
//
//        menu.add(Menu.NONE, Menu.FIRST + 3, 6, "帮助");
//
//        menu.add(Menu.NONE, Menu.FIRST + 4, 1, "添加");
//
//        menu.add(Menu.NONE, Menu.FIRST + 5, 4, "详细");
//
//        menu.add(Menu.NONE, Menu.FIRST + 6, 3, "发送");

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case Menu.FIRST + 1:
				Intent	intent=new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享1");
				intent.putExtra(Intent.EXTRA_TEXT, map.get(mSpinner.getSelectedItem()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getTitle()));

				break;
			case Menu.FIRST + 2:
				Intent intent1 = new Intent();
				intent1.setAction(intent1.ACTION_MAIN);
				intent1.addCategory(intent1.CATEGORY_HOME);
				startActivity(intent1);
			default:
				break;
		}
		return false;
	}


}
