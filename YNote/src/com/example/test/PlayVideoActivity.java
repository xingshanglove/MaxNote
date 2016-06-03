package com.example.test;

import com.example.test.R;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideoActivity extends Activity {
	ImageView back;
	String videoPath;
	VideoView vv_play;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_playvideo);
		videoPath = getIntent().getStringExtra("path");

		String path = videoPath.substring(0, videoPath.length() - 4);
		videoPath = path.substring(0, 14) + path.substring(19, path.length())
				+ ".mp4";

		Log.v("--->PLAY", videoPath);

		back = (ImageView) this.findViewById(R.id.iv_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayVideoActivity.this.finish();
			}
		});
		vv_play=(VideoView) this.findViewById(R.id.vv_play);
		vv_play.setVideoPath(videoPath);
		vv_play.start();
		vv_play.requestFocus();
	}

	@Override
	public void onBackPressed() {
		PlayVideoActivity.this.finish();
	}
}
