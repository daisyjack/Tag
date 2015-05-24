package mengcheng.tag.frontend.fragment;

import mengcheng.tag.frontend.R;
import mengcheng.tag.frontend.activity.TagActivity;
import mengcheng.tag.frontend.support.ReloadFragmentContent;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SeSonTagFragment extends Fragment implements ReloadFragmentContent{

	private int mCurrentTag;
	private int mFatherTag;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("ryk", "3onCreateView");
		View _View=inflater.inflate(R.layout.fragment_tag_content, null);
		Button _Button=(Button)_View.findViewById(R.id.to_father_tag);
		_Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TagActivity tagActivity=(TagActivity)getActivity();
				tagActivity.showDetails(SeSonTagFragment.this, 9);
			}
		});
		return _View;
	}
	
	

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
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
		Log.i("ryk", "3onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.i("ryk", "3onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("ryk", "3onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("ryk", "3onDestroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("ryk", "3onDestroyView");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("ryk", "3onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("ryk", "3onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("ryk", "3onResume");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("ryk", "3onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("ryk", "3onStop");
	}
	public void de()
	{
		
	}
}
