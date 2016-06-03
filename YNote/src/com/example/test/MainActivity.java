package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.test.R;
import com.yangpan.ynote.db.DatabaseOperation;
import com.yangpan.ynote.db.SkinSettingManager;
import com.yangpan.ynote.item.Data;

import cn.sharesdk.framework.ShareSDK;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private SkinSettingManager mSettingManager;
	// 换肤
	private LinearLayout[] layout;
	private int[] layouts = { R.id.am_ll_main, R.id.am_ll_main };

	// 用来记录是否显示checkBox
	public HashMap<Integer, Boolean> ischecknew = new HashMap<Integer, Boolean>();

	private Button bt_add;
	/**
	 * 批量编辑按钮
	 */
	private Button bt_delete;
	private SQLiteDatabase db;
	private DatabaseOperation dop;
	/**
	 * 笔记ListView
	 */
	private ListView lv_notes;
	/**
	 * 记事本Id
	 */
	private TextView tv_note_id;
	// 初始化数据列表
	private List<Data> pointList = new ArrayList<Data>();
	/**
	 * 取消按钮
	 */
	private Button cancle;
	/**
	 * 删除按钮
	 */
	private Button delete;

	private MyAdapter mAdapter;

	ViewHolder holder = null;

	private boolean edit;

	/**
	 * 取消和批量删除的布局
	 */
	private RelativeLayout relative;

	private Button skinBN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		// 设置自定义标题栏
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_main);

		// 数据库操作
		dop = new DatabaseOperation(this, db);

		lv_notes = (ListView) findViewById(R.id.lv_notes);
		relative = (RelativeLayout) findViewById(R.id.relative);
		bt_add = (Button) findViewById(R.id.bt_add);
		bt_add.setOnClickListener(new ClickEvent());
		skinBN = (Button) findViewById(R.id.bt_skin);
		skinBN.setOnClickListener(this);
		cancle = (Button) findViewById(R.id.cancle);
		delete = (Button) findViewById(R.id.delete);
		cancle.setOnClickListener(this);

		mAdapter = new MyAdapter();
		lv_notes.setAdapter(mAdapter);

		// 显示记事列表
		showNotesList();

		// 初始化map的选中状态
		for (int i = 0; i < pointList.size(); i++) {
			ischecknew.put(i, false);
		}

		// 为记事列表添加监听器
		lv_notes.setOnItemClickListener(new ItemClickEvent());
		// 为记事列表添加长按事件
		lv_notes.setOnItemLongClickListener(new ItemLongClickEvent());

		bt_delete = (Button) findViewById(R.id.bt_delete);
		bt_delete.setOnClickListener(this);

	}

	@Override
	protected void onResume() {

		// 清空数据
		pointList.clear();
		// 显示记事列表
		showNotesList();

		// 初始化map的选中状态
		for (int i = 0; i < pointList.size(); i++) {
			ischecknew.put(i, false);
		}
		// 每个页面都要重写这个方法和初始化皮肤的方法
		layout = new LinearLayout[layouts.length];
		for (int i = 0; i < layouts.length; i++) {
			layout[i] = (LinearLayout) findViewById(layouts[i]);
			mSettingManager = new SkinSettingManager(MainActivity.this,
					layout[i]);
			mSettingManager.initSkins();
		}
		super.onResume();
	}

	// 显示记事列表
	private void showNotesList() {
		// 创建或打开数据库
		dop.create_db();

		Cursor cursor = dop.query_db();
		while (cursor.moveToNext()) {
			Data mData = new Data();
			mData.setTv_note_id(cursor.getString(cursor.getColumnIndex("_id")));
			mData.setTv_note_title(cursor.getString(cursor
					.getColumnIndex("title")));
			mData.setTv_note_time(cursor.getString(cursor
					.getColumnIndex("time")));
			pointList.add(mData);
		}

		mAdapter.notifyDataSetChanged();
		dop.close_db();

	}

	// 记事列表长按监听器
	class ItemLongClickEvent implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			tv_note_id = (TextView) view.findViewById(R.id.tv_note_id);

			int item_id = Integer.parseInt(tv_note_id.getText().toString());
			simpleList(item_id);
			return true;
		}

	}

	// 简单列表对话框，用于选择操作
	public void simpleList(final int item_id) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,
				R.style.custom_dialog);
		alertDialogBuilder.setTitle("选择操作");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setItems(R.array.itemOperation,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						// 编辑
						case 0:
							Intent intent = new Intent(MainActivity.this,
									AddActy.class);
							intent.putExtra("editModel", "update");
							intent.putExtra("noteId", item_id);
							startActivity(intent);
							break;
						// 删除
						case 1:
							dop.create_db();
							dop.delete_db(item_id);
							dop.close_db();
							// 刷新列表显示
							mAdapter.notifyDataSetChanged();
							pointList.clear();
							showNotesList();
							break;
						}
					}
				});
		alertDialogBuilder.create();
		alertDialogBuilder.show();
	}

	// 记事列表单击监听器
	class ItemClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			tv_note_id = (TextView) view.findViewById(R.id.tv_note_id);
			int item_id = Integer.parseInt(tv_note_id.getText().toString());
			Intent intent = new Intent(MainActivity.this, AddActy.class);
			intent.putExtra("editModel", "update");
			intent.putExtra("noteId", item_id);
			startActivity(intent);
		}
	}

	// 点击进入添加模式
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_add:
				Intent intent = new Intent(MainActivity.this, AddActy.class);
				intent.putExtra("editModel", "newAdd");
				startActivity(intent);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		// menu.add(0, 1, 1, "新建");
		// menu.add(0, 2, 2, "换肤");
		// menu.add(0, 3, 3, "删除");
		// menu.add(0, 4, 4, "退出");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return pointList.size();
		}

		@Override
		public Object getItem(int position) {

			return pointList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(MainActivity.this,
						R.layout.item_note, null);

				holder.tv_note_id = (TextView) convertView
						.findViewById(R.id.tv_note_id);
				holder.tv_note_title = (TextView) convertView
						.findViewById(R.id.tv_note_title);
				holder.tv_note_time = (TextView) convertView
						.findViewById(R.id.tv_note_time);
				holder.check = (CheckBox) convertView.findViewById(R.id.check);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.check.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (v.getId() == holder.check.getId()) {
							if (ischecknew.get(position) == true) {
								ischecknew.put(position, false);
							} else {
								ischecknew.put(position, true);
							}

						}
					} catch (Exception e) {

					}

				}
			});

			try {
				if (ischecknew.get(position) == true) {
					holder.check.setChecked(true);

				} else {
					holder.check.setChecked(false);
				}
			} catch (Exception e) {

			}

			// 如果编辑就显示checkBox
			if (edit) {
				holder.check.setVisibility(View.VISIBLE);
			} else {
				holder.check.setVisibility(View.GONE);
			}

			// 删除
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					edit = false;
					relative.setVisibility(View.GONE);
					holder.check.setVisibility(View.GONE);
					dop.create_db();

					Iterator iterator = ischecknew.entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry entry = (Map.Entry) iterator.next();

						Object val = entry.getValue();
						if (entry.getValue().equals(true)) {

							int item_id = Integer.parseInt(pointList.get(
									(Integer) entry.getKey()).getTv_note_id());

							System.out.println(item_id);

							dop.delete_db(item_id);
							pointList.remove((Integer) entry.getKey());
							iterator.remove();
						}
					}

					dop.close_db();
					// 刷新列表显示
					notifyDataSetChanged();
					pointList.clear();
					showNotesList();

				}
			});

			holder.tv_note_id.setText(pointList.get(position).getTv_note_id());
			holder.tv_note_title.setText(pointList.get(position)
					.getTv_note_title());
			holder.tv_note_time.setText(pointList.get(position)
					.getTv_note_time());

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tv_note_id;
		public TextView tv_note_title;
		public TextView tv_note_time;
		public CheckBox check;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			edit = false;
			relative.setVisibility(View.GONE);
			holder.check.setVisibility(View.GONE);
			break;

		case R.id.bt_delete:
			edit = true;
			relative.setVisibility(View.VISIBLE);
			break;
		
		case R.id.bt_skin:
			Intent intent = new Intent(MainActivity.this, MySkinActy.class);
			startActivity(intent);
			break;
		}
	}

}
