/**
 * 
 */
package com.simbest.cores.app.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.app.model.FileUploader;
import com.simbest.cores.app.service.IFileUploaderService;
import com.simbest.cores.exceptions.InvalidateSNSUserException;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.AppCodeGenerator;
import com.simbest.cores.utils.AppFileUtils;
import com.simbest.cores.utils.AppFileUtils.StoreLocation;
import com.simbest.cores.utils.DateUtil;
import com.simbest.cores.utils.Digests;
import com.simbest.cores.utils.Encodes;
import com.simbest.cores.utils.configs.CoreConfig;
import com.simbest.cores.utils.enums.SNSLoginType;
import com.simbest.cores.web.LogicController;
import com.simbest.cores.web.filter.SNSAuthenticationToken;

/**
 * @author lishuyi
 *
 */
@Controller
@RequestMapping(value = {"/action/app/file", "/action/api/app/file", "/action/sso/app/file", "/action/anonymous/app/file"}) 
public class FileUploaderController extends LogicController<FileUploader, Long>{

	@Autowired
	private AppUserSession appUserSession;
	
	@Autowired
	private CoreConfig coreConfig;
	
	@Autowired
	private IFileUploaderService fileUploaderService;

	private StoreLocation location = null;
	
	@Autowired
	@Qualifier("appFileUtils")
	private AppFileUtils appFileUtils;
	
	public FileUploaderController() {
		super(FileUploader.class, null, null);
	}

	@PostConstruct
	private void initService() {
		setService(fileUploaderService);
		location = Enum.valueOf(StoreLocation.class, coreConfig.getValue("app.upload.file.store"));
	}
	
