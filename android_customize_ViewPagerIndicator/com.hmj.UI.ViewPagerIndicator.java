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
	
	private Paint mPaint;//����
	
	//Path�����Ԥ����View�Ͻ�N��������һ��"·��",
	//Ȼ�����Canvas��drawPath(path,paint)��������·������ͼ��
	private Path mPath;
	
	private int mTriangleWith;//�����εױ߳�����
	
	private int mTriangleHeight;//�����θ�
	
	//��Ӧ������Ļ�����������εײ���Ϊһ��Tab��1/6
	private static final float radio_triangle_width = 1/6F;
	
	private int mInitTranslationX;//��ʼ��������X���ϵ�ƫ��λ��
	
	private int mTranslationX;//VeiwPager����ʱ��������X�����ƶ�ʱ��ƫ��λ��
	
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
		//��ȡ�Զ������Կɼ�tab������ֵ
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ViewPagerIndicator);
		mvisiable_tab_count = a.getInt(R.styleable.ViewPagerIndicator_visiable_tab_count, mvisiable_tab_count_default);
		if(mvisiable_tab_count < 0){
			mvisiable_tab_count = mvisiable_tab_count_default;
		}
		a.recycle();
		//��ʼ������
		initPaint();
		
	}
	
	//��ViewGroup�ϻ��ƶ�����ʱ��������д����dispatchDraw()����������onDraw()������
	//--�����Զ���һ��Drawable����д����draw(Canvas c)�� getIntrinsicWidth(), 
	@Override
	protected void dispatchDraw(Canvas canvas) {
		
		canvas.save();//������(Ϊ�˱���֮ǰ�Ļ���״̬) 
		
		//�ѵ�ǰ������ԭ���Ƶ�(x,y),
		//����Ĳ�������(x,y)��Ϊ���յ㣬Ĭ��ԭ��Ϊ(0,0)  
		//getHeight()��ȡ��ǰ�ؼ��ĸ߶�
		canvas.translate(mInitTranslationX + mTranslationX, getHeight());
		
		//��ʼ�ڵ�ǰ�����ϻ���ͼ��
		canvas.drawPath(mPath, mPaint);
		
		canvas.restore();//�ѵ�ǰ�������أ�����������һ��save()״̬֮ǰ  
		
		super.dispatchDraw(canvas);
	}
	
	/*
	 * �ڼ�����ؼ�����ã�������������Tab�Ŀ���Ի����ɼ�����
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
			//����ÿ��Tab�Ŀ�ȣ���Ļ���/�ɼ�Tab��
			lp.width = getScreenWidth()/mvisiable_tab_count;
			view.setLayoutParams(lp);
		}
	}
	
	//��ȡ��Ļ�Ŀ��
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	private void initPaint() {
		//��ʼ������
		mPaint = new Paint();
		mPaint.setAntiAlias(true);//��ֹ���
		mPaint.setColor(Color.parseColor("#ffffff"));//���û�����ɫ
		mPaint.setStyle(Style.FILL);//Style.FILL��ʵ��
				
		//��path������ʽ��Ч������CornerPathEffect����Խ�·����ת�Ǳ��Բ����
		//radius����˼����ת�Ǵ���Բ���̶ȡ�
		mPaint.setPathEffect(new CornerPathEffect(3));	
	}

	//onSizeChanged()ʵ�ڲ��ַ����仯ʱ�Ļص�������
	//��ӻ�ȥ����onMeasure, onLayout�������²���
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		//w:�ؼ����
		//�����εĵױ߿��= ÿ��Tab��һ����������
		mTriangleWith = (int) (w / mvisiable_tab_count * radio_triangle_width);
		
		//�����εĳ�ʼֵ����X���ϵ�ƫ����
		mInitTranslationX = w/mvisiable_tab_count/2 - mTriangleWith/2;
		
		initTrangle();
	}

	//��ʼ��������
	private void initTrangle() {
		
		//�����������ǵȱ�ֱ�����ǣ������εĸ�Ϊ�ױߵ�һ��
		mTriangleHeight = mTriangleWith/2;
		mPath = new Path();
		mPath.moveTo(0, 0);
		//����һ��
		mPath.lineTo(mTriangleWith, 0);
		//���ڶ���,����Ϊ��,����Ϊ��������Ϊ��
		mPath.lineTo(mTriangleWith/2, -mTriangleHeight);
		//�պ�
		mPath.close();	
	}

	//ָʾ����������ָ���й���
	public void scroll(int position, float positionOffSet) {
		
		int tabWith = getWidth()/mvisiable_tab_count;
		
		//��������X���ϵ�ƫ����
		mTranslationX = (int) (tabWith*(positionOffSet+position));
		
		//�����ƶ�����tab�ƶ������һ���ɼ���tabʱ,
		if(position >=(mvisiable_tab_count-1) && positionOffSet > 0 &&getChildCount() > mvisiable_tab_count){
			if(mvisiable_tab_count != 1){
				this.scrollTo(
						(position-(mvisiable_tab_count-1))*tabWith + (int)(tabWith*positionOffSet),
						0);
			}else{
				this.scrollTo(position*tabWith+(int)(tabWith*positionOffSet),0);
			}
			
		}
		
		//�����ػ�View������draw()���̣�������ͼ������Сû�б仯�Ͳ������layout()���̣�����ֻ������Щ����Ҫ�ػ�ġ�
		//��ͼ����˭(View�Ļ���ֻ���Ƹ�View ��ViewGroup�����������ViewGroup)����invalidate()�������ͻ��Ƹ���ͼ��
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
	
	//����title����Tab
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
	
	//���ù�����viewPager
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
			
			//������ֹ֮ͣǰ���˷�����һֱ�õ����á��������������ĺ���ֱ�Ϊ��
			//position :��ǰҳ�棬������������ҳ��
			//positionOffSet:��ǰҳ��ƫ�Ƶİٷֱ�
			//positionOffSetPixels:��ǰҳ��ƫ�Ƶ�����λ��   
			@Override
			public void onPageScrolled(int position, float positionOffSet,
					int positionOffSetPixels) {
				//tabWith:ÿ��tab�Ŀ��
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
		
		//���õ�ǰ��ʾ�ĵڼ���ҳ��
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
	
	//����ĳ��tab���ı�
	private void highLightTextView(int pos){
		reSetTextViewColor_normal();
		View view = getChildAt(pos);
		if(view instanceof TextView){
			((TextView) view).setTextColor(color_text_title_highLighr);
		}
	}
	
	//����ĳ���ı�����ɫΪnormal
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
