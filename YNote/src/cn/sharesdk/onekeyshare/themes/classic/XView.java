/*
 * ������վ:http://www.mob.com
 * ����֧��QQ: 4006852216
 * �ٷ�΢��:ShareSDK   ����������°汾�Ļ������ǽ����һʱ��ͨ��΢�Ž��汾�����������͸��������ʹ�ù��������κ����⣬Ҳ����ͨ��΢��������ȡ����ϵ�����ǽ�����24Сʱ�ڸ���ظ���
 *
 * Copyright (c) 2013�� mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/** �༭ҳ����ɾ��ͼƬ��X����ť */
public class XView extends View {
	private float ratio;

	public XView(Context context) {
		super(context);
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	protected void onDraw(Canvas canvas) {
		int width = getWidth() / 2;
		int height = getHeight() / 2;

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0xffa0a0a0);
		canvas.drawRect(width, 0, getWidth(), height, paint);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3f * ratio);
		paint.setColor(0xffffffff);
		float left = 8f * ratio;
		canvas.drawLine(width + left, left, getWidth() - left, width - left, paint);
		canvas.drawLine(width + left, width - left, getWidth() - left, left, paint);
	}

}
