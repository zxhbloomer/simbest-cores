package com.simbest.cores.app.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.simbest.cores.app.mapper.ProcessTaskMapper;
import com.simbest.cores.app.model.ProcessTask;
import com.simbest.cores.app.service.IProcessTaskService;
import com.simbest.cores.service.impl.GenericMapperService;

@Service(value = "processTaskService")
public class ProcessTaskService extends GenericMapperService<ProcessTask,Long> implements IProcessTaskService{
	private ProcessTaskMapper mapper;
	
	public ProcessTaskService(SqlSession sqlSession,
			Class<ProcessTask> persistentMapper) {
		super(sqlSession, persistentMapper);
	}
		
	@Autowired
	public ProcessTaskService(@Qualifier(value="sqlSessionTemplateSimple") SqlSession sqlSession) {
		super(sqlSession);
		this.mapper = sqlSession.getMapper(ProcessTaskMapper.class);
		super.setMapper(mapper);
	}

	@Override
	public ProcessTask getCurrentUserTask(Integer typeId, Integer headerId,
			Long receiptId, Integer currentUserId) {
		return mapper.getCurrentUserTask(typeId, headerId, receiptId, currentUserId);
	}

}
