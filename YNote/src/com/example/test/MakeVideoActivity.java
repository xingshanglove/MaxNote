package com.example.test;

import java.io.File;
import java.io.IOException;

import cn.sharesdk.tencent.qq.h;

import com.example.test.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MakeVideoActivity extends Activity implements OnClickListener,
		Callback {

	SurfaceView sv_video;
	ImageView iv_control;
	TextView tv_cancle;
	TextView tv_ok;
	
	MediaRecorder recorder;
	SurfaceHolder sHolder;

	boolean isStrat = true;

	String file_path;
	File videFile;

	boolean isDelete = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_makevideo);
		initView();
		file_path = getFilePath();
	}

	private void initRecorder() {
		recorder = new MediaRecorder();// ����mediarecorder����
		recorder.reset();
		// ����¼����ƵԴΪCamera(���)
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// ����¼����ɺ���Ƶ�ķ�װ��ʽTHREE_GPPΪ3gp.MPEG_4Ϊmp4
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		// ����¼�Ƶ���Ƶ����h263 h264
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		// ������Ƶ¼�Ƶķֱ��ʡ�����������ñ���͸�ʽ�ĺ��棬���򱨴�
		recorder.setVideoSize(176, 144);
		// ����¼�Ƶ���Ƶ֡�ʡ�����������ñ���͸�ʽ�ĺ��棬���򱨴�
		recorder.setVideoFrameRate(5);
		
		videFile = new File(file_path);
		if(videFile.exists()){
			videFile.delete();
			try {
				videFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ������Ƶ�ļ������·��
		Log.v("--->", file_path);
		recorder.setOutputFile(videFile.getAbsolutePath());
		
		recorder.setPreviewDisplay(sHolder.getSurface());

		try {
			// ׼��¼��
			recorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		sv_video = (SurfaceView) this.findViewById(R.id.sv_video);
		sv_video.getHolder().addCallback(this);
		sv_video.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		sv_video.getHolder().setKeepScreenOn(true);

		iv_control = (ImageView) this.findViewById(R.id.iv_control);
		iv_control.setOnClickListener(this);
		tv_cancle = (TextView) this.findViewById(R.id.tv_cancle);
		tv_cancle.setOnClickListener(this);
		tv_ok=(TextView) this.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_control:
			if (isStrat) {
				iv_control.setImageDrawable(getResources().getDrawable(
						R.drawable.end));
				tv_ok.setVisibility(View.VISIBLE);
				// ��ʼ¼��
				initRecorder();
				recorder.start();
				isStrat = false;
			} else {
				iv_control.setImageDrawable(getResources().getDrawable(
						R.drawable.start));
				if (recorder != null) {
					// ֹͣ¼��
					recorder.stop();
					// �ͷ���Դ
					recorder.release();
					recorder = null;
				}
				isStrat = true;
			}
			break;
		case R.id.tv_cancle:
			if (videFile.exists() && isDelete)
				videFile.delete();
			MakeVideoActivity.this.finish();
			break;
		case R.id.tv_ok:
			if(isStrat){
				isDelete=false;
				Intent intent=new Intent();
				intent.putExtra("path", file_path);
				setResult(RESULT_OK, intent);
				MakeVideoActivity.this.finish();
			}
			break;
		}
	}

	private String getFilePath() {
		long time = System.currentTimeMillis();
		String path = "/sdcard/notes/" + time + ".mp4";
		return path;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.sHolder = holder;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		this.sHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		sv_video = null;
		holder = null;
		recorder = null;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (videFile.exists() && isDelete)
			videFile.delete();
		sv_video = null;
		sHolder = null;
		recorder = null;
		MakeVideoActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sv_video = null;
		sHolder = null;
		recorder = null;
		if (videFile.exists() && isDelete)
			videFile.delete();
	}
}
