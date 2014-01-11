package cilinet.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.AccountBookAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.business.AccountBookBusiness;
import cilinet.godutch.control.SlideMenuControl;
import cilinet.godutch.model.AccountBookModel;
import cilinet.godutch.utility.RegexTools;

/** 账本管理 **/
public class AccountBookActivity extends FrameActivity implements SlideMenuControl.OnSlideMenuItemClickListener{

	private ListView listV_accountBookList;
	
	private AccountBookBusiness mAccountBookBusiness;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_accoutbook);
		super.createSlideMenu(R.array.SlideMenuAccountBook);
		
		init();
	}
	
	private void init(){
		setTopBarTitle();
		initView();
		initBusiness();
	}
	
	private void initBusiness() {
		mAccountBookBusiness = new AccountBookBusiness(this);
	}

	private void setTopBarTitle(){
		super.setTopBarTitle(getString(R.string.appGridTextAccountBookManage));
	}
	
	private void initView(){
		listV_accountBookList = (ListView)super.findViewById(R.id.listV_accountBookList);
		bindAdapter();//绑定数据源
		super.registerForContextMenu(listV_accountBookList);//可以给任何View注册ContextMenu(长按弹出菜单窗口)
		
	}
	
	/** 在选中上下文菜单时，后面还要使用，所以保存为成员变量 **/
	private AccountBookModel mSelectedAccountBookModel;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		AdapterView.AdapterContextMenuInfo _adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
		
		ListAdapter _listAdapter = listV_accountBookList.getAdapter();//通过listv可以得到其绑定的Adapter
		mSelectedAccountBookModel = (AccountBookModel)_listAdapter.getItem(_adapterContextMenuInfo.position);
		
		menu.setHeaderIcon(R.drawable.account_book_small_icon);
		menu.setHeaderTitle(mSelectedAccountBookModel.name);
		
		super.createManageContextMenu(menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1: 
			showAccountBookEditDialog(mSelectedAccountBookModel);
			break;
		default: 
			break;
		}
		
		return super.onContextItemSelected(item);	
	}

	

	private void bindAdapter(){
		AccountBookAdapter _accountBookAdapter = new AccountBookAdapter(this);
		listV_accountBookList.setAdapter(_accountBookAdapter);
	}

	/** 当菜单选项被点击后调用 **/
	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		super.slideSlideMenu();
		if(slideMenuItem.itemId == 0){
			showAccountBookAddDialog();
		}
	}
	
	/** 添加账本 **/
	private void showAccountBookAddDialog(){
		View _dialogView = getLayoutInflater().inflate(R.layout.dialog_accountbook_addoredit, null);
		CheckBox chkBox_isDefault = (CheckBox)_dialogView.findViewById(R.id.chkBox_isDefault);
		chkBox_isDefault.setChecked(true);
		
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		_alertDialogBuilder.setIcon(R.drawable.account_book_big_icon)
						   .setTitle(getString(R.string.DialogTitleAccountBook, new Object[]{getString(R.string.TitleAdd)}))
						   .setView(_dialogView)
						   .setNeutralButton(R.string.ButtonTextSave, new OnAddOrEditAccountBookDialogListener(null, _dialogView, true))
						   .setNegativeButton(R.string.ButtonTextCancel, new OnAddOrEditAccountBookDialogListener(null, _dialogView, false))
						   .show();
	}
	
	/** 更新账本 **/
	private void showAccountBookEditDialog(AccountBookModel accountBookModel){
		View _dialogView = getLayoutInflater().inflate(R.layout.dialog_accountbook_addoredit, null);
		
		CheckBox chkBox_isDefault = (CheckBox)_dialogView.findViewById(R.id.chkBox_isDefault);
		chkBox_isDefault.setChecked((accountBookModel.isDefault == 1? true : false));
		
		EditText edTxt_accountBookName = (EditText)_dialogView.findViewById(R.id.edTxt_accountBookName);
		edTxt_accountBookName.setText(accountBookModel.name);
		
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		_alertDialogBuilder.setIcon(R.drawable.account_book_big_icon)
						   .setTitle(getString(R.string.DialogTitleAccountBook, new Object[]{getString(R.string.TitleEdit)}))
						   .setView(_dialogView)
						   .setNeutralButton(R.string.ButtonTextSave, new OnAddOrEditAccountBookDialogListener(accountBookModel, edTxt_accountBookName,chkBox_isDefault, true))
						   .setNegativeButton(R.string.ButtonTextCancel, new OnAddOrEditAccountBookDialogListener(accountBookModel, edTxt_accountBookName,chkBox_isDefault,false))
						   .show();
	}
	
	private class OnAddOrEditAccountBookDialogListener implements DialogInterface.OnClickListener {
		
		private EditText edTxt_accountBookName;
		private AccountBookModel mAccountBookModel;
		private CheckBox chkBox_isDefault;
		private boolean mIsClickedSaveBtn;
		
		public OnAddOrEditAccountBookDialogListener(AccountBookModel accountBookModel,View dialogView,boolean isClickedSaveBtn){
			mAccountBookModel = accountBookModel;
			this.edTxt_accountBookName = (EditText)dialogView.findViewById(R.id.edTxt_accountBookName);
			this.chkBox_isDefault = (CheckBox)dialogView.findViewById(R.id.chkBox_isDefault);
			mIsClickedSaveBtn = isClickedSaveBtn;
		}
		
		public OnAddOrEditAccountBookDialogListener(AccountBookModel accountBookModel,EditText edTxt_accountBookName,CheckBox chkBox_isDefault,boolean isClickedSaveBtn){
			mAccountBookModel = accountBookModel;
			this.edTxt_accountBookName = edTxt_accountBookName;
			this.chkBox_isDefault = chkBox_isDefault;
			mIsClickedSaveBtn = isClickedSaveBtn;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(mIsClickedSaveBtn){
				String _accountBookName = edTxt_accountBookName.getText().toString();
				int _isDefault = chkBox_isDefault.isChecked()? 1 : 0;
				
				//如果用户输入的账本名称是否符合数字、字母或中文
				if(!RegexTools.isChineseEnglishNum(_accountBookName)){
					AccountBookActivity.this.showToastMessage(getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{edTxt_accountBookName.getHint()}));
					AccountBookActivity.this.setAlertDialogClosable(dialog, false);
					return;
				}
				
				//如果用户输入的名称有效，则检查数据库中是否有同名的账本
				if(mAccountBookModel == null){
					mAccountBookModel = new AccountBookModel(_accountBookName,_isDefault);
				}else {
					mAccountBookModel.name = _accountBookName;
					mAccountBookModel.isDefault = _isDefault;
				}
				
				//判断账本名称是否存在
				if(mAccountBookBusiness.checkAccountBookNameIfExists(mAccountBookModel)){
					AccountBookActivity.this.showToastMessage(getString(R.string.CheckDataTextUserExist,new Object[]{mAccountBookModel.name}));
					AccountBookActivity.this.setAlertDialogClosable(dialog, false);
					return;
				}
				
				boolean _result = false;
				if(mAccountBookModel.id != 0){
					_result = mAccountBookBusiness.updateAccountBook(mAccountBookModel);
				}else {
					_result = mAccountBookBusiness.insertAccountBook(mAccountBookModel);
				}
				
				if(_result){
					AccountBookActivity.this.bindAdapter();
					AccountBookActivity.this.showToastMessage(getString(R.string.TipsAddSucceed));
				}else {
					AccountBookActivity.this.showToastMessage(getString(R.string.TipsAddFail));
				}
			}else {
				AccountBookActivity.this.setAlertDialogClosable(dialog, true);
			}
		}
		
	}

}
