package cilinet.godutch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.IndexGrdVAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.control.SlideMenuControl.OnSlideMenuItemClickListener;

/** 主框架 **/
public class IndexActivity extends FrameActivity implements OnSlideMenuItemClickListener{
	
	IndexGrdVAdapter mIndexGrdVAdapter;
	
	GridView grdV_index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_index);
		
		init();
	}
	
	private void init(){
		initIndexGrdVAdapter();
		initView();
	}
	
	private void initView(){
		grdV_index = (GridView)super.findViewById(R.id.grdV_index);
		grdV_index.setAdapter(mIndexGrdVAdapter);
		
		createSlideMenu(R.array.SlideMenuActivityMain);
	}
	
	private void initIndexGrdVAdapter(){
		 mIndexGrdVAdapter = new IndexGrdVAdapter(this);
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		Toast.makeText(this, slideMenuItem.itemTitle, Toast.LENGTH_SHORT).show();
	}
	
}
