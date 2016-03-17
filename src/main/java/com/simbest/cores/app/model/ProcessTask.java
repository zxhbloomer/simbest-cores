package com.simbest.cores.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;

/**
 * 业务代办
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_task", uniqueConstraints={@UniqueConstraint(columnNames={"typeId","headerId","receiptId","currentUserId"})})
@XmlRootElement
public class ProcessTask extends GenericModel<ProcessTask> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6503354417012395693L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
	private String title;
	
	@NotNullColumn(value="业务类型")
	@Column(name = "typeId", nullable = false)
	private Integer typeId;

	@NotNullColumn(value="业务单据")
	@Column(name = "headerId", nullable = false)
	private Integer headerId;
	
	//所有业务单据的主键必须统一类型，否则无法写入待办和审批记录
	@NotNullColumn(value="业务单据Id")
	@Column(name = "receiptId", nullable = false)
	private Long receiptId;
	
	@NotNullColumn(value="业务当前环节")
	@Column(name = "stepId", nullable = false)
	private Integer stepId;
	
	@Column(name = "stepCode", nullable = false, length = 20)
	private String stepCode;

	
	@NotNullColumn(value="受理部门")
	@Column(name = "currentOrgId", nullable = false)
	private Integer currentOrgId; 
	
	@Column(name = "currentOrgName", nullable = false)
	private String currentOrgName; 
	
	@NotNullColumn(value="受理人")
	@Column(name = "currentUserId", nullable = false, length = 50)
	private Integer currentUserId;	
	
	@Column(name = "currentUserCode", nullable = true, length = 50)
	private String currentUserCode;	
	
	@Column(name = "currentUserName", nullable = false, length = 50)
	private String currentUserName; 
	
	@NotNullColumn(value="创建部门")
	@Column(name = "createOrgId", nullable = false)
	private Integer createOrgId; 
	
	@Column(name = "createOrgName", nullable = false)
	private String createOrgName; 
	
	@NotNullColumn(value="创建人")
	@Column(name = "createUserId", nullable = false, length = 50)
	private Integer createUserId;	
	
	@Column(name = "createUserCode", nullable = true, length = 50)
	private String createUserCode;	
	
	@Column(name = "createUserName", nullable = false, length = 50)
	private String createUserName; 
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name = "createDate", nullable = false)
	private Date createDate;

	@NotNullColumn(value="已受理部门")
	@Column(name = "previousOrgId", nullable = false)
	private Integer previousOrgId; 
	
	@Column(name = "previousOrgName", nullable = false)
	private String previousOrgName; 
	
	@NotNullColumn(value="已受理人")
	@Column(name = "previousUserId", nullable = false, length = 50)
	private Integer previousUserId;	
	
	@Column(name = "previousUserCode", nullable = true, length = 50)
	private String previousUserCode;	
	
	@Column(name = "previousUserName", nullable = false, length = 50)
	private String previousUserName; 
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name = "previousDate", nullable = false)
	private Date previousDate;
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name = "generateDate")
	private Date generateDate;
	
	@Transient
	private String stepDesc;
	
    @Transient
    private String headerDesc;
  
    @Transient
    private String typeDesc;
	    
    @Transient
    private String roleName;
	    
    @Transient
    private String username;
	
    @Transient
    private String orgName;
    
    public ProcessTask() {
		super();
    }
    
	public ProcessTask(Long id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the receiptId
	 */
	public Long getReceiptId() {
		return receiptId;
	}

	/**
	 * @param receiptId the receiptId to set
	 */
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getCurrentUserCode() {
		return currentUserCode;
	}

	public void setCurrentUserCode(String currentUserCode) {
		this.currentUserCode = currentUserCode;
	}

	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	/**
	 * @return the typeId
	 */
	public Integer getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the headerId
	 */
	public Integer getHeaderId() {
		return headerId;
	}

	/**
	 * @param headerId the headerId to set
	 */
	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	/**
	 * @return the stepId
	 */
	public Integer getStepId() {
		return stepId;
	}

	/**
	 * @param stepId the stepId to set
	 */
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	/**
	 * @return the stepDesc
	 */
	public String getStepDesc() {
		return stepDesc;
	}

	/**
	 * @param stepDesc the stepDesc to set
	 */
	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	/**
	 * @return the headerDesc
	 */
	public String getHeaderDesc() {
		return headerDesc;
	}

	/**
	 * @param headerDesc the headerDesc to set
	 */
	public void setHeaderDesc(String headerDesc) {
		this.headerDesc = headerDesc;
	}

	/**
	 * @return the typeDesc
	 */
	public String getTypeDesc() {
		return typeDesc;
	}

	/**
	 * @param typeDesc the typeDesc to set
	 */
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the createUserId
	 */
	public Integer getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * @return the createUserCode
	 */
	public String getCreateUserCode() {
		return createUserCode;
	}

	/**
	 * @param createUserCode the createUserCode to set
	 */
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the createDate
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the generateDate
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	public Date getGenerateDate() {
		return generateDate;
	}

	/**
	 * @param generateDate the generateDate to set
	 */
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getPreviousUserId() {
		return previousUserId;
	}

	public void setPreviousUserId(Integer previousUserId) {
		this.previousUserId = previousUserId;
	}

	public String getPreviousUserCode() {
		return previousUserCode;
	}

	public void setPreviousUserCode(String previousUserCode) {
		this.previousUserCode = previousUserCode;
	}

	public String getPreviousUserName() {
		return previousUserName;
	}

	public void setPreviousUserName(String previousUserName) {
		this.previousUserName = previousUserName;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	public Date getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(Date previousDate) {
		this.previousDate = previousDate;
	}

	public Integer getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(Integer currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	public String getCurrentOrgName() {
		return currentOrgName;
	}

	public void setCurrentOrgName(String currentOrgName) {
		this.currentOrgName = currentOrgName;
	}

	public Integer getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(Integer createOrgId) {
		this.createOrgId = createOrgId;
	}

	public String getCreateOrgName() {
		return createOrgName;
	}

	public void setCreateOrgName(String createOrgName) {
		this.createOrgName = createOrgName;
	}

	public Integer getPreviousOrgId() {
		return previousOrgId;
	}

	public void setPreviousOrgId(Integer previousOrgId) {
		this.previousOrgId = previousOrgId;
	}

	public String getPreviousOrgName() {
		return previousOrgName;
	}

	public void setPreviousOrgName(String previousOrgName) {
		this.previousOrgName = previousOrgName;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}
	
}