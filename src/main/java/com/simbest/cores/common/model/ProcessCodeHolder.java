/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.common.model;

import com.simbest.cores.model.GenericModel;

import javax.persistence.*;

/**
 * 用途：持久化记录流程编码
 * 作者: lishuyi
 * 时间: 2016-12-08  14:27
 */
@Entity
@Table(name = "sys_process_code_holder")
public class ProcessCodeHolder extends GenericModel<ProcessCodeHolder> {
    private static final long serialVersionUID = -8026825782285229459L;

    @Id
    @SequenceGenerator(name="sys_process_code_holder_seq", sequenceName="sys_process_code_holder_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="sys_process_code_holder_seq")
    private Long id;

    private String prefix; //流程前缀

    private String processDate; //流程日期

    private Integer countCode; //计数编码

    private Integer countLength; //计数位数

    public ProcessCodeHolder() {
    }

    public ProcessCodeHolder(String prefix, String processDate, Integer countCode, Integer countLength) {
        this.prefix = prefix;
        this.processDate = processDate;
        this.countCode = countCode;
        this.countLength = countLength;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public Integer getCountCode() {
        return countCode;
    }

    public void setCountCode(Integer countCode) {
        this.countCode = countCode;
    }

    public Integer getCountLength() {
        return countLength;
    }

    public void setCountLength(Integer countLength) {
        this.countLength = countLength;
    }
}
