package com.simbest.cores.app.mapper;
import org.apache.ibatis.annotations.Param;

import com.simbest.cores.app.model.FileUploader;
import com.simbest.cores.mapper.ILogicMapper;

public interface FileUploaderMapper extends ILogicMapper<FileUploader,Long> {
	
	int deleteById(@Param("id") Long id);
}