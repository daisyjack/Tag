package mengcheng.tag.frontend.fragment;

import mengcheng.tag.frontend.R;
import mengcheng.tag.frontend.taglist.MDate;
import mengcheng.tag.frontend.taglist.TagAdapter;
import mengcheng.tag.frontend.taglist.TagBrief;
import mengcheng.tag.frontend.taglist.TagList;
import mengcheng.tag.frontend.taglist.TagList.RefreshListener;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class SonTagFragment extends Fragment implements RefreshListener {

	private int mCurrentTag;
	private int mFatherTag;
	TagList taglist;
	SimpleAdapter adapter;
	TagAdapter tagadpter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("ryk", "2onCreateView");
		View _View = inflater.inflate(R.layout.tag_list_view, null);

		taglist = (TagList) _View.findViewById(R.id.taglist);
		// adapter = new SimpleAdapter(getActivity(), getData(),
		// R.layout.text_tag, new String[] { "tagName", "parent",
		// "content" }, new int[] { R.id.title, R.id.parent,
		// R.id.content });
		// taglist.setAdapter(adapter);
		tagadpter = new TagAdapter(_View.getContext(), R.layout.text_tag,
				getActivity());
		taglist.setAdapter(tagadpter);
		taglist.setOnRefreshListener(this);

		initTagListData();
		taglist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击一个Item然后跳转到相应的TAG页面
				Log.i("wb", "click item " + position);

			}
		});
		return _View;
	}

	private void initTagListData() {
		// TODO Auto-generated method stub
		// String childtag = getChildTag();
		TagBrief tagbrief = new TagBrief();
		tagbrief.setAuthentic(false);
		tagbrief.setAuthor("Polish");
		tagbrief.setContent("just a test");
		tagbrief.setCreateTime(new MDate("2015.03.01"));
		tagbrief.setTagID(0);
		tagbrief.setTitle("title");
		tagbrief.setType(1);
		tagbrief.setUpdateTime(new MDate("2015.03.02"));
		tagbrief.setUrl(null);
		tagbrief.setParentTag("parent");
		for (int i = 0; i < 30; i++) {
			tagadpter.addTag(tagbrief);
			Log.i("wb", "init," + i);
		}
		tagadpter.notifyDataSetChanged();
	}

	private String getChildTag() {
		// TODO Auto-generated method stub
		// JSONTokener jsonpaser = new JSONTokener();
		// return json;
		return null;
	}

	public int getmCurrentTag() {
		return mCurrentTag;
	}

	public void setmCurrentTag(int mCurrentTag) {
		this.mCurrentTag = mCurrentTag;
	}

	public int getmFatherTag() {
		return mFatherTag;
	}

	public void setmFatherTag(int mFatherTag) {
		this.mFatherTag = mFatherTag;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i("ryk", "2onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.i("ryk", "2onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("ryk", "2onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("ryk", "2onDestroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("ryk", "2onDestroyView");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("ryk", "2onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("ryk", "2onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("ryk", "2onResume");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("ryk", "2onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("ryk", "2onStop");
	}

	public void de() {

	}

	@Override
	public Object refreshing() {
		// TODO Auto-generated method stub
		Log.i("wb", "下拉刷新");
		TagBrief tagbrief = new TagBrief();
		tagbrief.setAuthentic(false);
		tagbrief.setAuthor("Polish");
		tagbrief.setContent("我是刷新出来的");
		tagbrief.setCreateTime(new MDate("2015.03.01"));
		tagbrief.setTagID(0);
		tagbrief.setTitle("title");
		tagbrief.setType(1);
		tagbrief.setUpdateTime(new MDate("2015.03.02"));
		tagbrief.setUrl(null);
		tagbrief.setParentTag("parent");
		tagadpter.addTag(tagbrief, true);
		tagadpter.notifyDataSetChanged();
		return null;

	}

	@Override
	public void refreshed(Object obj) {
		// 下拉刷新
		Log.i("wb", "刷新完成");
	}

	@Override
	public void more() {
		// 加载更多
		Log.i("wb", "加载更多");
		TagBrief tagbrief = new TagBrief();
		tagbrief.setAuthentic(false);
		tagbrief.setAuthor("Polish");
		tagbrief.setContent("我是加载出来的");
		tagbrief.setCreateTime(new MDate("2015.03.01"));
		tagbrief.setTagID(0);
		tagbrief.setTitle("title");
		tagbrief.setType(1);
		tagbrief.setUpdateTime(new MDate("2015.03.02"));
		tagbrief.setUrl(null);
		tagbrief.setParentTag("parent");
		tagadpter.addTag(tagbrief);
		tagadpter.notifyDataSetChanged();

	}
}
