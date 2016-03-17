/**
 * 
 */
package com.simbest.cores.app.event;

import com.simbest.cores.app.model.ProcessTask;

/**
 * 流程待办创建时回调
 * 
 * @author lishuyi
 *
 */
public interface ProcessTaskCreateCallback {
	
	void execute(ProcessTask processTask);
	
}
