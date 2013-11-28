package cilinet.godutch.control;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cilinet.godutch.R;
import cilinet.godutch.adapter.SlideMenuAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.util.UnitTransformUtil;

/** 上下滑动的菜单的封装 **/
public class SlideMenuControl {
	
	/** include_bottom.xml 底部布局 **/
	private RelativeLayout include_bottom;
	
	/** 底部滑动ListView **/
	private ListView listV_slideMenu;
	
	private boolean mIsSlideUp;
	
	private Activity mActivity;
	
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
	private void slide(){
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
	}
	
	private void initView(){
		include_bottom = (RelativeLayout)mActivity.findViewById(R.id.include_bottom);
		include_bottom.setOnClickListener(new OnSlideMenuClickListener());
		
		listV_slideMenu = (ListView)mActivity.findViewById(R.id.listV_slideMenu);
	}
	
	/** 用户点击SlideMenu界面 **/
	private class OnSlideMenuClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			slide();
		}
		
	}
}
