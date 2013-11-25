package cilinet.godutch.activity.base;

import mobidever.godutch.R;
import android.os.Bundle;
import android.view.Window;


/** 显示的基本框架 **/
public class FrameActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.setContentView(R.layout.activity_main);
		
	}

}
