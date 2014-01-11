package cilinet.godutch.control;

import java.math.BigDecimal;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import cilinet.godutch.R;

/** 构建一个计算器Dialog界面 **/
public class NumberDialog extends Dialog implements View.OnClickListener {

	private OnNumberDialogListener mOnNumberDialogListener;
	
	public NumberDialog(Context context) {
		super(context);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.number_dialog);
		
		if(context instanceof OnNumberDialogListener){
			mOnNumberDialogListener = (OnNumberDialogListener)context;
		}
		
		init();
	}

	private void init() {
		initView();
	}

	private void initView() {
		findViewById(R.id.btnDot).setOnClickListener(this);
		findViewById(R.id.btnZero).setOnClickListener(this);
		findViewById(R.id.btnOne).setOnClickListener(this);
		findViewById(R.id.btnTwo).setOnClickListener(this);
		findViewById(R.id.btnThree).setOnClickListener(this);
		findViewById(R.id.btnFour).setOnClickListener(this);
		findViewById(R.id.btnFive).setOnClickListener(this);
		findViewById(R.id.btnSix).setOnClickListener(this);
		findViewById(R.id.btnSeven).setOnClickListener(this);
		findViewById(R.id.btnEight).setOnClickListener(this);
		findViewById(R.id.btnNine).setOnClickListener(this);
		findViewById(R.id.btnChange).setOnClickListener(this);
		findViewById(R.id.btnOk).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int _viewId = v.getId();

		EditText _editText = (EditText) findViewById(R.id.txtDisplay);
		String _number = _editText.getText().toString();

		switch (_viewId) {
		case R.id.btnDot:
			if (_number.indexOf(".") == -1) {
				_number += ".";
			}
			break;
		case R.id.btnOne:
			_number += "1";
			break;
		case R.id.btnTwo:
			_number += "2";
			break;
		case R.id.btnThree:
			_number += "3";
			break;
		case R.id.btnFour:
			_number += "4";
			break;
		case R.id.btnFive:
			_number += "5";
			break;
		case R.id.btnSix:
			_number += "6";
			break;
		case R.id.btnSeven:
			_number += "7";
			break;
		case R.id.btnEight:
			_number += "8";
			break;
		case R.id.btnNine:
			_number += "9";
			break;
		case R.id.btnZero:
			_number += "0";
			break;
		case R.id.btnChange:
			if (_number.length() != 0) {
				_number = _number.substring(0, _number.length() - 1);
			}
			break;
		case R.id.btnOk:
			BigDecimal _BigDecimal;
			if (!_number.equals(".") && _number.length() != 0) {
				_BigDecimal = new BigDecimal(_number);
			} else {
				_BigDecimal = new BigDecimal(0);
			}
				
			mOnNumberDialogListener.onNumberEnterFinished(_BigDecimal);
			
			dismiss();
			break;
		default:
			break;
		}

		_editText.setText(_number);
	}
	
	/** 选定了金额，通知实现接口的代理 **/
	public interface OnNumberDialogListener {
		public void onNumberEnterFinished(BigDecimal number);
	}
}
