package cilinet.godutch.model;

import java.io.Serializable;
import java.util.Date;

/** 类别 **/
public class CategoryModel implements Serializable {

	/** 类别id **/
	public int id;
	
	/** 类别名称 **/
	public String name;
	
	/** 类型标记名称 **/
	public String typeFlag;
	
	/** 父类别Id **/
	public int parentId = 0;
	
	/** 类别路径(面包屑) **/
	public String path;
	
	/** 创建日期 **/
	public Date createDate = new Date();
	
	/** 状态 0失效 1启用 **/
	public int state = 1;

	public CategoryModel(int id, String name, String typeFlag, int parentId,
			String path, Date createDate, int state) {
		this.id = id;
		this.name = name;
		this.typeFlag = typeFlag;
		this.parentId = parentId;
		this.path = path;
		this.createDate = createDate;
		this.state = state;
	}
	
	public CategoryModel() {}

	public String toString(){
		return name;
	}
	
}
