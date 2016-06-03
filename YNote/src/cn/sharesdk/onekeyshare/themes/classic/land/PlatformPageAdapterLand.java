/*
 * ������վ:http://www.mob.com
 * ����֧��QQ: 4006852216
 * �ٷ�΢��:ShareSDK   ����������°汾�Ļ������ǽ����һʱ��ͨ��΢�Ž��汾�����������͸��������ʹ�ù��������κ����⣬Ҳ����ͨ��΢��������ȡ����ϵ�����ǽ�����24Сʱ�ڸ���ظ���
 *
 * Copyright (c) 2013�� mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic.land;

import java.util.ArrayList;

import android.content.Context;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPage;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPageAdapter;

import com.mob.tools.utils.R;

/** �����ľŹ���ҳ�������� */
public class PlatformPageAdapterLand extends PlatformPageAdapter {
	private static final int DESIGN_SCREEN_WIDTH_L = 1280;
	private static final int DESIGN_CELL_WIDTH_L = 160;
	private static final int DESIGN_SEP_LINE_WIDTH = 1;
	private static final int DESIGN_LOGO_HEIGHT = 76;
	private static final int DESIGN_PADDING_TOP = 20;

	public PlatformPageAdapterLand(PlatformPage page, ArrayList<Object> cells) {
		super(page, cells);
	}

	protected void calculateSize(Context context, ArrayList<Object> plats) {
		int screenWidth = R.getScreenWidth(context);
		float ratio = ((float) screenWidth) / DESIGN_SCREEN_WIDTH_L;
		int cellWidth = (int) (DESIGN_CELL_WIDTH_L * ratio);
		lineSize = screenWidth / cellWidth;

		sepLineWidth = (int) (DESIGN_SEP_LINE_WIDTH * ratio);
		sepLineWidth = sepLineWidth < 1 ? 1 : sepLineWidth;
		logoHeight = (int) (DESIGN_LOGO_HEIGHT * ratio);
		paddingTop = (int) (DESIGN_PADDING_TOP * ratio);
		bottomHeight = (int) (DESIGN_BOTTOM_HEIGHT * ratio);
		cellHeight = (screenWidth - sepLineWidth * 3) / (lineSize - 1);
		panelHeight = cellHeight + sepLineWidth;
	}

	protected void collectCells(ArrayList<Object> plats) {
		int count = plats.size();
		if (count < lineSize) {
			int lineCount = (count / lineSize);
			if (count % lineSize != 0) {
				lineCount++;
			}
			cells = new Object[1][lineCount * lineSize];
		} else {
			int pageCount = (count / lineSize);
			if (count % lineSize != 0) {
				pageCount++;
			}
			cells = new Object[pageCount][lineSize];
		}

		for (int i = 0; i < count; i++) {
			int p = i / lineSize;
			cells[p][i - lineSize * p] = plats.get(i);
		}
	}

}
