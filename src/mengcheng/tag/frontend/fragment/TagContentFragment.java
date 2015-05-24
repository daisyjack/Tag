package mengcheng.tag.frontend.fragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mengcheng.tag.frontend.R;
import mengcheng.tag.frontend.R.layout;
import mengcheng.tag.frontend.activity.TagActivity;
import mengcheng.tag.frontend.listener.TagBtListener;
import mengcheng.tag.frontend.support.Locate;
import mengcheng.tag.frontend.support.LocateJson;
import mengcheng.tag.frontend.support.ReloadFragmentContent;
import android.R.integer;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TagContentFragment extends Fragment implements LocateJson,ReloadFragmentContent {

	private int mCurrentTag = 0;//此Fragment显示的tag id
	private int mFatherTag = 0;//此Fragment显示的tag的父tag的id
	private TagActivity mTagActivity;
	private View mView;
	private JsonObject mJsonObject=null;//此Fragment显示的tag的各种信息
	private JsonParser mJsonParser;
	private LocationManager mLocationManager;
	private Locate mLocate;
	private Handler mHandler;
	private WebView mWebView;
	private Button mToFatherBt; // 返回父级按钮
	private TextView mCurrentLocTexV; //当前定位
	private ImageButton mRelocateBt;// 重新定位按钮
	private TextView mCreatorTxV;//创建者
	private TextView mMark;//评分
	private TextView mSonTag;//子tag
	private TextView mContributer;//贡献者
	private TextView mLiker;//喜欢者
	private ImageButton mShareBt;// 分享按钮
	private ImageButton mFollowBt;// 关注按钮
	private ImageButton mLikeBt; // 喜欢按钮
	private ImageButton mMoreBt;// 更多按钮
	private boolean mMoreBtFlag=false;//更多按钮状态，false为未按下，true为已按下
	private PopupWindow mMorePopupWindow;// 更多按钮下拉菜单
	private ListView mMoreListView;// 更多按钮下拉菜单的listview
	private List<Map<String, String>> mMoreList;// mMoreListView所需数据
	public static final int LOCATE_MSG = 0;
	public static final int BACK = 1;
	public static final int REPOST = 2;
	public static final int RELOAD=3;

	private String blank = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	private String notice1 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
			+ "<h3 align='center'>关于关闭电话银行语音系统查询动态密码功能的公告</h3></head>"
			+ "<body>"
			+ "<p align='center'><i>发布日期：2011-04-25</i>"
			+ "</p><p>尊敬的客户："
			+ "</p><p>"
			+ blank
			+ "根据优化电话银行相关服务功能的整体安排，我行决定自4月25日起，在95595电话银行语音系统中，停止受理查询手机动态密码功能。给您带来的不便之处敬请谅解。如有问题，请致电我行24小时服务热线95595。"
			+ "</p><p>"
			+ blank
			+ "感谢您长期以来对我行的关注、支持与厚爱!"
			+ "</p><p>"
			+ blank
			+ "特此公告。"
			+ "</p><p align='right'>中国光大银行"
			+ "</p><p align='right'>2011年4月25日</p>"
			+ "<img  src='http://avatar.csdn.net/7/4/2/1_shuai123456.jpg' /></body></html>";

	private String htmlData = "&amp;lt;p style=&amp;quot;text-align:center&amp;quot;&amp;gt;　　&amp;lt;img src=&amp;quot;http://f.expoon.com/news/2013/10/09/881020.jpg&amp;quot; title=&amp;quot;旅游商品博览会资料图&amp;quot; alt=&amp;quot;旅游商品博览会资料图&amp;quot; /&amp;gt;&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:center&amp;quot;&amp;gt;&amp;lt;strong&amp;gt;旅游商品博览会资料图&amp;lt;/strong&amp;gt;&amp;lt;br /&amp;gt;&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;&amp;lt;strong&amp;gt;　　基本信息&amp;lt;/strong&amp;gt;&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　英文名称： Beijing Tourism Commodity Fair&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　所属行业： 旅游/酒店/餐饮&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展会时间： 2013.10.10-2013.10.13&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　所属地区： 中国北京朝阳区&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展会认证： 网展认证&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展会地址： 北京市朝阳区东三环北路16号&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　场 &amp;amp;nbsp; &amp;amp;nbsp;馆： 全国农业展览馆&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　主办单位： 北京市旅游发展委员会&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　承办单位： 北京京展佳会国际会议展览有限公司&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　支持单位： 中国旅游协会&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　&amp;lt;strong&amp;gt;展会信息&amp;lt;/strong&amp;gt;&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　主办单位：北京市旅游发展委员会&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　指导单位：中华人民共和国国家旅游局&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　支持单位：中国旅游协会&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　承办单位：北京京展佳会国际会议展览有限公司&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展览时间：2013年10月10日至13日(布展时间10月08日-09日)&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展览地点：北京全国农业展览馆&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展览面积：30000 &amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　预计商户：1000家&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　预计观众：10万人次&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　【秉承理念】&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　展会主题：平台同享 合作共赢&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　办展宗旨：展示精品、扩大交流、加强合作、促进交易&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;　　举办目的：着力打造三个平台，一是，打造旅游商品研发设计、生产营销、采购消费等上下游多方对接交流的平台;二是，打造旅游商品行业买家与卖家洽商交易的平台;三是，打造旅游商品行业发展的引领示范平台。&amp;lt;/p&amp;gt;&amp;lt;p style=&amp;quot;text-align:justify;&amp;quot;&amp;gt;";

	//
	StringEntity _Entity;
	String _String;

	//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mTagActivity=(TagActivity)getActivity();
		Log.i("ryk", "1onCreate");
		
		//mView=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tag_content, null);

		mHandler = new Handler() {

			/**
			 * 把Json载入UI
			 * 修改TagActivity的mCurrentTag和mFatherTag
			 */
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == LOCATE_MSG) {
					Log.i("rykhandler", "handler");
					Log.i("rykhandler", "handler" + mJsonObject.toString());
					 Log.i("rykJson", mJsonObject.toString());
					/*mJsonObject = (JsonObject)mJsonParser.parse("{tag_id:1234,tag_name:'蓝田三舍',mark:4.5,son_num:123,"
							+ "contributer:124,like_num:456,father_id:4321,father_name:'浙江大学',"
							+ "share:true,follow:true,like:false,creator:'任彦昆'}");*/
					mCurrentTag=mJsonObject.getAsJsonPrimitive("tag_id").getAsInt();
					Log.i("rykJson", "mCurrentTag"+mCurrentTag);
					mFatherTag=mJsonObject.getAsJsonPrimitive("father_id").getAsInt();
					mTagActivity.setmCurrentTag(mCurrentTag);
					mTagActivity.setmFatherTag(mFatherTag);
					 
					String tagName=mJsonObject.getAsJsonPrimitive("tag_name").getAsString();
					//String tagName=mJsonObject.getAsJsonObject("tag_name").getAsString();
					Log.i("rykJson", "tag_name"+tagName);
					mTagActivity.getmTitleTxView().setText(tagName);
					mTagActivity.getmNameRadioBt().setText(tagName);
					mToFatherBt.setText(mJsonObject.getAsJsonPrimitive("father_name").getAsString());
					mCurrentLocTexV.setText(tagName);
					mMark.setText(mJsonObject.getAsJsonPrimitive("mark").getAsString());
					mSonTag.setText(mJsonObject.getAsJsonPrimitive("son_num").getAsString());
					mContributer.setText(mJsonObject.getAsJsonPrimitive("contributer").getAsString());
					mLiker.setText(mJsonObject.getAsJsonPrimitive("like_num").getAsString());
					mCreatorTxV.setText(mJsonObject.getAsJsonPrimitive("creator").getAsString());
					
					mShareBt.setImageDrawable(initialButton(TagBtListener.SHARE_BT, mJsonObject.getAsJsonPrimitive("share").getAsBoolean()));
					mLikeBt.setImageDrawable(initialButton(TagBtListener.LIKE_BT, mJsonObject.getAsJsonPrimitive("like").getAsBoolean()));
					mFollowBt.setImageDrawable(initialButton(TagBtListener.FOLLOW_BT, mJsonObject.getAsJsonPrimitive("follow").getAsBoolean()));
					mShareBt.setOnClickListener(new TagBtListener(getActivity(),mCurrentTag,
							getResources().getString(R.string.share_url), mShareBt,
							TagBtListener.SHARE_BT,mJsonObject.getAsJsonPrimitive("share").getAsBoolean()));
					mLikeBt.setOnClickListener(new TagBtListener(getActivity(),mCurrentTag,
							getResources().getString(R.string.like_url), mLikeBt,
							TagBtListener.LIKE_BT,mJsonObject.getAsJsonPrimitive("like").getAsBoolean()));
					mFollowBt.setOnClickListener(new TagBtListener(getActivity(),mCurrentTag,
							getResources().getString(R.string.share_url), mFollowBt,
							TagBtListener.FOLLOW_BT,mJsonObject.getAsJsonPrimitive("follow").getAsBoolean()));
				}
			}
		};

		// 定位
		mLocationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		mLocate = new Locate(mLocationManager, getActivity(), mHandler, this);
		mLocate.locate();

		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * 
		 * 
		 * 
		 * 
		 * //网络交互测试 HttpClient _Client=new DefaultHttpClient(); HttpPost
		 * _Post=new HttpPost("http://121.40.88.229:3000/tags/getroot"); HttpGet
		 * _Get=new HttpGet("http://www.baidu.com"); try { _Entity=new
		 * StringEntity("{}"); _Post.setEntity(_Entity); } catch
		 * (UnsupportedEncodingException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * try { HttpResponse _Response=_Client.execute(_Post);
		 * _String=EntityUtils.toString(_Response.getEntity());
		 * Log.i("rykhttp",_String ); } catch (ClientProtocolException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } //网络交互测试
		 * 
		 * Message _msg=mHandler.obtainMessage(LOCATE_MSG);
		 * mHandler.sendMessage(_msg); } }).start();
		 */
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("ryk", "1onCreateView");
		initData();
		initPopupWindow();
		mView=inflater.inflate(R.layout.fragment_tag_content, null);
		
		mRelocateBt = (ImageButton) mView.findViewById(R.id.relocate_bt);
		mToFatherBt = (Button) mView.findViewById(R.id.to_father_tag);

		mCurrentLocTexV=(TextView)mView.findViewById(R.id.current_location);
		mMark=(TextView)mView.findViewById(R.id.mark_bt);
		mSonTag=(TextView)mView.findViewById(R.id.son_tag_bt);
		mContributer=(TextView)mView.findViewById(R.id.contributer_bt);
		mLiker=(TextView)mView.findViewById(R.id.liker_bt);
		mCreatorTxV=(TextView)mView.findViewById(R.id.creater_bt);
		
		mFollowBt = (ImageButton) mView.findViewById(R.id.follow_bt);
		mLikeBt = (ImageButton) mView.findViewById(R.id.like_bt);
		mMoreBt = (ImageButton) mView.findViewById(R.id.more_bt);
		mShareBt = (ImageButton) mView.findViewById(R.id.share_bt);
		
		mToFatherBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCurrentTag=mFatherTag;
				mTagActivity.setmCurrentTag(mCurrentTag);
				/*
				 * 根据mCurrentTag请求，setmJsonObject(),getAcitvity.setmJsonObject(),然后修改mFatherTag和mTagAcivity.setmFatherTag(mFatherTag)
				 */
				Message msg=mHandler.obtainMessage(RELOAD);
				mHandler.sendMessage(msg);
			}
		});
		

		mMoreBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("button", "morebutton");
				if (mMorePopupWindow.isShowing()) {
					Log.i("button", "morebutton is showing");
					mMoreBt.setImageDrawable(getResources().getDrawable(R.drawable.more_bt_unpressed));
					mMorePopupWindow.dismiss();
				} else {
					
					Log.i("button", "morebutton is not showing");

					mMorePopupWindow.showAsDropDown(mMoreBt, -65, 0);
					mMoreBt.setImageDrawable(getResources().getDrawable(R.drawable.more_bt_pressed));
				}
			}
		});

		mRelocateBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLocate.locate();
			}
		});
		mToFatherBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				/**
				 * 请求父tag;修改TagActivity的mCurrentTag,mFatherTag，UI
				 */

				Log.i("ryk", "button");
			}
		});

		mWebView = (WebView) mView.findViewById(R.id.tag_content_view);
		mWebView.loadDataWithBaseURL("about:blank", notice1, "text/html",
				"utf-8", null);
		
		/*Message _msg = mHandler.obtainMessage(BACK);
		mHandler.sendMessage(_msg);*/
		//Log.i("ryk", "handlerfinish");
		
		return mView;

	}

	// more按钮所需数据初始化
	private void initData() {

		mMoreList = new ArrayList<Map<String, String>>();
		String[] _TabTitles = getResources().getStringArray(
				R.array.tag_tab_titles);
		Map<String, String> _Map;
		_Map = new HashMap<String, String>();
		_Map.put("More", "  纠错");
		mMoreList.add(_Map);
		_Map = new HashMap<String, String>();
		_Map.put("More", "  举报");
		mMoreList.add(_Map);

	}

	// more按钮PopupWindow的初始化设置
	private void initPopupWindow() {

		LayoutInflater _Inflater = LayoutInflater.from(getActivity());
		View _Layout = _Inflater
				.inflate(R.layout.task_detail_popupwindow, null);
		mMoreListView = (ListView) _Layout.findViewById(R.id.lv_popup_list);
		mMoreListView.setVerticalScrollBarEnabled(false);
		mMorePopupWindow = new PopupWindow(_Layout);
		//mMorePopupWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件
		mMoreListView.setAdapter(new SimpleAdapter(getActivity(), mMoreList,
				R.layout.list_item_popupwindow, new String[] { "More" },
				new int[] { R.id.tv_list_item }));
		mMoreListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(getActivity(),
								mMoreList.get(position).get("More"),
								Toast.LENGTH_SHORT).show();
					}
				});

		// 控制popupwindow的宽度和高度自适应
		mMoreListView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		mMorePopupWindow.setWidth(mMoreListView.getMeasuredWidth());
		mMorePopupWindow
				.setHeight((mMoreListView.getMeasuredHeight() + 20) * 2);

		// 控制popupwindow点击屏幕其他地方消失
		// 设置背景图片，不能在布局中设置要通过代码来设置
		mMorePopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_popup_public));

		//mMorePopupWindow.setOutsideTouchable(false);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
	}
	
	public Drawable initialButton(int type,boolean status)
	{
		Drawable drawable=null;
		if(status)
		{
			switch(type)
			{
			case TagBtListener.SHARE_BT:
				drawable = mTagActivity.getResources().getDrawable(
						R.drawable.share_bt_pressed);
				break;
			case TagBtListener.FOLLOW_BT:
				drawable = mTagActivity.getResources().getDrawable(
						R.drawable.follow_bt_pressed);
				break;
			case TagBtListener.LIKE_BT:
				drawable = mTagActivity.getResources().getDrawable(
						R.drawable.like_bt_pressed);
				break;
			}
		}else{
			switch(type)
			{
			case TagBtListener.SHARE_BT:
				drawable = mTagActivity.getResources().getDrawable(
						R.drawable.share_bt_unpressed);
				break;
			case TagBtListener.FOLLOW_BT:
				drawable = mTagActivity.getResources().getDrawable(
						R.drawable.follow_bt_unpressed);
				break;
			case TagBtListener.LIKE_BT:
				drawable = mTagActivity.getResources().getDrawable(
						R.drawable.like_bt_unpressed);
				break;
			}
		}
		return drawable;
	}
	

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		if (mTagActivity.getmCurrentTag()!=mCurrentTag)
		{
			mFatherTag=mCurrentTag;
			mCurrentTag=mTagActivity.getmCurrentTag();
			mTagActivity.setmFatherTag(mCurrentTag);
			/*
			 * 根据id请求，然后用setmJsonObject(),getAcitvity.setmJsonObject()
			 */
			
			Message msg=mHandler.obtainMessage(RELOAD);
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public JsonObject getJsonObject() {
		// TODO Auto-generated method stub
		return getmJsonObject();
	}

	@Override
	public void setJsonObject(JsonObject mJsonObject) {
		// TODO Auto-generated method stub
		setmJsonObject(mJsonObject);
	}

	public JsonObject getmJsonObject() {
		return mJsonObject;
	}

	public void setmJsonObject(JsonObject mJsonObject) {
		this.mJsonObject = mJsonObject;
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
		Log.i("ryk", "1onActivityCreated");
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("ryk", "1onDestroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("ryk", "1onDestroyView");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("ryk", "1onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("ryk", "1onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("ryk", "1onResume");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("ryk", "1onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("ryk", "1onStop");
	}
	public void showww()
	{
		Log.i("ryktab", "啦啦啦啦");
	}

}
