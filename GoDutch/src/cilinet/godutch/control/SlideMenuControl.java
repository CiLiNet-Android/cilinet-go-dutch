package cilinet.godutch.control;

import java.util.List;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cilinet.godutch.R;
import cilinet.godutch.adapter.SlideMenuAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.utility.UnitTransformUtil;

/** 上下滑动的菜单的封装 **/
public class SlideMenuControl {
	
	/** include_bottom.xml 底部布局 **/
	private RelativeLayout include_bottom;
	
	/** 底部滑动ListView **/
	private ListView listV_slideMenu;
	
	private boolean mIsSlideUp;
	
	private Activity mActivity;
	
	private OnSlideMenuItemClickListener mSlideMenuItemClickListener;
	
	public SlideMenuControl(Activity activity){
		mActivity = activity;
		
		init();
	}
	
	/** 向上滑动 **/
	private void slideUp(){
		RelativeLayout.LayoutParams _layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
		_layoutParams.addRule(RelativeLayout.BELOW, R.id.include_top);
		
		include_bottom.setLayoutParams(_layoutParams);
		
		mIsSlideUp = true;
	}
	
	/** 向下滑动 **/
	private void slideDown(){
		RelativeLayout.LayoutParams _layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,UnitTransformUtil.dip2px(mActivity, 68));
		_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		include_bottom.setLayoutParams(_layoutParams);
		
		mIsSlideUp = false;
	}
	
	/** 根据当前情况滑动 **/
	public void slide(){
		if(mIsSlideUp){//如果已经滑动到顶部，则向下滑动
			slideDown();
		}else {
			slideUp();
		}
	}
	
	public void bindDatas(List<SlideMenuItem> boundDatas){
		SlideMenuAdapter _slideMenuAdapter = new SlideMenuAdapter(mActivity, boundDatas);
		listV_slideMenu.setAdapter(_slideMenuAdapter);
	}
	
	private void init(){
		initView();
		//只有实现了OnSlideMenuItemClickListener接口，才有底部监听事件和ListView
		if(mActivity instanceof OnSlideMenuItemClickListener){
			mSlideMenuItemClickListener = (OnSlideMenuItemClickListener)mActivity;
			initSlideMenu();
		}
	
	}
	
	/** 视图初始化 **/
	private void initView(){
		include_bottom = (RelativeLayout)mActivity.findViewById(R.id.include_bottom);
	}
	
	/** 初始化菜单 **/
	private void initSlideMenu(){
		include_bottom.setFocusableInTouchMode(true);//键盘点击时，让该控件获得焦点
		include_bottom.setOnClickListener(new OnSlideMenuClickListener());
		include_bottom.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
				if(keyCode == KeyEvent.KEYCODE_MENU && keyEvent.getAction() == KeyEvent.ACTION_UP){
					slide();
				}
				return false;
			}
		});
		
		listV_slideMenu = (ListView)mActivity.findViewById(R.id.listV_slideMenu);
		listV_slideMenu.setOnItemClickListener(new MyOnSlideMenuItemClickListener());
	}
	
	/** 用户点击SlideMenu界面时，只是滑动界面  高度69dp**/
	private class OnSlideMenuClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			slide();
		}
	}
	
	private class MyOnSlideMenuItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long arg3) {
			mSlideMenuItemClickListener.onSlideMenuItemClick(view, (SlideMenuItem)adapterView.getItemAtPosition(position));
		}
		
	}
	
	/** 用户点击SlideMenuItem时 **/
	public interface OnSlideMenuItemClickListener {
		//参数由SlideMenuContorl传给实现类
		public void onSlideMenuItemClick(View view,SlideMenuItem slideMenuItem) ;
	}

	public void removeSelf() {
		RelativeLayout lyot_includeMain = (RelativeLayout)mActivity.findViewById(R.id.lyot_includeMain);
		lyot_includeMain.removeViewInLayout(include_bottom);
		
		include_bottom = null;
	}
}
