package mengcheng.tag.frontend.listener;

import java.util.List;

import mengcheng.tag.frontend.R;
import mengcheng.tag.frontend.support.ReloadFragmentContent;
import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabOnCheckedChangedListener implements OnCheckedChangeListener {

	private Activity mContext;
	private List<Fragment> mFragments;
	private int mFragmentContentId;
	private int mCurrentFragmentId = 0;
	private RadioGroup mRadioGroup;

	public TabOnCheckedChangedListener(Activity context,
			List<Fragment> fragments, RadioGroup radioGroup,int fragmentContentId) {
		mContext = context;
		mFragments = fragments;
		mFragmentContentId = fragmentContentId;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.i("ryktab", "radio is checked");
		for(int i=0;i<group.getChildCount();i++)
		{
			if(group.getChildAt(i).getId()==checkedId)
			{
				Log.i("ryktab", "mCurrentFragmentId="+mCurrentFragmentId);
				Log.i("ryktab", "radio is checked "+i);
				if (mCurrentFragmentId != i) {
					Fragment fragment=mFragments.get(i);
					FragmentManager fManager=mContext.getFragmentManager();
					FragmentTransaction fTransaction = fManager.beginTransaction();
					Log.i("ryktab", "begin transaction");
					fTransaction.hide(mFragments.get(mCurrentFragmentId));
					Log.i("ryktab", "hide");
					if (!fragment.isAdded())
					{
						fTransaction.add(mFragmentContentId, fragment,"f"+checkedId);
					}else {
						fTransaction.show(fragment);
					}
					fTransaction.commit();
					Log.i("ryktab", "commit transaction");
					mCurrentFragmentId=i;
					//若currentTag改变，则重新加载列表
					if (fragment instanceof ReloadFragmentContent)
					{
						ReloadFragmentContent sonTagFragment=(ReloadFragmentContent)fragment;
						sonTagFragment.reload();
					}
					
				}
			}
		}
	}
	

}
