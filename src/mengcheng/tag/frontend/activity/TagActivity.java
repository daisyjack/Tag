package mengcheng.tag.frontend.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.tag2.EntryButtonActivity;
import com.tag2.LoginActivity;

import mengcheng.tag.frontend.R;
import mengcheng.tag.frontend.fragment.SeSonTagFragment;
import mengcheng.tag.frontend.fragment.TagContentFragment;
import mengcheng.tag.frontend.fragment.SonTagFragment;
import mengcheng.tag.frontend.listener.TabOnCheckedChangedListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author Luffy
 *
 */
public class TagActivity extends EntryButtonActivity {
	
	//private FragmentTabHost mFragmentTabHost;
	private int mCurrentTag=0;//屏幕上的tag的id
	private int mFatherTag=0;//屏幕上的tag的父tag的id
	private JsonObject mJsonObject=null;//屏幕上的tag的各种信息
	private Bundle mBundle;
	private RadioGroup mRg;
	private ImageButton mMeBt;//个人主页
	public List<Fragment> mFragments;
	private TextView mTitleTxView;
	private RadioButton mNameRadioBt;
	private ImageButton mBackBt;//后退
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tag);
		//initView();
		iniFooter();
		iniPopupData();
		mBackBt=(ImageButton)findViewById(R.id.back_button);
		mBackBt.setVisibility(View.GONE);
		
		mFragments=new ArrayList<Fragment>();
		mFragments.add(new TagContentFragment());
		mFragments.add(new SonTagFragment());
		mFragments.add(new SeSonTagFragment());
		mRg=(RadioGroup)findViewById(R.id.tabs_rg);
		mNameRadioBt=(RadioButton)mRg.getChildAt(0);
		mTitleTxView=(TextView)findViewById(R.id.tag_name_textview);
		if (savedInstanceState==null) {
			FragmentManager fManager = getFragmentManager();
			FragmentTransaction fTransaction = fManager.beginTransaction();
			fTransaction.add(R.id.tab_content, mFragments.get(0), "f0");
			fTransaction.commit();
		}
		mRg.setOnCheckedChangeListener(new TabOnCheckedChangedListener(this, mFragments, mRg,R.id.tab_content));
		
		
		mMeBt=(ImageButton)findViewById(R.id.me_button);
		mMeBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TagActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	public void initView()
	{
		/*mFragmentTabHost=(FragmentTabHost)findViewById(R.id.tag_fragment_tabhost);
		mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tag_content);
		
		TabSpec _TabSpec=null;
		TextView _TextView;
		Class _fragmentClass=null;
		String[] _TabTitles =getResources().getStringArray(R.array.tag_tab_titles);
		for (int i=0;i<_TabTitles.length+1;i++)
		{
			_TextView=(TextView)getLayoutInflater().inflate(R.layout.tag_tab_item, null);
			switch(i)
			{
			case 0:
				_fragmentClass=TagContentFragment.class;
				_TextView.setText("瑜伽");
				_TabSpec=mFragmentTabHost.newTabSpec("currenttab").setIndicator(_TextView);
				break;
			case 1:
				_fragmentClass=sonTagFragment.class;
				_TextView.setText(_TabTitles[i-1]);
				_TabSpec=mFragmentTabHost.newTabSpec(_TabTitles[i-1]).setIndicator(_TextView);
				break;
			case 2:
				_fragmentClass=SeSonTagFragment.class;
				_TextView.setText(_TabTitles[i-1]);
				_TabSpec=mFragmentTabHost.newTabSpec(_TabTitles[i-1]).setIndicator(_TextView);
			}
			mBundle=new Bundle();
			mBundle.putInt("currenttag", _currentTag);
			mBundle.putInt("fathertag", _fatherTag);
			mFragmentTabHost.addTab(_TabSpec, _fragmentClass, mBundle);
		}
		//mFragmentTabHost.setCurrentTab(1);
*/	
		mRg=(RadioGroup)findViewById(R.id.tabs_rg);
		mNameRadioBt=(RadioButton)mRg.getChildAt(0);
		mTitleTxView=(TextView)findViewById(R.id.tag_name_textview);
		mFragments=new ArrayList<Fragment>();
		mFragments.add(new TagContentFragment());
		mFragments.add(new SonTagFragment());
		mFragments.add(new SeSonTagFragment());
		FragmentManager fManager=getFragmentManager();
		FragmentTransaction fTransaction=fManager.beginTransaction();
		fTransaction.add(R.id.tab_content, mFragments.get(0), "f0");
		fTransaction.commit();
		mRg.setOnCheckedChangeListener(new TabOnCheckedChangedListener(this,mFragments, mRg,R.id.tab_content));
	}
	
	/**
	 * 
	 * @param thisFragment the instance of current Fragment(this)
	 * @param tagId the id of the pressed tag 
	 */

	public void showDetails(Fragment thisFragment,int tagId)
	{
/*		TagContentFragment fragment=(TagContentFragment)mFragments.get(0);
		FragmentManager fManager=getFragmentManager();
		FragmentTransaction fTransaction=fManager.beginTransaction();
		fTransaction.hide(thisFragment);
		fTransaction.show(fragment);
		fTransaction.commit();*/
		
		mFatherTag=mCurrentTag;
		mCurrentTag=tagId;
		RadioButton radioButton=(RadioButton)mRg.getChildAt(0);
		radioButton.setChecked(true);
	}
	
	

	@Override
	protected String getUrl() {
		// TODO Auto-generated method stub
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

	public TextView getmTitleTxView() {
		return mTitleTxView;
	}

	public void setmTitleTxView(TextView mTitleTxView) {
		this.mTitleTxView = mTitleTxView;
	}

	public RadioButton getmNameRadioBt() {
		return mNameRadioBt;
	}

	public void setmNameRadioBt(RadioButton mNameRadioBt) {
		this.mNameRadioBt = mNameRadioBt;
	}

	public JsonObject getmJsonObject() {
		return mJsonObject;
	}

	public void setmJsonObject(JsonObject mJsonObject) {
		this.mJsonObject = mJsonObject;
	}
	
	

	
	
	
}
