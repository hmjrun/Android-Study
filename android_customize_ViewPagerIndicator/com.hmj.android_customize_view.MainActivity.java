package com.hmj.android_customize_view;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.android_customize_view.R;
import com.hmj.UI.ViewPagerIndicator;

import android.os.Bundle;
import android.annotation.SuppressLint;

import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.Window;


@SuppressLint("NewApi")
public class MainActivity extends  FragmentActivity {
	
	private ViewPager mViewPager;
	private ViewPagerIndicator mPagerIndicator;
	
	private List<String> mTitles = Arrays.asList("����1","�ղ�2","�Ƽ�3","����4","�ղ�5","�Ƽ�6","����7","�ղ�8","�Ƽ�9");
	
	private List<ViewPagerSimpleFragment> mContent = new ArrayList<ViewPagerSimpleFragment>();
	private FragmentPagerAdapter mFragmentPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����ϵͳ��bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main2);
		
		//��ʼ������
		initView();
		initData();
		
		//��̬����Tab�������ı���
		mPagerIndicator.setVisiableTabCount(4);
		mPagerIndicator.setTabItemTitle(mTitles);
		
		//װ��ViewVager
		mViewPager.setAdapter(mFragmentPagerAdapter);
		
		//����Indicator��viewPager,�ڶ���������ʼ���ڼ���ҳ��
		mPagerIndicator.setIndicatorViewPager(mViewPager, 0);
	}

	private void initData() {
		
		for(String title : mTitles){
			ViewPagerSimpleFragment fragment = ViewPagerSimpleFragment.newInstance(title);
			mContent.add(fragment);
		}
	
		mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mContent.size();
			}
			@Override
			public Fragment getItem(int arg0) {
				return mContent.get(arg0);
			}
		};
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mPagerIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
	}

}
