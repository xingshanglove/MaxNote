/*
 * 技术支持QQ: 4006852216
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic.land;

import com.mob.tools.utils.R;

import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.onekeyshare.themes.classic.FriendListPage;

/** 横屏的好友列表 */
public class FriendListPageLand extends FriendListPage {
	private static final int DESIGN_SCREEN_WIDTH = 1280;
	private static final int DESIGN_TITLE_HEIGHT = 70;

	public FriendListPageLand(OnekeyShareThemeImpl impl) {
		super(impl);
	}

	protected float getRatio() {
		float screenWidth = R.getScreenWidth(activity);
		return screenWidth / DESIGN_SCREEN_WIDTH;
	}

	protected int getDesignTitleHeight() {
		return DESIGN_TITLE_HEIGHT;
	}

}
