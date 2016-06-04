package com.simbest.cores.admin.authority.service;
import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.service.IGenericAdvanceService;

import java.util.List;

public interface ISysOrgAdvanceService extends IGenericAdvanceService<SysOrg,Integer>, ISysOrgService{

    /**
     * 获取所属部门公司层级Id字符串
     * @param orgId
     * @return
     */
    String getHierarchyOrgIds(Integer orgId);

    /**
     * 获取所属部门公司层级对象
     * @param orgId
     * @return
     */
    List<SysOrg> getHierarchyOrgs(Integer orgId);
}
