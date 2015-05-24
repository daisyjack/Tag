package mengcheng.tag.frontend.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mengcheng.tag.frontend.R;

import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;







import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import android.R.integer;
import android.content.Context;
import android.content.Entity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class TagBtListener implements OnClickListener {

	private int mId;
	private JsonObject mJsonObject;
	private JsonParser mJsonParser;
	private String mUri;
	private StringEntity mEntity;
	private HttpClient mClient;
	private HttpPost mPost;
	private HttpResponse mResponse;
	private ImageButton mButton;
	private Context mContext;
	private int mType;
	private boolean mflag;//ture为按下，false为未按下
	public static final int SHARE_BT=0;
	public static final int FOLLOW_BT=1;
	public static final int LIKE_BT=2;
	public static final int MORE_BT=3;
	
	public TagBtListener(Context context,int id,String uri,ImageButton button,int type,boolean status) {
		// TODO Auto-generated constructor stub
		mContext=context;
		mId=id;
		//mUri=uri;
		mButton=button;
		mType=type;
		mflag=status;
		mJsonParser=new JsonParser();
		mJsonObject=new JsonObject();
	}
	@Override
	public void onClick(View v) {
		
		initial();
		mJsonObject.addProperty("tag_id", mId);
		mClient=new DefaultHttpClient();
		mPost=new HttpPost(mUri);
		try {
			mEntity=new StringEntity(mJsonObject.toString());
			mPost.setEntity(mEntity);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					mResponse=mClient.execute(mPost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JsonObject _JsonObject=(JsonObject)mJsonParser.parse(EntityUtils.toString(mResponse.getEntity()));
					Log.i("httpshare", _JsonObject.get("error").getAsString());
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
		
	}
	
	private void initial()
	{
		Drawable drawable=null;
		//判断按钮当前是否被按下
		if (!mflag) {
			switch (mType) {
			//判断是哪个按钮
			case SHARE_BT:
				mUri=mContext.getResources().getString(R.string.share_url);
				drawable = mContext.getResources().getDrawable(
						R.drawable.share_bt_pressed);
				break;
			case FOLLOW_BT:
				mUri=mContext.getResources().getString(R.string.follow_url);
				drawable = mContext.getResources().getDrawable(
						R.drawable.follow_bt_pressed);
				break;
			case LIKE_BT:
				mUri=mContext.getResources().getString(R.string.like_url);
				drawable = mContext.getResources().getDrawable(
						R.drawable.like_bt_pressed);
				break;
			}
		}else {
			switch (mType) {
			case SHARE_BT:
				mUri=mContext.getResources().getString(R.string.unshare_url);
				drawable = mContext.getResources().getDrawable(
						R.drawable.share_bt_unpressed);
				break;
			case FOLLOW_BT:
				mUri=mContext.getResources().getString(R.string.unfollow_url);
				drawable = mContext.getResources().getDrawable(
						R.drawable.follow_bt_unpressed);
				break;
			case LIKE_BT:
				mUri=mContext.getResources().getString(R.string.unlike_url);
				drawable = mContext.getResources().getDrawable(
						R.drawable.like_bt_unpressed);
				break;
			}
		}
		mflag=!mflag;
		mButton.setImageDrawable(drawable);
	}

}
