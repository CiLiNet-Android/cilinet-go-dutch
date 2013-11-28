package cilinet.godutch.activity;

import android.os.Bundle;
import android.widget.GridView;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.IndexGrdVAdapter;

/** 主框架 **/
public class IndexActivity extends FrameActivity {
	
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
	
}
