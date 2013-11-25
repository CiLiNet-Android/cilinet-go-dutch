package cilinet.godutch.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


/** 为所有的Activity添加常用的方法 **/
public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void showToastMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	/** 添加一个启动Activity的方法 **/
	protected void startActivity(Class<Activity> activityClass){
		Intent intent = new Intent(this,activityClass);
		super.startActivity(intent);
	}

}
