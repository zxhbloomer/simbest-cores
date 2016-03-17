/**
 * 
 */
package com.simbest.cores.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否需要记录操作日志
 * 
 * @author lishuyi
 *
 */ 
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface LogAudit {  
}
