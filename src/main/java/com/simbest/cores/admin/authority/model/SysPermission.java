package com.simbest.cores.admin.authority.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.simbest.cores.model.SystemModel;
import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.annotations.ReferenceTable;
import com.simbest.cores.utils.annotations.ReferenceTables;
import com.simbest.cores.utils.annotations.Unique;

@Entity
@Table(name = "sys_permission")
@ReferenceTables(joinTables={ @ReferenceTable(table="sys_permission", value="上级权限"), 
		@ReferenceTable(table="sys_role_permission", value="角色与权限")})
public class SysPermission extends SystemModel<SysPermission> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2036956426898112019L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNullColumn(value="资源描述")
	@Column(length = 200, nullable = false)
	private String description;

	@NotNullColumn(value="资源编码")
	@Column(length = 100, nullable = false, unique = true)
	@Unique
	private String name;

	@NotNullColumn(value="访问地址")
	@Column(length = 1000, unique = false)
	private String url;
	
	@NotNullColumn(value="资源类型")
	@Column(length = 10, nullable = false)
	private String type;
	
	@Column(length = 100)
	private String icon;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="parent_id", nullable=true) //根节点允许为空
	private SysPermission parent; 
	public static enum TYPE{system, module, menu, submenu, button};

	@Transient
	private Integer parentId;
	
	@Transient
	private String parentName;
	
	@Transient
	private String parentUrl;
	
	@Transient
	private String parentDescription;
	
	public SysPermission() {
		super();
	}

	public SysPermission(Integer id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the parent
	 */
	public SysPermission getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(SysPermission parent) {
		this.parent = parent;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public String getParentDescription() {
		return parentDescription;
	}

	public void setParentDescription(String parentDescription) {
		this.parentDescription = parentDescription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}