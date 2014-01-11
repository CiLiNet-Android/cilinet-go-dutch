package cilinet.godutch.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.CategoryAdapter;
import cilinet.godutch.adapter.PersonAdapter;
import cilinet.godutch.adapter.SelectAccountBookAdapter;
import cilinet.godutch.business.AccountBookBusiness;
import cilinet.godutch.business.PayoutBusiness;
import cilinet.godutch.control.NumberDialog;
import cilinet.godutch.model.AccountBookModel;
import cilinet.godutch.model.CategoryModel;
import cilinet.godutch.model.PayoutModel;
import cilinet.godutch.model.PersonModel;
import cilinet.godutch.utility.DateTools;
import cilinet.godutch.utility.RegexTools;

/** 记录消费 
 * 以界面为线索
 * **/
public class AddPayoutActivity extends FrameActivity implements View.OnClickListener,NumberDialog.OnNumberDialogListener{
	
	private static final String TAG = "AddPayoutActivity";
	
	/** 选择账本 **/
	private Button btn_selectAccountBook;
	private EditText edtTxt_selectAccountBook;
	private int mSelectedAccountBookModelId;
	private AccountBookBusiness mAccountBookBusiness;
	
	/** 输入金额 **/
	private Button btn_selectAmount;
	private EditText edtTxt_selectAmount;
	
	/** 选择类别 **/
	private Button btn_selectCategory;
	private AutoCompleteTextView atoComptEdtTxt_categoryName; 
	private int mSelectedCategoryModelId;
	
	/** 选择日期 **/
	private Button btn_selectPayoutDate;
	private EditText edtTxt_selectPayoutDate;
	
	/** 计算方式 **/
	private Button btn_selectPayoutType;
	private EditText edtTxt_PayoutType;
	
	/** 选择消费人 **/
	private Button btn_selectUser;
	private EditText edtTxt_payoutUser;
	private String mSelectedPayoutUserIds;//多个id,用逗号分隔
	
	private List<PersonModel> mSelectedPayoutUserModels;
	private List<View> mSelectedPayoutUserItemViews;
	
	/** 备注 **/
	private EditText edtTxt_comment;
	
	/** 保存和取消 **/
	private Button btn_savePayout;
	private Button btn_cancelSavePayout;
	
