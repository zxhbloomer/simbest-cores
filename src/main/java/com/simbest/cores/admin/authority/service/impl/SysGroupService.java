package com.simbest.cores.admin.authority.service.impl;

import com.simbest.cores.admin.authority.mapper.SysGroupMapper;
import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.admin.authority.service.ISysGroupService;
import com.simbest.cores.service.impl.GenericMapperService;
import com.simbest.cores.service.impl.SystemService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service(value = "sysGroupService")
public class SysGroupService extends GenericMapperService<SysGroup,String> implements ISysGroupService{

	private SysGroupMapper mapper;

	@Autowired
    public SysGroupService(@Qualifier(value="sqlSessionTemplateSimple") SqlSession sqlSession) {
		super(sqlSession);
		this.mapper = sqlSession.getMapper(SysGroupMapper.class);
		super.setMapper(mapper);
    }

    @Override
	public int createSysUserGroup(String userId,String groupId){
		return mapper.createSysUserGroup(userId,groupId);
	}

    @Override
    public int deleteSysUserGroupByGroupId(String groupId) {
        return mapper.deleteSysUserGroupByGroupId(groupId);
    }

	@Override
	public int deleteSysUserGroupByUserId(String userId) {		
		return mapper.deleteSysUserGroupByUserId(userId);
	}

    @Override
    public List<SysGroup> getByUser(String userId) {
        return mapper.getByUser(userId);
    }
}