	/**
	 * APP上传文件，并记录文件记录（attr1为MD5保存值）
	 * @param file
	 * @param accesstoken
	 * @param md5
	 * @param timestamp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createMd5File", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createMd5File(HttpServletRequest request, HttpServletResponse response, @RequestParam("accesstoken") String accesstoken,
			@RequestParam("md5") String md5, @RequestParam("timestamp") String timestamp, @RequestParam("fileClass") String fileClass) throws Exception{  
		Map<String, Object> map = Maps.newHashMap();
		Set<String> filePaths = Sets.newHashSet();
		int total = 0;
		int saved = 0;
		String actualMd5 = Digests.encryptMD5(config.getValue("app.api.key")+accesstoken+timestamp+fileClass);
		log.debug("Expect MD5: " +md5);
		log.debug("Actual MD5: " +actualMd5);
		if(StringUtils.isEmpty(accesstoken) || !md5.equals(actualMd5)){
			throw new InvalidateSNSUserException("MP100001", String.format("Invalidate user not allowed to access resource!"));
		}else{			
			if(appUserSession.isUserSessionTimeout()){
				SNSAuthenticationToken snsToken = new SNSAuthenticationToken(accesstoken, SNSLoginType.accesstoken);
				try{
					SecurityUtils.getSubject().login(snsToken);
				}catch(Exception e){
					throw new InvalidateSNSUserException("MP100001", String.format("Invalidate user not allowed to access resource!"));
				}
			}
			MultipartFile file = null;
			MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> files = mureq.getFileMap(); 
			if (files != null && files.size() != 0) { 
				total = files.size();
				for(String key : files.keySet()) {
			    	file=files.get(key);
			    	String filePath = fileUploaderService.createMd5File(file, fileClass);
			    	if(StringUtils.isNotEmpty(filePath)){
			    		saved++;
			    		filePaths.add(filePath);
			    	}
		    	}
			}
			map.put("message", String.format("共计接收%d个文件，成功保存%d个", total, saved));		
			map.put("data", filePaths);
			map.put("responseid", 1);
			return map;
		}	
	}
	
	/**
	 * 上传文件人口（单个文件）[只存储文件，不保存文件记录]
	 * @param request
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{  
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = response.getWriter();
		ShiroUser user = appUserSession.getCurrentUser();
		MultipartFile file = null;
		String storePath = null;
		String filename = null;
		if(StringUtils.isNotEmpty(request.getParameter("imageurl"))){ // 美图秀秀第二次返回剪裁
			log.debug("imageurl is not null ---------------------"+request.getParameter("imageurl"));
			filename = StringUtils.substringAfterLast(request.getParameter("imageurl"), "/");
			switch(location){
				case Cloud:				
					storePath = request.getParameter("imageurl");
			    	storePath = StringUtils.substringAfter(storePath, coreConfig.getValue("bae.bcs.bucket")); //截获bucket以后字符串
			    	storePath = StringUtils.substringBeforeLast(storePath, "/"); //去掉文件名
			    	storePath += "/"; //补全路径
					break;
				case Disk:			
					storePath = request.getParameter("imageurl");
			    	storePath = StringUtils.substringAfter(storePath, "/"+coreConfig.getCtx()); //截获项目名以后字符串
			    	storePath = StringUtils.substringBeforeLast(storePath, "/"); //去掉文件名
			    	storePath += "/"; //补全路径
					break;
				default:
					break;
			}
		}else{
			log.debug("imageurl is null ......................"); // 第一次上传文件
			storePath = "/static/uploadFiles/"+user.getLoginName()+"/"+DateUtil.getToday()+"/"+AppCodeGenerator.nextSystemUUID()+"/";			
		}
		MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = mureq.getFileMap(); 
		if (files != null && files.size() != 0) { 
			for(String key : files.keySet()) {
		    	file=files.get(key);
		    	if(StringUtils.isEmpty(request.getParameter("imageurl"))){
		    		filename = file.getOriginalFilename();
		    	}
		    	break;
	    	}
			log.debug("storePath is: "+storePath);
			String savePath = appFileUtils.uploadFromClient(file, filename, storePath);
			if(!StringUtils.isEmpty(savePath)){			
				out.println("<script type=\"text/javascript\">parent.imageupload=\"" + savePath +
		                "\";parent.imageOtherInfo={\"filename\":\"" + file.getOriginalFilename() + "\"}</script>"); 

			}else{
				out.println("<script type=\"text/javascript\">parent.imageMessage=\"系统异常\";</script>");
				out.close();
			}
		}else{
			out.println("<script type=\"text/javascript\">parent.imageMessage=\"未有文件提交上传\";</script>");
			out.close();
		}
	} 
	
	/**
	 * 上传情报正文及附件入口(历史遗留问题，情报系统专用NTKO控件)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "uploadFileContent", method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,String> uploadFileContent(HttpServletRequest request) throws Exception{  
		Map<String,String> map = Maps.newHashMap();
		String fileName = null;
		String savePath = null;
		ShiroUser user = appUserSession.getCurrentUser();
		MultipartFile file = null;
		MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = mureq.getFileMap(); 
		if (files != null && files.size() != 0) { 
			for(String key : files.keySet()) {
		    	file=files.get(key);
		    	break;
	    	}
	        String fileFullPath = Encodes.urlDecode(file.getOriginalFilename()); //NTKO控件新增和上传文件回传的文件名其实是一个http的网络绝对路径
	        log.debug(fileFullPath);
	        if(fileFullPath.indexOf("http") == -1 || fileFullPath.indexOf("/static/uploadFiles/") == -1){
	        	fileName = fileFullPath; //新建文件
	        }else{ //修改已上传文件
	        	fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/"));
	        }        
	        savePath = appFileUtils.uploadFromClient(file, fileName, "/static/uploadFiles/"+user.getLoginName()+"/"+DateUtil.getToday()+"/");
			log.debug(fileName);
			log.debug(savePath);
		} 
		map.put("fileName", StringUtils.isEmpty(savePath) ? "": Encodes.urlEncode(fileName));
		map.put("filePath", StringUtils.isEmpty(savePath) ? "": Encodes.urlEncode(savePath));
		return map;
	} 
	
	/**
	 * 既删除文件，又删除数据库记录
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteFiles", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFiles(String filePath) throws Exception {
		Map<String, Object> map = Maps.newHashMap();
		FileUploader o = new FileUploader();
		o.setFilePath(filePath);
		int ret = fileUploaderService.delete(o);
		map.put("message", ret>0 ? "删除成功!":"删除失败!");
		map.put("responseid", ret>0 ? 1:0);
		return map;
	}
	
	
	/**
	 * 上传文件入口（单个文件）[存储文件，并保存文件记录]
	 * @param request
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(value = "uploadFileAndSaveRecord", method = RequestMethod.POST)
	public void uploadFileAndSaveRecord(HttpServletRequest request, HttpServletResponse response) throws Exception{  
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = response.getWriter();
		ShiroUser user = appUserSession.getCurrentUser();
		MultipartFile file = null;
		String storePath = null;
		String filename = null;
		if(StringUtils.isNotEmpty(request.getParameter("imageurl"))){ // 美图秀秀第二次返回剪裁
			log.debug("imageurl is not null ---------------------"+request.getParameter("imageurl"));
			filename = StringUtils.substringAfterLast(request.getParameter("imageurl"), "/");
			switch(location){
				case Cloud:				
					storePath = request.getParameter("imageurl");
			    	storePath = StringUtils.substringAfter(storePath, coreConfig.getValue("bae.bcs.bucket")); //截获bucket以后字符串
			    	storePath = StringUtils.substringBeforeLast(storePath, "/"); //去掉文件名
			    	storePath += "/"; //补全路径
					break;
				case Disk:			
					storePath = request.getParameter("imageurl");
			    	storePath = StringUtils.substringAfter(storePath, "/"+coreConfig.getCtx()); //截获项目名以后字符串
			    	storePath = StringUtils.substringBeforeLast(storePath, "/"); //去掉文件名
			    	storePath += "/"; //补全路径
					break;
				default:
					break;
			}
		}else{
			log.debug("imageurl is null ......................"); // 第一次上传文件
			storePath = "/static/uploadFiles/"+user.getLoginName()+"/"+DateUtil.getToday()+"/"+AppCodeGenerator.nextSystemUUID()+"/";			
		}
		MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = mureq.getFileMap(); 
		if (files != null && files.size() != 0) { 
			for(String key : files.keySet()) {
		    	file=files.get(key);
		    	if(StringUtils.isEmpty(request.getParameter("imageurl"))){
		    		filename = file.getOriginalFilename();
		    	}
		    	break;
	    	}
			log.debug("storePath is: "+storePath);
			String savePath = appFileUtils.uploadFromClient(file, filename, storePath);
			if(!StringUtils.isEmpty(savePath)){	
				try {
					
					FileUploader fUploader = new FileUploader();
					fUploader.setFilePath(savePath);
					fUploader.setOrgId(user.getOrgId());
					fUploader.setCreateUserId(user.getUserId());
					fUploader.setCreateUserCode(user.getUserCode());
					fUploader.setCreateUserName(user.getUserName());
					fUploader.setCreateDate(DateUtil.getCurrent());
					fUploader.setFileClass(UUID.randomUUID().toString()+"-"+DateUtil.getDate(fUploader.getCreateDate(), DateUtil.timestampPattern1));
					fUploader.setFinalName(filename);
					int ret = fileUploaderService.create(fUploader);
					log.debug(ret);
					
					
					out.println("<script type=\"text/javascript\">parent.imageupload=\"" + savePath +
							"\";parent.imageOtherInfo={\"filename\":\"" + file.getOriginalFilename() + "\",\"id\":\"" + fUploader.getId() + "\"}</script>"); 
				} catch (Exception e) {
					out.println("<script type=\"text/javascript\">parent.imageMessage=\"系统异常\";</script>");
					out.close();
				}

			}else{
				out.println("<script type=\"text/javascript\">parent.imageMessage=\"系统异常\";</script>");
				out.close();
			}
		}else{
			out.println("<script type=\"text/javascript\">parent.imageMessage=\"未有文件提交上传\";</script>");
			out.close();
		}
	} 
	
	@RequestMapping(value = "/dowanloadSourceFile", method = RequestMethod.GET)
	public void dowanloadSourceFile(Long id, HttpServletResponse response) throws IOException {
		FileUploader fUploader = fileUploaderService.getById(id);
		String filePath = fUploader.getFilePath();
		File file = new File(appFileUtils.getFileLocation()+filePath.substring(filePath.indexOf(appFileUtils.getBaseUrl())+appFileUtils.getBaseUrl().length()));
		OutputStream outputStream = null;
		try {
			String filename = file.getName();
			filename = filename.replaceAll("\\s*", "");//过滤空格，如果有空格浏览器会转换
			response.reset();  
		    response.setContentType("application/octet-stream;charset=UTF-8");  	
		    response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes("UTF-8"), "ISO8859-1"));
		    response.setHeader("Content-Length", String.valueOf(file.length()));
		    outputStream = new BufferedOutputStream(response.getOutputStream());  	    
		    outputStream.write(FileUtils.readFileToByteArray(file));  
		    outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {  
	        if (outputStream != null) {  
	        	outputStream.close();  
	        }  
	    } 	
	}
}
