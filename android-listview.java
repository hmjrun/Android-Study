package com.hmj.listview;

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

public class ListviewListener  implements OnItemClickListener,OnScrollListener {
	private ListView listview;
	private Context context;
	private List<Map<String,Object>> datalist;
	private SimpleAdapter simp_adapter;
	
	public ListviewListener(Context c,ListView ls,List<Map<String,Object>> dalist,SimpleAdapter s_adapter){
		datalist = dalist;
		listview = ls;
		context = c;
		simp_adapter = s_adapter;
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollstate) {
		// TODO Auto-generated method stub
		switch (scrollstate) {
		case SCROLL_STATE_FLING:
			Toast.makeText(context, "手指离开，屏幕继续滑动", Toast.LENGTH_SHORT).show();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("pic", R.drawable.ic_launcher);
			map.put("text","增加的内容");
			datalist.add(map);
			simp_adapter.notifyDataSetChanged();
			break;
			
		case SCROLL_STATE_IDLE:
			Toast.makeText(context, "视图停止滑动", Toast.LENGTH_SHORT).show();
			break;
			
		case SCROLL_STATE_TOUCH_SCROLL:
			Toast.makeText(context, "手指没有离开屏幕，视图正在滑动", Toast.LENGTH_SHORT).show();
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		String text = listview.getItemAtPosition(position)+"";
		
		Toast.makeText(context, "position="+position+" text="+text, Toast.LENGTH_SHORT).show();
	}

}

