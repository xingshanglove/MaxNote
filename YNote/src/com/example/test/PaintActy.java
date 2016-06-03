package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.test.R;
import com.yangpan.ynote.view.PaintView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class PaintActy extends Activity {    
 
    
    private PaintView paintView;
    private GridView paint_bottomMenu;
    
    
    private ListView lv_popWindow;
    
    
    //菜单资源
    private int[]  paintItems = {
    		R.drawable.paint_more,
    		R.drawable.paint_pencil,
    		R.drawable.paint_icon_color,
    		R.drawable.paint_icon_back,
    		R.drawable.paint_icon_forward,
    		R.drawable.paint_icon_delete
    };
    
    //画笔大小资源 
    private int[] penceilSizes = {
    		
    };
    
    private Button btn_save;
    private Button btn_back;
    
    private Bitmap  mBitmap;
    
    
    
    private PopupWindow popupWindow;
    
    private int select_paint_size_index = 0;
    private int select_paint_style_index = 0;
    private int select_paint_color_index = 0;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new TouchView(this));
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.acty_paint);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_add);
		//将自定义标题栏的标题定义为绘图
		TextView title = (TextView)findViewById(R.id.tv_title);
		title.setText("绘图");
		paint_bottomMenu = (GridView)findViewById(R.id.paintBottomMenu);
		paint_bottomMenu.setOnItemClickListener(new MenuClickEvent());
		
		paintView = (PaintView)findViewById(R.id.paint_layout);
		
		
		
		InitPaintMenu();
		
		
		btn_save = (Button)findViewById(R.id.bt_save);
		btn_back = (Button)findViewById(R.id.bt_back);
		btn_save.setOnClickListener(new ClickEvent());
		btn_back.setOnClickListener(new ClickEvent());
		
    }
    
    
    //配置绘图菜单
    public void InitPaintMenu(){
    	ArrayList<Map<String,Object>> menus = new ArrayList<Map<String,Object>>();
		for(int i = 0;i < paintItems.length;i++){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("image",paintItems[i]);
			menus.add(item);
		}
		paint_bottomMenu.setNumColumns(paintItems.length);
		paint_bottomMenu.setSelector(R.drawable.bottom_item);
		SimpleAdapter mAdapter = new SimpleAdapter(PaintActy.this, menus,R.layout.item_button, new String[]{"image"}, new int[]{R.id.item_image});
		paint_bottomMenu.setAdapter(mAdapter);
    }
    
    
    class ClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v == btn_save){
				
				//得到调用该Activity的Intent对象 
				Intent intent = getIntent();
				Bundle b = new Bundle();
				String path = paintView.saveBitmap();
				b.putString("paintPath", path);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				PaintActy.this.finish();
			}
			else if(v == btn_back){
				PaintActy.this.finish();
			}
		}
    	
    }
    
    
    //设置菜单项监听器
  	class MenuClickEvent implements OnItemClickListener{

  		@Override
  		public void onItemClick(AdapterView<?> parent, View view, int position,
  				long id) {
  			Intent intent;
  			switch(position){
  			//选择画笔样式
  			case 0:
  				showMoreDialog(view);
  				break;
  			//画笔大小
  			case 1:
  				showPaintSizeDialog(view);
  				break;
  			//画笔颜色
  			case 2:
  				showPaintColorDialog(view);
  				break;
  			//撤销
  			case 3:
  				paintView.undo();
  				break;
  			//恢复 
  			case 4 :
  				paintView.redo();
  				break;
  			//清空	
  			case 5 :
  					AlertDialog.Builder builder = new AlertDialog.Builder(PaintActy.this,R.style.custom_dialog);
	  				builder.setTitle("清空提示");
  					builder.setMessage("您确定要清空所有吗？");
	  				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							paintView.removeAllPaint();
							dialog.cancel();
						}
					});
	  				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
	  				Dialog dialog = builder.create();
	  				dialog.show();
  				
  				break;
  			default :
  				break;
  			
  			}
  		
  		}
 
  	}
  	
  	
  	//弹出画笔大小选项菜单
  	public void showPencilSize(View parent){
  		if(popupWindow == null){
  			LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			View view = layoutInflater.inflate(R.layout.popup_window, null);
  			TextView txt = (TextView)findViewById(R.id.tv_test);
  			//加载数据 
  			//ListView lv_group = (ListView)view.findViewById(R.id.lv_group);
  			ArrayList<Map<String,Object>> more_item_list = new ArrayList<Map<String,Object>>();
  			int[] more_items = new int[]{
  					R.string.track_line,
  					R.string.straight_line,
  					R.string.rectangle,
  					R.string.oval,
  					R.string.circle,
  					R.string.dots,
  					R.string.Eraser
  			};
  			
  			
  			//填充数据
  			for(int i = 0;i < more_items.length;i++){
  				Map<String,Object> map = new HashMap<String, Object>();
  				map.put("more_item", more_items[i]);
  				more_item_list.add(map);
  			}
  			
  			//设置适配器
  			//SimpleAdapter adapter = new SimpleAdapter(PaintActivity.this, more_item_list, R.layout.popup_window_item, new String[]{"more_item"}, new int[]{R.id.tv_more_item});
  			//lv_group.setAdapter(adapter);
  			//创建PopupWindow
  			popupWindow = new PopupWindow(view);
  			
  			
  			// 使其聚集         
  			popupWindow.setFocusable(true);         
  			// 设置允许在外点击消失         
  			popupWindow.setOutsideTouchable(true);           
  			// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景        
  			popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
  			
  			popupWindow.showAsDropDown(parent);
  		}
  	}
  	
  //弹出画笔颜色选项对话框
  	public void showPaintColorDialog(View parent){
  		
  		
  		
  		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.custom_dialog);
		alertDialogBuilder.setTitle("选择画笔颜色：");
		
		alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor, select_paint_color_index, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				select_paint_color_index = which;
				paintView.selectPaintColor(which);
				dialog.dismiss();
			}
		});
		
		alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialogBuilder.create().show();
  	}
  	
  	
  	
    //弹出画笔大小选项对话框
  	public void showPaintSizeDialog(View parent){
  		
  		
  		
  		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.custom_dialog);
		alertDialogBuilder.setTitle("选择画笔大小：");
		
		alertDialogBuilder.setSingleChoiceItems(R.array.paintsize, select_paint_size_index, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				select_paint_size_index = which;
				paintView.selectPaintSize(which);
				dialog.dismiss();
			}
		});
		
		alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialogBuilder.create().show();
  	}
  	
  	
  	//弹出更多选项对话框
  	public void showMoreDialog(View parent){
  		
  		
  		
  		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.custom_dialog);
		alertDialogBuilder.setTitle("选择画笔或橡皮擦：");
		
		alertDialogBuilder.setSingleChoiceItems(R.array.paintstyle, select_paint_style_index, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				select_paint_style_index = which;
				paintView.selectPaintStyle(which);
				dialog.dismiss();
			}
		});
		
		alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialogBuilder.create().show();
  	}
       
}
