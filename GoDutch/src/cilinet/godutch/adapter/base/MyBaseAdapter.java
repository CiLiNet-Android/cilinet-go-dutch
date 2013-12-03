package cilinet.godutch.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/** 封装常用的Adapter **/
public abstract class MyBaseAdapter extends BaseAdapter {

	private List mBoundData;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public MyBaseAdapter(Context context,List boundDatas){
		mBoundData = boundDatas;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return mBoundData.size();
	}

	@Override
	public Object getItem(int position) {
		return mBoundData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

	public List getBoundData() {
		return mBoundData;
	}
	
	public void setBoundData(List boundData){
		mBoundData = boundData;
	}
	
}
