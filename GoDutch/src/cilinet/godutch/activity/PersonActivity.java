package cilinet.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.PersonAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.business.PersonBusiness;
import cilinet.godutch.control.SlideMenuControl;
import cilinet.godutch.model.PersonModel;
import cilinet.godutch.utility.RegexTools;

/** 人员管理 **/
public class PersonActivity extends FrameActivity implements SlideMenuControl.OnSlideMenuItemClickListener{
	private static final String TAG = "PersonActivity";
	
	private ListView listV_personList;
	private PersonAdapter mPersonAdapter;
	
	private PersonBusiness mPersonBusiness;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.appendCenterMainBody(R.layout.activity_person);
		super.createSlideMenu(R.array.SlideMenuUser);
		
		init();
	}
	
	private void init(){
		initView();
		bindAdapter();
		setTopBarTitle();
		initVariable();
	}

	private void initView(){
		listV_personList = (ListView)super.findViewById(R.id.listV_personList);
		super.registerForContextMenu(listV_personList);
	}

	/** 上下文菜单需要用到的实体 **/
	private PersonModel mSelectedPersonModel;
	
	/** ListView item长按后调用,显示上下文菜单 **/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.i(TAG, "onCreateContextMenu()...");
		
		AdapterView.AdapterContextMenuInfo _adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
		ListAdapter _listAdapter = listV_personList.getAdapter();
		
		//根据选中item的position选项，获得实体
		mSelectedPersonModel = (PersonModel)_listAdapter.getItem(_adapterContextMenuInfo.position);
		
		//设置菜单
		menu.setHeaderIcon(R.drawable.user_small_icon);
		menu.setHeaderTitle(mSelectedPersonModel.personName);
		
		createManageContextMenu(menu);
	}
	
	/** 当用户选择了上下文菜单的其中一项后调用,点击后上下文菜单自动消失 **/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.i(TAG, "onContextItemSelected()...itemId: " + item.getItemId());
		
		switch(item.getItemId()){
		case 1:
			showPersonAddOrEditDialog(mSelectedPersonModel);
			break;
		case 2:
			showPersonDeleteDialog(mSelectedPersonModel);
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	private void bindAdapter(){
		mPersonAdapter = new PersonAdapter(this);
		listV_personList.setAdapter(mPersonAdapter);
	}
	
	
	private void initVariable(){
		mPersonBusiness = new PersonBusiness(this);
	}
	
	private void setTopBarTitle(){
		super.setTopBarTitle(getString(R.string.appGridTextUserManage) + "(" + mPersonAdapter.getCount() + ")");
	}
	
	
	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		super.slideSlideMenu();
		if(slideMenuItem.itemId == 0){//新建人员
			showPersonAddOrEditDialog(null);
		}
	}
	
	/** 添加或修改人员 **/
	private void showPersonAddOrEditDialog(PersonModel personModel){
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_person_addoredit, null);
		
		EditText edTxt_personName = (EditText)dialogView.findViewById(R.id.edTxt_personName);
		String _dialogTitle = null;
		if(null == personModel){
			_dialogTitle = getString(R.string.DialogTitleUser,new Object[]{getString(R.string.TitleAdd)});
		}else {
			_dialogTitle = getString(R.string.DialogTitleUser,new Object[]{getString(R.string.TitleEdit)});
			edTxt_personName.setText(personModel.personName);
		}
		
		//构建提示框
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		_alertDialogBuilder.setTitle(_dialogTitle)
						   .setIcon(R.drawable.user_big_icon)
						   .setView(dialogView)//在message的区域显示
						   .setNeutralButton(getString(R.string.ButtonTextSave), new OnPersonAddOrEditDialogClickListener(personModel, edTxt_personName, true))
						   .setNegativeButton(getString(R.string.ButtonTextCancel), new OnPersonAddOrEditDialogClickListener(personModel, edTxt_personName, false))
						   .show();
	}
	
	/** AlertDialog监听事件 **/
	private class OnPersonAddOrEditDialogClickListener implements DialogInterface.OnClickListener {
		
		private PersonModel mPersonModel;
		private EditText edTxt_personName;
		/** 是否是点击了保存按钮 **/
		private boolean mIsClickedSaveBtn;
		
		public OnPersonAddOrEditDialogClickListener(PersonModel personModel,EditText edTxt_userName,boolean isClickedSaveBtn){
			mPersonModel = personModel;
			this.edTxt_personName = edTxt_userName;
			mIsClickedSaveBtn = isClickedSaveBtn;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int arg1) {
			if(mIsClickedSaveBtn){//如果点击了确定按钮
				String _personName = edTxt_personName.getText().toString().trim();
				
				//如果用户输入的用户名是否符合数字、字母或中文
				if(!RegexTools.isChineseEnglishNum(_personName)){
					PersonActivity.this.showToastMessage(getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{edTxt_personName.getHint()}));
					PersonActivity.this.setAlertDialogClosable(dialog, false);
					return;
				}
				
				//如果用户输入的名称有效，则检查数据库中是否有同名的人员
				if(mPersonModel == null){
					mPersonModel = new PersonModel(_personName);
				}else {
					mPersonModel.personName = _personName;
				}
				
				//判断用户名是否存在
				if(mPersonBusiness.checkPersonNameIfExists(mPersonModel)){
					PersonActivity.this.showToastMessage(getString(R.string.CheckDataTextUserExist,new Object[]{mPersonModel.personName}));
					PersonActivity.this.setAlertDialogClosable(dialog, false);
					return;
				}
				
				PersonActivity.this.setAlertDialogClosable(dialog, true);
				
				//校验通过后，则保存数据
				boolean _result = false;
				if(0 == mPersonModel.personId){
					_result = mPersonBusiness.insertPerson(mPersonModel);
				}else {
					_result = mPersonBusiness.updatePerson(mPersonModel);
				}
				
				//判断更新结果
				if(_result){
					PersonActivity.this.bindAdapter();
					PersonActivity.this.showToastMessage(getString(R.string.TipsAddSucceed));
				}else {
					PersonActivity.this.showToastMessage(getString(R.string.TipsAddFail));
				}
				
			}else {//点击了取消按钮
				PersonActivity.this.setAlertDialogClosable(dialog, true);
			}
		}
		
	}

	/** 删除人员 **/
	private void showPersonDeleteDialog(final PersonModel personModel){
		String _dialogMsg = super.getString(R.string.DialogMessageUserDelete,new Object[]{personModel.personName});
		super.showAlertDialog(getString(R.string.DialogTitleDelete), _dialogMsg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean _deleteResult = mPersonBusiness.deletePersonByPersonId(personModel.personId);
				if(_deleteResult){
					PersonActivity.this.showToastMessage(getString(R.string.DeleteSuccessed));
					PersonActivity.this.bindAdapter();
				}
			}
		});
	}
}
