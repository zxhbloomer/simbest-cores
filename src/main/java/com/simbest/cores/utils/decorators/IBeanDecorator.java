package com.simbest.cores.utils.decorators;


/**
 * @author lishuyi
 *
 */
public interface IBeanDecorator {

	/**
	 * 根据装饰策略strategy，将对象bean中的属性名称为property的值进行修饰
	 * @param bean
	 * @param property
	 * @param strategy
	 */
	void decorate(Object bean, String property, Object strategy);

}
