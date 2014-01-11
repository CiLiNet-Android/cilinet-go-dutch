package cilinet.godutch.activity.base;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cilinet.godutch.R;


/** 为所有的Activity添加常用的方法 **/
public class BaseActivity extends Activity {
	
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void showToastMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	protected void showProgressDialog(String dialogTitle,String dialogMessage){
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(dialogTitle);
		mProgressDialog.setMessage(dialogMessage);
		mProgressDialog.show();
	}
	
	protected void dismissProgressDialog(){
		if(null != mProgressDialog){
			mProgressDialog.dismiss();
		}
	}
	
	/** 添加一个启动Activity的方法 **/
	protected void startActivity(Class<?> activityClass){
		Intent intent = new Intent(this,activityClass);
		super.startActivity(intent);
	}
	
	protected void setAlertDialogClosable(DialogInterface alertDialog,Boolean isClosable){
		try {
			Field _Field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			_Field.setAccessible(true);//设置为true后，可以直接访问私有成员
		    _Field.set(alertDialog, isClosable);
		} catch (Exception e) {
		}
	} 
	
	/** 创建一般的alertDialog **/
	protected AlertDialog showAlertDialog(String title,String message,DialogInterface.OnClickListener neutralButtonOnClickListener){
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		return _alertDialogBuilder.setTitle(title)
				   				  .setMessage(message)
				                  .setNeutralButton(R.string.ButtonTextYes, neutralButtonOnClickListener)
				                  .setNegativeButton(R.string.ButtonTextNo, null)
				                  .show();
	}

}
