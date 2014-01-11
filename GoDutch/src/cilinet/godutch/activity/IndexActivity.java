package cilinet.godutch.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.IndexGrdVAdapter;
import cilinet.godutch.adapter.IndexGrdVItem;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.control.SlideMenuControl.OnSlideMenuItemClickListener;

/** 主框架 **/
public class IndexActivity extends FrameActivity implements OnSlideMenuItemClickListener{
	private static final String TAG = "IndexActivity";
	
	private IndexGrdVAdapter mIndexGrdVAdapter;
	
	private GridView grdV_index;
	
	public Handler mHander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case APPGRDV_ITEM_INDEX.PERSON_MANAGE: 
					IndexActivity.this.startActivity(PersonActivity.class);
					break;
				case APPGRDV_ITEM_INDEX.ACCOUNT_BOOK_MANAGE:
					IndexActivity.this.startActivity(AccountBookActivity.class);
					break;
				case APPGRDV_ITEM_INDEX.CATEGORY_MANAGE:
					IndexActivity.this.startActivity(CategoryActivity.class);
					break;
				case APPGRDV_ITEM_INDEX.PAYOUT_ADD:
					IndexActivity.this.startActivity(AddPayoutActivity.class);
					break;
				case APPGRDV_ITEM_INDEX.PAYOUT_MANAGE:
					IndexActivity.this.startActivity(PayoutActivity.class);
					break;
				case APPGRDV_ITEM_INDEX.PAYOUT_STATISTICS_MANAGE:
					IndexActivity.this.startActivity(PayoutStatisticsListActivity.class);
					break;
				default:
					break;
			}
			
		}
		
	};
	
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
		hideTitleBackButton();
		
		grdV_index = (GridView)super.findViewById(R.id.grdV_index);
		grdV_index.setAdapter(mIndexGrdVAdapter);
		grdV_index.setOnItemClickListener(new OnAppGridItemClickListener());
		
		createSlideMenu(R.array.SlideMenuActivityMain);
	}
	
	private void initIndexGrdVAdapter(){
		 mIndexGrdVAdapter = new IndexGrdVAdapter(this);
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		Toast.makeText(this, slideMenuItem.itemTitle, Toast.LENGTH_SHORT).show();
	}
	
	/** appGridView点击事件 **/
	private class OnAppGridItemClickListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long arg3) {
			IndexGrdVItem _indexGrdVItem = (IndexGrdVItem)adapterView.getAdapter().getItem(position);
			Log.i(TAG, "_indexGrdVItem.itemTitle(): " + IndexActivity.this.getString(_indexGrdVItem.nameResId));
			
			String _indexGrdVItemName = IndexActivity.this.getString(_indexGrdVItem.nameResId);
			if(_indexGrdVItemName.equals(IndexActivity.this.getString(R.string.appGridTextUserManage))){
				IndexActivity.this.mHander.sendEmptyMessage(APPGRDV_ITEM_INDEX.PERSON_MANAGE);
			}else if(_indexGrdVItemName.equals(getString(R.string.appGridTextAccountBookManage))){
				IndexActivity.this.mHander.sendEmptyMessage(APPGRDV_ITEM_INDEX.ACCOUNT_BOOK_MANAGE);
			}else if(_indexGrdVItemName.equals(getString(R.string.appGridTextCategoryManage))){
				IndexActivity.this.mHander.sendEmptyMessage(APPGRDV_ITEM_INDEX.CATEGORY_MANAGE);
			}else if(_indexGrdVItemName.equals(getString(R.string.appGridTextPayoutAdd))){
				IndexActivity.this.mHander.sendEmptyMessage(APPGRDV_ITEM_INDEX.PAYOUT_ADD);
			}else if(_indexGrdVItemName.equals(getString(R.string.appGridTextPayoutManage))){
				IndexActivity.this.mHander.sendEmptyMessage(APPGRDV_ITEM_INDEX.PAYOUT_MANAGE);
			}else if(_indexGrdVItemName.equals(getString(R.string.appGridTextStatisticsManage))){
				IndexActivity.this.mHander.sendEmptyMessage(APPGRDV_ITEM_INDEX.PAYOUT_STATISTICS_MANAGE);
			}
			
		}
		
	}
	
	/** 模块序号 **/
	private static final class APPGRDV_ITEM_INDEX {
		public static final int PERSON_MANAGE = 0;
		public static final int ACCOUNT_BOOK_MANAGE = 1;
		public static final int CATEGORY_MANAGE = 2;
		public static final int PAYOUT_ADD = 3;
		public static final int PAYOUT_MANAGE = 4;
		public static final int PAYOUT_STATISTICS_MANAGE = 5;
	}
	
	
	
}
