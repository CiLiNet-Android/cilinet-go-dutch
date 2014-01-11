package cilinet.godutch.business;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.content.Context;
import cilinet.godutch.R;
import cilinet.godutch.business.base.BaseBusiness;
import cilinet.godutch.model.PayoutModel;
import cilinet.godutch.model.PayoutStatisticsModel;

import jxl.write.Number;

/** 消费统计管理 **/
public class PayoutStatisticsBusiness extends BaseBusiness {
	
	private PayoutBusiness mPayoutBusiness;
	
	private PersonBusiness mPersonBusiness;
	
	private AccountBookBusiness mAccountBookBusiness;

	public PayoutStatisticsBusiness(Context context) {
		super(context);
		
		mPayoutBusiness = new PayoutBusiness(context);
		mPersonBusiness = new PersonBusiness(context);
		mAccountBookBusiness = new AccountBookBusiness(context);
	}
	
	public String queryPayoutUserIdsByAccountBookId(int accountBookId){
		StringBuilder _result = new StringBuilder();
		List<PayoutStatisticsModel> _totalPayoutStatisticsModels = queryFormatedPayoutStatisticsModelsByAccountBookId(accountBookId);
		
		//将得到的信息进行转换，以方便观看
		for (int i = 0; i < _totalPayoutStatisticsModels.size(); i++) {
			PayoutStatisticsModel _payoutStatisticsModel = _totalPayoutStatisticsModels.get(i);
			if(_payoutStatisticsModel.payoutType.equals("个人")) {
				_result.append(_payoutStatisticsModel.payoutPayerId).append("个人消费 ").append(_payoutStatisticsModel.cost.toString()).append("元\r\n");
			} else if(_payoutStatisticsModel.payoutType.equals("均分")) {
				if(_payoutStatisticsModel.payoutPayerId.equals(_payoutStatisticsModel.payoutConsumerId)) {
					_result.append(_payoutStatisticsModel.payoutPayerId).append("个人消费 ").append(_payoutStatisticsModel.cost.toString()).append("元\r\n");
				} else {
					_result.append(_payoutStatisticsModel.payoutConsumerId).append("应支付给").append(_payoutStatisticsModel.payoutPayerId).append(_payoutStatisticsModel.cost.toString()).append("元\r\n");
				}
			}
			//_Result += _ModelStatistics.PayerUserID + "#" + _ModelStatistics.ConsumerUserID + "#" + _ModelStatistics.Cost + "(" + _ModelStatistics.getPayoutType() + ")" + "\r\n";
		}
		
		return _result.toString();
	}
	
	private List<PayoutStatisticsModel> queryFormatedPayoutStatisticsModelsByAccountBookId(
			int accountBookModelId) {
		// 得到拆分好的统计信息
		List<PayoutStatisticsModel> _payoutStatisticsModels = queryPayoutStatisticsModelsByAccountBookId(accountBookModelId);
		// 存放按付款人分类的临时统计信息
		List<PayoutStatisticsModel> _tempPayoutStatisticsModels = new ArrayList<PayoutStatisticsModel>();
		// 存放统计好的汇总
		List<PayoutStatisticsModel> _totalPayoutStatisticsModels = new ArrayList<PayoutStatisticsModel>();
		StringBuilder _result = new StringBuilder();
		// 遍历拆分好的统计信息
		for (int i = 0; i < _payoutStatisticsModels.size(); i++) {
			// 得到拆分好的一条信息
			PayoutStatisticsModel _payoutStatisticsModel = _payoutStatisticsModels.get(i);
			_result.append(_payoutStatisticsModel.payoutPayerId).append("#");
			_result.append(_payoutStatisticsModel.payoutConsumerId).append("#");
			_result.append(_payoutStatisticsModel.cost).append("\r\n");
	
			// 保存当前的付款人ID
			String _currentPayerUserID = _payoutStatisticsModel.payoutPayerId;

			// 把当前信息按付款人分类的临时数组
			PayoutStatisticsModel _tempPayoutStatisticsModel = new PayoutStatisticsModel();
			_tempPayoutStatisticsModel.payoutPayerId = _payoutStatisticsModel.payoutPayerId;
			_tempPayoutStatisticsModel.payoutConsumerId = _payoutStatisticsModel.payoutConsumerId;
			_tempPayoutStatisticsModel.cost = _payoutStatisticsModel.cost;
			_tempPayoutStatisticsModel.payoutType = _payoutStatisticsModel.payoutType;
			
			_tempPayoutStatisticsModels.add(_tempPayoutStatisticsModel);

			// 计算下一行的索引
			int _nextIndex;
			// 如果下一行索引小于统计信息索引，则可以加1
			if ((i + 1) < _payoutStatisticsModels.size()) {
				_nextIndex = i + 1;
			} else {
				// 否则证明已经到尾，则索引赋值为当前行
				_nextIndex = i;
			}

			// 如果当前付款人与下一位付款人不同，则证明分类统计已经到尾，或者已经循环到统计数组最后一位，则就开始进入进行统计
			if (!_currentPayerUserID.equals(_payoutStatisticsModels
					.get(_nextIndex).payoutPayerId) || _nextIndex == i) {

				// 开始进行遍历进行当前分类统计数组的统计
				for (int j = 0; j < _tempPayoutStatisticsModels.size(); j++) {
					// 取出来一个
					PayoutStatisticsModel _totalPayoutStatisticsModel = _tempPayoutStatisticsModels
							.get(j);
					// 判断在总统计数组当中是否已经存在该付款人和消费人的信息
					int _index = GetPostionByConsumerUserID(_totalPayoutStatisticsModels,_totalPayoutStatisticsModel.payoutPayerId,_totalPayoutStatisticsModel.payoutConsumerId);
					// 如果已经存在，则开始在原来的数据上进行累加
					if (_index != -1) {
						_totalPayoutStatisticsModels.get(_index).cost = _totalPayoutStatisticsModels
								.get(_index).cost
								.add(_totalPayoutStatisticsModel.cost);
					} else {
						// 否则就是一条新信息，添加到统计数组当中
						_totalPayoutStatisticsModels.add(_totalPayoutStatisticsModel);
					}
				}
				// 全部遍历完后清空当前分类统计数组，进入下一个分类统计数组的计算，直到尾
				_tempPayoutStatisticsModels.clear();
			}

		}

		return _totalPayoutStatisticsModels;
	}
	
