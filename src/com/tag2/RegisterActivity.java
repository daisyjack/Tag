package com.tag2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mengcheng.tag.frontend.R;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tag.sha1.SHA1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends EntryButtonActivity implements TextWatcher{
	EditText etUserName,etPassword,etPhone,etVeriCode;
	String strUserName,strPassword,strPhone,strVeriCode;
	Boolean RegisterOn = false, ViewPassword = false, SendVCOn = false;
	ImageButton btnRegister;
	ImageButton btnEye;
	Button btnSendVeriCode;
	HttpClient client;
	JSONObject json;
	
	private Timer timer = null;
	private TimerTask task = null;
	private int time = 60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		
		client = new DefaultHttpClient();
		
		iniPopupData();
		iniFooter();
		ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnRegister = (ImageButton) findViewById(R.id.btnRegister);
		System.out.println("Succeeded");
		btnSendVeriCode = (Button) findViewById(R.id.btnSendVeriCode);
		
		
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etVeriCode = (EditText) findViewById(R.id.etVeriCode);
		etUserName.addTextChangedListener(this);
		etPassword.addTextChangedListener(this);
		etVeriCode.addTextChangedListener(this);
		
		etPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (SendVCOn == false && s.length()==11) {
					SendVCOn = true;
					btnSendVeriCode.setBackgroundResource(R.drawable.btn_send_vericode_on);
				} else if (SendVCOn == true && s.length() != 11) {
					SendVCOn = false;
					btnSendVeriCode.setBackgroundResource(R.drawable.btn_send_vericode_off);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		btnSendVeriCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (SendVCOn == true) {
					SendVCOn = false;
					btnSendVeriCode.setBackgroundResource(R.drawable.btn_send_vericode_off);
					startTime();
					strPhone = etPhone.getText().toString();
					if (strPhone.length()>0) {
						SendVeriCode(strPhone);
					}
				}
				
			}
		});
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (RegisterOn) {
					ReadText();
					try {
						Register();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnEye = (ImageButton) findViewById(R.id.imgEye);
		btnEye.setFocusable(false);
		btnEye.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (ViewPassword) {
					ViewPassword = false;
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					btnEye.setImageResource(R.drawable.eye_off);
					etPassword.setSelection(etPassword.getText().toString().length());
				} else {
					ViewPassword = true;
					etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					btnEye.setImageResource(R.drawable.eye_on);
					etPassword.setSelection(etPassword.getText().toString().length());
				}
			}
		});
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		if (s.length()>0) {
			if (!RegisterOn) {
				ReadText();
				if (strUserName.length()>0 && strPassword.length()>0 && strPhone.length()>0
						&& strVeriCode.length()>0) {
					RegisterOn = true;
					btnRegister.setImageResource(R.drawable.btn_register_on);
				}
			}
		} else {
			if (RegisterOn) {
				ReadText();
				RegisterOn = false;
				btnRegister.setImageResource(R.drawable.btn_register_off);
			}
		}
	}
	@Override
	public void afterTextChanged(Editable s) {}
	private void ReadText() {
		strUserName = etUserName.getText().toString();
		strPassword = etPassword.getText().toString();
		strPhone = etPhone.getText().toString();
		strVeriCode = etVeriCode.getText().toString();
	}
	private void SendVeriCode(String strPhone) {
		
		new AsyncTask<String, Void, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				String urlString = params[0];
				HttpPost post = new HttpPost(urlString);
				JSONObject jsonParams = new JSONObject();
				try {
					jsonParams.put("phone", params[1]);
					StringEntity entity = new StringEntity(jsonParams.toString());
					entity.setContentType("application/json");
					post.setEntity(entity);
					
					HttpResponse response = client.execute(post);
					String value = EntityUtils.toString(response.getEntity());
					JSONObject resJson = new JSONObject(value);
					
					String errNo = resJson.getString("error");
					System.out.println(errNo);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				return null;
			}
			
		}.execute("http://121.40.88.229:3000/accounts/getvc",strPhone);
	}
	private void Register() throws InterruptedException, ExecutionException {
		JSONObject result = new AsyncTask<String, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(String... params) {
				String urlString = params[0];
				HttpPost post = new HttpPost(urlString);
				JSONObject jsonParams = new JSONObject();
				JSONObject resJson = null;
				
				try {
					jsonParams.put("username", strUserName);
					jsonParams.put("password", SHA1.getSHA1(strPassword));
					jsonParams.put("phone", strPhone);
					jsonParams.put("verifycode", strVeriCode);
					System.out.println(jsonParams.toString());
					StringEntity entity = new StringEntity(jsonParams.toString());
					entity.setContentType("application/json");
					post.setEntity(entity);
					
					HttpResponse response = client.execute(post);
					String value = EntityUtils.toString(response.getEntity());
					
					resJson = new JSONObject(value);
					
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return resJson;
			}
			
		}.execute("http://121.40.88.229:3000/accounts/create").get();
		if (result != null) {
			
			try {
				String userid = result.getString("user_id");
				System.out.println(userid);
				Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				int errNo = 0;
				try {
					errNo = result.getInt("error");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				switch (errNo) {
				case 1:
					Toast.makeText(getApplicationContext(), "缺少用户名", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "缺少密码", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(getApplicationContext(), "缺少手机号码", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					Toast.makeText(getApplicationContext(), "缺少验证码", Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(getApplicationContext(), "用户名已被注册", Toast.LENGTH_SHORT).show();
					break;
				case 6:
					Toast.makeText(getApplicationContext(), "手机号已被注册", Toast.LENGTH_SHORT).show();
					break;
				case 7:
					Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
					break;
				case 8:
					Toast.makeText(getApplicationContext(), "手机号不一致", Toast.LENGTH_SHORT).show();
					break;
				case 9:
					Toast.makeText(getApplicationContext(), "验证码发送失败", Toast.LENGTH_SHORT).show();
				case 11:
					Toast.makeText(getApplicationContext(), "不存在的用户名", Toast.LENGTH_SHORT).show();
					break;
				case 12:
					Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
					break;
				case 102:
					Toast.makeText(getApplicationContext(), "数据库异常", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int t = msg.arg1;
			btnSendVeriCode.setText("重新发送("+t+")");
			if (t==0) {
				stopTime();
			} else {
				startTime();
			}
		};
	};
	
	public void startTime() {
		
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				time--;
				Message message = mHandler.obtainMessage();
				message.arg1 = time;
				mHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 1000);
	}
	public void stopTime() {
		timer.cancel();
		SendVCOn = true;
		btnSendVeriCode.setBackgroundResource(R.drawable.btn_send_vericode_on);
		btnSendVeriCode.setText("重新获取");
		time = 60;
	}
	@Override
	protected String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
