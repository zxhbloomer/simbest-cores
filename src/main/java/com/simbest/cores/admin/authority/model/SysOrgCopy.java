/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cores.admin.authority.model;

import com.simbest.cores.model.SystemModel;
import com.simbest.cores.utils.annotations.ExcelVOAttribute;
import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.annotations.Unique;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/4/28  21:27
 */
@Entity
@Table(name = "sys_org_copy")
public class SysOrgCopy extends SystemModel<SysOrgCopy> {

    private static final long serialVersionUID = -1447427183198771078L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="sys_org_copy_seq", sequenceName="sys_org_copy_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="sys_org_copy_seq")
    @ApiModelProperty(value="主键Id")
    private Integer id;

    @ExcelVOAttribute(name = "组织全路径", column = "D")
    @Column(name = "description", length = 200)
    private String description;

    @ExcelVOAttribute(name = "组织编码", column = "A")
    @NotNullColumn(value="组织编码")
    @Column(name = "orgCode", length = 20, unique = true)
    @Unique
    private String orgCode;

    @ExcelVOAttribute(name = "组织名称", column = "C")
    @NotNullColumn(value="组织名称")
    @Column(name = "orgName", nullable = false, length = 100)
    private String orgName;

    @ExcelVOAttribute(name = "父组织编码", column = "B")
    private String parent_id;

    @Transient
    private String parentName;

    @NotNullColumn(value="组织级别")
    @Column(nullable = true)
    private Integer orgLevel;

    @NotNullColumn(value="组织类型")
    @Column(nullable = true)
    private Integer orgType;

    @ExcelVOAttribute(name = "显示顺序", column = "E")
    @NotNullColumn(value="显示顺序")
    @Column(nullable = true)
    private Integer orderBy;

    @NotNullColumn(value="简称")
    @Column(nullable = true)
    private String shortName;

    @ExcelVOAttribute(name = "备注描述", column = "F")
    @NotNullColumn(value="备注")
    @Column(nullable = true)
    private String remark;

    public SysOrgCopy() {
        super();
    }

    public SysOrgCopy(Integer id) {
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

    /**
     * @return the orgCode
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * @param orgCode the orgCode to set
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return the orgLevel
     */
    public Integer getOrgLevel() {
        return orgLevel;
    }

    /**
     * @param orgLevel the orgLevel to set
     */
    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    /**
     * @return the orgType
     */
    public Integer getOrgType() {
        return orgType;
    }

    /**
     * @param orgType the orgType to set
     */
    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    /**
     * @return the orderBy
     */
    public Integer getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the parent_id
     */
    public String getParent_id() {
        return parent_id;
    }

    /**
     * @param parent_id the parent_id to set
     */
    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
