package com.tag2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import mengcheng.tag.frontend.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public abstract class EntryButtonActivity extends Activity implements OnClickListener{
	private final static int SEL_WORLD = 1;
	private final static int SEL_FOLLOW = 2;
	private final static int SEL_TALK = 3;
	private final static int SEL_ME = 4;
	PopupWindow pwMore;
	List<Map<String, String>> moreList;
	private ListView lvPopupList;
	private final int NUM_OF_VISIBLE_LIST_ROWS = 4;
	
	
	private static int Selection = 0;
	ImageButton btnWorld,btnFollow,btnTalk,btnMe,btnAdd,btnEntry;
	
	protected void iniFooter() {
		btnWorld = (ImageButton) findViewById(R.id.world_button);
		btnFollow = (ImageButton) findViewById(R.id.follow_button);
		btnTalk = (ImageButton) findViewById(R.id.talk_button);
		btnMe = (ImageButton) findViewById(R.id.me_button);
		btnWorld.setOnClickListener(this);
		btnFollow.setOnClickListener(this);
		btnTalk.setOnClickListener(this);
		btnMe.setOnClickListener(this);
	}
	
	@SuppressLint("InflateParams") 
	protected void iniPopupData() {
		btnEntry = (ImageButton) findViewById(R.id.entry_button);
		moreList = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		map = new HashMap<String, String>();
		map.put("share_key", "发布Tag");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "扫一扫");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "生成二维码");
		moreList.add(map);
		map = new HashMap<String, String> ();
		map.put("share_key", "申请认证");
		moreList.add(map);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMore = new PopupWindow(layout);
		pwMore.setFocusable(true);
		SimpleAdapter sa = new SimpleAdapter(getApplicationContext(), moreList,
				R.layout.list_item_popupwindow, new String[] { "share_key" },
				new int[] { R.id.tv_list_item });
		lvPopupList.setAdapter(sa);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					
					break;
				case 1:
					Toast.makeText(getApplicationContext(), "开始扫码", Toast.LENGTH_SHORT).show();
					Intent startScan = new Intent(getApplicationContext(),com.zxing.activity.CaptureActivity.class);
					startActivityForResult(startScan, 0);
					
					break;
				case 2:
					String url = getUrl();
					if (url == null) {
						Toast.makeText(getApplicationContext(), "不要乱点哦",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent i = new Intent(getApplicationContext(),BarcodeActivity.class);
						i.putExtra("url", url);
						startActivity(i);
					}
					break;
				case 3:
					
					break;
				}
		
			}
		});

		
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		pwMore.setWidth(lvPopupList.getMeasuredWidth()+10);
		pwMore.setHeight((lvPopupList.getMeasuredHeight()+10)
				* NUM_OF_VISIBLE_LIST_ROWS-2);

		pwMore.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.back_popupwindow));
		pwMore.setOutsideTouchable(true);
		
		
		btnEntry.setOnClickListener(this);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			Bundle b = data.getExtras();
			String str = b.getString("result");
			System.out.println(str);
			break;
		default:
			break;	
		}
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.entry_button:
			if (pwMore.isShowing()) {
				pwMore.dismiss();
			} else {
				pwMore.showAsDropDown(v);
			}
			break;
		case R.id.world_button:
			if (Selection!=SEL_WORLD) {
				Selection =SEL_WORLD ;
			}
			Toast.makeText(getApplicationContext(), "world", Toast.LENGTH_SHORT).show();
			Log.i("entry", "world");
			break;
		case R.id.follow_button:
			if (Selection!=SEL_FOLLOW) {
				Selection = SEL_FOLLOW;
			}
			Toast.makeText(getApplicationContext(), "follow", Toast.LENGTH_SHORT).show();
			break;
		case R.id.talk_button:
			if (Selection!=SEL_TALK) {
				Selection = SEL_TALK;
			}
			Toast.makeText(getApplicationContext(), "talk", Toast.LENGTH_SHORT).show();
			break;
		case R.id.me_button:
			if (Selection != SEL_ME) {
				Selection = SEL_ME;
				
				Context ctx = getApplicationContext();
				SharedPreferences sp = ctx.getSharedPreferences("Client-data", MODE_PRIVATE);
				String cookie = sp.getString("cookie", "none");
				String userID = sp.getString("user_id", "none");
				if (cookie == "none") {
					Intent i = new Intent(getApplicationContext(),LoginActivity.class);
					startActivity(i);
				} else {
					Intent i = new Intent(getApplicationContext(),SelfCenterActivity.class);
					i.putExtra("user_id", userID);
					startActivity(i);
				}
			}
			Toast.makeText(getApplicationContext(), "me", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	protected abstract String getUrl();
}
