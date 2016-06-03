/*
 * ������վ:http://www.mob.com
 * ����֧��QQ: 4006852216
 * �ٷ�΢��:ShareSDK   ����������°汾�Ļ������ǽ����һʱ��ͨ��΢�Ž��汾�����������͸��������ʹ�ù��������κ����⣬Ҳ����ͨ��΢��������ȡ����ϵ�����ǽ�����24Сʱ�ڸ���ظ���
 *
 * Copyright (c) 2013�� mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView.ScaleType;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;

import com.mob.tools.gui.ScaledImageView;

/** ͼƬ�������ͼ�� */
public class PicViewerPage extends OnekeySharePage implements OnGlobalLayoutListener {
	private Bitmap pic;
	/** ͼƬ��������ſؼ� */
	private ScaledImageView sivViewer;

	public PicViewerPage(OnekeyShareThemeImpl impl) {
		super(impl);
	}

	/** ����ͼƬ������� */
	public void setImageBitmap(Bitmap pic) {
		this.pic = pic;
	}

	public void onCreate() {
		activity.getWindow().setBackgroundDrawable(new ColorDrawable(0x4c000000));

		sivViewer = new ScaledImageView(activity);
		sivViewer.setScaleType(ScaleType.MATRIX);
		activity.setContentView(sivViewer);
		if (pic != null) {
			sivViewer.getViewTreeObserver().addOnGlobalLayoutListener(this);
		}
	}

	public void onGlobalLayout() {
		sivViewer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		sivViewer.post(new Runnable() {
			public void run() {
				sivViewer.setBitmap(pic);
			}
		});
	}

}
