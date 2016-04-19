/**
 * 
 */
package com.simbest.cores.app.event;

import com.simbest.cores.app.model.ProcessTask;
import org.springframework.context.ApplicationEvent;

/**
 * @author lishuyi
 *
 */
public class ProcessTaskCreateCallbackEvent extends ApplicationEvent {


    private static final long serialVersionUID = 5604270358795528321L;

    private ProcessTaskCreateCallback createCallback;

    private ProcessTask processTask;


	public ProcessTaskCreateCallbackEvent(Object source, ProcessTaskCreateCallback createCallback, ProcessTask processTask) {
		super(source);
		this.createCallback = createCallback;
        this.processTask = processTask;
	}

    public ProcessTaskCreateCallback getCreateCallback() {
        return createCallback;
    }

    public void setCreateCallback(ProcessTaskCreateCallback createCallback) {
        this.createCallback = createCallback;
    }

    public ProcessTask getProcessTask() {
        return processTask;
    }

    public void setProcessTask(ProcessTask processTask) {
        this.processTask = processTask;
    }
}
