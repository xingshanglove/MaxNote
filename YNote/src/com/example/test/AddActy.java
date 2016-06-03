package com.example.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.AlreadyConnectedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.test.R;
import com.yangpan.ynote.bean.Alarm;
import com.yangpan.ynote.bean.Contacts;
import com.yangpan.ynote.broadcast.AlarmReceiver;
import com.yangpan.ynote.db.AlarmDao;
import com.yangpan.ynote.db.DatabaseOperation;
import com.yangpan.ynote.utils.FindInfo;
import com.yangpan.ynote.utils.LineEditText;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class AddActy extends Activity {
	private Button bt_back;
	private Button bt_save;
	private TextView tv_title;
	private SQLiteDatabase db;
	private DatabaseOperation dop;

	private RelativeLayout rl_root;
	private LineEditText et_Notes;
	private GridView bottomMenu;
	// 底部按钮
	private int[] bottomItems = { R.drawable.tabbar_handwrite,
			R.drawable.tabbar_paint, R.drawable.tabbar_microphone,
			R.drawable.tabbar_photo, R.drawable.tabbar_camera,
			R.drawable.tabbar_appendix };
	InputMethodManager imm;
	Intent intent;
	String editModel = null;
	int item_Id;

	private int alarmCount = 0;

	private static String IMGPATH = "/sdcard/notes/yyyyMMddHHmmsspaint.png";
	// 记录editText中的图片，用于单击时判断单击的是那一个图片
	private List<Map<String, String>> imgList = new ArrayList<Map<String, String>>();

	private Uri imageUri;
	private ImageView test;
	private Uri uri;
	private Button bt_reminder;
	private NotificationManager manager;
	private PendingIntent pendingIntent;

	// private UMSocialService mController;
	AlarmDao dao;
	private AlarmManager alarmManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.acty_add);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_add);
		initView();
		initAlarmStat();

	}

	private void initAlarmStat() {
		dao = new AlarmDao(this);
		alarmCount = dao.findCount();
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		if (alarmCount > 0) {
			bt_reminder.setBackground(getResources().getDrawable(
					R.drawable.alarm_on));
		} else {
			bt_reminder.setBackground(getResources().getDrawable(
					R.drawable.alarm_no));
		}
	}

	private void initView() {
		rl_root = (RelativeLayout) this.findViewById(R.id.rl_root);
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(new ClickEvent());
		bt_save = (Button) findViewById(R.id.bt_save);
		bt_save.setOnClickListener(new ClickEvent());

		// 闹钟功能
		bt_reminder = (Button) findViewById(R.id.bt_reminder);
		bt_reminder.setOnClickListener(new ClickEvent());
		bt_reminder.setVisibility(View.VISIBLE);
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_Notes = (LineEditText) findViewById(R.id.et_note);

		bottomMenu = (GridView) findViewById(R.id.bottomMenu);

		// 配置菜单
		initBottomMenu();
		// 为菜单设置监听器
		bottomMenu.setOnItemClickListener(new MenuClickEvent());
		// 默认关闭软键盘,可以通过失去焦点设置
		// et_Notes.setFocusable(false);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_Notes.getWindowToken(), 0);
		//
		// et_Notes.setOnTouchListener(new TextTouchEvent());
		dop = new DatabaseOperation(this, db);
		intent = getIntent();
		editModel = intent.getStringExtra("editModel");
		item_Id = intent.getIntExtra("noteId", 0);
		// 加载数据
		loadData();
		// 给editText添加单击事件
		// et_Notes.setOnTouchListener(new TextTouchEvent());
		et_Notes.setOnClickListener(new TextClickEvent());
		/*
		 * et_Notes.setOnTouchListener(new OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * 
		 * int inType = et_Notes.getInputType(); // backup the input type
		 * et_Notes.setInputType(InputType.TYPE_NULL); // disable soft input
		 * et_Notes.onTouchEvent(event); // call native handler
		 * et_Notes.setInputType(inType); // restore input type
		 * et_Notes.setSelection(et_Notes.getText().length()); return true; }
		 * });
		 */
	}

	// 加载数据
	private void loadData() {

		// 如果是新增记事模式，则将editText清空
		if (editModel.equals("newAdd")) {
			et_Notes.setText("");
		}
		// 如果编辑的是已存在的记事，则将数据库的保存的数据取出，并显示在EditText中
		else if (editModel.equals("update")) {
			tv_title.setText("编辑记事");

			dop.create_db();
			Cursor cursor = dop.query_db(item_Id);
			cursor.moveToFirst();
			// 取出数据库中相应的字段内容
			String context = cursor.getString(cursor.getColumnIndex("context"));

			// 定义正则表达式，用于匹配路径
			Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
			Matcher m = p.matcher(context);
			int startIndex = 0;
			while (m.find()) {
				// 取出路径前的文字
				if (m.start() > 0) {
					et_Notes.append(context.substring(startIndex, m.start()));
				}

				SpannableString ss = new SpannableString(m.group().toString());

				// 取出路径
				String path = m.group().toString();
				// 取出路径的后缀
				String type = path.substring(path.length() - 3, path.length());
				Bitmap bm = null;
				Bitmap rbm = null;
				// 判断附件的类型，如果是录音文件，则从资源文件中加载图片
				if (type.equals("amr")) {
					bm = BitmapFactory.decodeResource(getResources(),
							R.drawable.record_icon);
					// 缩放图片
					rbm = resize(bm, 200);
				} else {
					// 取出图片
					bm = BitmapFactory.decodeFile(m.group());
					// 缩放图片
					rbm = resize(bm, 480);
				}

				// 为图片添加边框效果
				rbm = getBitmapHuaSeBianKuang(rbm);

				ImageSpan span = new ImageSpan(this, rbm);
				ss.setSpan(span, 0, m.end() - m.start(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				System.out.println(m.start() + "-------" + m.end());
				et_Notes.append(ss);
				startIndex = m.end();

				// 用List记录该录音的位置及所在路径，用于单击事件
				Map<String, String> map = new HashMap<String, String>();
				map.put("location", m.start() + "-" + m.end());
				map.put("path", path);
				imgList.add(map);
			}
			// 将最后一个图片之后的文字添加在TextView中
			et_Notes.append(context.substring(startIndex, context.length()));
			dop.close_db();
		}
	}

	// 为EidtText设置监听器
	class TextClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			Spanned s = et_Notes.getText();
			ImageSpan[] imageSpans;
			imageSpans = s.getSpans(0, s.length(), ImageSpan.class);

			int selectionStart = et_Notes.getSelectionStart();
			for (ImageSpan span : imageSpans) {

				int start = s.getSpanStart(span);
				int end = s.getSpanEnd(span);
				// 找到图片
				if (selectionStart >= start && selectionStart < end) {

					String path = null;
					for (int i = 0; i < imgList.size(); i++) {
						Map map = imgList.get(i);
						// 找到了
						if (map.get("location").equals(start + "-" + end)) {
							path = imgList.get(i).get("path");
							break;
						}
					}
					// 接着判断当前图片是否是录音，如果为录音，则跳转到试听录音的Activity，如果不是，则跳转到查看图片的界面
					// 录音，则跳转到试听录音的Activity
					if (path.substring(path.length() - 3, path.length())
							.equals("amr")) {
						Intent intent = new Intent(AddActy.this,
								ShowRecordActy.class);
						intent.putExtra("audioPath", path);
						startActivity(intent);
					}
					// 图片，则跳转到查看图片的界面
					else if ((path.substring(path.length() - 3, path.length())
							.equals("png") || (path.substring(
							path.length() - 3, path.length()).equals("jpg")))) {
						// 使用自定义Activity
						if (path.startsWith("/sdcard/notes/video")) {
							playVideo(path);
						} else {
							Intent intent = new Intent(AddActy.this,
									ShowPictureActy.class);
							intent.putExtra("imgPath", path);
							startActivity(intent);
						}
					}
				} else
					// 如果单击的是空白出或文字，则获得焦点，即打开软键盘
					imm.showSoftInput(et_Notes, 0);
			}
		}
	}

	// 播放视频
	public void playVideo(String path) {
		Intent intent = new Intent(AddActy.this, PlayVideoActivity.class);
		intent.putExtra("path", path);
		startActivity(intent);
	}

	// 给编辑区域设置触摸监听器
	class TextTouchEvent implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			Spanned s = et_Notes.getText();
			ImageSpan[] imageSpans;
			imageSpans = s.getSpans(0, s.length(), ImageSpan.class);

			int selectionStart = et_Notes.getSelectionStart();
			for (ImageSpan span : imageSpans) {

				int start = s.getSpanStart(span);
				int end = s.getSpanEnd(span);
				int inType = et_Notes.getInputType(); // backup the input type
				// 找到图片
				if (selectionStart >= start && selectionStart < end) {
					Bitmap bitmap = ((BitmapDrawable) span.getDrawable())
							.getBitmap();
					System.out.println(span.getSource()
							+ "---------------------------");

					et_Notes.setInputType(InputType.TYPE_NULL); // disable soft
																// input
					et_Notes.onTouchEvent(event); // call native handler
					et_Notes.setInputType(inType); // restore input type

					AddActy.this.finish();

				} else {
					// 如果单击的是空白出或文字，则获得焦点，即打开软键盘
					imm.showSoftInput(et_Notes, 0);
					et_Notes.setInputType(inType);
				}
			}
			return true;

		}

	}

	// 设置按钮监听器
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_back:
				// 当前Activity结束，则返回上一个Activity
				AddActy.this.finish();
				break;

			// 设置闹钟
			case R.id.bt_reminder:
				// time
				// Intent intent = new Intent(AddActy.this, SetTimeActy.class);
				// // 如果为空不能去设置闹钟
				// if (TextUtils.isEmpty(et_Notes.getText())) {
				// Toast.makeText(AddActy.this, "请输入内容", 0).show();
				// } else {
				// intent.putExtra("notes", et_Notes.getText());
				// startActivity(intent);
				// }
				if (alarmCount == 0) {
					GotoCreateAlarm();
				} else {
					Intent intent = new Intent(AddActy.this,
							AlarmActivity.class);
					startActivity(intent);
				}
				break;

			// 将记事添加到数据库中
			case R.id.bt_save:
				// 取得EditText中的内容
				String context = et_Notes.getText().toString();

				if (TextUtils.isEmpty(context)) {
					Toast.makeText(AddActy.this, "记事为空!", Toast.LENGTH_LONG)
							.show();
				} else {
					// 取得当前时间
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					String time = formatter.format(curDate);
					// 截取EditText中的前一部分作为标题，用于显示在主页列表中
					String title = getTitle(context);
					// 打开数据库
					dop.create_db();
					// 判断是更新还是新增记事
					if (editModel.equals("newAdd")) {
						// 将记事插入到数据库中
						dop.insert_db(title, context, time);

					}
					// 如果是编辑则更新记事即可
					else if (editModel.equals("update")) {
						dop.update_db(title, context, time, item_Id);
					}
					dop.close_db();
					
					//自动保存为闹钟
					Log.v("--->content", context);
					List<Alarm> findData = FindInfo.findData(context);
					Log.v("--->content size", findData.size() + "");
					addDataToAlarm(findData);
					
					// 结束当前activity
					AddActy.this.finish();
				}
				break;
			}
		}

	}

	/**
	 * 用于创建一个新的闹钟
	 */
	private void GotoCreateAlarm() {
		View diaView = View.inflate(this, R.layout.dialog_add_alarm, null);
		final TextView tv_time = (TextView) diaView.findViewById(R.id.tv_time);
		final RadioButton rb_meeting = (RadioButton) diaView
				.findViewById(R.id.rb_meeting);
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		tv_time.setText(hour + ":" + min);
		TimePicker timePicker = (TimePicker) diaView
				.findViewById(R.id.timepicker);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				tv_time.setText(hourOfDay + ":" + minute);
			}
		});

		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(diaView);
		builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String time = tv_time.getText().toString();
				String type;
				if (rb_meeting.isChecked()) {
					type = Contacts.TYPE_MEET;
				} else {
					type = Contacts.TYPE_FLIGHT;
				}
				int status = 1;
				// 添加闹钟
				Alarm alarm = new Alarm(time, type, status);
				dao.addAlarm(alarm);
				// 修改图标状态
				initAlarmStat();
				// 打开闹钟服务
				openAlarm(alarm);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();

	}

	/**
	 * 再返回的时候 将内容识别
	 * 
	 * @param findData
	 */
	public void addDataToAlarm(List<Alarm> findData) {
		for(Alarm alarm:findData){
			openAlarm(alarm);
			dao.addAlarm(alarm);
		}
	}
	/**
	 * 为新创建的闹钟打开服务
	 * 
	 * @param alarm
	 */
	protected void openAlarm(Alarm alarm) {
		String[] time = alarm.getTime().split(":");
		int hour = Integer.parseInt(time[0]);
		int min = Integer.parseInt(time[1]);
		Calendar c = Calendar.getInstance();// 获取日期对象
		c.setTimeInMillis(System.currentTimeMillis()); // 设置Calendar对象
		c.set(Calendar.HOUR, hour); // 设置闹钟小时数
		c.set(Calendar.MINUTE, min); // 设置闹钟的分钟数
		c.set(Calendar.SECOND, 0); // 设置闹钟的秒数
		c.set(Calendar.MILLISECOND, 0); // 设置闹钟的毫秒数
		Intent intent = new Intent(AddActy.this, AlarmReceiver.class); // 创建Intent对象
		PendingIntent pi = PendingIntent.getBroadcast(AddActy.this, 0, intent,
				0); // 创建PendingIntent
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi); // 设置闹钟
		Toast.makeText(AddActy.this, "闹钟设置成功", Toast.LENGTH_LONG).show();// 提示用户
	}

	// 截取EditText中的前一部分作为标题，用于显示在主页列表中
	private String getTitle(String context) {
		// 定义正则表达式，用于匹配路径
		Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
		Matcher m = p.matcher(context);
		StringBuffer strBuff = new StringBuffer();
		String title = "";
		int startIndex = 0;
		while (m.find()) {
			// 取出路径前的文字
			if (m.start() > 0) {
				strBuff.append(context.substring(startIndex, m.start()));
			}
			// 取出路径
			String path = m.group().toString();
			// 取出路径的后缀
			String type = path.substring(path.length() - 3, path.length());
			// 判断附件的类型
			if (type.equals("amr")) {
				strBuff.append("[录音]");
			} else {
				strBuff.append("[图片]");
			}
			startIndex = m.end();
			// 只取出前15个字作为标题
			if (strBuff.length() > 15) {
				// 统一将回车,等特殊字符换成空格
				title = strBuff.toString().replaceAll("\r|\n|\t", " ");
				return title;
			}
		}
		strBuff.append(context.substring(startIndex, context.length()));
		// 统一将回车,等特殊字符换成空格
		title = strBuff.toString().replaceAll("\r|\n|\t", " ");
		return title;
	}

	// 配置菜单
	private void initBottomMenu() {
		ArrayList<Map<String, Object>> menus = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < bottomItems.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("image", bottomItems[i]);
			menus.add(item);
		}
		bottomMenu.setNumColumns(bottomItems.length);
		bottomMenu.setSelector(R.drawable.bottom_item);
		SimpleAdapter mAdapter = new SimpleAdapter(AddActy.this, menus,
				R.layout.item_button, new String[] { "image" },
				new int[] { R.id.item_image });
		bottomMenu.setAdapter(mAdapter);
	}

	// 设置菜单项监听器
	class MenuClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final Intent intent;
			switch (position) {
			// 手写
			case 0:
				intent = new Intent(AddActy.this, HandWriteActy.class);
				startActivityForResult(intent, 5);
				break;

			// 绘图
			case 1:
				intent = new Intent(AddActy.this, PaintActy.class);
				startActivityForResult(intent, 3);
				break;
			// 语音
			case 2:
				intent = new Intent(AddActy.this, RecordActy.class);
				startActivityForResult(intent, 4);
				break;
			// 照片
			case 3:

				File getImage = new File(IMGPATH);
				if (getImage.exists()) {
					getImage.delete();
				}
				try {
					getImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(getImage);
				intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 设置图片输出的uri地址
				startActivityForResult(intent, 6);

				break;
			// 拍照
			case 4:
				// 调用系统拍照界面
				View popRoot = View.inflate(AddActy.this,
						R.layout.choice_action, null);

				final PopupWindow popupWindow = new PopupWindow(popRoot,
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
						true);
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						android.R.color.transparent));

				TextView tv_takepicture = (TextView) popRoot
						.findViewById(R.id.tv_takepicture);
				tv_takepicture.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						Intent intentP = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intentP.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
						// 区分选择相片
						startActivityForResult(intentP, 2);
					}
				});
				TextView tv_makevideo = (TextView) popRoot
						.findViewById(R.id.tv_makevideo);
				tv_makevideo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						Intent intentV = new Intent(AddActy.this,
								MakeVideoActivity.class);
						// 区分选择相片
						startActivityForResult(intentV, 10);
					}
				});
				TextView tv_cancle = (TextView) popRoot
						.findViewById(R.id.tv_cancle);
				tv_cancle.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (popupWindow.isShowing())
							popupWindow.dismiss();
					}
				});

				popupWindow.setOutsideTouchable(false);
				popupWindow
						.setAnimationStyle(android.R.anim.decelerate_interpolator);
				popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

				break;

			// 分享
			case 5:

				showShare();

				break;

			}

		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 取得数据

			if (data != null) {
				uri = data.getData();
			}
			ContentResolver cr = AddActy.this.getContentResolver();
			Bitmap bitmap = null;
			Bundle extras = null;
			// 如果是选择照片
			if (requestCode == 6) {

				Intent intent1 = new Intent("com.android.camera.action.CROP");
				intent1.setDataAndType(uri, "image/*"); // 注意这里设置的是uri
				intent1.putExtra("scale", true); // 允许裁剪,不然崩溃
				intent1.putExtra("crop", true);
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent1, 7);

			} else if (requestCode == 7) {
				Bitmap bitma;
				try {
					bitma = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(imageUri));
					try {
						InsertBitmap(bitma, 480, IMGPATH);
					} catch (Exception e) {

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();

				}
			}
			// 如果选择的是拍照
			else if (requestCode == 2) {

				try {

					if (uri != null)
						// 这个方法是根据Uri获取Bitmap图片的静态方法
						bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
					// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
					else {
						extras = data.getExtras();
						bitmap = extras.getParcelable("data");
					}
					// 将拍的照片存入指定的文件夹下
					// 获得系统当前时间，并以该时间作为文件名
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					String str = formatter.format(curDate);
					String paintPath = "";
					str = str + "paint.png";
					File dir = new File("/sdcard/notes/");
					File file = new File("/sdcard/notes/", str);
					if (!dir.exists()) {
						dir.mkdir();
					} else {
						if (file.exists()) {
							file.delete();
						}
					}
					FileOutputStream fos = new FileOutputStream(file);
					// 将 bitmap 压缩成其他格式的图片数据
					bitmap.compress(CompressFormat.PNG, 100, fos);
					fos.flush();
					fos.close();
					String path = "/sdcard/notes/" + str;
					// 插入图片
					InsertBitmap(bitmap, 480, path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 返回的是绘图后的结果
			else if (requestCode == 3) {
				extras = data.getExtras();
				String path = extras.getString("paintPath");
				// 通过路径取出图片，放入bitmap中
				bitmap = BitmapFactory.decodeFile(path);
				// 插入绘图文件
				InsertBitmap(bitmap, 480, path);
			}
			// 返回的是录音文件
			else if (requestCode == 4) {
				extras = data.getExtras();
				String path = extras.getString("audio");
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.record_icon);
				// 插入录音图标
				InsertBitmap(bitmap, 200, path);

			}
			// 返回的是手写文件
			else if (requestCode == 5) {
				extras = data.getExtras();
				String path = extras.getString("handwritePath");
				// 通过路径取出图片，放入bitmap中
//				bitmap = BitmapFactory.decodeFile(path);
				// 插入绘图文件
//				InsertBitmap(bitmap, 480, path);
				et_Notes.append(path);
			}
			// 视频录制结束
			else if (requestCode == 10) {
				String videoPath = data.getStringExtra("path");
				MediaMetadataRetriever retriever = new MediaMetadataRetriever();
				retriever.setDataSource(videoPath);
				Bitmap clipBitmap = retriever.getFrameAtTime();
				String clipImagePath = "/sdcard/notes/";
				String fileName = videoPath.split("/")[3];
				fileName = fileName.substring(0, fileName.lastIndexOf('.'))
						+ ".png";
				fileName = "video" + fileName;
				clipImagePath += fileName;
				Log.v("---", clipImagePath);
				if (saveBitmap(clipBitmap, clipImagePath)) {
					InsertBitmap(clipBitmap, 200, clipImagePath);
				}
			}
		}
	}

	public boolean saveBitmap(Bitmap bm, String path) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 将图片等比例缩放到合适的大小并添加在EditText中
	void InsertBitmap(Bitmap bitmap, int S, String imgPath) {

		bitmap = resize(bitmap, S);
		// 添加边框效果
		bitmap = getBitmapHuaSeBianKuang(bitmap);
		// bitmap = addBigFrame(bitmap,R.drawable.line_age);
		final ImageSpan imageSpan = new ImageSpan(this, bitmap);
		SpannableString spannableString = new SpannableString(imgPath);
		spannableString.setSpan(imageSpan, 0, spannableString.length(),
				SpannableString.SPAN_MARK_MARK);
		// 光标移到下一行
		// et_Notes.append("\n");
		Editable editable = et_Notes.getEditableText();
		int selectionIndex = et_Notes.getSelectionStart();
		spannableString.getSpans(0, spannableString.length(), ImageSpan.class);

		// 将图片添加进EditText中
		editable.insert(selectionIndex, spannableString);
		// 添加图片后自动空出两行
		et_Notes.append("\n");

		// 用List记录该录音的位置及所在路径，用于单击事件
		Map<String, String> map = new HashMap<String, String>();
		map.put("location", selectionIndex + "-"
				+ (selectionIndex + spannableString.length()));
		map.put("path", imgPath);
		imgList.add(map);
	}

	// 等比例缩放图片
	private Bitmap resize(Bitmap bitmap, int S) {
		int imgWidth = bitmap.getWidth();
		int imgHeight = bitmap.getHeight();
		double partion = imgWidth * 1.0 / imgHeight;
		double sqrtLength = Math.sqrt(partion * partion + 1);
		// 新的缩略图大小
		double newImgW = S * (partion / sqrtLength);
		double newImgH = S * (1 / sqrtLength);
		float scaleW = (float) (newImgW / imgWidth);
		float scaleH = (float) (newImgH / imgHeight);

		Matrix mx = new Matrix();
		// 对原图片进行缩放
		mx.postScale(scaleW, scaleH);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx,
				true);
		return bitmap;
	}

	// 给图片加边框，并返回边框后的图片
	public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
		float frameSize = 0.2f;
		Matrix matrix = new Matrix();

		// 用来做底图
		Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		// 设置底图为画布
		Canvas canvas = new Canvas(bitmapbg);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));

		float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
				/ (bitmap.getWidth());
		float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
				/ (bitmap.getHeight());
		matrix.reset();
		matrix.postScale(scale_x, scale_y);

		// 对相片大小处理(减去边框的大小)
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL);

		// 绘制底图边框
		canvas.drawRect(
				new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
				paint);
		// 绘制灰色边框
		paint.setColor(Color.BLUE);
		canvas.drawRect(
				new Rect((int) (frameSize), (int) (frameSize), bitmapbg
						.getWidth() - (int) (frameSize), bitmapbg.getHeight()
						- (int) (frameSize)), paint);

		canvas.drawBitmap(bitmap, frameSize + 1, frameSize + 1, paint);

		return bitmapbg;
	}

	// 分享
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.ssdk_oks_share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(et_Notes.getText().toString());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

}
