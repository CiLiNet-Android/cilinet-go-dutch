package cilinet.godutch.database.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** 封装所有DAL都要用到的方法 **/
@SuppressWarnings("unchecked")
public abstract class BaseDAL implements SQLiteDatabaseOpenHelper.SQLiteTableOpenHelper{
	
	private Context mContext;
	
	private SQLiteDatabase mSQLiteDatabase;
	
	public BaseDAL(Context context){
		mContext = context;
	}
	
	/** 事务开始 **/
	public void beginTransaction(){
		getSQLiteDatabase().beginTransaction();
	}
	
	/** 事务成功设置 **/
	public void setTransactionSuccessful(){
		getSQLiteDatabase().setTransactionSuccessful();
	}
	
	/** 事务结束 **/
	public void endTransaction(){
		getSQLiteDatabase().endTransaction();
	}
	
	/** 获取表名和主键 **/
	protected abstract String[] getTableNameAndPK();
	
	public int getCount(Map<String,String> whereConditions){
		return getCount(getTableNameAndPK()[0], getTableNameAndPK()[1], whereConditions);
	}
	
	public int getCount(){
		return getCount(getTableNameAndPK()[0], getTableNameAndPK()[1], null);
	}
	
	public int getCount(String tableName,String PK,Map<String,String> whereConditions){
		StringBuilder _sql = new StringBuilder("SELECT " + PK + " FROM " + tableName + " WHERE 1=1");
		
		Cursor _cursor = null;
		if(null != whereConditions && whereConditions.size() > 0){
			String[] _conditionValues = new String[whereConditions.size()];
			int _index = 0;
			for(Map.Entry<String, String> entry : whereConditions.entrySet()){
				_sql.append(" AND ").append(entry.getKey());//格式：a=?
				
				_conditionValues[_index] = entry.getValue();
				_index ++;
			}
			_cursor = rawQuery(_sql.toString(), _conditionValues);
		}else {
			_cursor = rawQuery(_sql.toString(), null);
		}
	
		return getCountFromCursor(_cursor);
	}
	
	/** 另一类封装方法：删除 **/
	public boolean delete(String tableName,Map<String,String> whereConditions){
		StringBuilder _whereClause = new StringBuilder("1=1");
		String[] _whereArgs = null;
		
		if(null != whereConditions && whereConditions.size() > 0){
			_whereArgs = new String[whereConditions.size()];
			int _index = 0;
			for(Map.Entry<String, String> entry : whereConditions.entrySet()){
				_whereClause.append(" AND ").append(entry.getKey());
				
				_whereArgs[_index] = entry.getValue();
				_index ++;
			}
		}
		
		return getSQLiteDatabase().delete(tableName, _whereClause.toString(), _whereArgs) >= 0;
	}
	
	private int getCountFromCursor(Cursor cursor){
		int _count = cursor.getCount();
		cursor.close();
		
		return _count;
	}
	
	protected void execSql(String sql,Object[] bindArgs){
		if(null == bindArgs){
			getSQLiteDatabase().execSQL(sql);
		}else {
			getSQLiteDatabase().execSQL(sql, bindArgs);
		}
		
	}
	
	/** 通过SQL语句获取表中的记录 **/
	protected List queryData(String sql){
		Cursor _cursor = rawQuery(sql, null);
		return cursorToList(_cursor);
	}
	
	protected abstract Object createModelByCursor(Cursor cursor);
	
	protected List cursorToList(Cursor cursor){
		List _list = new ArrayList();
		while(cursor.moveToNext()){
			_list.add(createModelByCursor(cursor));
		}
		cursor.close();
		return _list;
	}
	
	protected Cursor rawQuery(String sql,String[] selectionArgs){
		return getSQLiteDatabase().rawQuery(sql, selectionArgs);
	}
	
	/** 获取数据库操作类 **/
	protected SQLiteDatabase getSQLiteDatabase(){
		if(null == mSQLiteDatabase){
			mSQLiteDatabase = SQLiteDatabaseOpenHelper.getInstance(mContext).getWritableDatabase();
		}
		return mSQLiteDatabase;
	}
	
	/** 获取上下文 **/
	public Context getContext(){
		return mContext;
	}
	

}
