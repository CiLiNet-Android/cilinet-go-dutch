package cilinet.godutch.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/** 封装所有DAL都要用到的方法 **/
public class BaseDAL {
	
	private Context mContext;
	
	private SQLiteDatabase mSQLiteDatabase;
	
	public BaseDAL(Context context){
		mContext = context;
		mSQLiteDatabase = SQLiteDatabaseOpenHelper.getInstance(mContext).getWritableDatabase();
	}
	
	
	
	/** 事务开始 **/
	public void beginTransaction(){
		mSQLiteDatabase.beginTransaction();
	}
	
	/** 事务成功设置 **/
	public void setTransactionSuccessful(){
		mSQLiteDatabase.setTransactionSuccessful();
	}
	
	/** 事务结束 **/
	public void endTransaction(){
		mSQLiteDatabase.endTransaction();
	}
	
	/** 获取数据库操作类 **/
	public SQLiteDatabase getSQLiteDatabase(){
		return mSQLiteDatabase;
	}
	
	/** 获取上下文 **/
	public Context getContext(){
		return mContext;
	}

}
