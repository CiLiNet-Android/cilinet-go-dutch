package cilinet.godutch.database.dal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cilinet.godutch.R;
import cilinet.godutch.database.base.BaseDAL;
import cilinet.godutch.database.dal.PersonDAL.TABLE;
import cilinet.godutch.model.AccountBookModel;
import cilinet.godutch.model.PersonModel;
import cilinet.godutch.utility.DateTools;

/** 账本DAL类 **/
@SuppressWarnings("unchecked")
public class AccountBookDAL extends BaseDAL {
	private static final String TAG = "AccountBookDAL";
	
	public static final class TABLE {
		public static final String TABLE_NAME = "AccountBook";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_STATE = "state";
		public static final String COLUMN_CREATEDATE = "createDate";
		public static final String COLUMN_ISDEFAULT = "isDefault";
	}

	public AccountBookDAL(Context context) {
		super(context);
	}
	
	/** 根据条件获取账本 **/
	public List<AccountBookModel> queryAccountBookByWhereCondition(String whereCondition){
		StringBuilder _sql = new StringBuilder("SELECT * FROM " + TABLE.TABLE_NAME);
		if(null != whereCondition &&!"".equals(whereCondition)){
			_sql.append(" WHERE ").append(whereCondition);
		}
		
		return queryData(_sql.toString());//queryData()方法依赖于createModelByCursor()
	}

	/** 保存AccountBookModel **/
	public boolean insertAccountBook(AccountBookModel accountBookModel) {
		ContentValues _contentValues = createContentValuesFromModel(accountBookModel);
		long _newId = getSQLiteDatabase().insert(TABLE.TABLE_NAME, null, _contentValues);
		accountBookModel.id = (int)_newId;
		
		return _newId > 0;
	}
	
	/** 更新AccountBookModel **/
	public boolean updateAccountBook(AccountBookModel accountBookModel){
		ContentValues _contentValues = createContentValuesFromModel(accountBookModel);
		long _updatedRows = getSQLiteDatabase().update(TABLE.TABLE_NAME, _contentValues, TABLE.COLUMN_ID + "=?", new String[]{String.valueOf(accountBookModel.id)});
		return _updatedRows > 0;
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE.TABLE_NAME,TABLE.COLUMN_ID};
	}

	@Override
	protected Object createModelByCursor(Cursor cursor) {
		int _id = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_ID));
		String _name = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_NAME));
		Date _createDate = DateTools.getDate(cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_CREATEDATE)), DateTools.DEFAULT_DATETIME_FORMAT);
		int _state = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_STATE));
		int _isDefault = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_ISDEFAULT));
		
		return new AccountBookModel(_id,_name,_createDate,_state,_isDefault);
	}
	
	/** 构造ContentValues **/
	private ContentValues createContentValuesFromModel(AccountBookModel accountBookModel){
		ContentValues _contentValues = new ContentValues();
		_contentValues.put(TABLE.COLUMN_NAME, accountBookModel.name);
		_contentValues.put(TABLE.COLUMN_CREATEDATE, DateTools.getFormatDateTime(accountBookModel.createDate, DateTools.DEFAULT_DATETIME_FORMAT));
		_contentValues.put(TABLE.COLUMN_STATE, accountBookModel.state);
		_contentValues.put(TABLE.COLUMN_ISDEFAULT, accountBookModel.isDefault);
		
		return _contentValues;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		Log.i(TAG, "onCreate..");
		
		StringBuilder _reateTableScript = new StringBuilder();

		_reateTableScript.append("		CREATE  TABLE ").append(TABLE.TABLE_NAME).append("(");
		_reateTableScript.append("				[").append(TABLE.COLUMN_ID).append("] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_NAME).append("] VARCHAR(10) NOT NULL");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_CREATEDATE).append("] DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_STATE).append("] INTEGER NOT NULL DEFAULT 1");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_ISDEFAULT).append("] INTEGER NOT NULL DEFAULT 1");
		_reateTableScript.append("				)");
		
		//创建表
		sqLiteDatabase.execSQL(_reateTableScript.toString());
		
		//初始化一些数据
		initDefaultAccountBook(sqLiteDatabase);
	}

	private void initDefaultAccountBook(SQLiteDatabase sqLiteDatabase) {
		String[] _accountBookNames = getContext().getResources().getStringArray(R.array.InitDefaultDataAccountBookName);
		for(String _accountBookName : _accountBookNames){
			sqLiteDatabase.execSQL("INSERT INTO " + TABLE.TABLE_NAME + "(" + TABLE.COLUMN_NAME + ") VALUES(?)", new Object[]{_accountBookName});
		}
	}


	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {

	}
	
	
}
