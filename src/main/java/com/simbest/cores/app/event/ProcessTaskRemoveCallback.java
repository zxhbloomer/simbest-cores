/**
 * 
 */
package com.simbest.cores.app.event;

import com.simbest.cores.app.model.ProcessTask;

/**
 * 流程待办删除时回调
 * 
 * @author lishuyi
 *
 */
public interface ProcessTaskRemoveCallback {
	
	void execute(ProcessTask processTask);
	
}
