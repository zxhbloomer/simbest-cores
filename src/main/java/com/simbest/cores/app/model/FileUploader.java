package com.simbest.cores.app.model;

import javax.persistence.*;

import com.simbest.cores.model.LogicModel;
import com.simbest.cores.utils.annotations.NotNullColumn;

/**
 * 文档信息
 * @author lishuyi
 *
 */
@Entity
//@Table(name = "app_file_upload", uniqueConstraints={@UniqueConstraint(columnNames={"processTypeId", "processHeaderId","receiptId","createUserId","fileClass","finalName"}),
//		@UniqueConstraint(columnNames={"attr1", "receiptId","fileClass"})})
@Table(name = "app_file_upload", uniqueConstraints={
        @UniqueConstraint(columnNames={"attr1", "receiptId","fileClass"})})
public class FileUploader extends LogicModel<FileUploader> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5250391046853452376L;

	@Id
	@Column(name = "id")
    @SequenceGenerator(name="app_file_upload_seq", sequenceName="app_file_upload_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="app_file_upload_seq")
	private Long id;

	@Column(name = "description", length = 200)
	private String description;

	@NotNullColumn(value="流程类型")
	@Column(name = "processTypeId") 
	private Integer processTypeId;

	@Transient
	private String processTypeName;
	
	@NotNullColumn(value="流程描述")
	@Column(name = "processHeaderId") 
	private Integer processHeaderId;	
	
	@Transient
	private String processHeaderName;
	
	@Column(name = "receiptId")
	private Long receiptId;          //适用于工作流实体
	
	@Column(name = "receiptCode")    //适用于其他主键非Long型的实体
	private String receiptCode;
	
	@NotNullColumn(value="文件用途分类 ")
	@Column(name = "fileClass", nullable=false, length = 100)
	private String fileClass;
	
	@Column(name = "finalName", length = 100) //先记录MD5不存储文件，因此该字段可能没值
	private String finalName;
	
	@Column(name = "filePath", length = 255) //先记录MD5不存储文件，因此该字段可能没值
	private String filePath;
	
	@Column(name = "fileSize", length = 50)
	private Long fileSize;
	
	@NotNullColumn(value="提交部门信息")
	@Column(name = "orgId")
	private Integer orgId; 	 
	
	@Column(name = "attr1", length = 100) //MD5校验值
	private String attr1;
	
	@Column(name = "attr2", length = 100)
	private String attr2;
	
	@Column(name = "attr3", length = 100)
	private String attr3;
	
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
	 * @return the processTypeId
	 */
	public Integer getProcessTypeId() {
		return processTypeId;
	}
	/**
	 * @param processTypeId the processTypeId to set
	 */
	public void setProcessTypeId(Integer processTypeId) {
		this.processTypeId = processTypeId;
	}
	/**
	 * @return the processTypeName
	 */
	public String getProcessTypeName() {
		return processTypeName;
	}
	/**
	 * @param processTypeName the processTypeName to set
	 */
	public void setProcessTypeName(String processTypeName) {
		this.processTypeName = processTypeName;
	}
	/**
	 * @return the processHeaderId
	 */
	public Integer getProcessHeaderId() {
		return processHeaderId;
	}
	/**
	 * @param processHeaderId the processHeaderId to set
	 */
	public void setProcessHeaderId(Integer processHeaderId) {
		this.processHeaderId = processHeaderId;
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

	/**
	 * @return the finalName
	 */
	public String getFinalName() {
		return finalName;
	}
	/**
	 * @param finalName the finalName to set
	 */
	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the orgId
	 */
	public Integer getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the attr1
	 */
	public String getAttr1() {
		return attr1;
	}
	/**
	 * @param attr1 the attr1 to set
	 */
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	/**
	 * @return the attr2
	 */
	public String getAttr2() {
		return attr2;
	}
	/**
	 * @param attr2 the attr2 to set
	 */
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	/**
	 * @return the attr3
	 */
	public String getAttr3() {
		return attr3;
	}
	/**
	 * @param attr3 the attr3 to set
	 */
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	/**
	 * @return the processHeaderName
	 */
	public String getProcessHeaderName() {
		return processHeaderName;
	}
	/**
	 * @param processHeaderName the processHeaderName to set
	 */
	public void setProcessHeaderName(String processHeaderName) {
		this.processHeaderName = processHeaderName;
	}
	
	public String getReceiptCode() {
		return receiptCode;
	}
	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}
	public String getFileClass() {
		return fileClass;
	}
	public void setFileClass(String fileClass) {
		this.fileClass = fileClass;
	}
	
	
}