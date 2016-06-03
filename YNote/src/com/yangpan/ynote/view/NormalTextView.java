package com.yangpan.ynote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NormalTextView extends View{

	int width;
	int height;
	Paint paint;
	char [] datas=new char[10];
	onCheckListner listner;
	public void setCheckListener(onCheckListner listner){
		this.listner=listner;
	}
	public interface onCheckListner{
		public void onCheck(String s);
	}
	
	public NormalTextView(Context context) {
		this(context,null);
		init();
	}
	public NormalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private void init() {
		paint=new Paint();
		paint.setTextSize(25);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int space=width/10;
		for(int i=0;i<datas.length;i++){
			canvas.drawText(datas[i]+"", i*space+5, height/2, paint);
		}
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		Log.v("top", top+"/"+right);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		width=w;
		height=h;
	}
	public void setData(char [] datas){
		this.datas=datas;
		invalidate();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x=(int) event.getX();
		int which=x/(width/10);
		if(datas.length>which){
			listner.onCheck(datas[which]+"");
		}
		return super.onTouchEvent(event);
	}
	
}