	private PayoutBusiness mPayoutBusiness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_payout_addoredit);
		super.removeIncludeBottom();
		
		init();
	}

	private void init() {
		setTopBarTitle(getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[]{getString(R.string.TitleAdd)}));
		
		initVariable();
		initView();
	}

	private void initVariable() {
		mAccountBookBusiness = new AccountBookBusiness(this);
		mPayoutBusiness = new PayoutBusiness(this);
	}

	private void initView() {
		//选择账本
		edtTxt_selectAccountBook = (EditText)findViewById(R.id.edtTxt_selectAccountBook);
		AccountBookModel _defaultAccountBookModel = mAccountBookBusiness.queryDefaultAccountBook();
		edtTxt_selectAccountBook.setText(_defaultAccountBookModel.name);
		
		btn_selectAccountBook = (Button)findViewById(R.id.btn_selectAccountBook);
		btn_selectAccountBook.setOnClickListener(this);
		
		mSelectedAccountBookModelId = _defaultAccountBookModel.id;
		
		
		//输入金额
		edtTxt_selectAmount = (EditText)findViewById(R.id.edtTxt_selectAmount);
		
		btn_selectAmount = (Button)findViewById(R.id.btn_selectAmount);
		btn_selectAmount.setOnClickListener(this);
		
		
		//选择类别
		atoComptEdtTxt_categoryName = (AutoCompleteTextView)findViewById(R.id.atoComptEdtTxt_categoryName);
		//atoComptEdtTxt_categoryName.setAdapter(null);
		
		btn_selectCategory = (Button)findViewById(R.id.btn_selectCategory);
		btn_selectCategory.setOnClickListener(this);
		
		
		//选择日期
		edtTxt_selectPayoutDate = (EditText)findViewById(R.id.edtTxt_selectPayoutDate);
		edtTxt_selectPayoutDate.setText(DateTools.getCurrentDate());
		
		btn_selectPayoutDate = (Button)findViewById(R.id.btn_selectPayoutDate);
		btn_selectPayoutDate.setOnClickListener(this);
		
		
		//计算方式
		edtTxt_PayoutType = (EditText)findViewById(R.id.edtTxt_PayoutType);
		String _defaultPayoutType = getResources().getStringArray(R.array.PayoutType)[0];
		edtTxt_PayoutType.setText(_defaultPayoutType);
		
		btn_selectPayoutType = (Button)findViewById(R.id.btn_selectPayoutType);
		btn_selectPayoutType.setOnClickListener(this);
		
		
		//选择消费人
		edtTxt_payoutUser = (EditText)findViewById(R.id.edtTxt_payoutUser);
		
		btn_selectUser = (Button)findViewById(R.id.btn_selectUser);
		btn_selectUser.setOnClickListener(this);
		
		
		//备注
		edtTxt_comment = (EditText)findViewById(R.id.edtTxt_comment);
		
		
		//保存和取消
		btn_savePayout = (Button)findViewById(R.id.btn_savePayout);
		btn_savePayout.setOnClickListener(this);
		
		btn_cancelSavePayout = (Button)findViewById(R.id.btn_cancelSavePayout);
		btn_cancelSavePayout.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		int _viewId = view.getId();
		switch(_viewId){
		case R.id.btn_selectAccountBook: 
			showSelectAccountBookDialog();
			break;
		case R.id.btn_selectAmount: 
			new NumberDialog(this).show();
			break;
		case R.id.btn_selectCategory:
			showSelectCategoryDialog();
			break;
		case R.id.btn_selectPayoutDate:
			showSelectPayoutDateDialog();
			break;
		case R.id.btn_selectPayoutType:
			showSelectPayoutTypeDialog();
			break;
		case R.id.btn_selectUser:
			showSelectPayoutUserDialog();
			break;
		case R.id.btn_savePayout:
			addPayout();
			break;
		case R.id.btn_cancelSavePayout: 
			finish();
			break;
		default:
			break;
		}
	}

	/** 添加记录到后台 **/
	private void addPayout() {
		if(isFormDataAvailable()){
			PayoutModel _payoutModel = new PayoutModel();
			_payoutModel.accountBookID = mSelectedAccountBookModelId;
			_payoutModel.amount = (new BigDecimal(edtTxt_selectAmount.getText().toString().trim()));
			_payoutModel.categoryID = mSelectedCategoryModelId;
			_payoutModel.payoutDate = DateTools.getDate(edtTxt_selectPayoutDate.getText().toString().trim(), "yyyy-MM-dd");
			_payoutModel.payoutType = edtTxt_PayoutType.getText().toString().trim();
			_payoutModel.payoutUserID = mSelectedPayoutUserIds;
			_payoutModel.comment = edtTxt_comment.getText().toString().trim();
			
			boolean _isSuccessed = mPayoutBusiness.insertPayout(_payoutModel);
			if(_isSuccessed){
				showToastMessage(getString(R.string.TipsAddSucceed));
				finish();
			}else {
				showToastMessage(getString(R.string.TipsAddFail));
			}
		}
	}

	/** 校验表单 **/
	private boolean isFormDataAvailable() {
		//金额
		if(!RegexTools.isMoney(edtTxt_selectAmount.getText().toString().trim())){
			showToastMessage(getString(R.string.CheckDataTextMoney));
			
			return false;
		}
		
		//类别id
		if(!RegexTools.isNull(mSelectedCategoryModelId)){
			btn_selectCategory.setFocusable(true);
			btn_selectCategory.setFocusable(true);
			btn_selectCategory.setFocusableInTouchMode(true);
			btn_selectCategory.requestFocus();
			
			showToastMessage(getString(R.string.CheckDataTextCategoryIsNull));
			
			return false;
		}
		
		//用户选择
		String _selectedPayoutType = edtTxt_PayoutType.getText().toString();
		String[] _payoutTypes = getResources().getStringArray(R.array.PayoutType);
		if(null == mSelectedPayoutUserIds || "".equals(mSelectedPayoutUserIds)){
			btn_selectUser.setFocusable(true);
			btn_selectUser.setFocusableInTouchMode(true);
			btn_selectUser.requestFocus();
			
			if(_selectedPayoutType.equals(_payoutTypes[2])){
				showToastMessage(getString(R.string.CheckDataTextPayoutUser2));
			}else {
				showToastMessage(getString(R.string.CheckDataTextPayoutUser));
			}
			
			return false;
		}else {
			if(_selectedPayoutType.equals(_payoutTypes[0]) || _selectedPayoutType.equals(_payoutTypes[1])){
				if(mSelectedPayoutUserIds.split(",").length <=1 ){
					showToastMessage(getString(R.string.CheckDataTextPayoutUser));
					
					return false;
				}
			}
		}
		
		return true;
	}

	private void showSelectPayoutUserDialog() {
		AlertDialog.Builder _builder = new AlertDialog.Builder(this);
		_builder.setTitle(R.string.ButtonTextSelectUser);
		_builder.setIcon(R.drawable.user_small_icon);
		_builder.setNegativeButton(R.string.ButtonTextOk, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mSelectedPayoutUserModels.size() > 0){
					StringBuilder _selectedPersonNames = new StringBuilder();
					StringBuilder _selectedPersonIds = new StringBuilder();
					
					for(PersonModel _personModel : mSelectedPayoutUserModels){
						_selectedPersonNames.append(_personModel.personName).append(",");
						
						_selectedPersonIds.append(_personModel.personId).append(",");
					}
					_selectedPersonNames.deleteCharAt(_selectedPersonNames.lastIndexOf(","));
					_selectedPersonIds.deleteCharAt(_selectedPersonIds.lastIndexOf(","));
					
					edtTxt_payoutUser.setText(_selectedPersonNames.toString());
					mSelectedPayoutUserIds = _selectedPersonIds.toString();
					
					mSelectedPayoutUserItemViews = null;
					mSelectedPayoutUserModels = null;
				}
				
			}
		});
		
		View _view = getLayoutInflater().inflate(R.layout.activity_person, null);
		ListView listV_personList = (ListView)_view.findViewById(R.id.listV_personList);
		listV_personList.setAdapter(new PersonAdapter(this));
		listV_personList.setBackgroundColor(getResources().getColor(R.color.blue));
		
		_builder.setView(_view);
		
		AlertDialog _alertDialog = _builder.create();
		_alertDialog.show();
		
		String _payoutType = edtTxt_PayoutType.getText().toString();
		listV_personList.setOnItemClickListener(new OnPayoutUserItemClickListener(_alertDialog, _payoutType));
		
		
		//初始化用户的选择记录
		if(null == mSelectedPayoutUserModels){
			mSelectedPayoutUserModels = new ArrayList<PersonModel>();
		}
		if(null == mSelectedPayoutUserItemViews) {
			mSelectedPayoutUserItemViews = new ArrayList<View>();//包含了选中的Person View
		}
	}
	
	private class OnPayoutUserItemClickListener implements OnItemClickListener {
		
		private AlertDialog mAlertDialog;
		private String mPayoutType;
		
		public OnPayoutUserItemClickListener(AlertDialog alertDialog,String payoutType) {
			mAlertDialog = alertDialog;
			mPayoutType = payoutType;
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long arg3) {
			 
			PersonModel _personModel = (PersonModel)adapterView.getAdapter().getItem(position);
			
			String[] _payoutTypes = getResources().getStringArray(R.array.PayoutType);
			if(null == mPayoutType || "".equals(mPayoutType) || mPayoutType.equals(_payoutTypes[2])){
				edtTxt_PayoutType.setText(_payoutTypes[2]);//当用户没选择计算方式
				mSelectedPayoutUserModels.add(_personModel);
				edtTxt_payoutUser.setText(_personModel.personName);
				mAlertDialog.dismiss();
			}else { //借贷和均分方式
	
				if(mSelectedPayoutUserItemViews.contains(view)){//如果之前已经选中该View
					mSelectedPayoutUserItemViews.remove(view);
					mSelectedPayoutUserModels.remove(_personModel);
					
					view.setBackgroundColor(getResources().getColor(R.color.blue));
				}else {
					mSelectedPayoutUserItemViews.add(view);
					mSelectedPayoutUserModels.add(_personModel);
					
					view.setBackgroundColor(getResources().getColor(R.color.red));
				}
				
			}
		}
		
	}

	private void showSelectPayoutTypeDialog() {
		AlertDialog.Builder _builder = new AlertDialog.Builder(this);
		_builder.setTitle(R.string.ButtonTextSelectPayoutType);
		_builder.setNegativeButton(R.string.ButtonTextBack, null);
		
		View _view = getLayoutInflater().inflate(R.layout.payout_type_select_list, null);
		ListView listV_selectPayoutType = (ListView)_view.findViewById(R.id.listV_selectPayoutType);
		
		_builder.setView(_view);
		
		AlertDialog _alertDialog = _builder.create();
		_alertDialog.show();
		
		listV_selectPayoutType.setOnItemClickListener(new OnPayoutTypeItemClickListener(_alertDialog));
	}
	
	private class OnPayoutTypeItemClickListener implements OnItemClickListener {

		private AlertDialog mAlertDialog;
		
		public OnPayoutTypeItemClickListener(AlertDialog alertDialog){
			mAlertDialog = alertDialog;
		}
		
		@Override
		public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
				long arg3) {
			String _selectedPayoutType = (String)adapterView.getAdapter().getItem(position);
			
			edtTxt_PayoutType.setText(_selectedPayoutType);
			
			//转换计算方式后，先清空已选用户
			edtTxt_payoutUser.setText("");
			mSelectedPayoutUserIds = "";
			
			mAlertDialog.dismiss();
		}
		
	}

	private void showSelectPayoutDateDialog() {
		Calendar _calendar = Calendar.getInstance();
		new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar _calendar = Calendar.getInstance();
				_calendar.set(Calendar.YEAR, year);
				_calendar.set(Calendar.MONTH, monthOfYear);
				_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				
				String _selectedPayoutDate = DateTools.getFormatDateTime(_calendar.getTime(), "yyyy-MM-dd");
				edtTxt_selectPayoutDate.setText(_selectedPayoutDate);
			}
		},_calendar.get(Calendar.YEAR),_calendar.get(Calendar.MONTH),_calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void showSelectAccountBookDialog(){
		AlertDialog.Builder _builder = new AlertDialog.Builder(this);
		_builder.setTitle(R.string.ButtonTextSelectAccountBook);
		_builder.setNegativeButton(R.string.ButtonTextBack, null);
		
		View _view = getLayoutInflater().inflate(R.layout.dialog_list, null);
		ListView listV_listItemSelector = (ListView)_view.findViewById(R.id.listV_listItemSelector);
		listV_listItemSelector.setAdapter(new SelectAccountBookAdapter(this));
		
		_builder.setView(_view);
		
		AlertDialog _alertDialog = _builder.create();
		_alertDialog.show();
		
		listV_listItemSelector.setOnItemClickListener(new OnAccountBookItemClickListener(_alertDialog));
	}
	
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
		
		private AlertDialog mAlertDialog;
		
		public OnAccountBookItemClickListener(AlertDialog alertDialog){
			mAlertDialog = alertDialog;
		}
		
		@Override
		public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
				long arg3) {
			AccountBookModel _selectedAccountBookModel = (AccountBookModel)adapterView.getAdapter().getItem(position);
			edtTxt_selectAccountBook.setText(_selectedAccountBookModel.name);
			mSelectedAccountBookModelId = _selectedAccountBookModel.id;
			mAlertDialog.dismiss();
		}
		
	}
	
	private void showSelectCategoryDialog() {
		AlertDialog.Builder _builder = new AlertDialog.Builder(this);
		_builder.setIcon(R.drawable.category_small_icon);
		_builder.setTitle(R.string.ButtonTextSelectCategory);
		
		View _view = getLayoutInflater().inflate(R.layout.category_select_list, null);
		ExpandableListView _expandableListView = (ExpandableListView)_view.findViewById(R.id.expListV_selectCategory);
		CategoryAdapter _categoryAdapter = new CategoryAdapter(this);
		_expandableListView.setAdapter(_categoryAdapter);
		
		_builder.setView(_view);
		
		_builder.setNegativeButton(R.string.ButtonTextBack, null);
		
		AlertDialog _alertDialog = _builder.create();
		_alertDialog.show();
		
		_expandableListView.setOnGroupClickListener(new OnCategoryGroupItemClickListener(_alertDialog,_categoryAdapter));
		_expandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(_alertDialog,_categoryAdapter));
	}
	
	/** ExpandableListView的group点击事件 **/
	private class OnCategoryGroupItemClickListener implements ExpandableListView.OnGroupClickListener {
		
		private AlertDialog mAlertDialog;
		private CategoryAdapter mCategoryAdapter;
		
		public OnCategoryGroupItemClickListener(AlertDialog alertDialog,CategoryAdapter categoryAdapter){
			mAlertDialog = alertDialog;
			mCategoryAdapter = categoryAdapter;
		}
		
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			int _count = mCategoryAdapter.getChildrenCount(groupPosition);
			if(_count == 0){//如果没有子类，则可以选择根类
				CategoryModel _categoryModel = (CategoryModel)mCategoryAdapter.getGroup(groupPosition);
				atoComptEdtTxt_categoryName.setText(_categoryModel.name);
				mSelectedCategoryModelId = _categoryModel.id;
				
				mAlertDialog.dismiss();
			}
			
			return false;
		}
		
	}
	
	/** ExpandableListView的child点击事件 **/
	private class OnCategoryChildItemClickListener implements ExpandableListView.OnChildClickListener {
		
		private AlertDialog mAlertDialog;
		private CategoryAdapter mCategoryAdapter;
		
		public OnCategoryChildItemClickListener(AlertDialog alertDialog,CategoryAdapter categoryAdapter){
			mAlertDialog = alertDialog;
			mCategoryAdapter = categoryAdapter;
		}
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			CategoryModel _selectCategoryModel = (CategoryModel)mCategoryAdapter.getChild(groupPosition, childPosition);
			mSelectedCategoryModelId = _selectCategoryModel.id;
			atoComptEdtTxt_categoryName.setText(_selectCategoryModel.name);
			
			mAlertDialog.dismiss();
			
			return false;
		}
		
	}

	/** 输入金额结束，回调 **/
	@Override
	public void onNumberEnterFinished(BigDecimal number) {
		edtTxt_selectAmount.setText(number.toString());
	}
	
	

}
