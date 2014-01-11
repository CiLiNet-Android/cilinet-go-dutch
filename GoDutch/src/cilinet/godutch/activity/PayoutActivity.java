package cilinet.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.PayoutAdapter;
import cilinet.godutch.adapter.SelectAccountBookAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.business.AccountBookBusiness;
import cilinet.godutch.business.PayoutBusiness;
import cilinet.godutch.control.SlideMenuControl;
import cilinet.godutch.model.AccountBookModel;
import cilinet.godutch.model.PayoutModel;

/** 查询消费 **/
public class PayoutActivity extends FrameActivity implements
		SlideMenuControl.OnSlideMenuItemClickListener {

	private ListView listV_payoutList;

	// private AccountBookBusiness mAccountBookBusiness;

	private AccountBookModel mAccountBookModel;
	
	private PayoutBusiness mPayoutBusiness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_payout);
		super.createSlideMenu(R.array.SlideMenuPayout);

		init();
	}

	private void init() {
		initVariable();
		initView();
	}

	private void initVariable() {
		AccountBookBusiness _accountBookBusiness = new AccountBookBusiness(this);
		mAccountBookModel = _accountBookBusiness.queryDefaultAccountBook();
		
		mPayoutBusiness = new PayoutBusiness(this);
	}

	private void initView() {
		listV_payoutList = (ListView) findViewById(R.id.listV_payoutList);
		bindData();
		super.registerForContextMenu(listV_payoutList);

	}

	private void bindData() {
		PayoutAdapter _payoutAdapter = new PayoutAdapter(this,
				mAccountBookModel.id);
		listV_payoutList.setAdapter(_payoutAdapter);

		setTopBarTitle(getString(R.string.ActivityTitlePayout, new Object[] {
				mAccountBookModel.name, listV_payoutList.getCount() }));
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		super.slideSlideMenu();
		if (slideMenuItem.itemId == 0) {
			showSelectAccountBookDialog();
		}
	}

	private void showSelectAccountBookDialog() {
		AlertDialog.Builder _builder = new AlertDialog.Builder(this);
		_builder.setTitle(R.string.ButtonTextSelectAccountBook);
		_builder.setNegativeButton(R.string.ButtonTextBack, null);

		View _view = getLayoutInflater().inflate(R.layout.dialog_list, null);
		ListView listV_listItemSelector = (ListView) _view
				.findViewById(R.id.listV_listItemSelector);
		listV_listItemSelector.setAdapter(new SelectAccountBookAdapter(this));

		_builder.setView(_view);

		AlertDialog _alertDialog = _builder.create();
		_alertDialog.show();

		listV_listItemSelector
				.setOnItemClickListener(new OnAccountBookItemClickListener(
						_alertDialog));

	}

	private class OnAccountBookItemClickListener implements
			AdapterView.OnItemClickListener {

		private AlertDialog mAlertDialog;

		public OnAccountBookItemClickListener(AlertDialog alertDialog) {
			mAlertDialog = alertDialog;
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View arg1,
				int position, long arg3) {
			mAccountBookModel = (AccountBookModel) adapterView.getAdapter()
					.getItem(position);

			bindData();

			mAlertDialog.dismiss();
		}

	}
	
	private PayoutModel mSelectedPayoutModel;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// 获取当前被选择的菜单项的信息
		AdapterContextMenuInfo _adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		ListAdapter _listAdapter = listV_payoutList.getAdapter();
		
		mSelectedPayoutModel = (PayoutModel) _listAdapter.getItem(_adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.payout_small_icon);
		menu.setHeaderTitle(mSelectedPayoutModel.categoryName);
		
		super.createManageContextMenu(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent _intent = new Intent(this, EditPayoutActivity.class);
			_intent.putExtra("SelectedPayoutModel", mSelectedPayoutModel);
			this.startActivityForResult(_intent, 1);
			break;
		case 2:
			showDeletePayoutDialog(mSelectedPayoutModel);
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	private void showDeletePayoutDialog(final PayoutModel payoutModel) {
		String _dialogMessage = getString(R.string.DialogMessagePayoutDelete, new Object[]{payoutModel.categoryName});
		showAlertDialog(getString(R.string.DialogTitleDelete), _dialogMessage, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean _isSuccessed = mPayoutBusiness.deletePayout(payoutModel);
				if(_isSuccessed){
					showToastMessage("删除成功");
					bindData();
				}else {
					showToastMessage("删除失败");
				}
				dialog.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bindData();
	}
	
	

}
