package com.example.test;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewAnimator;

public class ShowPictureActy extends Activity {
	private ImageView img;
	private Bitmap bm;
	private DisplayMetrics dm;  
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF mid = new PointF();
	private PointF start = new PointF();
	private static int DRAG = 2;
	private static int ZOOM = 1;
	private static int NONE = 0;
	private int mode = 0;
	private float oldDist = 1f;
	private static float MINSCALER = 0.3f;
	private static float MAXSCALER = 3.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.acty_showpicture);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_add);
		//设置标题
		TextView tv_title = (TextView)findViewById(R.id.tv_title);
		tv_title.setText("查看图片");
		Button bt_back = (Button)findViewById(R.id.bt_back);
		bt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ShowPictureActy.this.finish();
			}
		});
		Button bt_save = (Button)findViewById(R.id.bt_save);
		bt_save.setVisibility(View.GONE);
		dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm); //获取分辨率  
		
		
		
		img = (ImageView)findViewById(R.id.iv_showPic);
		
		Intent intent = this.getIntent();
		String imgPath = intent.getStringExtra("imgPath");
		bm = BitmapFactory.decodeFile(imgPath);
		//设置居中显示
		savedMatrix.setTranslate((dm.widthPixels - bm.getWidth())/2 , (dm.heightPixels - bm.getHeight()) / 2);
		img.setImageMatrix(savedMatrix);
		//绑定图片
		img.setImageBitmap(bm);
		img.setScaleType(ScaleType.MATRIX);
		//触摸事件
		img.setOnTouchListener(new TouchEvent());
	}
	
	//添加触摸事件，实现图片的手势缩放
	class TouchEvent implements OnTouchListener{
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch(event.getActionMasked()){
				//单击触控，用于拖动
			   	case MotionEvent.ACTION_DOWN :
			   		matrix.set(img.getImageMatrix());
			   		savedMatrix.set(matrix);
			   		start.set(event.getX(), event.getY());
			   		mode = DRAG;
			   		break;
				//多点触控，按下时
				case MotionEvent.ACTION_POINTER_DOWN :
					oldDist = getSpacing(event);
					savedMatrix.set(matrix);
					getMidPoint(mid,event);
					mode = ZOOM;
					break;
				//多点触控，抬起时
				case MotionEvent.ACTION_POINTER_UP :
					mode = NONE;
					break;
				case MotionEvent.ACTION_MOVE :
					if(mode == DRAG){
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
					}
					//缩放
					else if(mode == ZOOM){
						//取得多指移动的直径，如果大于10，则认为是缩放手势
						float newDist = getSpacing(event);
						if(newDist > 10){
							matrix.set(savedMatrix);
							float scale = newDist / oldDist;
							
							matrix.postScale(scale, scale,mid.x,mid.y);
						}
					}
					break;
			}
			img.setImageMatrix(matrix);
			controlScale();
			//setCenter();
			center();
			return true;
		}
	}
	
	//求距离
	private float getSpacing(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	
	//求中点
	private void getMidPoint(PointF mid,MotionEvent event){
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		mid.set(x / 2, y / 2);
	}
	//控制缩放比例
	private void controlScale(){
		float values[] = new float[9];
		matrix.getValues(values);
		if(mode == ZOOM){
			if(values[0] < MINSCALER)
				matrix.setScale(MINSCALER, MINSCALER);
			else if(values[0] > MAXSCALER)
				matrix.setScale(MAXSCALER, MAXSCALER);
		}
	}
	//自动居中  左右及上下都居中  
    protected void center()  
    {  
        center(true,true);  
    }  
  
    private void center(boolean horizontal, boolean vertical)  
    {  
        Matrix m = new Matrix();  
        m.set(matrix);  
        RectF rect = new RectF(0, 0, bm.getWidth(), bm.getHeight());  
        m.mapRect(rect);  
        float height = rect.height();  
        float width = rect.width();  
        float deltaX = 0, deltaY = 0;  
        if (vertical)  
        {  
            int screenHeight = dm.heightPixels;  //手机屏幕分辨率的高度  
            //int screenHeight = 400;  
            if (height < screenHeight)  
            {  
                deltaY = (screenHeight - height)/2 - rect.top;  
            }else if (rect.top > 0)  
            {  
                deltaY = -rect.top;  
            }else if (rect.bottom < screenHeight)  
            {  
                deltaY = screenHeight - rect.bottom;  
            }  
        }  
          
        if (horizontal)  
        {  
            int screenWidth = dm.widthPixels;  //手机屏幕分辨率的宽度  
            //int screenWidth = 400;  
            if (width < screenWidth)  
            {  
                deltaX = (screenWidth - width)/2 - rect.left;  
            }else if (rect.left > 0)  
            {  
                deltaX = -rect.left;      
            }else if (rect.right < screenWidth)  
            {  
                deltaX = screenWidth - rect.right;  
            }  
        }  
        matrix.postTranslate(deltaX, deltaY);  
    }  
}
