package cilinet.godutch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.business.AccountBookBusiness;
import cilinet.godutch.business.PayoutStatisticsBusiness;
import cilinet.godutch.control.SlideMenuControl;
import cilinet.godutch.model.AccountBookModel;

public class PayoutStatisticsListActivity extends FrameActivity implements SlideMenuControl.OnSlideMenuItemClickListener{

	private AccountBookBusiness mAccountBookBusiness;
	private AccountBookModel mSelectedAccountBookModel;
	
	private PayoutStatisticsBusiness mPayoutStatisticsBusiness;
	
	private TextView txtV_statisticsResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_payout_statisticslist);
		super.createSlideMenu(R.array.SlideMenuStatistics);
		
		init();
	}

	private void init() {
		initVariable();
		initView();
		
		setTopBarTitle(getString(R.string.ActivityTitleStatistics, new Object[]{mSelectedAccountBookModel.name}));
	}

	private void initView() {
		txtV_statisticsResult = (TextView)findViewById(R.id.txtV_statisticsResult);
		bindData();
	}

	private void initVariable() {
		mAccountBookBusiness = new AccountBookBusiness(this);
		mSelectedAccountBookModel = mAccountBookBusiness.queryDefaultAccountBook();
		
		mPayoutStatisticsBusiness = new PayoutStatisticsBusiness(this);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				String _result = (String)msg.obj;
				txtV_statisticsResult.setText(_result);
				dismissProgressDialog();
				break;
			default:
				break;
			}
		}
		
	};
	
	private void bindData(){
		super.showProgressDialog(getString(R.string.StatisticsProgressDialogTitle), getString(R.string.StatisticsProgressDialogWaiting));
		
		//这里使用了一个子线程来从后台统计数据，当数据统计完成后，通过Handler通知前台跟新UI
		new BindDataThread().start();
	}
	
	private class BindDataThread extends Thread{
		@Override
		public void run() {
			String _result = mPayoutStatisticsBusiness.queryPayoutUserIdsByAccountBookId(mSelectedAccountBookModel.id);
			Message _message = new Message();
			_message.obj = _result;
			_message.what = 1;
			mHandler.sendMessage(_message);
		}
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		if(slideMenuItem.itemId == 0){//切换账本
			
		}else if(slideMenuItem.itemId == 1){//导出表格
			exportData();
		} 
	}

	private void exportData() {		
		String _result = "";
		try {
			_result = mPayoutStatisticsBusiness.exportPayoutStatistics(mSelectedAccountBookModel.id);
		} catch (Exception e) {
			_result = "导出失败！";
		}
		showToastMessage(_result);
	}
	
}
