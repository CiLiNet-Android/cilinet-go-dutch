package cilinet.godutch.business.base;

import android.content.Context;

/** 业务层基类 **/
public class BaseBusiness {

	private Context mContext;

	protected BaseBusiness(Context context) {
		mContext = context;
	}
	
	protected String getString(int resId){
		return mContext.getString(resId);
	}
	
	protected String getString(int resId,Object... formatArgs){
		return mContext.getString(resId, formatArgs);
	}
	
	protected Context getContext(){
		return mContext;
	}

}
