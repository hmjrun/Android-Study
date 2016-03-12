package com.hmj.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private ListView listview;
	private ArrayAdapter<String> arr_adapter;
	private SimpleAdapter simp_adapter;
	private List<Map<String,Object>> datalist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listview = (ListView) findViewById(R.id.listView);
		
		/*1, 新建一个适配器ArrayAdapter<String>(context,layout,data)
		 * arg1:上下文
		 * arg2:当前ListView加载的每一个列表所对应的布局文件
		 * arg3:数据源
		 */
		//2, 适配器ArrayAdapter加载数据源
		String[] arr_data = {"huangmj","黄","mei军","1234"};
		arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr_data);
		//3, 视图（ListView）加载适配器ArrayAdapter
		//listview.setAdapter(arr_adapter);
		
		/*1, 新建一个适配器SimpleAdapter(context, data, resource, from, to);
		 * arg1:context>上下文
		 * arg2:data>	数据源(List<? extends Map<String,?>> data) 一个Map所组成的list集合
		 * 				每一个Map都会去对应ListView 列表的一行
		 * 				每一个Map(键-值对)中的键必须包含所有form中所指定的键
		 * arg3:resource>列表布局文件
		 * arg4:from>	Map中的键名
		 * arg5:to>		绑定数据视图的ID，与from成对应关系
		 */
		//2, 适配器SimpleAdapter加载数据源
		datalist = new ArrayList<Map<String,Object>>();
		datalist = getdata();//获取数据
		simp_adapter = new SimpleAdapter(this,datalist , R.layout.item, new String[]{"pic","text"}, new int[]{R.id.pic,R.id.text});
		//3, 视图（ListView）加载适配器SimpleAdapter
		
		listview.setAdapter(simp_adapter);
		
		//设置对listview 的监听item点击和滚动事件
		new ListviewListener(this, listview,datalist,simp_adapter);
	}

	private List<Map<String,Object>> getdata(){
		for(int i=0;i<20;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("pic", R.drawable.ic_launcher);
			map.put("text", "listview黄美军");
			datalist.add(map);
		}
		return datalist;
	}

}
