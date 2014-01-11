package cilinet.godutch.business;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;
import cilinet.godutch.model.CategoryModel;

public class CategoryBusinessTest extends AndroidTestCase {
	
	private static final String TAG = "CategoryBusinessTest";

	public void testQueryAvailableCategory(){
		CategoryBusiness _categoryBusiness = new CategoryBusiness(this.getContext());
		List<CategoryModel> _categoryModels = _categoryBusiness.queryAvailableCategory();
		for(CategoryModel _categoryModel : _categoryModels){
			Log.i(TAG, _categoryModel.path);
		}
	}
	
	public void testQueryAvailableRootCategory(){
		CategoryBusiness _categoryBusiness = new CategoryBusiness(this.getContext());
		List<CategoryModel> _categoryModels = _categoryBusiness.queryAvailableRootCategory();
		for(CategoryModel _categoryModel : _categoryModels){
			Log.i(TAG, _categoryModel.toString());
		}
	}
	
	public void testQueryAvailableChildCategoryByParentId(){
		CategoryBusiness _categoryBusiness = new CategoryBusiness(this.getContext());
		List<CategoryModel> _categoryModels = _categoryBusiness.queryAvailableChildCategoryByParentId(3);
		for(CategoryModel _categoryModel : _categoryModels){
			Log.i(TAG, _categoryModel.toString());
		}
	}
	
	public void testQueryAvailableChildCategoryCountByParentId(){
		CategoryBusiness _categoryBusiness = new CategoryBusiness(this.getContext());
		int count = _categoryBusiness.queryAvailableChildCategoryCountByParentId(3);
		Log.i(TAG, "count: " + count);
	}
	
	public void testInsertCategory(){
		CategoryBusiness _categoryBusiness = new CategoryBusiness(this.getContext());
		_categoryBusiness.insertCategory("化妆品", 2);
	}
	
//	public void testCheckCategoryNameIfExists(){
//		CategoryBusiness _categoryBusiness = new CategoryBusiness(this.getContext());
//		Log.i(TAG, "testCheckCategoryNameIfExists: " + _categoryBusiness.checkCategoryNameIfExists("化妆品",-1));
//	}
	
}
