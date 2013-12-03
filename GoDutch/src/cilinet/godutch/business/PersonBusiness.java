package cilinet.godutch.business;

import java.util.List;

import android.content.Context;
import cilinet.godutch.business.base.BaseBusiness;
import cilinet.godutch.database.dal.PersonDAL;
import cilinet.godutch.model.PersonModel;

/** 业务层：人员管理 **/
public class PersonBusiness extends BaseBusiness{
	
	/** 只是创建该类的实例，不能打开数据库。必须等访问的时候，才会打开 **/
	private PersonDAL mPersonDAL;

	public PersonBusiness(Context context) {
		super(context);
		mPersonDAL = new PersonDAL(context);
	}
	
	/** 添加人员 **/
	public boolean insertPerson(PersonModel personModel){
		return mPersonDAL.insertUser(personModel);
	}
	
	/** 获得人员数 **/
	public int getPersonsCount(){
		return mPersonDAL.getCount();
	} 
	
	/** 获得正常状态的人员 **/
	public List<PersonModel> queryNormalStatePerson(){
		String _condition = "AND " + PersonDAL.TABLE.COLUMN_STATE + "=1";
		return queryPerson(_condition);
	}
	
	/** 把通用方法写成了私有 **/
	private List<PersonModel> queryPerson(String condition){
		return mPersonDAL.queryPerson(condition);
	}
	

}
