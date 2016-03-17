/**
 * 
 */
package com.simbest.cores.utils.decorators;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simbest.cores.app.model.ProcessStep;
import com.simbest.cores.app.service.IProcessStepAdvanceService;
import com.simbest.cores.exceptions.Exceptions;

/**
 * @author lishuyi
 *
 */
@Component
public class ProcessStepDecorator extends AbstractBeanDecorator {

	@Autowired
	private IProcessStepAdvanceService processStepAdvanceService;
	
	@Override
	public void decorate(Object bean, String property, Object strategy) {		
		try {
			Object stepCode = PropertyUtils.getProperty(bean, property);		
			if(stepCode != null){
				ProcessStep processStep = processStepAdvanceService.loadByUnique((String) stepCode);								
				if(processStep != null)
					PropertyUtils.setProperty(bean, (String)strategy, processStep.getStepDesc());				
			}
		} catch (NullPointerException | IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {			
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}

}
