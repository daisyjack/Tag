package com.tag2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import mengcheng.tag.frontend.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tag.sha1.SHA1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends EntryButtonActivity {
	HttpClient client;
	String set_cookie;
	EditText etUserName,etPassword;
	ImageButton btnLogin;
	
	boolean loginOn=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.login);
		
		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etUserName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length()>0) {
					if (etPassword.getText().toString().equals("") == false) {
						if (!loginOn) {
							//Change to On
							btnLogin.setImageResource(R.drawable.btn_login_on);
							loginOn=true;
						}
					}
				} else {
					if (etPassword.getText().toString().equals("")==true) {
						if (loginOn) {
							//Change to Off
							btnLogin.setImageResource(R.drawable.btn_login_off);
							loginOn=false;
						}
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		etPassword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length()>0) {
					if (etUserName.getText().toString().equals("") == false) {
						if (!loginOn) {
							//Change to On
							btnLogin.setImageResource(R.drawable.btn_login_on);
							loginOn=true;
						}
					}
				} else {
					if (etUserName.getText().toString().equals("")==true) {
						if (loginOn) {
							//Change to Off
							btnLogin.setImageResource(R.drawable.btn_login_off);
							loginOn=false;
						}
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		
		client = new DefaultHttpClient();
		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (loginOn) {
					login();

			    }
				
			}
		});
//-------------End of Login--------------
//-------------Other Buttons-------------
		iniPopupData();
		iniFooter();
		ImageButton btnBack;
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView btnStartRegister;
		btnStartRegister = (TextView) findViewById(R.id.btnStartRegister);
		btnStartRegister.setClickable(true);
		btnStartRegister.setFocusable(true);
		btnStartRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				System.out.println("prepare to start");
				startActivity(i);
				System.out.println("Start Accomplished");
			}
		});
	}
	
	
	private void login() {
		new AsyncTask<String, Void, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				String urlString = params[0];
				HttpPost post = new HttpPost(urlString);

				JSONObject jsonParam = new JSONObject();
				JSONObject resJson = new JSONObject();
				try {
					jsonParam.put("username", etUserName.getText().toString());
					jsonParam.put("password", SHA1.getSHA1(etPassword.getText().toString()));
					StringEntity entity = new StringEntity(jsonParam.toString());
					entity.setContentType("application/json");
					post.setEntity(entity);
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 				
				try {
					HttpResponse response =  client.execute(post);
					String value = EntityUtils.toString(response.getEntity());
					System.out.println(value);
//					Toast.makeText(LoginActivity.this, value, 0).show();
					resJson = new JSONObject(value);
					
					String userID = resJson.getString("user_id");
					
					set_cookie = new String(response.getHeaders("Set-Cookie")[0].getValue());
					System.out.println(set_cookie);
					
					Context ctx = getApplicationContext();       
					SharedPreferences sp = ctx.getSharedPreferences("Client-data", MODE_PRIVATE);

					Editor editor = sp.edit();
					editor.putString("user_id", userID);
					editor.putString("cookie", set_cookie);
					editor.commit();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					try {
						int errNo = resJson.getInt("error");
						switch (errNo) {
						case 11:
							Toast.makeText(getApplicationContext(), "不存在的用户名", Toast.LENGTH_SHORT).show();
							break;
						case 12:
							Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
							break;
						default :
							Toast.makeText(getApplicationContext(), "错误号："+errNo, Toast.LENGTH_SHORT).show();	
						}
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					
				}
				return null;
			}										
		}.execute("http://121.40.88.229:3000/accounts/login");
	}


	@Override
	protected String getUrl() {
		
		return null;
	}
}
