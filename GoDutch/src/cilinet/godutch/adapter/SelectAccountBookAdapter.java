package cilinet.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.adapter.base.BaseAdapter;
import cilinet.godutch.business.AccountBookBusiness;
import cilinet.godutch.model.AccountBookModel;

public class SelectAccountBookAdapter extends BaseAdapter {

	public SelectAccountBookAdapter(Context context) {
		super(context, null);
		
		AccountBookBusiness _accountBookBusiness = new AccountBookBusiness(context);
		List<AccountBookModel> _accountBookModels = _accountBookBusiness.getAvailableAccountBook();
		super.setBoundData(_accountBookModels);
	}

	private class ViewHolder{
		public ImageView imgV_selectAccountBookIcon;
		public TextView txtV_selectAccountBookName;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder _viewHolder = null;
		if(null == convertView){
			convertView = getLayoutInflater().inflate(R.layout.dialog_list_item_accountbook, null);
			
			_viewHolder = new ViewHolder();
			_viewHolder.imgV_selectAccountBookIcon = (ImageView)convertView.findViewById(R.id.imgV_selectAccountBookIcon);
			_viewHolder.txtV_selectAccountBookName = (TextView)convertView.findViewById(R.id.txtV_selectAccountBookName);
			
			convertView.setTag(_viewHolder);
		}else {
			_viewHolder = (ViewHolder)convertView.getTag();
		}
		
		AccountBookModel _accountBookModel = (AccountBookModel)super.getItem(position);
		_viewHolder.imgV_selectAccountBookIcon.setImageResource(R.drawable.account_book_small_icon);
		_viewHolder.txtV_selectAccountBookName.setText(_accountBookModel.name);
		
		return convertView;
	}

}
