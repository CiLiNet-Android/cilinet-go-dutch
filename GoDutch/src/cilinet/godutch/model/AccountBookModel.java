package cilinet.godutch.model;

import java.util.Date;

/** 账本 **/
public class AccountBookModel {
	
	/** 账本主键 **/
	public int id;
	/** 账本名称 **/
	public String name;
	/** 创建日期 **/
	public Date createDate;
	/** 状态 0失效 1启用 **/
	public int state = 1;
	/** 是否默账本 0否1是 **/
	public int isDefault = 1;
	
	public AccountBookModel(){}
	
	public AccountBookModel(int id,String name,Date createDate,int state,int isDefault){
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.state = state;
		this.isDefault = isDefault;
	}
	
	public AccountBookModel(String name,int isDefault){
		this(0,name,new Date(),1,isDefault);//构造方法中调用构造方法
	}
	
	public String toString(){
		return "AccountBookModel{id: "+ id + ",name: " + name + ",state: " + state + "}";
	}
	
}
