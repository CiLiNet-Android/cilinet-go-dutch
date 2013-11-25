package cilinet.godutch.activity.base;

import android.app.Activity;
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

}
