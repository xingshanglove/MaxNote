/*
 * ������վ:http://www.mob.com
 * ����֧��QQ: 4006852216
 * �ٷ�΢��:ShareSDK   ����������°汾�Ļ������ǽ����һʱ��ͨ��΢�Ž��汾�����������͸��������ʹ�ù��������κ����⣬Ҳ����ͨ��΢��������ȡ����ϵ�����ǽ�����24Сʱ�ڸ���ظ���
 *
 * Copyright (c) 2013�� mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare;

import static com.mob.tools.utils.BitmapHelper.captureView;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import com.mob.tools.utils.R;

/**
* ��ݷ�������
* <p>
* ͨ����ͬ��setter���ò�����Ȼ�����{@link #show(Context)}����������ݷ���
*/
public class OnekeyShare {
	private HashMap<String, Object> params;

	public OnekeyShare() {
		params = new HashMap<String, Object>();
		params.put("customers", new ArrayList<CustomerLogo>());
		params.put("hiddenPlatforms", new HashMap<String, String>());
	}

	/** address�ǽ����˵�ַ��������Ϣ���ʼ�ʹ�ã�������Բ��ṩ */
	public void setAddress(String address) {
		params.put("address", address);
	}

	/**
	 * title���⣬��ӡ��ʼǡ����䡢��Ϣ��΢�ţ��������ѡ�����Ȧ���ղأ���
	 * ���ţ��������ѡ�����Ȧ������������QQ�ռ�ʹ�ã�������Բ��ṩ
	 */
	public void setTitle(String title) {
		params.put("title", title);
	}

	/** titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ�ã�������Բ��ṩ */
	public void setTitleUrl(String titleUrl) {
		params.put("titleUrl", titleUrl);
	}

	/** text�Ƿ����ı�������ƽ̨����Ҫ����ֶ� */
	public void setText(String text) {
		params.put("text", text);
	}

	/** ��ȡtext�ֶε�ֵ */
	public String getText() {
		return params.containsKey("text") ? String.valueOf(params.get("text")) : null;
	}

	/** imagePath�Ǳ��ص�ͼƬ·������Linked-In�������ƽ̨��֧������ֶ� */
	public void setImagePath(String imagePath) {
		if(!TextUtils.isEmpty(imagePath))
			params.put("imagePath", imagePath);
	}

	/** imageUrl��ͼƬ������·��������΢������������QQ�ռ��Linked-In֧�ִ��ֶ� */
	public void setImageUrl(String imageUrl) {
		if (!TextUtils.isEmpty(imageUrl))
			params.put("imageUrl", imageUrl);
	}

	/** url��΢�ţ��������ѡ�����Ȧ�ղأ������ţ��������Ѻ�����Ȧ����ʹ�ã�������Բ��ṩ */
	public void setUrl(String url) {
		params.put("url", url);
	}

	/** filePath�Ǵ�����Ӧ�ó���ı���·��������΢�ţ����ţ����Ѻ�Dropbox��ʹ�ã�������Բ��ṩ */
	public void setFilePath(String filePath) {
		params.put("filePath", filePath);
	}

	/** comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ�ã�������Բ��ṩ */
	public void setComment(String comment) {
		params.put("comment", comment);
	}

	/** site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ�ã�������Բ��ṩ */
	public void setSite(String site) {
		params.put("site", site);
	}

	/** siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ�ã�������Բ��ṩ */
	public void setSiteUrl(String siteUrl) {
		params.put("siteUrl", siteUrl);
	}

	/** foursquare����ʱ�ĵط��� */
	public void setVenueName(String venueName) {
		params.put("venueName", venueName);
	}

	/** foursquare����ʱ�ĵط����� */
	public void setVenueDescription(String venueDescription) {
		params.put("venueDescription", venueDescription);
	}

	/** �����γ�ȣ�����΢������Ѷ΢����foursquare֧�ִ��ֶ� */
	public void setLatitude(float latitude) {
		params.put("latitude", latitude);
	}

	/** ����ؾ��ȣ�����΢������Ѷ΢����foursquare֧�ִ��ֶ� */
	public void setLongitude(float longitude) {
		params.put("longitude", longitude);
	}

	/** �Ƿ�ֱ�ӷ��� */
	public void setSilent(boolean silent) {
		params.put("silent", silent);
	}

	/** ���ñ༭ҳ�ĳ�ʼ��ѡ��ƽ̨ */
	public void setPlatform(String platform) {
		params.put("platform", platform);
	}

	/** ����KakaoTalk��Ӧ�����ص�ַ */
	public void setInstallUrl(String installurl) {
		params.put("installurl", installurl);
	}

	/** ����KakaoTalk��Ӧ�ô򿪵�ַ */
	public void setExecuteUrl(String executeurl) {
		params.put("executeurl", executeurl);
	}

