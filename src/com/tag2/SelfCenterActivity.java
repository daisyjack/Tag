package com.tag2;

import mengcheng.tag.frontend.R;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class SelfCenterActivity extends EntryButtonActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selfcenter);
		iniFooter();
		ImageButton btnSelfBack = (ImageButton) findViewById(R.id.btnSelfBack);
		btnSelfBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	protected String getUrl() {
		return null;
	}
	
}
