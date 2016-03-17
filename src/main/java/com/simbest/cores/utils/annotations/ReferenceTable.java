/**
 * 
 */
package com.simbest.cores.utils.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * @author lishuyi
 *
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface ReferenceTable {
	String table() default "";
	String value() default "";
}
