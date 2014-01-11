package cilinet.godutch.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cilinet.godutch.R;
import cilinet.godutch.activity.base.FrameActivity;
import cilinet.godutch.business.CategoryBusiness;
import cilinet.godutch.model.CategoryModel;
import cilinet.godutch.utility.RegexTools;

/** 新建类别 **/
@SuppressWarnings("unchecked")
public class AddCategoryActivity extends FrameActivity implements android.view.View.OnClickListener{
	
	private Spinner spiner_parentID;
	private Button btn_saveCategory;
	private Button btn_cancelSaveCategory;
	private EditText edTxt_CategoryName;
	
	private CategoryBusiness mCategoryBusiness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.appendCenterMainBody(R.layout.activity_category_addoredit);
		
		init();
	}

	private void init() {
		initVariable();
		initView();
		setTopBarTitle(getString(R.string.ActivityTitleCategoryAddOrEdit, new Object[]{getString(R.string.TitleAdd)}));
	}

	private void initVariable() {
		mCategoryBusiness = new CategoryBusiness(this);
	}

	private void initView() {
		super.removeIncludeBottom();
		
		spiner_parentID = (Spinner)findViewById(R.id.spiner_parentID);
		bindAdapter();
		
		btn_saveCategory = (Button)super.findViewById(R.id.btn_saveCategory);
		btn_saveCategory.setOnClickListener(this);
		
		btn_cancelSaveCategory = (Button)findViewById(R.id.btn_cancelSaveCategory);
		btn_cancelSaveCategory.setOnClickListener(this);
		
		edTxt_CategoryName = (EditText)findViewById(R.id.edTxt_CategoryName);
		
	}
	
	private void bindAdapter(){
		List _categoryModels = mCategoryBusiness.queryAvailableRootCategory();
		_categoryModels.add(0, "== 请选择  ==");
		
		ArrayAdapter _arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, _categoryModels);
		_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spiner_parentID.setAdapter(_arrayAdapter);
	}

	@Override
	public void onClick(View v) {
		int _btnResId = v.getId();
		switch(_btnResId){
		case R.id.btn_saveCategory:
			addCategory();
			break;
		case R.id.btn_cancelSaveCategory:
			finish();
			break;
		default: 
			break;
		}
	}
	
	/** 添加类别 **/
	private void addCategory(){
		//categoryName
		String _categoryName = edTxt_CategoryName.getText().toString();
		boolean _checkResult = RegexTools.isChineseEnglishNum(_categoryName);
		if(!_checkResult){
			super.showToastMessage(getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{getString(R.string.TextViewTextCategoryName)}));	
		    return;
		}
		
		//parentId
		int _parentCategoryId = 0;
		if(!spiner_parentID.getSelectedItem().toString().equals("== 请选择  ==")){
			CategoryModel _categoryModel = (CategoryModel)spiner_parentID.getSelectedItem();
			if(null != _categoryModel){
				_parentCategoryId = _categoryModel.id;
			}
		}
		
		//如果经过字符校验，则查看数据库中是否已有改名字的类别
		_checkResult = mCategoryBusiness.checkCategoryNameIfExists(_categoryName,-1,_parentCategoryId);
		if(_checkResult){//如果存在
			super.showToastMessage(getString(R.string.CheckDataTextCategoryExist));
			return;
		}
		
		//满足条件，则添加类别
		boolean result = mCategoryBusiness.insertCategory(_categoryName,_parentCategoryId);
		if(result){
			super.showToastMessage(getString(R.string.TipsAddSucceed));
			super.setResult(100);
			finish();
		}else {
			super.showToastMessage(getString(R.string.TipsAddFail));
		}
	}

}
