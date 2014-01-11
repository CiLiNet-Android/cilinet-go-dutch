package cilinet.godutch.business;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;
import cilinet.godutch.model.PersonModel;

/** 单元测试方法 **/
public class PersonBusinessTestCase extends AndroidTestCase {
	
	private static final String TAG = "PersonBusinessTestCase";
	
	public void testInit(){
		PersonBusiness _personBusiness = new PersonBusiness(this.getContext());
	}
	
	public void testGetPersonsCount(){
		PersonBusiness _personBusiness = new PersonBusiness(this.getContext());
		Log.i(TAG, String.valueOf(_personBusiness.getPersonsCount()));
	}
	
	public void testQueryNormalStatePerson(){
		PersonBusiness _personBusiness = new PersonBusiness(this.getContext());
		List<PersonModel> _personModels = _personBusiness.queryAvailablePerson();
		for(PersonModel _personModel : _personModels){
			Log.i(TAG, _personModel.personId + ": " + _personModel.personName);
		}
	}
	
	public void testInsertPerson(){
		PersonBusiness _personBusiness = new PersonBusiness(this.getContext());
		PersonModel _personModel = new PersonModel("张学友");
		_personBusiness.insertPerson(_personModel);
		
		Log.i(TAG, "personId: " + _personModel.personId);
	}
	
	public void testDeletePerson(){
		
	}
	
	public void testQueryPersonNamesByPersonIds(){
		PersonBusiness _personBusiness = new PersonBusiness(this.getContext());
		String _personNames = _personBusiness.queryPersonNamesByPersonIds("1,2");
		Log.i(TAG, "_personNames: " + _personNames);
	}
	
}
