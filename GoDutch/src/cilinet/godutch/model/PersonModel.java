package cilinet.godutch.model;

import java.util.Date;

public class PersonModel {
	/** 用户表主键ID **/
	public int personId;
	/** 用户名称 **/
	public String personName;
	/** 添加日期 **/
	public Date createDate = new Date();
	/** 人员状态 :0表示停用，1表示正常**/
	public int state = 1;
	
	public PersonModel(String personName,Date createDate,int state){
		this.personName = personName;
		this.createDate = createDate;
		this.state = state;
	}
	
	public PersonModel(String personName){
		this.personName = personName;
	}
	
	public PersonModel(int personId,String personName,Date createDate,int state){
		this.personId = personId;
		this.personName = personName;
		this.createDate = createDate;
		this.state = state;
	}

}
