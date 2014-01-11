package cilinet.godutch.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;

/** 封装常用的Adapter **/
public abstract class BaseAdapter extends android.widget.BaseAdapter {

	//问好为泛型中的类型限制符
	private List<?> mBoundData;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public BaseAdapter(Context context,List<?> boundDatas){
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

	public List<?> getBoundData() {
		return mBoundData;
	}
	
	public void setBoundData(List<?> boundData){
		mBoundData = boundData;
	}
	
	protected Context getContext(){
		return mContext;
	}
	
}
