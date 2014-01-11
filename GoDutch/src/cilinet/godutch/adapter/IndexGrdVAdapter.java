package cilinet.godutch.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.adapter.base.BaseAdapter;

public class IndexGrdVAdapter extends BaseAdapter {
	
	private static List<IndexGrdVItem> mIndexGrdVItems;
	
	/** 初始化图标和名称 **/
	static {
		mIndexGrdVItems = new ArrayList<IndexGrdVItem>();
		mIndexGrdVItems.add(new IndexGrdVItem(R.drawable.grid_payout, R.string.appGridTextPayoutAdd));
		mIndexGrdVItems.add(new IndexGrdVItem(R.drawable.grid_bill, R.string.appGridTextPayoutManage));
		mIndexGrdVItems.add(new IndexGrdVItem(R.drawable.grid_report, R.string.appGridTextStatisticsManage));
		mIndexGrdVItems.add(new IndexGrdVItem(R.drawable.grid_account_book, R.string.appGridTextAccountBookManage));	
		mIndexGrdVItems.add(new IndexGrdVItem(R.drawable.grid_category, R.string.appGridTextCategoryManage));
		mIndexGrdVItems.add(new IndexGrdVItem(R.drawable.grid_user, R.string.appGridTextUserManage));
	}
	
	public IndexGrdVAdapter(Context context) {
		super(context,mIndexGrdVItems);
	}

	/** 用于打包界面数据 **/
	private class ViewHolder {
		public ImageView itemIcon;
		public TextView itemName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IndexGrdVItem _indexGrdVItem = (IndexGrdVItem)super.getBoundData().get(position);
		
		ViewHolder _viewHolder = null;
		
		if(null == convertView){
			convertView = super.getLayoutInflater().inflate(R.layout.index_grdv_item, null);
			
			_viewHolder = new ViewHolder();
			_viewHolder.itemIcon = (ImageView)convertView.findViewById(R.id.imgV_indexItemIcon);
			_viewHolder.itemName = (TextView)convertView.findViewById(R.id.txtV_indexItemName);
			
			convertView.setTag(_viewHolder);
		}else {
			_viewHolder = (ViewHolder)convertView.getTag();
		}
		
		_viewHolder.itemIcon.setImageResource(_indexGrdVItem.iconResId);
		_viewHolder.itemName.setText(_indexGrdVItem.nameResId);
		
		return convertView;
	}
	

}
