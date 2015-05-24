package com.tag2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import mengcheng.tag.frontend.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class BarcodeActivity extends Activity {
	protected Bitmap qrcode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.generate_bar_code);
		Intent i = getIntent();
		String url = i.getStringExtra("url");
		try {
			ImageView imgBar = (ImageView) findViewById(R.id.imgBar);
			qrcode = EncodingHandler.createQRCode(url, 400);
			imgBar.setImageBitmap(qrcode);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				saveImageToGallery(getApplicationContext(), qrcode);
				
			}
		});
	}
	protected static void saveImageToGallery(Context context, Bitmap bmp) {
	    // 首先保存图片
	    File appDir =null;
	    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	    	appDir = new File(Environment.getExternalStorageDirectory(), "mcTag");
	    } else {
	    	
	    }
	    
	    
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    System.out.println(fileName);
	    File file = new File(appDir, fileName);
	    System.out.println(file.getAbsolutePath());
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    
	    // 其次把文件插入到系统图库
	    try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
	}
}
