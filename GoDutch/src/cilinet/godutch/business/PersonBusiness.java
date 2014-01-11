package cilinet.godutch.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import cilinet.godutch.business.base.BaseBusiness;
import cilinet.godutch.database.dal.PersonDAL;
import cilinet.godutch.database.dal.PersonDAL.TABLE;
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
		return mPersonDAL.insertPerson(personModel);
	}
	
	/** 更新人员 **/
	public boolean updatePerson(PersonModel personModel){
		return mPersonDAL.updatePerson(personModel);
	}
	
	/** 逻辑删除人员 **/
	public boolean deletePersonByPersonId(int personId){
		return mPersonDAL.disablePersonByPersonId(personId);
	}
	
	/** 获得人员数 **/
	public int getPersonsCount(){
		return mPersonDAL.getCount();
	} 
	
	/** 获得正常状态的人员 **/
	public List<PersonModel> queryAvailablePerson(){
		String _condition = "AND " + PersonDAL.TABLE.COLUMN_STATE + "=1";
		return queryPerson(_condition);
	}
	
	/** 把通用方法写成了私有 **/
	private List<PersonModel> queryPerson(String condition){
		return mPersonDAL.queryPerson(condition);
	}
	
	/** 查看用户是否已存在 **/
	public boolean checkPersonNameIfExists(PersonModel personModel){
		Map<String,String> _whereConditions = new HashMap<String, String>();
		
		_whereConditions.put(PersonDAL.TABLE.COLUMN_PERSONID + "<>?", String.valueOf(personModel.personId));
		_whereConditions.put(PersonDAL.TABLE.COLUMN_PERSONNAME + "=?", personModel.personName);
		
		return mPersonDAL.getCount(_whereConditions) > 0;
	}
	
	/** 根据多个personId，获取用户名 ,用,分割**/
	public String queryPersonNamesByPersonIds(String personIds){
		String _whereConditions = "AND " + TABLE.COLUMN_PERSONID + " IN (" + personIds + ")";
		
		List<PersonModel> _personModels = queryPerson(_whereConditions);
		
		StringBuilder _personNames = new StringBuilder();
		for(PersonModel _personModel : _personModels){
			_personNames.append(_personModel.personName).append(",");
		}
		_personNames.deleteCharAt(_personNames.lastIndexOf(","));
		
		return _personNames.toString();
	}

}
