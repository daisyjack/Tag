package mengcheng.tag.frontend.taglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mengcheng.tag.frontend.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MoreOptionPopWindow extends PopupWindow {
	private LinearLayout layout;
	private ListView moreOptionListView;
	private TextView moreOptionItem;
	private List<Map<String, String>> moreList;

	public MoreOptionPopWindow(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		initItemData();
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.more_option, null);
		moreOptionListView = (ListView) layout
				.findViewById(R.id.more_option_listview);
		SimpleAdapter adapter = new SimpleAdapter(context, moreList,
				R.layout.more_option_item, new String[] { "option" },
				new int[] { R.id.more_option_text });
		moreOptionListView.setAdapter(adapter);
		moreOptionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("wb", "option");

			}
		});
		moreOptionListView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);

		this.setWidth(moreOptionListView.getMeasuredWidth());
		this.setHeight((moreOptionListView.getMeasuredHeight() + 0) * 5);

		// this.setWidth(LayoutParams.FILL_PARENT);
		// // 设置SelectPicPopupWindow弹出窗体的高
		// this.setHeight(LayoutParams.WRAP_CONTENT);

		this.setFocusable(true);
		this.setContentView(layout);

		// 控制popupwindow点击屏幕其他地方消失
		this.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.more_option));// 设置背景图片，不能在布局中设置，要通过代码来设置
		this.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

		/*
		 * this.setFocusable(true); this.setContentView(moreOptionListView); //
		 * 设置SelectPicPopupWindow弹出窗体的宽 this.setWidth(LayoutParams.FILL_PARENT);
		 * // 设置SelectPicPopupWindow弹出窗体的高
		 * this.setHeight(LayoutParams.WRAP_CONTENT);
		 */

	}

	private void initItemData() {
		moreList = new ArrayList<Map<String, String>>();
		Map<String, String> map;

		map = new HashMap<String, String>();
		map.put("option", "置顶");
		moreList.add(map);

		map = new HashMap<String, String>();
		map.put("option", "编辑");
		moreList.add(map);

		map = new HashMap<String, String>();
		map.put("option", "删除");
		moreList.add(map);

		map = new HashMap<String, String>();
		map.put("option", "纠错");
		moreList.add(map);

		map = new HashMap<String, String>();
		map.put("option", "举报");
		moreList.add(map);
	}

}
