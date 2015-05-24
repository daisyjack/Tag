package mengcheng.tag.frontend.taglist;

import java.text.SimpleDateFormat;
import java.util.Date;

import mengcheng.tag.frontend.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TagList extends ListView implements OnScrollListener {

	private float mDownY;
	private float mMoveY;
	private int mHeaderHeight;
	private int mCurrentScrollState;

	private final static int NONE_PULL_REFRESH = 0;
	private final static int ENTER_PULL_REFRESH = 1;
	private final static int OVER_PULL_REFRESH = 2;
	private final static int EXIT_PULL_REFRESH = 3;
	private int mPullRefreshState = 0;

	private final static int REFRESH_BACKING = 0;
	private final static int REFRESH_BACED = 1;
	private final static int REFRESH_RETURN = 2;
	private final static int REFRESH_DONE = 3;

	private LinearLayout mHeaderLinearLayout = null;
	private ImageView mHeaderPullDownImageView = null;
	private ImageView mHeaderReleaseDownImageView = null;
	private ProgressBar mHeaderProgressBar = null;
	private TextView mHeaderTextPull = null;
	private TextView mHeaderTextTime = null;
	private TextView mHeaderTime = null;

	private LinearLayout mFooterLinearLayout = null;
	private ProgressBar mFooterProgressBar = null;
	private TextView mFooterTextView = null;

	private SimpleDateFormat mSimpleDateFormat;

	private Object mRefreshObject = null;
	private RefreshListener mRefreshListener = null;

	public void setOnRefreshListener(RefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	public TagList(Context context) {
		this(context, null);
	}

	public TagList(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public TagList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(final Context context) {
		mHeaderLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.listview_header, null);
		addHeaderView(mHeaderLinearLayout);
		mHeaderProgressBar = (ProgressBar) findViewById(R.id.refresh_list_header_progressbar);
		mHeaderPullDownImageView = (ImageView) findViewById(R.id.refresh_list_header_pull_down);
		mHeaderReleaseDownImageView = (ImageView) findViewById(R.id.refresh_list_header_release);
		mHeaderTextPull = (TextView) findViewById(R.id.refresh_list_header_text_pull);
		mHeaderTextTime = (TextView) findViewById(R.id.refresh_list_header_text_time);
		mHeaderTime = (TextView) findViewById(R.id.refresh_list_header_time);

		mFooterLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.listview_footer, null);
		addFooterView(mFooterLinearLayout);
		mFooterProgressBar = (ProgressBar) findViewById(R.id.refresh_list_footer_progressbar);
		mFooterTextView = (TextView) findViewById(R.id.refresh_list_footer_textview);

		mFooterLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (context.getString(R.string.listview_footer_hint_normal)
						.equals(mFooterTextView.getText())) {
					mFooterTextView
							.setText(R.string.listview_footer_hint_ready);
					mFooterProgressBar.setVisibility(View.VISIBLE);
					if (mRefreshListener != null) {
						mRefreshListener.more();
					}
					mFooterTextView
							.setText(R.string.listview_footer_hint_normal);
				}
			}
		});

		setSelection(1);
		setOnScrollListener(this);
		measureView(mHeaderLinearLayout);
		mHeaderHeight = mHeaderLinearLayout.getMeasuredHeight();

		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		mHeaderTextPull.setText("上次刷新时间:");
		mHeaderTime.setText(mSimpleDateFormat.format(new Date()));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean isClick = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveY = event.getY();
			if (mPullRefreshState == OVER_PULL_REFRESH) {
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) ((mMoveY - mDownY) / 3),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
			}
			break;
		case MotionEvent.ACTION_UP:
			float currentY = event.getY();
			if (currentY == mDownY) {
				isClick = true;
			}
			if (mPullRefreshState == OVER_PULL_REFRESH
					|| mPullRefreshState == ENTER_PULL_REFRESH) {
				new Thread() {
					public void run() {
						Message msg;
						while (mHeaderLinearLayout.getPaddingTop() > 1) {
							msg = mHandler.obtainMessage();
							msg.what = REFRESH_BACKING;
							mHandler.sendMessage(msg);
							try {
								sleep(5);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						msg = mHandler.obtainMessage();
						if (mPullRefreshState == OVER_PULL_REFRESH) {
							msg.what = REFRESH_BACED;
						} else {
							msg.what = REFRESH_RETURN;
						}
						mHandler.sendMessage(msg);
					}
				}.start();
			}
			break;
		}
		if (isClick)
			performClick();
		return super.onTouchEvent(event);

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_BACKING:
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) (mHeaderLinearLayout.getPaddingTop() * 0.75f),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				break;
			case REFRESH_BACED:
				mHeaderTextPull.setText("正在加载...");
				mHeaderProgressBar.setVisibility(View.VISIBLE);
				mHeaderPullDownImageView.setVisibility(View.GONE);
				mHeaderReleaseDownImageView.setVisibility(View.GONE);
				mPullRefreshState = EXIT_PULL_REFRESH;
				// new Thread() {
				// public void run() {
				if (mRefreshListener != null) {
					mRefreshObject = mRefreshListener.refreshing();
				}
				Message msg2 = mHandler.obtainMessage();
				msg2.what = REFRESH_DONE;
				mHandler.sendMessage(msg2);
				// };
				// }.start();
				break;
			case REFRESH_RETURN:
				mHeaderTextPull.setText("下拉刷新");
				mHeaderProgressBar.setVisibility(View.INVISIBLE);
				mHeaderPullDownImageView.setVisibility(View.VISIBLE);
				mHeaderReleaseDownImageView.setVisibility(View.GONE);
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(), 0,
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState = NONE_PULL_REFRESH;
				setSelection(1);
				break;
			case REFRESH_DONE:
				mHeaderTextPull.setText("下拉刷新");
				mHeaderProgressBar.setVisibility(View.INVISIBLE);
				mHeaderPullDownImageView.setVisibility(View.VISIBLE);
				mHeaderReleaseDownImageView.setVisibility(View.GONE);
				mHeaderTextTime.setText("上次刷新时间:");
				mHeaderTime.setText(mSimpleDateFormat.format(new Date()));
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(), 0,
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState = NONE_PULL_REFRESH;
				setSelection(1);
				if (mRefreshListener != null) {
					mRefreshListener.refreshed(mRefreshObject);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& mHeaderLinearLayout.getBottom() >= 0
				&& mHeaderLinearLayout.getBottom() < mHeaderHeight) {
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = ENTER_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& mHeaderLinearLayout.getBottom() >= mHeaderHeight) {
			if (mPullRefreshState == ENTER_PULL_REFRESH
					|| mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = OVER_PULL_REFRESH;
				mDownY = mMoveY;
				mHeaderTextPull.setText("放开刷新数据");
				mHeaderPullDownImageView.setVisibility(View.GONE);
				mHeaderReleaseDownImageView.setVisibility(View.VISIBLE);
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem != 0) {
			if (mPullRefreshState == ENTER_PULL_REFRESH) {
				mPullRefreshState = NONE_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING) {
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				if (firstVisibleItem == 0) {
					setSelection(1);
				} else {
					setSelection(firstVisibleItem);
				}

			}
		}

		if ((totalItemCount - visibleItemCount - firstVisibleItem) <= 3) {

			mFooterTextView.setText(R.string.listview_footer_hint_ready);
			mFooterProgressBar.setVisibility(View.VISIBLE);
			if (mRefreshListener != null) {
				mRefreshListener.more();
			}
			mFooterTextView.setText(R.string.listview_footer_hint_normal);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		mCurrentScrollState = scrollState;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setSelection(1);
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);

	}

	public interface RefreshListener {
		Object refreshing();

		void refreshed(Object obj);

		void more();
	}

	public void finishFootView() {
		mFooterProgressBar.setVisibility(View.INVISIBLE);
		mFooterTextView.setText(R.string.listview_footer_hint_normal);
	}

	public void addFootView() {
		if (getFooterViewsCount() == 0) {
			addFooterView(mFooterLinearLayout);
		}
	}

	public void removeFootView() {
		removeFooterView(mFooterLinearLayout);
	}
}
