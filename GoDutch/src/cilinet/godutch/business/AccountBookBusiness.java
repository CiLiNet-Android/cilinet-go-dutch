package cilinet.godutch.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import cilinet.godutch.business.base.BaseBusiness;
import cilinet.godutch.database.dal.AccountBookDAL;
import cilinet.godutch.database.dal.AccountBookDAL.TABLE;
import cilinet.godutch.model.AccountBookModel;

/** 账本业务类 **/
public class AccountBookBusiness extends BaseBusiness {

	private AccountBookDAL mAccountBookDAL;

	public AccountBookBusiness(Context context) {
		super(context);
		mAccountBookDAL = new AccountBookDAL(context);
	}

	/** 获取可用账本 **/
	public List<AccountBookModel> getAvailableAccountBook() {
		String _whereCondition = TABLE.COLUMN_STATE + "=1";
		return mAccountBookDAL
				.queryAccountBookByWhereCondition(_whereCondition);
	}

	/** 检查账本名称是否已经存在 **/
	public boolean checkAccountBookNameIfExists(
			AccountBookModel accountBookModel) {
		Map<String, String> _whereConditions = new HashMap<String, String>();

		_whereConditions.put(AccountBookDAL.TABLE.COLUMN_ID + "<>?",
				String.valueOf(accountBookModel.id));
		_whereConditions.put(AccountBookDAL.TABLE.COLUMN_NAME + "=?",
				accountBookModel.name);

		return mAccountBookDAL.getCount(_whereConditions) > 0;
	}
	
	/** 获得默认账本 **/
	public AccountBookModel queryDefaultAccountBook(){
		List<AccountBookModel> _accountBookModels = mAccountBookDAL.queryAccountBookByWhereCondition(TABLE.COLUMN_ISDEFAULT + "=1");
		return _accountBookModels.get(0);
	}
	
	public String queryAccountBookNameByAccountBookId(int accountBookId){
		String _whereConditions =  TABLE.COLUMN_ID + "=" + accountBookId + " AND " + TABLE.COLUMN_STATE + "=1";
		List<AccountBookModel> _accountBookModels = mAccountBookDAL.queryAccountBookByWhereCondition(_whereConditions);
		return (_accountBookModels.size() > 0)? _accountBookModels.get(0).name : null;
	}

	public boolean updateAccountBook(AccountBookModel accountBookModel) {
		if(accountBookModel.isDefault == 1){
			mAccountBookDAL.beginTransaction();
			try{
				cancelDefaultAccountBook();
				boolean _result = mAccountBookDAL.updateAccountBook(accountBookModel);
				if(!_result){
					return false;
				}else {
					mAccountBookDAL.setTransactionSuccessful();
					return true;
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}finally{
				mAccountBookDAL.endTransaction();
			}
		}else {
			return mAccountBookDAL.updateAccountBook(accountBookModel);
		}
		
	}

	public boolean insertAccountBook(AccountBookModel accountBookModel) {
		if(accountBookModel.isDefault == 1){//设为默认
			mAccountBookDAL.beginTransaction();
			try{
				cancelDefaultAccountBook();
				boolean _result = mAccountBookDAL.insertAccountBook(accountBookModel);
				if(!_result){
					return false;
				}else {
					mAccountBookDAL.setTransactionSuccessful();
					return true;
				}

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}finally{//finally块在方法返回后，依然会被执行
				mAccountBookDAL.endTransaction();
			}	
		}else {//不设置为默认
			return mAccountBookDAL.insertAccountBook(accountBookModel);
		}
	}
	
	/** 将账本撤销默认 **/
	private void cancelDefaultAccountBook(){
		String _sql = "UPDATE " + TABLE.TABLE_NAME + " SET " + TABLE.COLUMN_ISDEFAULT + "=0 WHERE " + TABLE.COLUMN_ISDEFAULT + "=?";
		mAccountBookDAL.execSql(_sql, new Object[]{1});
	}

}
