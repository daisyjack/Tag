package mengcheng.tag.frontend.support;

import java.util.List;

import mengcheng.tag.frontend.activity.TagActivity;
import mengcheng.tag.frontend.fragment.TagContentFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Locate {
	private JsonObject mJsonObject;
	private JsonParser mJsonParser;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private Context mContext;
	private TagActivity mTagActivity;
	private Handler mHandler;
	private LocateJson mLocateJson;
	// mFlag=0代表已定位，mFlag=1代表未定位
	private int mFlag = 0;

	public Locate(LocationManager mLocationManager, Context mContext,Handler mHandler,LocateJson mLocateJson) {
		super();
		this.mLocationManager = mLocationManager;
		this.mContext = mContext;
		this.mHandler=mHandler;
		this.mLocateJson=mLocateJson;
		this.mTagActivity=(TagActivity)mContext;
		mJsonParser=new JsonParser();
	}

	public void locate() {
		// 定位
		Log.i("ryklocate", "locate start");
		mLocationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.i("ryklocata", provider.toString() + " locate start");
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.i("ryklocata", provider.toString() + " locate stop");
			}

			@Override
			public void onLocationChanged(Location location) {
				Log.i("ryklocate", "enter onlocationchanged");
				mLocationManager.removeUpdates(mLocationListener);
				Log.i("rykhandler",
						"经度：" + convertLocation(location.getLongitude())
								+ " 纬度："
								+ convertLocation(location.getLatitude()));
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i("ryklocate", "locationManager run");
						try {
							Thread.sleep(2000);
						} catch (Exception e) {
							// TODO: handle exception
						}
						mJsonObject = (JsonObject)mJsonParser.parse("{tag_id:1234,tag_name:'蓝田三舍',mark:4.5,son_num:123,"
								+ "contributer:124,like_num:456,father_id:4321,father_name:'浙江大学',"
								+ "share:true,follow:true,like:false,creator:'任彦昆'}");
						//修改TagContentFragment的JsonObject
						mLocateJson.setJsonObject(mJsonObject);
						//修改TagActivity的JsonObject
						mTagActivity.setmJsonObject(mJsonObject);
						
						Message _msg=mHandler.obtainMessage(TagContentFragment.LOCATE_MSG);
						mHandler.sendMessage(_msg);
					}
				}).start();
				
			}
		};
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder _Builder = new AlertDialog.Builder(mContext);
			_Builder.setMessage("请开启GPS");
			_Builder.setPositiveButton("确定", null).show();
			// Toast.makeText(mContext, "请开启GPS", Toast.LENGTH_LONG).show();
		}else
		{
			Log.i("ryklocate", "bind locationManager");
			//禁止GPS时测试用
		
/*		 //禁止GPS时测试用
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, mLocationListener);
		
		mJsonObject = (JsonObject)mJsonParser.parse("{tag_id:1234,tag_name:'蓝田三舍',mark:4.5,son_num:123,"
				+ "contributer:124,like_num:456,father_id:4321,father_name:'浙江大学',"
				+ "share:true,follow:true,like:false,creator:'任彦昆'}");
		mLocateJson.setJsonObject(mJsonObject);
		Message _msg=mHandler.obtainMessage(TagContentFragment.LOCATE_MSG);
		mHandler.sendMessage(_msg);*/
		}
	}

	public int convertLocation(double number) {
		int _convertion;
		if (number < 0) {
			_convertion = (int) (1800000 - 10000 * number);
		} else {
			_convertion = (int) (number * 10000);
		}

		return _convertion;
	}

	public int getmFlag() {
		return mFlag;
	}

	public void setmFlag(int mFlag) {
		this.mFlag = mFlag;
	}

}
