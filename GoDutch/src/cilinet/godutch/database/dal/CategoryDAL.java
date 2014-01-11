package cilinet.godutch.database.dal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cilinet.godutch.R;
import cilinet.godutch.database.base.BaseDAL;
import cilinet.godutch.model.CategoryModel;
import cilinet.godutch.utility.DateTools;


@SuppressWarnings("unchecked")
public class CategoryDAL extends BaseDAL {
	
	public static final class TABLE {
		public static final String TABLE_NAME = "Category";
		public static final String COLUMN_ID = "CategoryID";
		public static final String COLUMN_NAME = "CategoryName";
		public static final String COLUMN_TYPEFLAG = "TypeFlag";
		public static final String COLUMN_PARENTID = "ParentID";
		public static final String COLUMN_PATH = "Path";
		public static final String COLUMN_STATE = "State";
		public static final String COLUMN_CREATEDATE = "CreateDate";
	}

	public CategoryDAL(Context context) {
		super(context);
	}
	
	/** 添加类别 **/
	public boolean insertCategory(CategoryModel categoryModel){
		ContentValues _contentValues = createContentValuesFromModel(categoryModel);
		Long _newCategoryId = getSQLiteDatabase().insert(TABLE.TABLE_NAME, null, _contentValues);
		categoryModel.id = _newCategoryId.intValue();
		
		return _newCategoryId > 0;
	}
	
	/** 查询类别 **/
	public List<CategoryModel> queryCategory(String whereCondition){
		StringBuilder _sql = new StringBuilder("SELECT * FROM " + TABLE.TABLE_NAME);
		if(null != whereCondition && !"".equals(whereCondition)){
			_sql.append(" WHERE ").append(whereCondition);
		}
		
		return super.queryData(_sql.toString());
	}
	
	public CategoryModel queryCategoryByCategoryId(int categoryId) {
		Cursor _cursor = getSQLiteDatabase().rawQuery("SELECT * FROM " + TABLE.TABLE_NAME + " WHERE " + TABLE.COLUMN_ID + "=?", new String[]{String.valueOf(categoryId)});
		
		CategoryModel _categoryModel = null;
		if(_cursor.moveToNext()){
			_categoryModel = (CategoryModel)createModelByCursor(_cursor);
		}
		return _categoryModel;
	}
	
	public boolean updateCategory(CategoryModel categoryModel){
		ContentValues _contentValues = createContentValuesFromModel(categoryModel);
		long _rowId = getSQLiteDatabase().update(TABLE.TABLE_NAME, _contentValues, TABLE.COLUMN_ID + "=?", new String[]{String.valueOf(categoryModel.id)});
		return _rowId > 0;
	}

	
	/** 将从上层创建的类别转换成ContentValues **/
	private ContentValues createContentValuesFromModel(CategoryModel categoryModel){
		ContentValues _contentValues = new ContentValues();

		_contentValues.put(TABLE.COLUMN_NAME, categoryModel.name);
		_contentValues.put(TABLE.COLUMN_TYPEFLAG, categoryModel.typeFlag);
		_contentValues.put(TABLE.COLUMN_PARENTID, categoryModel.parentId);
		_contentValues.put(TABLE.COLUMN_PATH, categoryModel.path);
		_contentValues.put(TABLE.COLUMN_STATE, categoryModel.state);
		_contentValues.put(TABLE.COLUMN_CREATEDATE, DateTools.getFormatDateTime(categoryModel.createDate, DateTools.DEFAULT_DATETIME_FORMAT));
		
		return _contentValues;
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE.TABLE_NAME,TABLE.COLUMN_ID};
	}

	/** 选中一条记录时调用 **/
	@Override
	protected Object createModelByCursor(Cursor cursor) {
		int _id = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_ID));
		String _name = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_NAME));
		String _typeFlag = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_TYPEFLAG));
		int _parentId = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_PARENTID));
		String _path = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_PATH));
		Date _createDate = DateTools.getDate(cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_CREATEDATE)), DateTools.DEFAULT_DATETIME_FORMAT);
		int _state = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_STATE));
		
		return new CategoryModel(_id, _name, _typeFlag, _parentId, _path, _createDate, _state);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		StringBuilder s_CreateTableScript = new StringBuilder();

		s_CreateTableScript.append("		CREATE  TABLE " + TABLE.TABLE_NAME + "(");
		s_CreateTableScript.append("				[" + TABLE.COLUMN_ID + "] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_NAME + "] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_TYPEFLAG + "] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_PARENTID + "] integer NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_PATH + "] text NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_CREATEDATE + "] datetime NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_STATE + "] integer NOT NULL");
		s_CreateTableScript.append("				)");

		sqLiteDatabase.execSQL(s_CreateTableScript.toString());
		initDefaultData(sqLiteDatabase);
	}

	/** 初始化数据,注意对path字段的维护 **/
	private void initDefaultData(SQLiteDatabase sqLiteDatabase) {
		CategoryModel _categoryModel = new CategoryModel();
		_categoryModel.typeFlag = getContext().getString(R.string.PayoutTypeFlag);
		_categoryModel.path = "-";
		
		String[] _categoryNames = getContext().getResources().getStringArray(R.array.InitDefaultCategoryName);
		for(String _categoryName : _categoryNames){
			_categoryModel.name = _categoryName;
			
			ContentValues _contentValues = createContentValuesFromModel(_categoryModel);
			long _newCategoryId = sqLiteDatabase.insert(TABLE.TABLE_NAME, null, _contentValues);

			String _categoryPath = String.valueOf(_newCategoryId);
			sqLiteDatabase.execSQL("UPDATE " + TABLE.TABLE_NAME + " SET " + TABLE.COLUMN_PATH + "=? WHERE " + TABLE.COLUMN_ID + "=?",new Object[]{_categoryPath,_newCategoryId});
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {

	}

	
}
