package cilinet.godutch.database.dal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cilinet.godutch.database.base.BaseDAL;
import cilinet.godutch.model.PayoutModel;
import cilinet.godutch.utility.DateTools;

/**
 * 将数据存入表中
 * 但从视图中获取数据
 * 
 * **/
@SuppressWarnings("unchecked")
public class PayoutDAL extends BaseDAL {
	
	private static final String TAG = "PayoutDAL";
	
	public static final class TABLE {
		public static final String TABLE_NAME = "Payout";
		
		public static final String COLUMN_PAYOUTID = "PayoutID";
		public static final String COLUMN_ACCOUNTBOOKID = "AccountBookID";
		public static final String COLUMN_CATEGORYID = "CategoryID";
		public static final String COLUMN_AMOUNT = "Amount";
		public static final String COLUMN_PAYOUTDATE = "PayoutDate";
		public static final String COLUMN_PAYOUTTYPE = "PayoutType";
		public static final String COLUMN_PAYOUTUSERID = "PayoutUserID";
		public static final String COLUMN_COMMENT = "Comment";
		public static final String COLUMN_CREATEDATE = "CreateDate";
		public static final String COLUMN_STATE = "State";
		
	}

	public PayoutDAL(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		StringBuilder s_CreateTableScript = new StringBuilder();
		
		s_CreateTableScript.append("		Create  TABLE " + TABLE.TABLE_NAME + "(");
		s_CreateTableScript.append("				[" + TABLE.COLUMN_PAYOUTID + "] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_ACCOUNTBOOKID + "] integer NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_AMOUNT + "] decimal NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_CATEGORYID + "] integer NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_PAYOUTDATE + "] datetime NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_PAYOUTTYPE + "] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_PAYOUTUSERID + "] text NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_COMMENT + "] text");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_CREATEDATE + "] datetime NOT NULL");
		s_CreateTableScript.append("				,[" + TABLE.COLUMN_STATE + "] integer NOT NULL");
		s_CreateTableScript.append("				)");
		
		Log.i(TAG, s_CreateTableScript.toString());
		
		sqLiteDatabase.execSQL(s_CreateTableScript.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {

	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE.TABLE_NAME,TABLE.COLUMN_PAYOUTID};
	}
	
	/** 增 **/
	public boolean insertPayout(PayoutModel payoutModel){
		ContentValues _contentValues = createContentValuesFromModel(payoutModel);
		Long _rowId = getSQLiteDatabase().insert(TABLE.TABLE_NAME, null, _contentValues);
		payoutModel.payoutID = _rowId.intValue();
		
		return _rowId > 0;
	}
	
	/** 删 **/
	public boolean deletePayout(PayoutModel payoutModel){
		Map<String,String> _whereConditions = new HashMap<String, String>();
		_whereConditions.put(TABLE.COLUMN_PAYOUTID + "=?", String.valueOf(payoutModel.payoutID));
			
		return delete(TABLE.TABLE_NAME, _whereConditions);
	}
	
	/** 改 **/
	public boolean updatePayout(PayoutModel payoutModel){
		ContentValues _contentValues = createContentValuesFromModel(payoutModel);
		
		//此处为被更新的了多少条记录
		int _rowId = getSQLiteDatabase().update(TABLE.TABLE_NAME, _contentValues, TABLE.COLUMN_PAYOUTID + "=?", new String[]{String.valueOf(payoutModel.payoutID)});
	
		return _rowId > 0;
	}
	
	/** 查 **/
	public List<PayoutModel> queryPayout(String whereConditions){
		List<PayoutModel> _payoutModels = null;
		
		StringBuilder _sql = new StringBuilder("SELECT * FROM v_Payout");
		if(null != whereConditions && !"".equals(whereConditions)){
			_sql.append(" WHERE ").append(whereConditions);
		}
		
		_payoutModels = queryData(_sql.toString());
		
		return _payoutModels;
	}
	
	private ContentValues createContentValuesFromModel(PayoutModel payoutModel){
		ContentValues _contentValues = new ContentValues();
		_contentValues.put(TABLE.COLUMN_ACCOUNTBOOKID, payoutModel.accountBookID);
		_contentValues.put(TABLE.COLUMN_AMOUNT, String.valueOf(payoutModel.amount));
		_contentValues.put(TABLE.COLUMN_CATEGORYID, payoutModel.categoryID);
		_contentValues.put(TABLE.COLUMN_PAYOUTDATE, DateTools.getFormatDateTime(payoutModel.payoutDate, "yyyy-MM-dd"));
		_contentValues.put(TABLE.COLUMN_PAYOUTTYPE, payoutModel.payoutType);
		_contentValues.put(TABLE.COLUMN_PAYOUTUSERID, payoutModel.payoutUserID);
		_contentValues.put(TABLE.COLUMN_COMMENT, payoutModel.comment);
		_contentValues.put(TABLE.COLUMN_CREATEDATE,DateTools.getFormatDateTime(payoutModel.createDate, DateTools.DEFAULT_DATETIME_FORMAT));
		_contentValues.put(TABLE.COLUMN_STATE, payoutModel.state);
		
		return _contentValues;
	}

	/** 从视图中构建 **/
	@Override
	protected Object createModelByCursor(Cursor cursor) {
		PayoutModel _payoutModel = new PayoutModel();
		_payoutModel.payoutID = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_PAYOUTID));
		_payoutModel.accountBookID = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_ACCOUNTBOOKID));
		_payoutModel.amount = new BigDecimal(cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_AMOUNT)));
		_payoutModel.categoryID = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_CATEGORYID));
		_payoutModel.payoutDate = DateTools.getDate(cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_PAYOUTDATE)), "yyyy-MM-dd");	
		_payoutModel.payoutType = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_PAYOUTTYPE));
		_payoutModel.payoutUserID = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_PAYOUTUSERID));
		_payoutModel.comment = cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_COMMENT));
		_payoutModel.createDate = DateTools.getDate(cursor.getString(cursor.getColumnIndex(TABLE.COLUMN_CREATEDATE)), "yyyy-MM-dd HH:mm:ss");	
		_payoutModel.state = cursor.getInt(cursor.getColumnIndex(TABLE.COLUMN_STATE));
		
		_payoutModel.accountBookName = cursor.getString(cursor.getColumnIndex("AccountBookName"));
		_payoutModel.categoryName = cursor.getString(cursor.getColumnIndex("CategoryName"));
		_payoutModel.path = cursor.getString(cursor.getColumnIndex("Path"));
		
		return _payoutModel;
	}
	
	public Cursor rawQuery(String sql,String[] selectionArgs){
		return getSQLiteDatabase().rawQuery(sql, selectionArgs);
	}

}
