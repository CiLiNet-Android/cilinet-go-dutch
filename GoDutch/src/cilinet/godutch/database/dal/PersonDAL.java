package cilinet.godutch.database.dal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cilinet.godutch.R;
import cilinet.godutch.database.base.BaseDAL;
import cilinet.godutch.model.PersonModel;
import cilinet.godutch.utility.DateTools;

/** 人员DAL:类似于DAO 完成基本的增删改查功能
 * 
 *  比较简单的数据库操作可以使用封装的操作
 * **/
@SuppressWarnings("unchecked")
public class PersonDAL extends BaseDAL{
	
	private static final String TAG = "PersonDAL";
	
	/** 表结构 **/
	public static final class TABLE {
		public static final String TABLE_NAME = "Person";
		public static final String COLUMN_PERSONID = "personId";
		public static final String COLUMN_PERSONNAME = "personName";
		public static final String COLUMN_CREATEDATE = "createDate";
		public static final String COLUMN_STATE = "state";
	}
	
	
	public PersonDAL(Context context) {
		super(context);
	}
	
	/** 增 **/
	public boolean insertUser(PersonModel personModel){
		ContentValues _contentValues = createContentValuesFromModel(personModel);
		long _newPersonId = getSQLiteDatabase().insert(TABLE.TABLE_NAME, null, _contentValues);
		personModel.personId = (int)_newPersonId;
		
		return  _newPersonId > 0;
	}
	
	/** 删 **/
	public boolean deleteUser(Map<String,String> whereConditions){
		return super.delete(TABLE.TABLE_NAME, whereConditions);
	}
	
	/** 更新 **/
	//public boolean updateUser(){}
	
	/** 查询用户 **/
	public List<PersonModel> queryPerson(String condition){
		List<PersonModel> _personModels = null;
		
		StringBuilder _sql = new StringBuilder("SELECT * FROM " + TABLE.TABLE_NAME + " WHERE 1=1 ");
		if(null != condition && !"".equals(condition)){
			_sql.append(condition);
		}
		
		_personModels = queryData(_sql.toString());
		
		return _personModels;
	}
	
	public List<PersonModel> queryAllPerson(){
		return queryPerson(null);
	}
	
	

	/** 创建表，要通过反射来调用的 **/
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		Log.i(TAG, "onCreate..");
		
		StringBuilder _reateTableScript = new StringBuilder();

		_reateTableScript.append("		Create  TABLE ").append(TABLE.TABLE_NAME).append("(");
		_reateTableScript.append("				[").append(TABLE.COLUMN_PERSONID).append("] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_PERSONNAME).append("] varchar(10) NOT NULL");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_CREATEDATE).append("] datetime NOT NULL");
		_reateTableScript.append("				,[").append(TABLE.COLUMN_STATE).append("] integer NOT NULL");
		_reateTableScript.append("				)");
		
		//创建表
		sqLiteDatabase.execSQL(_reateTableScript.toString());
		
		//初始化一些数据
		initDefaultPersons(sqLiteDatabase);
	}
	
	/** 初始化数据，数据在arrays.xml中存放 **/
	private void initDefaultPersons(SQLiteDatabase sqLiteDatabase){
		String[] _personNames = getContext().getResources().getStringArray(R.array.InitDefaultUserName);
		for(String _personName : _personNames){
			sqLiteDatabase.execSQL("INSERT INTO " + TABLE.TABLE_NAME + "(" + TABLE.COLUMN_PERSONNAME + "," + TABLE.COLUMN_CREATEDATE + "," + TABLE.COLUMN_STATE + ") VALUES(?,?,?)", new Object[]{_personName,DateTools.getFormatDateTime(new Date(), DateTools.DEFAULT_DATETIME_FORMAT),1});
		}
	}

	/** 更新表 **/
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {
		
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE.TABLE_NAME,TABLE.COLUMN_PERSONID};
	}

	/** 构造ContentValues **/
	private ContentValues createContentValuesFromModel(PersonModel personModel){
		ContentValues _contentValues = new ContentValues();
		_contentValues.put(TABLE.COLUMN_PERSONNAME, personModel.personName);
		_contentValues.put(TABLE.COLUMN_CREATEDATE, DateTools.getFormatDateTime(personModel.createDate, DateTools.DEFAULT_DATETIME_FORMAT));
		_contentValues.put(TABLE.COLUMN_STATE, personModel.state);
		
		return _contentValues;
	}

	@Override
	protected Object createModelByCursor(Cursor cursor) {
		int _personId = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_PERSONID));
		String _personName = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_PERSONNAME));
		Date _createDate = DateTools.getDate(cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_CREATEDATE)), DateTools.DEFAULT_DATETIME_FORMAT);
		int _state = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_STATE));
		
		return new PersonModel(_personId,_personName,_createDate,_state);
	}
	
	
	
}
