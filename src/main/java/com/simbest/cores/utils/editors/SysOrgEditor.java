/**
 * 
 */
package com.simbest.cores.utils.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.admin.authority.service.ISysOrgService;

/**
 * @author lishuyi
 *
 */
public class SysOrgEditor extends PropertyEditorSupport{
	private ISysOrgService service;
	
	public SysOrgEditor(ISysOrgService service) {
		super();
		this.service = service;
	}

	@Override
	public void setAsText(String textValue) throws IllegalArgumentException {
		if(StringUtils.isEmpty(textValue))
			setValue(null);
		else{
			Integer id = Integer.parseInt(textValue);
			SysOrg o = service.getById(id);
			setValue(o);
		}
	}
}