	/** ����΢�ŷ�������ֵĵ�ַ */
	public void setMusicUrl(String musicUrl) {
		params.put("musicUrl", musicUrl);
	}

	/** �����Զ�����ⲿ�ص� */
	public void setCallback(PlatformActionListener callback) {
		params.put("callback", callback);
	}

	/** ���ز����ص� */
	public PlatformActionListener getCallback() {
		return R.forceCast(params.get("callback"));
	}

	/** �������ڷ�������У����ݲ�ͬƽ̨�Զ���������ݵĻص� */
	public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
		params.put("customizeCallback", callback);
	}

	/** �Զ��岻ͬƽ̨����ͬ���ݵĻص� */
	public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
		return R.forceCast(params.get("customizeCallback"));
	}

	/** �����Լ�ͼ��͵���¼��������ظ�������Ӷ�� */
	public void setCustomerLogo(Bitmap logo, String label, OnClickListener ocl) {
		CustomerLogo cl = new CustomerLogo();
		cl.logo = logo;
		cl.label = label;
		cl.listener = ocl;
		ArrayList<CustomerLogo> customers = R.forceCast(params.get("customers"));
		customers.add(cl);
	}

	/** ����һ���ܿ��أ������ڷ���ǰ����Ҫ��Ȩ�������sso���� */
	public void disableSSOWhenAuthorize() {
		params.put("disableSSO", true);
	}

	/** ������Ƶ�����ַ */
	public void setVideoUrl(String url) {
		params.put("url", url);
		params.put("shareType", Platform.SHARE_VIDEO);
	}

	/** ���ñ༭ҳ�����ʾģʽΪDialogģʽ */
	@Deprecated
	public void setDialogMode() {
		params.put("dialogMode", true);
	}

	/** ���һ�����ص�platform */
	public void addHiddenPlatform(String platform) {
		HashMap<String, String> hiddenPlatforms = R.forceCast(params.get("hiddenPlatforms"));
		hiddenPlatforms.put(platform, platform);
	}

	/** ����һ��������ͼ�����View , surfaceView�ǽز���ͼƬ��*/
	public void setViewToShare(View viewToShare) {
		try {
			Bitmap bm = captureView(viewToShare, viewToShare.getWidth(), viewToShare.getHeight());
			params.put("viewToShare", bm);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/** ��Ѷ΢���������ͼƬ */
	public void setImageArray(String[] imageArray) {
		params.put("imageArray", imageArray);
	}

	/** ������ִ�з���QQ��QZone��ͬʱ��������ͬ��������Ѷ΢�� */
	public void setShareToTencentWeiboWhenPerformingQQOrQZoneSharing() {
		params.put("isShareTencentWeibo", true);
	}

	/** ���÷���������ʽ��Ŀǰֻ��һ�֣�����Ҫ���� */
	public void setTheme(OnekeyShareTheme theme) {
		params.put("theme", theme.getValue());
	}

	@SuppressWarnings("unchecked")
	public void show(Context context) {
		HashMap<String, Object> shareParamsMap = new HashMap<String, Object>();
		shareParamsMap.putAll(params);

		ShareSDK.initSDK(context);

		// �򿪷���˵���ͳ��
		ShareSDK.logDemoEvent(1, null);

		int iTheme = 0;
		try {
			iTheme = R.parseInt(String.valueOf(shareParamsMap.remove("theme")));
		} catch (Throwable t) {}
		OnekeyShareTheme theme = OnekeyShareTheme.fromValue(iTheme);
		OnekeyShareThemeImpl themeImpl = theme.getImpl();

		themeImpl.setShareParamsMap(shareParamsMap);
		themeImpl.setDialogMode(shareParamsMap.containsKey("dialogMode") ? ((Boolean) shareParamsMap.remove("dialogMode")) : false);
		themeImpl.setSilent(shareParamsMap.containsKey("silent") ? ((Boolean) shareParamsMap.remove("silent")) : false);
		themeImpl.setCustomerLogos((ArrayList<CustomerLogo>) shareParamsMap.remove("customers"));
		themeImpl.setHiddenPlatforms((HashMap<String, String>) shareParamsMap.remove("hiddenPlatforms"));
		themeImpl.setPlatformActionListener((PlatformActionListener) shareParamsMap.remove("callback"));
		themeImpl.setShareContentCustomizeCallback((ShareContentCustomizeCallback) shareParamsMap.remove("customizeCallback"));
		if (shareParamsMap.containsKey("disableSSO") ? ((Boolean) shareParamsMap.remove("disableSSO")) : false) {
			themeImpl.disableSSO();
		}

		themeImpl.show(context);
	}

}