	private int GetPostionByConsumerUserID(List<PayoutStatisticsModel> totalPayoutStatisticsModel,String payerId,String consumerId){
		int _index = -1;
		for (int i = 0; i < totalPayoutStatisticsModel.size(); i++) {
			if (totalPayoutStatisticsModel.get(i).payoutPayerId.equals(payerId) && totalPayoutStatisticsModel.get(i).payoutConsumerId.equals(consumerId)) {
				_index = i;
			}
		}
		
		return _index;
	}
	
	/** 根据whereConditions从Payout表中查询数据，再进行相关处理 **/
	private List<PayoutStatisticsModel> queryPayoutStatisticsModelsByAccountBookId(int accountBookModelId){
		//按支付人ID排序取出消费记录
		List<PayoutModel> _payoutModels = mPayoutBusiness.queryOrderedAvailablePayoutByAccountBookId(accountBookModelId);
		
		//获取计算方式数组
		String[] _payoutTypes = getContext().getResources().getStringArray(R.array.PayoutType);
		
		List<PayoutStatisticsModel> _payoutStatisticsModels = new ArrayList<PayoutStatisticsModel>();
		
		for(PayoutModel _payoutModel : _payoutModels){
			//将消费人ID转换为真实名称
			String _payoutUserNames[] = mPersonBusiness.queryPersonNamesByPersonIds(_payoutModel.payoutUserID).split(",");
			String _payoutUserIds[] = _payoutModel.payoutUserID.split(",");
			
			//取出计算方式
			String _payoutType = _payoutModel.payoutType;
			
			//存放计算后的消费金额
			BigDecimal _cost;
			
			//判断本次消费记录的消费类型，如果是均分，则除以本次消费人的个数，算出平均消费金额
			if(_payoutType.equals(_payoutTypes[0])){
				//得到消费人数
				int _payoutTotal = _payoutUserNames.length;
				
				/*金额的数据类型是BigDecimal 
				通过BigDecimal的divide方法进行除法时当不整除，出现无限循环小数时，就会抛异常的，异常如下：java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. at java.math.BigDecimal.divide(Unknown Source) 

				应用场景：一批中供客户的单价是1000元/年，如果按月计算的话1000/12=83.3333333333.... 

				解决之道：就是给divide设置精确的小数点divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN) */
				//得到计算后的平均消费金额
				_cost = _payoutModel.amount.divide(new BigDecimal(_payoutTotal),2, BigDecimal.ROUND_HALF_EVEN);
			}else {//如果是借贷或是个人消费，则直接取出消费金额
				_cost = _payoutModel.amount;
			}
			
			//遍历消费人数组
			for (int j = 0; j < _payoutUserIds.length; j++) {
				
				//如果是借贷则跳过第一个索引，因为第一个人是借贷人自己
				if (_payoutType.equals(_payoutTypes[1]) && j == 0) {
					continue;
				}
				
				//声明一个统计类
				PayoutStatisticsModel _payoutStatisticsModel = new PayoutStatisticsModel();
				//将统计类的支付人设置为消费人数组的第一个人
				_payoutStatisticsModel.payoutPayerId = _payoutUserIds[0];
				//设置消费人
				_payoutStatisticsModel.payoutConsumerId = _payoutUserIds[j];
				//设置消费类型
				_payoutStatisticsModel.payoutType = _payoutType;
				//设置算好的消费金额
				_payoutStatisticsModel.cost = _cost;
				
				_payoutStatisticsModels.add(_payoutStatisticsModel);
			}
		}
		
		return _payoutStatisticsModels;
	}

	
	public String exportPayoutStatistics(int accountBookId) throws Exception{
		StringBuilder _result = new StringBuilder();
		String _accountBookName = mAccountBookBusiness.queryAccountBookNameByAccountBookId(accountBookId);
		Date _date = new Date();
//		String _FileName = _AccountBookName + String.valueOf(_Date.getYear()) + String.valueOf(_Date.getMonth()) + String.valueOf(_Date.getDay()) + ".xls";
		String _fileName = String.valueOf(_date.getYear()) + String.valueOf(_date.getMonth()) + String.valueOf(_date.getDay()) + ".xls";
		File _fileDir = new File("/sdcard/GoDutch/Export/");//文件路径/mnt/sdcard
		if (!_fileDir.exists()) {
			_fileDir.mkdirs();
		}
		File _file = new File("/sdcard/GoDutch/Export/" + _fileName);
		if(!_file.exists()) {
			_file.createNewFile();
		}
		
		WritableWorkbook wBookData;
		//创建工作簿
		wBookData = Workbook.createWorkbook(_file);
		//创建工作表
		WritableSheet wsAccountBook = wBookData.createSheet(_accountBookName, 0);
		
		String[] _Titles = {"编号", "姓名", "金额", "消费信息", "消费类型"};
		Label _Label;
		//添加标题行
		for (int i = 0; i < _Titles.length; i++) {
			_Label = new Label(i, 0, _Titles[i]);
			wsAccountBook.addCell(_Label);
		}
		
		/*
		 * 添加行
		 */
		List<PayoutStatisticsModel> _totalPayoutStatisticsModels =  queryFormatedPayoutStatisticsModelsByAccountBookId(accountBookId);
		
		for(int i = 0;i < _totalPayoutStatisticsModels.size(); i++) {
			PayoutStatisticsModel _payoutStatisticsModel = _totalPayoutStatisticsModels.get(i);
			
			//添加编号列
			Number _idCell = new Number(0, i+1, i+1);
			wsAccountBook.addCell(_idCell);
			
			//添加姓名
			Label _lblName = new Label(1, i+1, _payoutStatisticsModel.payoutPayerId);
			wsAccountBook.addCell(_lblName);
			
			//格式化金额类型显示
			NumberFormat nfMoney = new NumberFormat("#.##");
			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);
			
			//添加金额
			Number _CostCell = new Number(2, i+1, _payoutStatisticsModel.cost.doubleValue(), wcfFormat);
			wsAccountBook.addCell(_CostCell);
			
			//添加消费信息
			String _info = "";
			if(_payoutStatisticsModel.payoutType.equals("个人")) {
				_info = _payoutStatisticsModel.payoutPayerId + "个人消费 " + _payoutStatisticsModel.cost.toString() + "元";
			} else if(_payoutStatisticsModel.payoutType.equals("均分")) {
				if(_payoutStatisticsModel.payoutPayerId.equals(_payoutStatisticsModel.payoutConsumerId)) {
					_info = _payoutStatisticsModel.payoutPayerId + "个人消费 " + _payoutStatisticsModel.cost.toString() + "元";
				} else {
					_info = _payoutStatisticsModel.payoutConsumerId + "应支付给" + _payoutStatisticsModel.payoutPayerId + _payoutStatisticsModel.cost + "元";
				}
			} 
			Label _lblInfo = new Label(3, i+1, _info);
			wsAccountBook.addCell(_lblInfo);
			
			//添加消费类型
			Label _lblPayoutType = new Label(4, i+1, _payoutStatisticsModel.payoutType);
			wsAccountBook.addCell(_lblPayoutType);
		}
		
		wBookData.write();
		wBookData.close();
		_result.append("数据已经导出！位置在：/sdcard/GoDutch/Export/").append(_fileName);
		
		return _result.toString();
	}

}
