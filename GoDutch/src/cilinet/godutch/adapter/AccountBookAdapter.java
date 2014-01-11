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

public class AccountBookAdapter extends BaseAdapter {

	public AccountBookAdapter(Context context) {
		//这里传入null，原因是构造方法必须第一行调用
		super(context, null);
		
		//在adapter创建的时候，绑定数据
		AccountBookBusiness _accountBookBusiness = new AccountBookBusiness(context);
		List<AccountBookModel> _accountBooks = _accountBookBusiness.getAvailableAccountBook();
		super.setBoundData(_accountBooks);
	}
	
	private class ViewHolder {
		public ImageView imgV_accountBookIcon;
		public TextView txtV_accountBookName;
		public TextView txtV_accountBookConsumeTimes;
		public TextView txtV_accountBookConsumeMoney;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder _viewHolder = null;
		if(null == convertView){
			_viewHolder = new ViewHolder();
			
			convertView = (View)getLayoutInflater().inflate(R.layout.list_accountbook_item, null);
			
			_viewHolder.imgV_accountBookIcon = (ImageView)convertView.findViewById(R.id.imgV_accountBookIcon);
			_viewHolder.txtV_accountBookName = (TextView)convertView.findViewById(R.id.txtV_accountBookName);
			_viewHolder.txtV_accountBookConsumeTimes = (TextView)convertView.findViewById(R.id.txtV_accountBookConsumeTimes);
			_viewHolder.txtV_accountBookConsumeMoney = (TextView)convertView.findViewById(R.id.txtV_accountBookConsumeMoney);
			
			convertView.setTag(_viewHolder);
		}else {
			_viewHolder = (ViewHolder)convertView.getTag();
		}
		
		//获取数据
		AccountBookModel _accountBook = (AccountBookModel)super.getItem(position);
		
		//设置图标
		if(1 == _accountBook.isDefault){
			_viewHolder.imgV_accountBookIcon.setImageResource(R.drawable.account_book_default);
		}else {
			_viewHolder.imgV_accountBookIcon.setImageResource(R.drawable.account_book_big_icon);
		}
		
		//设置名称
		_viewHolder.txtV_accountBookName.setText(_accountBook.name);
		//设置消费笔数
		_viewHolder.txtV_accountBookConsumeTimes.setText("共0笔");
		//设置合计消费金额
		_viewHolder.txtV_accountBookConsumeMoney.setText("合计消费0元");
		
		return convertView;
	}

}
