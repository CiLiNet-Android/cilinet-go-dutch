package cilinet.godutch.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;
import cilinet.godutch.model.PayoutModel;

public class PayoutBusinessTestCase extends AndroidTestCase {
	private static final String TAG = "PayoutBusinessTestCase";

	public void testInsertPayout(){
		PayoutBusiness _payoutBusiness = new PayoutBusiness(getContext());
		
		PayoutModel _payoutModel = new PayoutModel();
		_payoutModel.accountBookID = 1;
		_payoutModel.amount = new BigDecimal(12.3);
		_payoutModel.categoryID = 3;
		_payoutModel.payoutDate = new Date();
		_payoutModel.payoutType = "均分";
		_payoutModel.payoutUserID = "1,2";
		_payoutModel.comment = "我自己加的相关备注3";
		
		boolean _isSuccessed = _payoutBusiness.insertPayout(_payoutModel);
		
		assertEquals(true, _isSuccessed);
	}
	
	public void testQueryAvailablePayoutByAccountBookId(){
		PayoutBusiness _payoutBusiness = new PayoutBusiness(getContext());
		List<PayoutModel> _payoutModels = _payoutBusiness.queryAvailablePayoutByAccountBookId(1);
		System.out.println(_payoutModels);
	}
	
	public void testQueryPayoutStatisticsByDateAndAccountBookId(){
		PayoutBusiness _payoutBusiness = new PayoutBusiness(getContext());
		String[] _payoutStatics = _payoutBusiness.queryPayoutStatisticsByDateAndAccountBookId(new Date(), 1);
		Log.i(TAG, "总笔数：" + _payoutStatics[0] + " && 总金额：" + _payoutStatics[1]);
	}

}
