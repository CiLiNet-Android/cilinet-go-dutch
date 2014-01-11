package cilinet.godutch.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BaseApplication extends Application {
	
	/** 应用启动时，获得其高度和宽度 **/
	public static int sScreenHeight;
	public static int sScreenWidth;
	
	/** 上下文 **/
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		
		sContext = super.getBaseContext();
		
		WindowManager _windowManager = (WindowManager)super.getSystemService(Context.WINDOW_SERVICE);
		
		//获得分辨率的例子
		DisplayMetrics _displayMetrics = new DisplayMetrics();
		_windowManager.getDefaultDisplay().getMetrics(_displayMetrics);
		boolean _isPortrait = _displayMetrics.widthPixels < _displayMetrics.heightPixels;//如果小于就是竖屏，width和height绑定了特定的边
		sScreenWidth = _isPortrait? _displayMetrics.widthPixels : _displayMetrics.heightPixels;
		sScreenHeight = _isPortrait? _displayMetrics.heightPixels :  _displayMetrics.widthPixels;
		if(sScreenWidth == 0){
			sScreenHeight = 854;
			sScreenWidth = 480;
		}
		
	}

}
