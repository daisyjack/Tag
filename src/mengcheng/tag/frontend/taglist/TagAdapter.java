package mengcheng.tag.frontend.taglist;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mengcheng.tag.frontend.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TagAdapter extends BaseAdapter {

	private Context context;
	private Activity activity;
	private List<TagBrief> listViewData;
	private int layoutID;

	// private final int ROOTTAG = 0;
	// private final int TEXTTAG = 1;
	// private final int IMAGETAG = 2;
	// private final int AUDIOTAG = 3;
	// private final int VIDEOTAG = 4;
	// private final int REFERENCETAG = 5;

	public TagAdapter(Context context, int layoutID, Activity activity) {
		this.context = context;
		this.layoutID = layoutID;
		listViewData = new ArrayList<TagBrief>();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listViewData == null) {
			return 0;
		} else {
			return listViewData.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listViewData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TagBrief tag = listViewData.get(position);
		ViewItemHolder viewItemHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.text_tag, null);
			viewItemHolder = new ViewItemHolder();
			viewItemHolder.tagName = (TextView) convertView
					.findViewById(R.id.title);
			viewItemHolder.parent = (TextView) convertView
					.findViewById(R.id.parent);
			viewItemHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewItemHolder.content_image = (ImageView) convertView
					.findViewById(R.id.content_image);
			viewItemHolder.content_video = (ImageView) convertView
					.findViewById(R.id.content_video);
			viewItemHolder.share = (ImageView) convertView
					.findViewById(R.id.share);
			viewItemHolder.follow = (ImageView) convertView
					.findViewById(R.id.follow);
			viewItemHolder.like = (ImageView) convertView
					.findViewById(R.id.like);
			viewItemHolder.more = (ImageView) convertView
					.findViewById(R.id.more);
			convertView.setTag(viewItemHolder);
		} else {
			viewItemHolder = (ViewItemHolder) convertView.getTag();
		}
		viewItemHolder.tagName.setText(tag.getTitle());
		viewItemHolder.parent.setText(tag.getParentTag());
		viewItemHolder.content.setText(tag.getContent());
		if (tag.getUrl() != null) {
			Drawable drawable;
			try {
				drawable = Drawable.createFromStream(new URL(tag.getUrl()
						.get(0)).openStream(), "image.jpg");
				viewItemHolder.content_image.setImageDrawable(drawable);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		initButton(viewItemHolder, position);
		return convertView;
	}

	private void initButton(ViewItemHolder viewItemHolder, int position) {
		// TODO Auto-generated method stub
		viewItemHolder.share.setFocusable(true);
		viewItemHolder.share
				.setOnClickListener(new TagListButtonOnClickListener(position, context));
		viewItemHolder.follow.setFocusable(true);
		viewItemHolder.follow
				.setOnClickListener(new TagListButtonOnClickListener(position, context));
		viewItemHolder.like.setFocusable(true);
		viewItemHolder.like
				.setOnClickListener(new TagListButtonOnClickListener(position, context));
		viewItemHolder.more.setFocusable(true);
		viewItemHolder.more
				.setOnClickListener(new TagListButtonOnClickListener(position, context));
	}

	public void addTag(TagBrief tag) {
		listViewData.add(tag);
	}

	public void addTag(TagBrief tag, boolean insertHead) {
		if (insertHead) {
			listViewData.add(0, tag);
		} else {
			listViewData.add(tag);
		}
	}

	public TagBrief getTag(int position) {
		if (position < 0 || position > listViewData.size() - 1) {
			return null;
		} else {
			return listViewData.get(position);
		}
	}

	public void clear() {
		listViewData.clear();
	}
}

class ViewItemHolder {
	TextView tagName;
	TextView parent;
	TextView content;
	ImageView content_image;
	ImageView content_video;
	ImageView share;
	ImageView follow;
	ImageView like;
	ImageView more;
}
