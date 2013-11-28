package cilinet.godutch.activity.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import cilinet.godutch.R;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.control.SlideMenuControl;


/** 显示的基本框架 **/
public class FrameActivity extends BaseActivity {
	
	private SlideMenuControl mSlideMenuControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** 让界面全屏显示 **/
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.include_main);	
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

}
