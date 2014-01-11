package cilinet.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.adapter.CategoryAdapter;
import cilinet.godutch.adapter.SlideMenuItem;
import cilinet.godutch.business.CategoryBusiness;
import cilinet.godutch.control.SlideMenuControl;
import cilinet.godutch.model.CategoryModel;

/** 类别管理 **/
public class CategoryActivity extends FrameActivity implements SlideMenuControl.OnSlideMenuItemClickListener {
	private static final String TAG = "CategoryActivity";
	
	private ExpandableListView expListV_categoryList;
	/** 因为ExpandableListAdapter与ListAdapter不兼容，所以需要保存一份。留在上下文菜单中使用 **/
	private CategoryAdapter mCategoryAdapter;
	
	private CategoryBusiness mCategoryBusiness;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_category);
		super.createSlideMenu(R.array.SlideMenuCategory);
		
		init();
	}

	private void init() {
		initVariable();
		initView();
		setTopBarTitle(getString(R.string.ActivityTitleCategory,12));
	}

	private void initVariable() {
		mCategoryBusiness = new CategoryBusiness(this);
	}

	private void initView() {
		expListV_categoryList = (ExpandableListView)super.findViewById(R.id.expListV_categoryList);
		bindAdapter();
		super.registerForContextMenu(expListV_categoryList);
	}
	
	private void bindAdapter(){
		mCategoryAdapter = new CategoryAdapter(this);
		expListV_categoryList.setAdapter(mCategoryAdapter);
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		if(0 == slideMenuItem.itemId){//新建类别
			Intent _intent = new Intent(this,AddCategoryActivity.class);
			startActivityForResult(_intent, slideMenuItem.itemId);
		}else if(1 == slideMenuItem.itemId){//统计类别
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bindAdapter();
		if(100 == resultCode){//添加类别后的返回
			super.slideSlideMenu();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/** 选中的菜单 **/
	private CategoryModel mSelectedCategoryModel;
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo _expandableListContextMenuInfo = (ExpandableListContextMenuInfo)menuInfo;
		
		long _packedPosition = _expandableListContextMenuInfo.packedPosition;
		int _packedPositionType = ExpandableListView.getPackedPositionType(_packedPosition);
		int _packedGroupPosition = ExpandableListView.getPackedPositionGroup(_packedPosition);
		
		switch (_packedPositionType) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP://如果是group
			mSelectedCategoryModel = (CategoryModel)mCategoryAdapter.getGroup(_packedGroupPosition);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int _packedChildPosition = ExpandableListView.getPackedPositionChild(_packedPosition);
			mSelectedCategoryModel = (CategoryModel)mCategoryAdapter.getChild(_packedGroupPosition, _packedChildPosition);
			break;
		default:
			break;
		}
		
		menu.setHeaderIcon(R.drawable.category_small_icon);
		if(mSelectedCategoryModel != null){
			menu.setHeaderTitle(mSelectedCategoryModel.name);
		}
		super.createManageContextMenu(menu);
		
		menu.add(0, 3, 3, R.string.ActivityCategoryTotal);
		
		//如果根类别下还有子类，则把删除菜单屏蔽
		if(mCategoryAdapter.getChildCountOfGroup(_packedGroupPosition) != 0 && mSelectedCategoryModel.parentId == 0){
			menu.findItem(2).setEnabled(false);
		}
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent _intent;
		switch (item.getItemId()) {
		case 1://修改
			_intent = new Intent(this, EditCategoryActivity.class);
			_intent.putExtra("SelectedCategoryModel", mSelectedCategoryModel);
			this.startActivityForResult(_intent, 1);
			break;
		case 2://删除
			deleteSelectedCategory();
			break;
		case 3://统计类别
//			List<ModelCategoryTotal> _List = mBusinessCategory.CategoryTotalByParentID(mSelectModelCategory.GetCategoryID());
//			_Intent = new Intent();
//			_Intent.putExtra("Total", (Serializable)_List);
//			_Intent.setClass(this, ActivityCategoryChart.class);
//			startActivity(_Intent);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	/** 删除选中的类别 **/
	private void deleteSelectedCategory(){
		super.showAlertDialog(getString(R.string.DialogTitleDelete), getString(R.string.DialogMessageCategoryDelete,new Object[]{mSelectedCategoryModel.name}), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean _deleteResult = mCategoryBusiness.deleteCategory(mSelectedCategoryModel);
				if(_deleteResult){
					showToastMessage(getString(R.string.DeleteSuccessed));
					CategoryActivity.this.bindAdapter();
				}else {
					showToastMessage(getString(R.string.TipsDeleteFail));
				}
			}
		});
		
	}

	
}
