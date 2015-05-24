package com.tag2;


import mengcheng.tag.frontend.R;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	private EditText etInput;
	private ImageView imgBar;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "TabSelected" + tab.getPosition(), 0).show();
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};
        Tab tab = actionBar.newTab();
        tab.setText("���"); //should get the real name
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
        tab = actionBar.newTab();
        tab.setText("����TAG"); //should get the real name
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
        tab = actionBar.newTab();
        tab.setText("����TAG"); //should get the real name
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(i);
			}
		
		});
        
        Button btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "ɨ���ά��", Toast.LENGTH_SHORT).show();
				Intent startScan = new Intent(MainActivity.this,com.zxing.activity.CaptureActivity.class);
//				startActivity(startScan);
				startActivityForResult(startScan, 0);
			}
		});
        
        Button btnGenerate = (Button) findViewById(R.id.btnGenerate);
        imgBar = (ImageView) findViewById(R.id.imgBar);
        etInput = (EditText) findViewById(R.id.etInput);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String in = etInput.getText().toString();
					if (in.length() == 0) {
						Toast.makeText(MainActivity.this, "�������ı�", Toast.LENGTH_SHORT).show();
					} else {
						Bitmap qrcode = EncodingHandler.createQRCode(in, 400);
						imgBar.setImageBitmap(qrcode);
					}
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		});
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_person, container, false);
            return rootView;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode == RESULT_OK) {
    		String result = data.getExtras().getString("result");
    		TextView tvResult = (TextView) findViewById(R.id.ScanResult);
    		tvResult.setText(result);
    	}
    }
}
