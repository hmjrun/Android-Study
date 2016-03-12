package com.hmj.webview;

/*
 *webview的使用技巧，示例。
 */
public class MainActivity extends Activity {
	private String urls="http://www.ideaofmy.sinaapp.com/zzk_sae/fanka.php?s=/Index.html";
	private WebView webview;
	private ProgressDialog progressdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl(urls);
		
		//覆盖webView默认通过第三方浏览器打开网页的行为，使得网页在WebView 中打开
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;//返回值为true，不用其他浏览器打开
			}
		});
		
		//启用支持js
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		
		//webview 优先使用缓存
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
		//判断网页是否加载完毕
		webview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				//newProgress 1-100的整数
				if(newProgress == 100){
					//网页加载完毕,关闭提示进度条
					closeDialog();
				}else{
					//网页正在加载，打开提示进度条
					openDialog(newProgress);
				}
				
			}

			private void openDialog(int newProgress) {
				if(progressdialog == null){
					progressdialog = new ProgressDialog(MainActivity.this);
					progressdialog.setTitle("正在加载...");
					progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressdialog.setProgress(newProgress);
					progressdialog.show();
				}else{
					progressdialog.setProgress(newProgress);
				}
				
			}

			private void closeDialog() {
				if(progressdialog != null && progressdialog.isShowing()){
					progressdialog.dismiss();
					progressdialog = null;
				}
				
			}
		});
	}
		
	//改写物理按键-返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode ==KeyEvent.KEYCODE_BACK)
		{
			if(webview.canGoBack())
			{
				webview.goBack();//返回上一页面
				return true;
			}else{
				System.exit(0);//退出应用
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
