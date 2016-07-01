package com.hmj.android_customize_view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPagerSimpleFragment extends Fragment {

	private String mTitle;
	public static final String BUNDEL_TITLE="title";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//��Bundle��ͨ����ֵ�Ե���ʽȡ������title
		Bundle bundle = getArguments();
		if(bundle != null){
			mTitle = bundle.getString(BUNDEL_TITLE);
		}
		
		//ֱ��newһ��TextView
		TextView tv = new TextView(getActivity());
		tv.setText(mTitle);
		tv.setTextColor(Color.GREEN);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	
	//��fragment��Ҫ����ʱ
	//ͨ��newInstance������Bundle��ֵ�Եķ�ʽ��Fragment���ݲ��� newInstance(String title)
	public static ViewPagerSimpleFragment newInstance(String title){
		Bundle bundle = new Bundle();
		bundle.putString(BUNDEL_TITLE, title);
		ViewPagerSimpleFragment vpsFragment = new ViewPagerSimpleFragment();
		vpsFragment.setArguments(bundle);
		return vpsFragment;
	}
}
