package cilinet.godutch.database.base;

import java.util.ArrayList;
import java.util.List;

import cilinet.godutch.R;

import android.content.Context;

/** 数据库级别的配置信息 
 * 
 * 在构造方面，使用了单例模式
 * **/
public class SQLiteDatebaseConfig {
	/** 数据库名称 **/
	public static final String DATABASE_NAME = "GoDutchDataBase";
	/** 数据库版本号 **/
	public static final int DATABASE_VERSION = 1;
	
	private static SQLiteDatebaseConfig mSqLiteDateBaseConfig;
	
	/** 因为要在该类中从资源文件中获取各个数据表的名称，所以需要一个Context **/
	private Context mContext;
	
	private SQLiteDatebaseConfig(Context context){
		mContext = context;
	}
	
	public static SQLiteDatebaseConfig getInstance(Context context){
		if(null == mSqLiteDateBaseConfig){
			mSqLiteDateBaseConfig = new SQLiteDatebaseConfig(context);
		}
		return mSqLiteDateBaseConfig;
	}
	
	/** 
	 * 数据库中有哪些表需要创建
	 * 从资源文件中获得需要创建数据表的类名，然后将来通过反射来调用这些类的创建表的方法 **/
	public List<String> getSqliteDALClassName(){
		List<String> _sqliteDALClassNames = new ArrayList<String>();
		
		String packageName = mContext.getPackageName();
		String[] _sqliteDALSimpleClassNames = mContext.getResources().getStringArray(R.array.SQLiteDALClassName);
		for(String _sqliteDALSimpleClassName : _sqliteDALSimpleClassNames){
			_sqliteDALClassNames.add(packageName + ".database.base." + _sqliteDALSimpleClassName);
		}
		
		return _sqliteDALClassNames;
	}
	
	

}
