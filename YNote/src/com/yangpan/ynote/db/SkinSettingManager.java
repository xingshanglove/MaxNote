package com.yangpan.ynote.db;



import com.example.test.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.style.LeadingMarginSpan;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * PadQzoneƤ��������
 * 
 */
public class SkinSettingManager {

	public final static String SKIN_PREF = "skinSetting";

	public SharedPreferences skinSettingPreference;

	private int[] skinResources = { R.drawable.background1,
			R.drawable.background2, R.drawable.background3,
			R.drawable.background4, R.drawable.background5,
			R.drawable.background6 };

	private Activity mActivity;
	private LinearLayout mlayout;

	public SkinSettingManager(Activity activity, LinearLayout layout) {
		this.mActivity = activity;
		this.mlayout = layout;
		skinSettingPreference = mActivity.getSharedPreferences(SKIN_PREF, 3);
	}

	public SkinSettingManager(Activity activity) {
		this.mActivity = activity;
		skinSettingPreference = mActivity.getSharedPreferences(SKIN_PREF, 3);
	}

	

	/**
	 * ��ȡ��ǰ�����Ƥ�����
	 * 
	 * @return
	 */
	public int getSkinType() {
		String key = "skin_type";
		return skinSettingPreference.getInt(key, 0);
	}

	/**
	 * ��Ƥ�����д��ȫ��������ȥ
	 * 
	 * @param j
	 */
	public void setSkinType(int j) {
		SharedPreferences.Editor editor = skinSettingPreference.edit();
		String key = "skin_type";

		editor.putInt(key, j);
		editor.commit();
	}

	/**
	 * ��ȡ��ǰƤ���ı���ͼ��Դid
	 * 
	 * @return
	 */
	public int getCurrentSkinRes() {
		int skinLen = skinResources.length;
		int getSkinLen = getSkinType();
		if (getSkinLen >= skinLen) {
			getSkinLen = 0;
		}

		return skinResources[getSkinLen];
	}

	/**
	 * ���ڵ�����Ƥ����ť�л�Ƥ��
	 */
	public void toggleSkins(int skinType) {
		setSkinType(skinType);
		mActivity.getWindow().setBackgroundDrawable(null);
		try {
			mActivity.getWindow().setBackgroundDrawableResource(
					getCurrentSkinRes());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ڳ�ʼ��Ƥ��
	 */
	// public void initSkins(){
	// System.out.println("------->00");
	// mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
	// }
	public void initSkins() {
		if (mlayout == null) {
			mActivity.getWindow().setBackgroundDrawableResource(
					getCurrentSkinRes());
		} else {
			mlayout.setBackgroundResource(getCurrentSkinRes());
			;
		}
	}
}
