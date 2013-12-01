package cilinet.godutch.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseOpenHelper extends SQLiteOpenHelper {
	
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

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

	}

}
