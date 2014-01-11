package cilinet.godutch.database.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cilinet.godutch.database.base.SQLiteDatabaseOpenHelper;

public class CreatePayoutViewDAL implements SQLiteDatabaseOpenHelper.SQLiteTableOpenHelper {
	private static final String TAG = "CreatePayoutViewDAL";

	private Context mContext;
	
	public CreatePayoutViewDAL(Context context) {//反射机制的限制
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		StringBuilder s_CreateTableScript = new StringBuilder();
		
		s_CreateTableScript.append("		Create View v_Payout As ");
		s_CreateTableScript.append("		select a.*,b.ParentID,b.categoryname,b.Path,b.TypeFlag,c.name AS AccountBookName from payout a LEFT JOIN category b ON a.categoryID = b.categoryID  LEFT JOIN accountbook c ON a.AccountBookID = c.id");
		
		Log.i(TAG, s_CreateTableScript.toString());
		
		sqLiteDatabase.execSQL(s_CreateTableScript.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {

	}
}
