package cilinet.godutch.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 消费记录 **/
public class PayoutModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// 支出表主键ID
	public int payoutID;
	// 账本ID外键
	public int accountBookID;
	// 账本名称
	public String accountBookName;
	// 支出类别ID外键
	public int categoryID;
	// 类别名称
	public String categoryName;
	// 路径
	public String path;
	// 消费金额
	public BigDecimal amount;
	// 消费日期
	public Date payoutDate;
	// 计算方式
	public String payoutType;
	// 消费人ID外键
	public String payoutUserID;
	// 备注
	public String comment;
	// 添加日期
	public Date createDate = new Date();
	// 状态 0失效 1启用
	public int state = 1;
}
