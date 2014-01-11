package cilinet.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.adapter.base.BaseAdapter;
import cilinet.godutch.business.PayoutBusiness;
import cilinet.godutch.business.PersonBusiness;
import cilinet.godutch.model.PayoutModel;
import cilinet.godutch.utility.DateTools;

public class PayoutAdapter extends BaseAdapter {
	
	private PersonBusiness mPersonBusiness;
	
	private PayoutBusiness mPayoutBusiness;

	public PayoutAdapter(Context context,int accountBookModelId) {
		super(context, null);
		mPersonBusiness = new PersonBusiness(context);
		
		mPayoutBusiness = new PayoutBusiness(context);
		List<PayoutModel> _payoutModels = mPayoutBusiness.queryAvailablePayoutByAccountBookId(accountBookModelId);
		setBoundData(_payoutModels);
	}
	
	private class ViewHolder{
		public ImageView imgV_payoutIcon;
		public TextView txtV_payoutName;
		public TextView txtV_payoutTotal;
		public TextView txtV_payoutUserAndPayoutType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder _viewHolder = null;
		if(null == convertView){
			convertView = getLayoutInflater().inflate(R.layout.list_payout_item, null);
			
			_viewHolder = new ViewHolder();
			_viewHolder.imgV_payoutIcon = (ImageView)convertView.findViewById(R.id.imgV_payoutIcon);
			_viewHolder.txtV_payoutName = (TextView)convertView.findViewById(R.id.txtV_payoutName);
			_viewHolder.txtV_payoutTotal = (TextView)convertView.findViewById(R.id.txtV_payoutTotal);
			_viewHolder.txtV_payoutUserAndPayoutType = (TextView)convertView.findViewById(R.id.txtV_payoutUserAndPayoutType);
			
			convertView.setTag(_viewHolder);
		}else {
			_viewHolder = (ViewHolder)convertView.getTag();
		}
		
		PayoutModel _currentPayoutModel = (PayoutModel)getItem(position);
		
		//设置Header
		String _currentPayoutModelPayoutDate = DateTools.getFormatDateTime(_currentPayoutModel.payoutDate, "yyyy-MM-dd");
		if(position > 0){//如果不是第一条记录
			PayoutModel _lastPayoutModel = (PayoutModel)getItem(position - 1);
			String _lastPayoutModelPayoutDate = DateTools.getFormatDateTime(_lastPayoutModel.payoutDate, "yyyy-MM-dd");
			if(!_currentPayoutModelPayoutDate.equals(_lastPayoutModelPayoutDate)){
				RelativeLayout _convertViewLayout = (RelativeLayout)convertView;
				
				//移动XML配置中的View
				RelativeLayout _payoutListBody = (RelativeLayout)_convertViewLayout.findViewById(R.id.relatLyot_payoutListBody);
				RelativeLayout.LayoutParams _layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				_layoutParams.addRule(RelativeLayout.BELOW, R.id.relatLyot_payoutListHeader);
				_payoutListBody.setLayoutParams(_layoutParams);
				
				_convertViewLayout.addView(createListGroupHeader(_currentPayoutModel));
				
			}
		}else {
			RelativeLayout _convertViewLayout = (RelativeLayout)convertView;
			
			//移动XML配置中的View
			RelativeLayout _payoutListBody = (RelativeLayout)_convertViewLayout.findViewById(R.id.relatLyot_payoutListBody);
			RelativeLayout.LayoutParams _layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			_layoutParams.addRule(RelativeLayout.BELOW, R.id.relatLyot_payoutListHeader);
			_payoutListBody.setLayoutParams(_layoutParams);
			
			_convertViewLayout.addView(createListGroupHeader(_currentPayoutModel));
		}
		
		_viewHolder.imgV_payoutIcon.setBackgroundResource(R.drawable.payout_small_icon);
		_viewHolder.txtV_payoutName.setText(_currentPayoutModel.categoryName);
		_viewHolder.txtV_payoutTotal.setText(_currentPayoutModel.amount.toString());
		
		String _currentPayoutPersonNames = mPersonBusiness.queryPersonNamesByPersonIds(_currentPayoutModel.payoutUserID);
		_viewHolder.txtV_payoutUserAndPayoutType.setText(_currentPayoutPersonNames + "    " + _currentPayoutModel.payoutType);
		
		return convertView;
	}
	
	private RelativeLayout createListGroupHeader(PayoutModel payoutModel){
		RelativeLayout _view = (RelativeLayout)getLayoutInflater().inflate(R.layout.list_payout_item_header, null);
		
		TextView txtV_itemHeader_payoutDate = (TextView)_view.findViewById(R.id.txtV_itemHeader_payoutDate);
		txtV_itemHeader_payoutDate.setText(DateTools.getFormatDateTime(payoutModel.payoutDate, "yyyy-MM-dd"));
		
		TextView txtV_itemHeader_payoutTotal = (TextView)_view.findViewById(R.id.txtV_itemHeader_payoutTotal);
		//String[0]总笔数，String[1]总金额
		String[] _payoutStatics = mPayoutBusiness.queryPayoutStatisticsByDateAndAccountBookId(payoutModel.payoutDate, payoutModel.accountBookID);
		txtV_itemHeader_payoutTotal.setText(getContext().getString(R.string.TextViewTextPayoutTotal, new Object[]{_payoutStatics[0],_payoutStatics[1]}));
		
		return _view;
	}

}
