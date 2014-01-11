package cilinet.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.adapter.base.BaseAdapter;
import cilinet.godutch.business.PersonBusiness;
import cilinet.godutch.model.PersonModel;


public class PersonAdapter extends BaseAdapter {

	public PersonAdapter(Context context) {
		super(context, null);

		PersonBusiness _personBusiness = new PersonBusiness(super.getContext());
		List<PersonModel> _personModels = _personBusiness.queryAvailablePerson();
		super.setBoundData(_personModels);
	}
	
	private class ViewHolder {
		public ImageView itemPersonIcon;
		public TextView itemPersonName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder _viewHolder = null;
		
		if(convertView == null){
			_viewHolder = new ViewHolder();
			
			convertView = getLayoutInflater().inflate(R.layout.list_person_item, null);
			
			_viewHolder.itemPersonIcon = (ImageView)convertView.findViewById(R.id.imgV_personIcon);
			_viewHolder.itemPersonName = (TextView)convertView.findViewById(R.id.txtV_personName);
			
			convertView.setTag(_viewHolder);
		}else {
			_viewHolder = (ViewHolder)convertView.getTag();
		}
		
		//数据从boundData中得到
		PersonModel _personModel = (PersonModel)getBoundData().get(position);
		_viewHolder.itemPersonIcon.setImageResource(R.drawable.user_big_icon);
		_viewHolder.itemPersonName.setText(_personModel.personName);
		
		return convertView;
	}

	

}
