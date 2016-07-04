----------------------------------------------------------------------
res/layout/widget.xml
---------------------------------------------------------------------
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
<ImageView 
    android:id="@+id/widget_imageView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/widget_bill"
    />
</LinearLayout>

-----------------------------------------------------------------------
 res/xml/appwidget_provide_info.xml
-----------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?>
 <!-- updatePeriodMillis 单位毫秒，每隔这段时间，会更新触发 -->
<appwidget-provider
   xmlns:android="http://schemas.android.com/apk/res/android"
   android:initialLayout="@layout/widget"
   android:minHeight="84dp"
   android:minWidth="84dp"
   android:updatePeriodMillis="86400000"
    >
</appwidget-provider>

-------------------------------------------------------------------------
AndroidManifest.xml / <application>
-------------------------------------------------------------------------
<receiver 
    android:name="com.hmj.widget.MyAppWidgetProvider">

    <meta-data  
        android:name="android.appwidget.provider"
        android:resource="@xml/appwidget_provide_info"
    />

    <intent-filter> 
        <action android:name="com.hmj.widget.CLICK"/>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
    </intent-filter>
</receiver>

-------------------------------------------------------------------------
com.hmj.widget.MyAppWidgetProvider
-------------------------------------------------------------------------
package com.hmj.widget;
import com.example.android_customize_view.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import android.widget.Toast;

public class MyAppWidgetProvider extends AppWidgetProvider {
	
	public static final String TAG = "MyAppWidgetProvider";  
	public static final String CLICK_ACTION = "com.hmj.widget.CLICK";
	
	public MyAppWidgetProvider() {
		super();
	} 
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.i(TAG, "onReceive :action = "+intent.getAction());
		//这里判断是自己的action,做自己的事，比如小部件被单击后的触发操作，
		//（这里是做一个动画效果）
		if(intent.getAction().equals(CLICK_ACTION)){
			Toast.makeText(context, "click it ! action="+intent.getAction(), Toast.LENGTH_LONG).show();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), 
							R.drawable.widget_bill);
					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
					for(int i = 0 ;i<37;i++){
						float degree = (i*10) %360;
						RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
						remoteViews.setImageViewBitmap(R.id.widget_imageView, rotateBitmap(context, srcBitmap, degree));
						Intent intentClick = new Intent();
						intentClick.setAction(CLICK_ACTION);
						PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
						remoteViews.setOnClickPendingIntent(R.id.widget_imageView,pendingIntent);
						appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
						SystemClock.sleep(30);
					}
				}
			}).start();
		}
	}
	
	//每次小部件更新时都会调用一次该方法
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		Log.i(TAG, "onUpdate");
		final int counter = appWidgetIds.length;
		for(int i = 0; i < counter;i++){ 
			int appWidgetId = appWidgetIds[i];
			onWidgetUpdate(context,appWidgetManager,appWidgetId);
		}
	}

	private void onWidgetUpdate(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		Log.i(TAG, "appWidgetId="+appWidgetId);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		//“桌面小部件”单击事件发送的Intent广播
		Intent intentClick = new Intent();
		intentClick.setAction(CLICK_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_imageView,pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		
	}

	protected Bitmap rotateBitmap(Context context,Bitmap bitmap,float degree) {
		Matrix matrix = new Matrix() ;
		matrix.reset();
		matrix.setRotate(degree);
		Bitmap temoBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
		return temoBitmap;
	}

}
