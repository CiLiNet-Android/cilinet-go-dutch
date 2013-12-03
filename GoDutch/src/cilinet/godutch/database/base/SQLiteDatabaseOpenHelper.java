package cilinet.godutch.database.base;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cilinet.godutch.utility.Reflection;

public class SQLiteDatabaseOpenHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "SQLiteDatabaseOpenHelper";
	
	private Context mContext;
	
	private static SQLiteDatabaseOpenHelper mSQLiteDatabaseOpenHelper;

	private SQLiteDatabaseOpenHelper(Context context) {
		super(context, SQLiteDatebaseConfig.DATABASE_NAME, null, SQLiteDatebaseConfig.DATABASE_VERSION);
		mContext = context;
	}
	
	public static SQLiteDatabaseOpenHelper getInstance(Context context){
		if(null == mSQLiteDatabaseOpenHelper){
			mSQLiteDatabaseOpenHelper = new SQLiteDatabaseOpenHelper(context);
		}
		return mSQLiteDatabaseOpenHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		createTables(sqLiteDatabase);
	}
	
	/** 通过反射创建表格 **/
	private void createTables(SQLiteDatabase sqLiteDatabase){
		List<String> _sqliteDALClassNames = SQLiteDatebaseConfig.getInstance(mContext).getSqliteDALClassNames();
		
		Reflection _reReflection = new Reflection();
		
		for(String _sqliteDALClassName : _sqliteDALClassNames){
			Log.i(TAG, "sqliteDALClassName: " + _sqliteDALClassName);
			
			try {
				SQLiteTableOpenHelper _sqliteTableOpenHelper = (SQLiteTableOpenHelper)_reReflection.newInstance(_sqliteDALClassName, new Object[]{mContext}, new Class[]{Context.class});
				_sqliteTableOpenHelper.onCreate(sqLiteDatabase);
			} catch (Exception e){
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

	}
	
	public interface SQLiteTableOpenHelper{
		/** 表的创建 **/
		public void onCreate(SQLiteDatabase sqLiteDatabase);

		/** 表的更新 **/
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion);
	}

}
