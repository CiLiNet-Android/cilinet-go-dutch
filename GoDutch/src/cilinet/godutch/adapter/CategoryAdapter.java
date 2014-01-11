package cilinet.godutch.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cilinet.godutch.R;
import cilinet.godutch.business.CategoryBusiness;
import cilinet.godutch.model.CategoryModel;

public class CategoryAdapter extends BaseExpandableListAdapter{
	private static final String TAG = "CategoryAdapter";

	private Context mContext;
	
	private CategoryBusiness mCategoryBusiness;
	
	/** 记录每个group下child的数量 **/
	private List<Integer> mChildCountOfGroup; 
	
	
	/** 根类别 **/
	private List<CategoryModel> mAvailableRootCategories;
	
	public CategoryAdapter(Context context){
		mContext = context;
		mCategoryBusiness = new CategoryBusiness(context);
		
		mAvailableRootCategories = mCategoryBusiness.queryAvailableRootCategory();
		
		mChildCountOfGroup = new ArrayList<Integer>();
	}
	
	public Object getGroup(int groupPosition) {
		return mAvailableRootCategories.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mAvailableRootCategories.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	private class GroupViewHolder {
		public TextView txtV_categoryName;
		public TextView txtV_categoryTotal;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder _groupViewHolder = null;
		if(null == convertView){
			_groupViewHolder = new GroupViewHolder();
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_category_groupitem, null);
		
			_groupViewHolder.txtV_categoryName = (TextView)convertView.findViewById(R.id.txtV_categoryName);
			_groupViewHolder.txtV_categoryTotal = (TextView)convertView.findViewById(R.id.txtV_categoryTotal);
			
			convertView.setTag(_groupViewHolder);
		}else {
			_groupViewHolder = (GroupViewHolder)convertView.getTag();
		}
		
		CategoryModel _categoryModel = (CategoryModel)getGroup(groupPosition);
		_groupViewHolder.txtV_categoryName.setText(_categoryModel.name);
		
		int _childCategoryCount = mCategoryBusiness.queryAvailableChildCategoryCountByParentId(_categoryModel.id);
		//记录每一个group下child的数量
		mChildCountOfGroup.add(_childCategoryCount);
		_groupViewHolder.txtV_categoryTotal.setText(mContext.getString(R.string.TextViewTextChildrenCategory,_childCategoryCount));
		
		return convertView;
	}

	
	/**** 子菜单项 ***/
	@Override
	public int getChildrenCount(int groupPosition) {
		CategoryModel _categoryModel = (CategoryModel)getGroup(groupPosition);
		List<CategoryModel> _availableChildCategories = mCategoryBusiness.queryAvailableChildCategoryByParentId(_categoryModel.id);
		return _availableChildCategories.size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		CategoryModel _categoryModel = (CategoryModel)getGroup(groupPosition);
		List<CategoryModel> _availableChildCategories = mCategoryBusiness.queryAvailableChildCategoryByParentId(_categoryModel.id);
		return _availableChildCategories.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	private class ChildViewHolder {
		public TextView txtV_childCategoryName;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder _childViewHolder = null;
		if(null == convertView){
			_childViewHolder = new ChildViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_category_childitem, null);
			
			_childViewHolder.txtV_childCategoryName = (TextView)convertView.findViewById(R.id.txtV_childCategoryName);
			
			convertView.setTag(_childViewHolder);
		}else {
			_childViewHolder = (ChildViewHolder)convertView.getTag();
		}
		
		CategoryModel _childCategoryModel = (CategoryModel)getChild(groupPosition, childPosition);
		_childViewHolder.txtV_childCategoryName.setText(_childCategoryModel.name);
		
		return convertView;
	}


	//行是否具有唯一id
	//是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID
	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	//行是否可选
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public int getChildCountOfGroup(int groupPosition) {
		return mChildCountOfGroup.get(groupPosition);
	}

	
}
