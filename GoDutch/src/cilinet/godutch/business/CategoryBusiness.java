package cilinet.godutch.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import cilinet.godutch.R;
import cilinet.godutch.business.base.BaseBusiness;
import cilinet.godutch.database.dal.CategoryDAL;
import cilinet.godutch.database.dal.CategoryDAL.TABLE;
import cilinet.godutch.model.CategoryModel;

/** 类别管理业务层 **/
public class CategoryBusiness extends BaseBusiness {
	
	private CategoryDAL mCategoryDAL;

	public CategoryBusiness(Context context) {
		super(context);
		mCategoryDAL = new CategoryDAL(context);
	}
	
	/** 查询类别 **/
	public List<CategoryModel> queryAvailableCategory(){
		String _whereCondition = CategoryDAL.TABLE.COLUMN_STATE + "=1";
		return queryCategory(_whereCondition);
	}
	
	/** 查询所有的根类别 **/
	public List<CategoryModel> queryAvailableRootCategory() {
		String _whereCondition = CategoryDAL.TABLE.COLUMN_PARENTID + "=0 AND " + CategoryDAL.TABLE.COLUMN_STATE + "=1";
		return queryCategory(_whereCondition);
	}
	
	/** 根据父类Id别查询其子类别 **/
	public List<CategoryModel> queryAvailableChildCategoryByParentId(int parentCategoryId){
		String _whereCondition = CategoryDAL.TABLE.COLUMN_STATE + "=1 AND " + CategoryDAL.TABLE.COLUMN_PARENTID + "=" + parentCategoryId;
		return queryCategory(_whereCondition);
	}
	
	/** 根据父类Id别查询其子类别数量 **/
	public int queryAvailableChildCategoryCountByParentId(int parentCategoryId){
		Map<String,String> _whereConditions = new HashMap<String, String>();
		_whereConditions.put(CategoryDAL.TABLE.COLUMN_STATE + "=?", "1");
		_whereConditions.put(CategoryDAL.TABLE.COLUMN_TYPEFLAG + "=?", getString(R.string.PayoutTypeFlag));
		_whereConditions.put(CategoryDAL.TABLE.COLUMN_PARENTID + "=?", String.valueOf(parentCategoryId));
		
		return mCategoryDAL.getCount(_whereConditions);
	}
	
	private List<CategoryModel> queryCategory(String whereCondition){
		String _whereCondition = CategoryDAL.TABLE.COLUMN_TYPEFLAG + "='" + getString(R.string.PayoutTypeFlag) +"' AND " + whereCondition;
		return mCategoryDAL.queryCategory(_whereCondition);
	}

	private boolean insertCategory(CategoryModel categoryModel) {
		mCategoryDAL.beginTransaction();
		try{
			boolean _insertResult = mCategoryDAL.insertCategory(categoryModel);
			if(_insertResult){
				if(categoryModel.parentId == 0){//根类别
					categoryModel.path = String.valueOf(categoryModel.id);
				}else {
					CategoryModel _parentCategoryModel = mCategoryDAL.queryCategoryByCategoryId(categoryModel.parentId);
					if(null != _parentCategoryModel){
						categoryModel.path = _parentCategoryModel.path + "-" + categoryModel.id;
					}
				}
				
				boolean _updateResult = mCategoryDAL.updateCategory(categoryModel);
				if(_updateResult){
					mCategoryDAL.setTransactionSuccessful();
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			mCategoryDAL.endTransaction();
		}

		return false;
	}

	public boolean insertCategory(String _categoryName, int _parentCategoryId) {
		CategoryModel _categoryModel = new CategoryModel();
		_categoryModel.name = _categoryName;
		_categoryModel.parentId = _parentCategoryId;
		_categoryModel.path = "";
		_categoryModel.typeFlag = getString(R.string.PayoutTypeFlag);
		
		return insertCategory(_categoryModel);
	}
	
	public boolean updateCategory(CategoryModel categoryModel){
		mCategoryDAL.beginTransaction();
		try{
			if(categoryModel.parentId > 0){//如果是子类别要更新
				CategoryModel _parentCategoryModel = mCategoryDAL.queryCategoryByCategoryId(categoryModel.parentId);
				categoryModel.path = _parentCategoryModel.path + "-" + categoryModel.id;
			}else {//跟类别
				categoryModel.path = String.valueOf(categoryModel.id);
			}
			
			boolean updateResult = mCategoryDAL.updateCategory(categoryModel);
			if(updateResult){
				mCategoryDAL.setTransactionSuccessful();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			mCategoryDAL.endTransaction();
		}
		
		return false;
	}
	
	/** 删除类别 (逻辑删除)**/
	public boolean deleteCategory(CategoryModel categoryModel){
		categoryModel.state = 0;
		return mCategoryDAL.updateCategory(categoryModel);
	}

	
	/** 查看类别的名称是否已存在,如果是新添加的，则categoryId可以填-1,如果存在，则返回true**/
	public boolean checkCategoryNameIfExists(String categoryName,int categoryId,int parentCategoryId){
		mCategoryDAL.beginTransaction();
		try{
			Map<String,String> _whereConditions = new HashMap<String, String>();
			
			_whereConditions.put(TABLE.COLUMN_PARENTID + "=?", String.valueOf(parentCategoryId));//同一级下的子类别
			_whereConditions.put(TABLE.COLUMN_NAME + "=?", categoryName);
			
			if(categoryId > 0) {
				_whereConditions.put(TABLE.COLUMN_ID + "<>?", String.valueOf(categoryId));
			}
			
			mCategoryDAL.setTransactionSuccessful();
			
			return mCategoryDAL.getCount(_whereConditions) > 0;
			
		}catch(Exception e){
			return false;
		}finally{
			mCategoryDAL.endTransaction();
		}

	}

}
