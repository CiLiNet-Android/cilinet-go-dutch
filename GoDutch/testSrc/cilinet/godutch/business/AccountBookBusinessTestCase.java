package cilinet.godutch.business;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;
import cilinet.godutch.model.AccountBookModel;

public class AccountBookBusinessTestCase extends AndroidTestCase{
	
	private static final String TAG = "AccountBookBusinessTestCase";

	public void testGetAvailableAccountBook(){
		AccountBookBusiness _accountBookBusiness = new AccountBookBusiness(getContext());
		List<AccountBookModel> _accountBooks = _accountBookBusiness.getAvailableAccountBook();
		for(AccountBookModel _accountBook : _accountBooks){
			Log.i(TAG, _accountBook.toString());
		}
	}
	
	public void testQueryDefaultAccountBook(){
		AccountBookBusiness _accountBookBusiness = new AccountBookBusiness(getContext());
		AccountBookModel _accountBookModel = _accountBookBusiness.queryDefaultAccountBook();
		Log.i(TAG, _accountBookModel.id + ": " + _accountBookModel.name);
	}
}
