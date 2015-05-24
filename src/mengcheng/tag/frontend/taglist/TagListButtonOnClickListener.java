package mengcheng.tag.frontend.taglist;

import mengcheng.tag.frontend.R;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

public class TagListButtonOnClickListener implements OnClickListener {
	private int position;
	private Context context;

	public TagListButtonOnClickListener(int position, Context context) {
		// TODO Auto-generated constructor stub
		this.position = position;
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share: {
			Log.i("wb", "点击转发");
			break;
		}
		case R.id.follow: {
			Log.i("wb", "点击关注");
			break;
		}
		case R.id.like: {
			Log.i("wb", "点击喜欢");
			break;
		}
		case R.id.more: {
			MoreOptionPopWindow moreOption = new MoreOptionPopWindow(context);
			moreOption.showAsDropDown(v);
			moreOption.showAtLocation(v,  Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		}
		default:
			break;
		}
	}

	private void showPopupWindow(View view) {

	}

}
