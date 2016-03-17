/**
 * 
 */
package com.simbest.cores.utils.decorators;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.enums.GenericEnum;

/**
 * @author lishuyi
 *
 */
@Component
public class EnumsDecorator extends AbstractBeanDecorator {
	
	@Override
	public void decorate(Object bean, String property, Object strategy) {
		try {
			GenericEnum someEnum = (GenericEnum) PropertyUtils.getProperty(bean, property);
			if (someEnum != null) {
				PropertyUtils.setProperty(bean, (String)strategy, someEnum.getValue());
			}
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
}
