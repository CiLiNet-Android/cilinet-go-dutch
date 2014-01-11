package cilinet.godutch.business;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import cilinet.godutch.business.base.BaseBusiness;
import cilinet.godutch.database.dal.PayoutDAL;
import cilinet.godutch.model.PayoutModel;
import cilinet.godutch.utility.DateTools;

public class PayoutBusiness extends BaseBusiness {

	private PayoutDAL mPayoutDAL;
	
	public PayoutBusiness(Context context) {
		super(context);
		mPayoutDAL = new PayoutDAL(context);
	}
	
	/** 新建消费 **/
	public boolean insertPayout(PayoutModel payoutModel){
		return mPayoutDAL.insertPayout(payoutModel);
	}
	
	/** 修改消费 **/
	public boolean updatePayout(PayoutModel payoutModel) {
		return mPayoutDAL.updatePayout(payoutModel);
	}
	
	/** 删除消费 **/
	public boolean deletePayout(PayoutModel payoutModel){
		payoutModel.state = 0;
		return mPayoutDAL.updatePayout(payoutModel);
	}

	/** 从视图中获取所有可用的消费记录 **/
	public List<PayoutModel> queryAvailablePayoutByAccountBookId(int accountBookModelId){
		String _whereConditions = "AccountBookID=" + accountBookModelId + " AND State=1 ORDER BY PayoutDate DESC,PayoutID DESC";
		return mPayoutDAL.queryPayout(_whereConditions);
	}
	
	public List<PayoutModel> queryOrderedAvailablePayoutByAccountBookId(int accountBookModelId){
		String _whereConditions = "AccountBookID=" + accountBookModelId + " AND State=1 ORDER BY PayoutUserID";
		return mPayoutDAL.queryPayout(_whereConditions);
	}
	
	/** 根据账本和日期，获取消费统计 String[0]：总笔数      String[1]：总消费数 **/
	public String[] queryPayoutStatisticsByDateAndAccountBookId(Date payoutDate,int accountBookId){
		String _whereConditions = "PayoutDate='" + DateTools.getFormatDateTime(payoutDate, "yyyy-MM-dd") + "' AND AccountBookID=" + accountBookId + " AND State=1";
		return queryPayoutStatics(_whereConditions);
	}
	
	/** 获取消费统计 String[0]：总笔数      String[1]：总消费数 **/
	private String[] queryPayoutStatics(String whereConditions){
		StringBuilder _sql = new StringBuilder("SELECT IFNULL(SUM(Amount),0) AS SumAmount,COUNT(Amount) AS Count FROM Payout");
		if(null != whereConditions && !"".equals(whereConditions)){
			_sql.append(" WHERE ").append(whereConditions);
		}
		
		String[] _payoutStatics = new String[2];
		Cursor _cursor = mPayoutDAL.rawQuery(_sql.toString(), null);
		while(_cursor.moveToNext()){
			_payoutStatics[0] = _cursor.getString(_cursor.getColumnIndex("Count"));
			_payoutStatics[1] = _cursor.getString(_cursor.getColumnIndex("SumAmount"));
		}
		
		return _payoutStatics;
	}

	
}
