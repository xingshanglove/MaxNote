package com.example.test;
 


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class LogoActy extends Activity {
	public ImageView splash_image; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_splash);

		
		startAnimation();
	}

	private void startAnimation() {
		splash_image = (ImageView) findViewById(R.id.splash_image);
		AlphaAnimation anima = new AlphaAnimation(0, 1);
		anima.setDuration(1500);             // 持续15秒
		splash_image.startAnimation(anima); // 开始动画
		//动画监听
		anima.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				Intent intent = new Intent(LogoActy.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}
}
