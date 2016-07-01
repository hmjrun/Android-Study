package com.hmj.UI;

import java.util.List;

import com.example.android_customize_view.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {
	
	private Paint mPaint;//画笔
	
	//Path类可以预先在View上将N个点连成一条"路径",
	//然后调用Canvas的drawPath(path,paint)即可沿着路径绘制图形
	private Path mPath;
	
	private int mTriangleWith;//三角形底边长（宽）
	
	private int mTriangleHeight;//三角形高
	
	//适应所有屏幕，给定三角形底部宽为一个Tab的1/6
	private static final float radio_triangle_width = 1/6F;
	
	private int mInitTranslationX;//初始三角形在X轴上的偏移位置
	
	private int mTranslationX;//VeiwPager滑动时三角形在X轴上移动时的偏移位置
	
	private int mvisiable_tab_count;
	private static final int mvisiable_tab_count_default=4;
	private static final int color_text_title_normal=0x77FFFFFF;
	private static final int color_text_title_highLighr=0x99FFFFFF;
	
	private List<String> mTitles;
	
	
	public ViewPagerIndicator(Context context) {
		this(context, null);
	}
	
	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取自定义属性可见tab的数量值
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ViewPagerIndicator);
		mvisiable_tab_count = a.getInt(R.styleable.ViewPagerIndicator_visiable_tab_count, mvisiable_tab_count_default);
		if(mvisiable_tab_count < 0){
			mvisiable_tab_count = mvisiable_tab_count_default;
		}
		a.recycle();
		//初始化画笔
		initPaint();
		
	}
	
	//在ViewGroup上绘制东西的时候往往重写的是dispatchDraw()方法而不是onDraw()方法，
	//--或者自定制一个Drawable，重写它的draw(Canvas c)和 getIntrinsicWidth(), 
	@Override
	protected void dispatchDraw(Canvas canvas) {
		
		canvas.save();//锁画布(为了保存之前的画布状态) 
		
		//把当前画布的原点移到(x,y),
		//后面的操作都以(x,y)作为参照点，默认原点为(0,0)  
		//getHeight()获取当前控件的高度
		canvas.translate(mInitTranslationX + mTranslationX, getHeight());
		
		//开始在当前画布上绘制图形
		canvas.drawPath(mPath, mPaint);
		
		canvas.restore();//把当前画布返回（调整）到上一个save()状态之前  
		
		super.dispatchDraw(canvas);
	}
	
	/*
	 * 在加载完控件后调用，这里用来划定Tab的宽度以划定可见数量
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int childCount = getChildCount();
		if(childCount == 0)return ;
		for(int i=0;i<childCount;i++){
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
			lp.weight = 0;
			//设置每个Tab的宽度：屏幕宽度/可见Tab数
			lp.width = getScreenWidth()/mvisiable_tab_count;
			view.setLayoutParams(lp);
		}
	}
	
	//获取屏幕的宽度
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	private void initPaint() {
		//初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);//防止锯齿
		mPaint.setColor(Color.parseColor("#ffffff"));//设置画笔颜色
		mPaint.setStyle(Style.FILL);//Style.FILL：实心
				
		//给path设置样式（效果）。CornerPathEffect则可以将路径的转角变得圆滑，
		//radius，意思就是转角处的圆滑程度。
		mPaint.setPathEffect(new CornerPathEffect(3));	
	}

	//onSizeChanged()实在布局发生变化时的回调函数，
	//间接回去调用onMeasure, onLayout函数重新布局
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		//w:控件宽度
		//三角形的底边宽度= 每个Tab的一定比例长度
		mTriangleWith = (int) (w / mvisiable_tab_count * radio_triangle_width);
		
		//三角形的初始值，在X轴上的偏移量
		mInitTranslationX = w/mvisiable_tab_count/2 - mTriangleWith/2;
		
		initTrangle();
	}

	//初始化三角形
	private void initTrangle() {
		
		//假设三角形是等边直角三角，三角形的高为底边的一半
		mTriangleHeight = mTriangleWith/2;
		mPath = new Path();
		mPath.moveTo(0, 0);
		//画第一边
		mPath.lineTo(mTriangleWith, 0);
		//画第二边,向下为正,向上为负，向右为正
		mPath.lineTo(mTriangleWith/2, -mTriangleHeight);
		//闭合
		mPath.close();	
	}

	//指示三角形随手指进行滚动
	public void scroll(int position, float positionOffSet) {
		
		int tabWith = getWidth()/mvisiable_tab_count;
		
		//三角形在X轴上的偏移量
		mTranslationX = (int) (tabWith*(positionOffSet+position));
		
		//容器移动，当tab移动到最后一个可见的tab时,
		if(position >=(mvisiable_tab_count-1) && positionOffSet > 0 &&getChildCount() > mvisiable_tab_count){
			if(mvisiable_tab_count != 1){
				this.scrollTo(
						(position-(mvisiable_tab_count-1))*tabWith + (int)(tabWith*positionOffSet),
						0);
			}else{
				this.scrollTo(position*tabWith+(int)(tabWith*positionOffSet),0);
			}
			
		}
		
		//请求重绘View树，即draw()过程，假如视图发生大小没有变化就不会调用layout()过程，并且只绘制那些“需要重绘的”
		//视图，即谁(View的话，只绘制该View ；ViewGroup，则绘制整个ViewGroup)请求invalidate()方法，就绘制该视图。
		invalidate();
	}
	
	public void setTabItemTitle(List<String> titles){
		if(titles != null && titles.size() > 0){
			mTitles = titles;
			for(String title : mTitles){
				addView(generateTextView(title));
			}
			setItemClickEvent();
		}
	}

	
	
	public void setVisiableTabCount(int count){
		mvisiable_tab_count = count > 0?count:mvisiable_tab_count_default;
	}
	
	//根据title创建Tab
	private View generateTextView(String title) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth()/mvisiable_tab_count;
		tv.setText(title);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		tv.setTextColor(color_text_title_normal);
		tv.setLayoutParams(lp);
		return tv;
	}

	private ViewPager mViewpager;
	
	//设置关联的viewPager
	public void setIndicatorViewPager(ViewPager viewPager,int postion){
		mViewpager = viewPager;
		mViewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(mListener != null){
					mListener.onPageSelected(position);
				}
				
				highLightTextView(position);
			}
			
			//滑动被停止之前，此方法回一直得到调用。其中三个参数的含义分别为：
			//position :当前页面，及你点击滑动的页面
			//positionOffSet:当前页面偏移的百分比
			//positionOffSetPixels:当前页面偏移的像素位置   
			@Override
			public void onPageScrolled(int position, float positionOffSet,
					int positionOffSetPixels) {
				//tabWith:每个tab的宽度
				//mTranslationX = (int) (tabWith*positionOffSet+tabWith*position);
				scroll(position,positionOffSet);
				if(mListener != null){
					mListener.onPageScrolled(position,positionOffSet,positionOffSetPixels);
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
				if(mListener != null){
					mListener.onPageScrollStateChanged(state);
				}
			}
		});
		
		//设置当前显示的第几个页面
		mViewpager.setCurrentItem(postion);
	}
	
	public interface PageOnChangeListener{
		public void onPageSelected(int position);
		public void onPageScrolled(int position, float positionOffSet,int positionOffSetPixels) ;
		public void onPageScrollStateChanged(int state);
	}
	
	public PageOnChangeListener mListener;
	
	public void setOnPageOnChangeListener(PageOnChangeListener listener){
		this.mListener = listener;
	}
	
	//高亮某个tab的文本
	private void highLightTextView(int pos){
		reSetTextViewColor_normal();
		View view = getChildAt(pos);
		if(view instanceof TextView){
			((TextView) view).setTextColor(color_text_title_highLighr);
		}
	}
	
	//重置某个文本的颜色为normal
	private void reSetTextViewColor_normal(){
		for(int i = 0;i<getChildCount();i++){
			View view = getChildAt(i);
			if(view instanceof TextView){
				((TextView) view).setTextColor(color_text_title_normal);
			}
		}
	}
	
	private void setItemClickEvent(){
		for(int i = 0;i<getChildCount();i++){
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mViewpager.setCurrentItem(j);
				}
			});
		}
	}
	
}
