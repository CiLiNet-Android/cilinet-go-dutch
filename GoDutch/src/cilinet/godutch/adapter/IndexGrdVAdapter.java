package cilinet.godutch.adapter;

import cilinet.godutch.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexGrdVAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private IndexGrdVItem[] mIndexGrdVItems;
	
	public IndexGrdVAdapter(Context context,IndexGrdVItem[] indexGrdVItems) {
		this.mContext = context;
		this.mIndexGrdVItems = indexGrdVItems;
	}

	@Override
	public int getCount() {
		return mIndexGrdVItems.length;
	}

	@Override
	public Object getItem(int position) {
		return mIndexGrdVItems[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/** 用于打包界面数据 **/
	private class ViewHolder {
		public ImageView itemIcon;
		public TextView itemName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IndexGrdVItem _indexGrdVItem = mIndexGrdVItems[position];
		
		ViewHolder _viewHolder;
		
		if(null == convertView){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_grdv_item, null);
			
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
