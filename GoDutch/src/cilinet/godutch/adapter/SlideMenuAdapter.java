package cilinet.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.adapter.base.MyBaseAdapter;

public class SlideMenuAdapter extends MyBaseAdapter {

	public SlideMenuAdapter(Context context, List<SlideMenuItem> boundDatas) {
		super(context, boundDatas);
	}
	
	private class ViewHolder {
		public TextView txtV_slideMenuItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		SlideMenuItem _slideMenuItem = (SlideMenuItem)getBoundDatas().get(position);
		
		ViewHolder _viewHolder;
		if(null == convertView){
			convertView = super.getLayoutInflater().inflate(R.layout.slide_menu_item, null);
		
			_viewHolder = new ViewHolder();
			convertView.setTag(_viewHolder);
		}
		
		_viewHolder = (ViewHolder)convertView.getTag();
		
		_viewHolder.txtV_slideMenuItem = (TextView)convertView.findViewById(R.id.txtV_slideMenuItem);
		
		_viewHolder.txtV_slideMenuItem.setText(_slideMenuItem.itemTitle);
		
		return convertView;
	}

}
