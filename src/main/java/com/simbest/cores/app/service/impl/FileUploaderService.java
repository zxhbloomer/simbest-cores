package com.simbest.cores.app.service.impl;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysUserAdvanceService;
import com.simbest.cores.app.mapper.FileUploaderMapper;
import com.simbest.cores.app.model.FileUploader;
import com.simbest.cores.app.service.IFileUploaderService;
import com.simbest.cores.app.service.IProcessHeaderAdvanceService;
import com.simbest.cores.service.impl.LogicService;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.AppCodeGenerator;
import com.simbest.cores.utils.AppFileUtils;
import com.simbest.cores.utils.DateUtil;
import com.simbest.cores.utils.Digests;
import com.simbest.cores.utils.ObjectUtil;
import com.simbest.cores.utils.enums.FileClassDynaEnum;

@Service(value = "fileUploaderService")
public class FileUploaderService extends LogicService<FileUploader,Long> implements IFileUploaderService{
	
	private FileUploaderMapper mapper;
	
	@Autowired
	private IProcessHeaderAdvanceService processHeaderAdvanceService;

	@Autowired
	private AppFileUtils appFileUtils;
	
	@Autowired
	private AppUserSession appUserSession;
	
	@Autowired
	private FileClassDynaEnum fileClassEnum;
	
	@Autowired
	private ISysUserAdvanceService sysUserAdvanceService;
	
	@Autowired
    public FileUploaderService(@Qualifier(value="sqlSessionTemplateSimple") SqlSession sqlSession) {
		super(sqlSession);
		this.mapper = sqlSession.getMapper(FileUploaderMapper.class);
		super.setMapper(mapper);
    }
	
	@Override
	public String createMd5File(MultipartFile file, String fileClass){
		String result = null;
		String fileMD5 = Digests.getUploadFileMd5(file); 
		Map<String, Object> params = Maps.newHashMap();
		params.put("attr1", fileMD5);
		Collection<FileUploader> checkList = mapper.getAll(params);
		if(checkList.size() == 0 ){ //从未保存过该MD5的文件
			String savePath = appFileUtils.uploadFromClient(file, file.getOriginalFilename(), "/static/uploadFiles/"+DateUtil.getToday()+"/"+AppCodeGenerator.nextSystemUUID()+"/");
			if(StringUtils.isNotEmpty(savePath)){
				ShiroUser user = appUserSession.getCurrentUser();
				if("UTX".equals(fileClass)){ //用户头像
					SysUser sysUser = sysUserAdvanceService.loadByKey(user.getUserId());
					sysUser.setHeadimgurl(savePath);
					user.setHeadUrl(savePath);
					int ret = sysUserAdvanceService.update(sysUser);
					log.debug(ret);
				}else if("UBJ".equals(fileClass)){ //用户背景
					SysUser sysUser = sysUserAdvanceService.loadByKey(user.getUserId());
					sysUser.setBackgroundurl(savePath);
					int ret = sysUserAdvanceService.update(sysUser);
					log.debug(ret);
				}else{
					FileUploader newFile = new FileUploader();
					newFile.setAttr1(fileMD5);
					newFile.setFinalName(file.getOriginalFilename());
					newFile.setFilePath(savePath);					
					newFile.setOrgId(user.getOrgId());
					newFile.setCreateUserId(user.getUserId());
					newFile.setCreateUserCode(user.getUserCode());
					newFile.setCreateUserName(user.getUserName());
					newFile.setCreateDate(DateUtil.getCurrent());
					newFile.setFileClass(fileClassEnum.value(fileClass).name());
//					fUploader.setProcessTypeId(co.getProcessTypeId());
//					fUploader.setProcessHeaderId(co.getProcessHeaderId());
//					fUploader.setReceiptId(co.getId());
//					fUploader.setReceiptCode(co.getCode());
					int ret = create(newFile);
					log.debug(ret);
				}
				result = savePath;
			}
		}else{
			result = checkList.iterator().next().getFilePath();
		}
		return result;
	}
	
	/**
	 * 既删除文件，又删除数据库记录
	 */
	@Override
	public int delete(FileUploader o){
		int ret = 0;
		if(ObjectUtil.isEmpty(o)){
			log.warn("@FileUploaderService forbidden delete all objects with empty object: " + o);
			return ret;
		}else{
			Collection<FileUploader> list = mapper.getAll(o);
			if(list.size() > 0){
				Collection<String> filePaths = Sets.newHashSet();
				for(FileUploader del:list){
					log.warn("@FileUploaderService will delete records and files with object:"+del);
					ret = mapper.deleteById(del.getId());
					log.warn(ret);
					if(ret == 1){
						filePaths.add(del.getFilePath());						
					}					
				}
				ret = appFileUtils.deleteFiles(filePaths);
			}
			return ret;
		}
	}

	/**
	 * 仅删除数据库记录
	 */
	@Override
	public int deleteRecord(FileUploader o) {
		int ret = 0;
		if(ObjectUtil.isEmpty(o)){
			log.warn("@FileUploaderService forbidden delete all objects with empty object: " + o);
			return ret;
		}else{
			Collection<FileUploader> list = mapper.getAll(o);
			if(list.size() > 0){
				for(FileUploader del:list){
					log.warn("@FileUploaderService will delete records with object:"+del);
					ret = mapper.deleteById(del.getId());
					log.warn(ret);
				}
			}
			return ret;
		}
	}

	/**
	 * 仅删除数据库记录
	 */
	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return mapper.deleteById(id);
	}
}
