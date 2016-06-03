package com.yangpan.ynote.view;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.example.test.R;
import com.wwengine.hw.WWHandWrite;
import com.yangpan.ynote.utils.GetCutBitmapLocation;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {

	private Bitmap mBitmap, myBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private Handler bitmapHandler;
	GetCutBitmapLocation getCutBitmapLocation;
	private Timer timer;
	DisplayMetrics dm;
	private int w, h;
	private int currentColor = Color.RED;
	private int currentSize = 5;

	// 画笔颜色
	private int[] paintColor = { Color.RED, Color.BLUE, Color.BLACK,
			Color.GREEN, Color.YELLOW, Color.CYAN, Color.LTGRAY };

	private static char[] mResult1;
	private static short[] mTracks;
	private static int mCount;
	private Context context;

	boolean isClean = false;

	public TouchView(Context context) {
		super(context);
		this.context = context;
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		w = dm.widthPixels;
		h = dm.heightPixels;
		initPaint();
		hw_init();
	}

	public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		w = dm.widthPixels;
		h = dm.heightPixels;
		initPaint();
		hw_init();
	}

	private void hw_init() {
		// 加载手写数据
		byte[] hwData = readData(context.getAssets(), "hwdata.bin");
		if (hwData == null) {
			return;
		}

		// 绑定App，传入 Context
		WWHandWrite.apkBinding(context);

		if (WWHandWrite.hwInit(hwData, 0) != 0) {
			// 失败
			return;
		}
		mResult1 = new char[256];
		mTracks = new short[1024];
		mCount = 0;
	}

	// //////////////////////////////////////////////////////////////
	// readData
	// //////////////////////////////////////////////////////////////
	private static byte[] readData(AssetManager am, String name) {
		try {
			InputStream is = am.open(name);
			if (is == null) {
				return null;
			}
			int length = is.available();
			if (length <= 0) {
				return null;
			}
			byte[] buf = new byte[length];
			if (buf == null) {
				return null;
			}

			if (is.read(buf, 0, length) == -1) {
				return null;
			}
			is.close();

			return buf;

		} catch (Exception ex) {
			return null;
		}
	}

	// 设置handler
	public void setHandler(Handler mBitmapHandler) {
		bitmapHandler = mBitmapHandler;
	}

	// 初始化画笔，画布
	private void initPaint() {
		// 设置画笔样式
		setPaintStyle();

		getCutBitmapLocation = new GetCutBitmapLocation();

		// 画布大小
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap); // 所有mCanvas画的东西都被保存在了mBitmap中

		mCanvas.drawColor(Color.TRANSPARENT);
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		timer = new Timer(true);
	}

	// 设置画笔样式
	public void setPaintStyle() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(currentSize);
		mPaint.setColor(currentColor);
	}

	// 设置画笔的大小
	public void selectHandWritetSize(int which) {
		int size = Integer.parseInt(this.getResources().getStringArray(
				R.array.paintsize)[which]);
		currentSize = size;
		setPaintStyle();
	}

	// 设置画笔颜色
	public void selectHandWriteColor(int which) {

		currentColor = paintColor[which];
		setPaintStyle();
	}

	/**
	 * 处理屏幕显示
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// myBitmap = getCutBitmap(mBitmap);
				// Message message = new Message();
				// message.what=1;
				// Bundle bundle = new Bundle();;
				// bundle.putParcelable("bitmap",myBitmap);
				// message.setData(bundle);
				// bitmapHandler.sendMessage(message);
				// RefershBitmap();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 发送消息给handler更新ACTIVITY
	 */
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			Log.i("线程", "来了");
			handler.sendMessage(message);
		}
	};

	// 切割画布中的字并返回
	public Bitmap getCutBitmap(Bitmap mBitmap) {
		// 得到手写字的四周位置，并向外延伸10px
		float cutLeft = getCutBitmapLocation.getCutLeft() - 10;
		float cutTop = getCutBitmapLocation.getCutTop() - 10;
		float cutRight = getCutBitmapLocation.getCutRight() + 10;
		float cutBottom = getCutBitmapLocation.getCutBottom() + 10;

		cutLeft = (0 > cutLeft ? 0 : cutLeft);
		cutTop = (0 > cutTop ? 0 : cutTop);

		cutRight = (mBitmap.getWidth() < cutRight ? mBitmap.getWidth()
				: cutRight);
		cutBottom = (mBitmap.getHeight() < cutBottom ? mBitmap.getHeight()
				: cutBottom);

		// 取得手写的的高度和宽度
		float cutWidth = cutRight - cutLeft;
		float cutHeight = cutBottom - cutTop;

		Bitmap cutBitmap = Bitmap.createBitmap(mBitmap, (int) cutLeft,
				(int) cutTop, (int) cutWidth, (int) cutHeight);
		if (myBitmap != null) {
			myBitmap.recycle();
			myBitmap = null;
		}

		return cutBitmap;
	}

	// 刷新画布
	private void RefershBitmap() {
		initPaint();
		invalidate();
		if (task != null)
			task.cancel();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint); // 显示旧的画布
		canvas.drawPath(mPath, mPaint); // 画最后的path
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	// 手按下时
	private void touch_start(float x, float y) {
		mPath.reset();// 清空path
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		if (task != null)
			task.cancel();// 取消之前的任务
		task = new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				Log.i("线程", "来了");
				handler.sendMessage(message);
			}
		};
		getCutBitmapLocation.setCutLeftAndRight(mX, mY);

		mTracks[mCount++] = (short) x;
		mTracks[mCount++] = (short) y;
	}

	// 手移动时
	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, x, y);
			// mPath.quadTo(mX, mY, (x + mX)/2, (y +
			// mY)/2);//源代码是这样写的，可是我没有弄明白，为什么要这样？
			mX = x;
			mY = y;
			if (task != null)
				task.cancel();// 取消之前的任务
			task = new TimerTask() {

				@Override
				public void run() {
					Message message = new Message();
					message.what = 1;
					Log.i("线程", "来了");
					handler.sendMessage(message);
				}
			};
			getCutBitmapLocation.setCutLeftAndRight(mX, mY);
			mTracks[mCount++] = (short) x;
			mTracks[mCount++] = (short) y;
		}
	}

	// 手抬起时
	private void touch_up() {
		// mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		// mPath.reset();

		if (timer != null) {
			if (task != null) {
				task.cancel();
				task = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				};
				timer.schedule(task, 1000, 1000); // 2200秒后发送消息给handler更新Activity
			}
		} else {
			timer = new Timer(true);
			timer.schedule(task, 1000, 1000); // 2200秒后发送消息给handler更新Activity
		}

		mTracks[mCount++] = -1;
		mTracks[mCount++] = 0;
		recognize();
	}

	private void recognize() {
		short[] mTracksTemp;
		int countTemp = mCount;

		mTracksTemp = mTracks.clone();
		mTracksTemp[countTemp++] = -1;
		mTracksTemp[countTemp++] = -1;

		WWHandWrite.hwRecognize(mTracksTemp, mResult1, 10, 0xFFFF);

		Message msg = bitmapHandler.obtainMessage();
		msg.obj = mResult1;
		bitmapHandler.sendMessage(msg);
	}

	// 重置
	public void reset_recognize() {
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mCount = 0;
		mResult1 = new char[256];
		mPath.reset();
		invalidate();
	}

	// 处理界面事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate(); // 刷新
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

}
