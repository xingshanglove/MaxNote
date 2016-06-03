package com.example.test;

import com.example.test.R;
import com.yangpan.ynote.db.SkinSettingManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MySkinActy extends Activity implements OnClickListener{
	private SkinSettingManager mSettingManager;
	private ImageView iv1,iv2,iv3,iv4,iv5,iv6;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_myskin);
        init();
       }
	private void init() {
		//��ʼ��Ƥ��
		mSettingManager=new SkinSettingManager(this);
		mSettingManager.initSkins();
		iv1=(ImageView) findViewById(R.id.am_iv_background1);
		iv1.setOnClickListener(MySkinActy.this);
		iv2=(ImageView) findViewById(R.id.am_iv_background2);
		iv2.setOnClickListener(MySkinActy.this);
		iv3=(ImageView) findViewById(R.id.am_iv_background3);
		iv3.setOnClickListener(MySkinActy.this);
		iv4=(ImageView) findViewById(R.id.am_iv_background4);
		iv4.setOnClickListener(MySkinActy.this);
		iv5=(ImageView) findViewById(R.id.am_iv_background5);
		iv5.setOnClickListener(MySkinActy.this);
		iv6=(ImageView) findViewById(R.id.am_iv_background6);
		iv6.setOnClickListener(MySkinActy.this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.am_iv_background1:
			mSettingManager.toggleSkins(0);
			finish();
			break;
		case R.id.am_iv_background2:
			mSettingManager.toggleSkins(1);
			finish();
			break;
		case R.id.am_iv_background3:
			mSettingManager.toggleSkins(2);
			finish();
			break;
		case R.id.am_iv_background4:
			mSettingManager.toggleSkins(3);
			finish();
			break;
		case R.id.am_iv_background5:
			mSettingManager.toggleSkins(4);
			finish();
			break;
		case R.id.am_iv_background6:
			mSettingManager.toggleSkins(5);
			finish();
			break;
		}
	}
}
