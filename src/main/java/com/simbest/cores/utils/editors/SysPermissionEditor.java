/**
 * 
 */
package com.simbest.cores.utils.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

import com.simbest.cores.admin.authority.model.SysPermission;
import com.simbest.cores.admin.authority.service.ISysPermissionService;

/**
 * @author lishuyi
 *
 */
public class SysPermissionEditor extends PropertyEditorSupport{
	private ISysPermissionService service;
	
	public SysPermissionEditor(ISysPermissionService service) {
		super();
		this.service = service;
	}

	@Override
	public void setAsText(String textValue) throws IllegalArgumentException {
		if(StringUtils.isEmpty(textValue))
			setValue(null);
		else{
			Integer id = Integer.parseInt(textValue);
			SysPermission o = service.getById(id);
			setValue(o);
		}
	}
}
