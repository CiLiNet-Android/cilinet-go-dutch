package cilinet.godutch.activity.base;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.control.SlideMenuControl;


/** 显示的基本框架 **/
public class FrameActivity extends BaseActivity {
	
	private SlideMenuControl mSlideMenuControl;
	
	private ImageView imgV_topBarBackImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** 让界面全屏显示 **/
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				  WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.setContentView(R.layout.include_main);
		
		init();
	}
	
	private void init() {
		initView();
	}

	private void initView() {
		imgV_topBarBackImg = (ImageView)findViewById(R.id.imgV_topBarBackImg);
		imgV_topBarBackImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
	
	protected void hideTitleBackButton(){
		imgV_topBarBackImg.setVisibility(View.GONE);
	}

	/** 添加页面主题区域方法 **/
	protected void appendCenterMainBody(int resId){
		FrameLayout _includeCenter = (FrameLayout)super.findViewById(R.id.include_center);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		
		View _view = LayoutInflater.from(this).inflate(resId, null);
		_includeCenter.addView(_view, layoutParams);
	}
	
	/** 创建滑动菜单 **/
	protected void createSlideMenu(int arrayResId){
		mSlideMenuControl = new SlideMenuControl(this);
		
		List<SlideMenuItem> _boundDatas = new ArrayList<SlideMenuItem>();
		String[] _slideMenuItemsArray = super.getResources().getStringArray(arrayResId); 
		for(int i = 0; i < _slideMenuItemsArray.length; i ++){
			_boundDatas.add(new SlideMenuItem(i,_slideMenuItemsArray[i]));
		}
		
		mSlideMenuControl.bindDatas(_boundDatas);
	}
	
	/** 滑动菜单的滑动动作 **/
	protected void slideSlideMenu() {
		mSlideMenuControl.slide();
	}
	
	/** 创建上下文菜单 **/
	protected void createManageContextMenu(Menu menu){
		int _menuItems[] = {1,2};
		int _groupId = 0;
		int _order = 0;
		
		for(int i = 0; i < _menuItems.length; i ++){
			switch(_menuItems[i]){
			case 1:
				menu.add(_groupId, _menuItems[i], _order, R.string.MenuTextEdit);
				break;
			case 2:
				menu.add(_groupId, _menuItems[i], _order, R.string.MenuTextDelete);
				break;
			default:
				break;
			}
		}
	}
	
	/** 设置头部标题 **/
	protected void setTopBarTitle(String title){
		TextView txtV_topBarTitle = (TextView)super.findViewById(R.id.txtV_topBarTitle);
		txtV_topBarTitle.setText(title);
	}
	
	/** 移除底部 **/
	protected void removeIncludeBottom(){
		mSlideMenuControl = new SlideMenuControl(this);
		mSlideMenuControl.removeSelf();
	}

}
