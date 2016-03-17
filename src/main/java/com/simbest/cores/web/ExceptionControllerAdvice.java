/**
 * 
 */
package com.simbest.cores.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simbest.cores.exceptions.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.google.common.collect.Maps;
import com.simbest.cores.admin.authority.model.SysPermission;
import com.simbest.cores.admin.authority.model.SysRole;
import com.simbest.cores.admin.authority.service.ISysPermissionAdvanceService;
import com.simbest.cores.admin.authority.service.ISysRoleAdvanceService;
import com.simbest.cores.model.JsonResponse;
import com.simbest.cores.utils.configs.CoreConfig;

/**
 * @author lishuyi
 *
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	public final Log log = LogFactory.getLog(ExceptionControllerAdvice.class);
	
	@Autowired
	private ISysPermissionAdvanceService sysPermissionService;
	
	@Autowired
	private ISysRoleAdvanceService sysRoleService;
	
	@Autowired
	private CoreConfig config;
	
	/**
	 * 上传文件超过最大限额
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public String uploadError(HttpServletRequest request, MaxUploadSizeExceededException e) {	
		log.error(request.getRequestURI());
		Exceptions.printException(e);
		String errmsg = "<script type=\"text/javascript\">parent.imageMessage=\"上传文件过大!\";</script>";
		return errmsg;
	}
	
	/**
	 * 期望一条数据，但数据库不止一条记录
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(FoundMoreThanOneException.class)
	@ResponseBody
	public JsonResponse foundMoreThanOneException(HttpServletRequest request, FoundMoreThanOneException e) {	
		log.error(request.getRequestURI());
		Exceptions.printException(e);
		JsonResponse response = new JsonResponse();	
		response.setMessage("发现存在不止一条记录！");
		response.setResponseid(0);
		return response;
	}
	
    @ExceptionHandler({ProcessUnavailableException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processProcessUnavailableException(HttpServletRequest request, ProcessUnavailableException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "当前流程状态为禁用模式!");
		result.put("responseid", 0);
		return result;
    }
    
    @ExceptionHandler({UpdateProcessFailedException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processUpdateProcessFailedException(HttpServletRequest request, UpdateProcessFailedException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "编辑更新流程失败!");
		result.put("responseid", 0);
		return result;
    }
    
    @ExceptionHandler({UnExpectedAuditException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processUnExpectedAuditException(HttpServletRequest request, UnExpectedAuditException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "加载当前环节审批配置信息失败!");
		result.put("responseid", 0);
		return result;
    }

    @ExceptionHandler({UnExpectedAuditUserException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processUnExpectedAuditUserException(HttpServletRequest request, UnExpectedAuditUserException e) {
        log.error(request.getRequestURI());
        Exceptions.printException(e);
        Map<String, Object> result = Maps.newHashMap();
        result.put("message", "下一环节处理人无效，流程不可提交!");
        result.put("responseid", 0);
        return result;
    }

    @ExceptionHandler({UnExpectedStepException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processUnExpectedStepException(HttpServletRequest request, UnExpectedStepException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "加载流程审批环节失败!");
		result.put("responseid", 0);
		return result;
    }
    
    /**
     * 页面找不到404
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({NoSuchRequestHandlingMethodException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> noSuchRequestHandlingMethod(HttpServletRequest request, Exception e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "请求的资源不存在!");
		result.put("responseid", 0);
		return result;
    }
    
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processUnauthenticatedException(HttpServletRequest request, UnauthorizedException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	String message = "权限不足！";
    	String errors = e.getMessage();
    	int begin = errors.indexOf("[")+1;
    	int end = errors.indexOf("]");
    	if(begin>0 && end!=-1){
	    	String key = errors.substring(begin, end);
	    	if(StringUtils.contains(errors, "role")){
	    		SysRole sysRole = sysRoleService.getByUnique(key);
	    		if(sysRole != null)
	    			message = "您没有授权'"+sysRole.getDescription()+"'角色!";
	    	}
	    	if(StringUtils.contains(errors, "permission")){
	    		SysPermission sysPermission = sysPermissionService.getByUnique(key);
	    		if(sysPermission != null)
	    			message = "您没有授权'"+sysPermission.getDescription()+"'操作权限!";
	    	}
    	}
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", message);
		result.put("responseid", 0);
		return result;
    }

    @ExceptionHandler({UnLoginException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Object> processUnLoginException(HttpServletRequest request, UnLoginException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "请先登录!");
		result.put("responseid", 200);
		return result;
    }
    
    @ExceptionHandler({UnknownAccountException.class})
    @ResponseBody
    public Map<String, Object> processUnknownAccountException(HttpServletRequest request, UnknownAccountException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "用户名不存在!");
		result.put("responseid", 0);
		return result;
    }
    
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public Map<String, Object> processAuthenticationException(HttpServletRequest request, AuthenticationException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "用户名/密码错误!");
		result.put("responseid", 0);
		return result;
    }
    
    @ExceptionHandler({InvalidateSNSAdminUserException.class})
    public String processInvalidateSNSAdminUserException(HttpServletRequest request, InvalidateSNSAdminUserException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
		return "redirect:/html/errors/InvalidateSNSAdminUserException.html";
    }
    
    @ExceptionHandler({SNSUserBindFailedLoginException.class})
    @ResponseBody
    public Map<String, Object> processSNSUserBindFailedLoginException(HttpServletRequest request, SNSUserBindFailedLoginException e) {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
    	Map<String, Object> result = Maps.newHashMap();		    	
		result.put("message", "绑定用户账号异常!");
		result.put("responseid", 0);
		return result;
    }
    
    /**
     * 应用异常
     * @param e
     * @return
     * @throws IOException 
     */
    @ExceptionHandler({AppException.class })
    @ResponseBody
	public Map<String, Object> processAppException(HttpServletRequest request, HttpServletResponse response, AppException e) throws IOException {
		log.error(request.getRequestURI());
		Exceptions.printException(e);
		Map<String, Object> map = Maps.newHashMap();
		map.put("responseid", 0);
		map.put("message", getUnKnowErrorMsg());
		return map;
    }
    
	/**
	 * 系统未知异常
	 * @param e
	 * @return
	 * @throws IOException 
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Map<String, Object> processUnknownException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {	
		log.error(request.getRequestURI());
		Exceptions.printException(e);
		Map<String, Object> map = Maps.newHashMap();
		map.put("responseid", 0);
		map.put("message", getUnKnowErrorMsg());
		return map;
	}
	
	private String getUnKnowErrorMsg(){
		String unKnowErrorMsg = StringUtils.isNotEmpty(config.getValue("app.unKnowErrorMsg"))?config.getValue("app.unKnowErrorMsg"):"服务器忙，请稍后尝试!";
		return unKnowErrorMsg;
	}
}
