﻿package com.example.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.test.R;
import com.yangpan.ynote.utils.LineEditText;
import com.yangpan.ynote.view.NormalTextView;
import com.yangpan.ynote.view.NormalTextView.onCheckListner;
import com.yangpan.ynote.view.TouchView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HandWriteActy extends Activity {

	private LinearLayout paintLayout;
	private GridView paint_bottomMenu;

	private LineEditText et_handwrite;

	private ListView lv_popWindow;

	// 菜单资源
	private int[] paintItems = { R.drawable.paint_pencil,
			R.drawable.paint_icon_color, R.drawable.paint_icon_back,
			R.drawable.paint_icon_forward, R.drawable.paint_icon_delete };

	private int select_handwrite_color_index = 0;
	private int select_handwrite_size_index = 0;

	private Bitmap mBitmap;

	private Button btn_save;
	private Button btn_back;

	private PopupWindow popupWindow;

	private TouchView touchView;
	private ArrayList<CharSequence> deleteChar = new ArrayList<CharSequence>();

	
	NormalTextView chose_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(new TouchView(this));
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.acty_handwrite);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_add);
		// 将自定义标题栏的标题定义为手写
		TextView title = (TextView) findViewById(R.id.tv_title);
		title.setText("手写");

		paint_bottomMenu = (GridView) findViewById(R.id.paintBottomMenu);
		paint_bottomMenu.setOnItemClickListener(new MenuClickEvent());

		et_handwrite = (LineEditText) findViewById(R.id.et_handwrite);

		InitPaintMenu();

		touchView = (TouchView) findViewById(R.id.touch_view);
		touchView.setHandler(handler);

		btn_save = (Button) findViewById(R.id.bt_save);
		btn_back = (Button) findViewById(R.id.bt_back);
		btn_save.setOnClickListener(new ClickEvent());
		btn_back.setOnClickListener(new ClickEvent());
		
		
		chose_text=(NormalTextView) this.findViewById(R.id.chose_text);
	}

	// 配置绘图菜单
	public void InitPaintMenu() {
		ArrayList<Map<String, Object>> menus = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < paintItems.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("image", paintItems[i]);
			menus.add(item);
		}
		paint_bottomMenu.setNumColumns(paintItems.length);
		paint_bottomMenu.setSelector(R.drawable.bottom_item);
		SimpleAdapter mAdapter = new SimpleAdapter(HandWriteActy.this, menus,
				R.layout.item_button, new String[] { "image" },
				new int[] { R.id.item_image });
		paint_bottomMenu.setAdapter(mAdapter);
	}

	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == btn_save) {

				// 得到调用该Activity的Intent对象
				Intent intent = getIntent();
				Bundle b = new Bundle();
//				String path = saveBitmap();
				String content=et_handwrite.getText().toString();
				b.putString("handwritePath", content);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				HandWriteActy.this.finish();

			} else if (v == btn_back) {
				HandWriteActy.this.finish();
			}
		}

	}

	// 处理界面
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// Bundle bundle = new Bundle();
			// bundle = msg.getData();
			// Bitmap myBitmap = bundle.getParcelable("bitmap");
			// InsertToEditText(myBitmap);
			char[] results = (char[]) msg.obj;
			if (results != null) {
				chose_text.setData(results);
				chose_text.setCheckListener(new onCheckListner() {
					@Override
					public void onCheck(String s) {
						InsertToEditText(s);
					}
				});
			}
		}
	};

	private void InsertToEditText(String s) {
		et_handwrite.append(s);
	}
	// 将手写字插入到EditText中
	private void InsertToEditText(Bitmap mBitmap) {
		// int S = 120;
		//
		// int imgWidth = mBitmap.getWidth();
		// int imgHeight = mBitmap.getHeight();
		// double partion = imgWidth*1.0/imgHeight;
		// double sqrtLength = Math.sqrt(partion*partion + 1);
		//
		// //新的缩略图大小
		// double newImgW = S*(partion / sqrtLength);
		// double newImgH = S*(1 / sqrtLength);
		// float scaleW = (float) (80f/imgWidth);
		// float scaleH = (float) (100f/imgHeight);
		//
		// Matrix mx = new Matrix();
		// //对原图片进行缩放
		// mx.postScale(scaleW, scaleH);
		//
		// mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, imgWidth, imgHeight, mx,
		// true);
		// //将手写的字插入到edittext中
		// SpannableString ss = new SpannableString("1");
		// ImageSpan span = new ImageSpan(mBitmap, ImageSpan.ALIGN_BOTTOM);
		// ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		// et_handwrite.append(ss);

	}

	// 设置菜单项监听器
	class MenuClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent;
			switch (position) {
			// 画笔大小
			case 0:
				showPaintSizeDialog(view);
				break;
			// 颜色
			case 1:
				showPaintColorDialog(view);
				break;
			// 删除
			case 2:
				Editable editable = et_handwrite.getText();
				// 找到最后一个手写字,并删除最后一个手写字
				int selectionEnd = et_handwrite.getSelectionEnd();
				System.out.println("end = " + selectionEnd);
				if (selectionEnd < 1) {
					et_handwrite.setText("");
				} else if (selectionEnd == 1) {
					et_handwrite.setText("");
					CharSequence deleteCharSeq = editable.subSequence(0, 1);
					deleteChar.add(deleteCharSeq);
				} else {
					System.out.println("delete");
					CharSequence charSeq = editable.subSequence(0,
							selectionEnd - 1);
					CharSequence deleteCharSeq = editable.subSequence(
							selectionEnd - 1, selectionEnd);
					et_handwrite.setText(charSeq);
					et_handwrite.setSelection(selectionEnd - 1);
					// 将删除的字存储到列表中，以便恢复使用
					deleteChar.add(deleteCharSeq);
				}
				break;
			// 恢复
			case 3:
				// 取出删除列表中的元素
				int length = deleteChar.size();
				if (length > 0) {
					et_handwrite.append(deleteChar.get(deleteChar.size() - 1));
					deleteChar.remove(deleteChar.size() - 1);
				}

				break;
			// 清空屏幕
			case 4:
				touchView.reset_recognize();
//				if (et_handwrite.getSelectionEnd() > 0) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							HandWriteActy.this, R.style.custom_dialog);
//					builder.setTitle("清空提示");
//					builder.setMessage("您确定要清空所有吗？");
//					builder.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									et_handwrite.setText("");
//									dialog.cancel();
//								}
//							});
//					builder.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.cancel();
//								}
//							});
//					Dialog dialog = builder.create();
//					dialog.show();
//				}
				break;
			default:
				break;
			}
		}
	}

	// 弹出画笔颜色选项对话框
	public void showPaintColorDialog(View parent) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,
				R.style.custom_dialog);
		alertDialogBuilder.setTitle("选择画笔颜色：");

		alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor,
				select_handwrite_color_index,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						select_handwrite_color_index = which;
						touchView.selectHandWriteColor(which);
						dialog.dismiss();
					}
				});

		alertDialogBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialogBuilder.create().show();
	}

	// 弹出画笔大小选项对话框
	public void showPaintSizeDialog(View parent) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,
				R.style.custom_dialog);
		alertDialogBuilder.setTitle("选择画笔大小：");

		alertDialogBuilder.setSingleChoiceItems(R.array.paintsize,
				select_handwrite_size_index,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						select_handwrite_size_index = which;
						touchView.selectHandWritetSize(which);
						dialog.dismiss();
					}
				});

		alertDialogBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialogBuilder.create().show();
	}

	// 保存手写文件
	public String saveBitmap() {
		// 获得系统当前时间，并以该时间作为文件名
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String paintPath = "";
		str = str + "write.png";
		File dir = new File("/sdcard/notes/");
		File file = new File("/sdcard/notes/", str);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			if (file.exists()) {
				file.delete();
			}
		}

		// 将view转换成图片
		et_handwrite.setDrawingCacheEnabled(true);
		Bitmap cutHandwriteBitmap = Bitmap.createBitmap(et_handwrite
				.getDrawingCache());
		et_handwrite.setDrawingCacheEnabled(false);
		try {
			// 保存绘图文件路径
			paintPath = "/sdcard/notes/" + str;
			FileOutputStream out = new FileOutputStream(file);
			cutHandwriteBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paintPath;
	}


}
