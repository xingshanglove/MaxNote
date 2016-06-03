/*
 * ������վ:http://www.mob.com
 * ����֧��QQ: 4006852216
 * �ٷ�΢��:ShareSDK   ����������°汾�Ļ������ǽ����һʱ��ͨ��΢�Ž��汾�����������͸��������ʹ�ù��������κ����⣬Ҳ����ͨ��΢��������ȡ����ϵ�����ǽ�����24Сʱ�ڸ���ظ���
 *
 * Copyright (c) 2013�� mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.themes.classic.FriendAdapter.Following;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.BitmapProcessor;
import com.mob.tools.utils.R;

/** �����б��item */
public class FriendListItem extends LinearLayout {
	private static final int DESIGN_AVATAR_WIDTH = 64;
	private static final int DESIGN_AVATAR_PADDING = 24;
	private static final int DESIGN_ITEM_HEIGHT = 96;
	private static final int DESIGN_ITEM_PADDING = 20;

	private ImageView ivCheck;
	private AsyncImageView aivIcon;
	private TextView tvName;
	/** �����б��У���ѡ�е�checkboxͼ�� */
	private Bitmap bmChd;
	/** �����б��У�ûѡ�е�checkboxͼ�� */
	private Bitmap bmUnch;

	public FriendListItem(Context context, float ratio) {
		super(context);
		int itemPadding = (int) (ratio * DESIGN_ITEM_PADDING);
		setPadding(itemPadding, 0, itemPadding, 0);
		setMinimumHeight((int) (ratio * DESIGN_ITEM_HEIGHT));
		setBackgroundColor(0xffffffff);

		ivCheck = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		addView(ivCheck, lp);

		aivIcon = new AsyncImageView(context);
		int avatarWidth = (int) (ratio * DESIGN_AVATAR_WIDTH);
		lp = new LinearLayout.LayoutParams(avatarWidth, avatarWidth);
		lp.gravity = Gravity.CENTER_VERTICAL;
		int avatarMargin = (int) (ratio * DESIGN_AVATAR_PADDING);
		lp.setMargins(avatarMargin, 0, avatarMargin, 0);
		addView(aivIcon, lp);

		tvName = new TextView(context);
		tvName.setTextColor(0xff000000);
		tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvName.setSingleLine();
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		addView(tvName, lp);

		int resId = R.getBitmapRes(context, "ssdk_oks_classic_check_checked");
		if (resId > 0) {
			bmChd = BitmapFactory.decodeResource(context.getResources(), resId);
		}
		resId = R.getBitmapRes(getContext(), "ssdk_oks_classic_check_default");
		if (resId > 0) {
			bmUnch = BitmapFactory.decodeResource(context.getResources(), resId);
		}
	}

	public void update(Following following, boolean fling) {
		tvName.setText(following.screenName);
		ivCheck.setImageBitmap(following.checked ? bmChd : bmUnch);
		if (aivIcon != null) {
			if (fling) {
				Bitmap bm = BitmapProcessor.getBitmapFromCache(following.icon);
				if (bm != null && !bm.isRecycled()) {
					aivIcon.setImageBitmap(bm);
				} else {
					aivIcon.execute(null, 0);
				}
			} else {
				aivIcon.execute(following.icon, 0);
			}
		}
	}

}
