package cilinet.godutch.activity;

import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.IndexGrdVAdapter;
import cilinet.godutch.adapter.IndexGrdVItem;
import android.os.Bundle;
import android.widget.GridView;

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
	}
	
	/** 初始化图标和名称 **/
	private IndexGrdVItem[] initIndexGrdVItems(){
		IndexGrdVItem[] _indexGrdVItems = new IndexGrdVItem[]{
				new IndexGrdVItem(R.drawable.grid_payout, R.string.appGridTextPayoutAdd),
				new IndexGrdVItem(R.drawable.grid_bill, R.string.appGridTextPayoutManage),
				new IndexGrdVItem(R.drawable.grid_report, R.string.appGridTextStatisticsManage),
				new IndexGrdVItem(R.drawable.grid_account_book, R.string.appGridTextAccountBookManage),
				new IndexGrdVItem(R.drawable.grid_category, R.string.appGridTextCategoryManage),
				new IndexGrdVItem(R.drawable.grid_user, R.string.appGridTextUserManage)
		};
		
		return _indexGrdVItems;
	}
	
	private void initIndexGrdVAdapter(){
		 mIndexGrdVAdapter = new IndexGrdVAdapter(this, initIndexGrdVItems());
	}
	
}
