/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.admin.task.distributed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用途： 测试任务样例
 * 作者: lishuyi 
 * 时间: 2016-11-30  18:57 
 */
@Component
public class MyJob extends DistributedJobExecutor{
    protected transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DistributedMasterUtil masterUtil;

    @Scheduled(cron = "0/5 * * * * ?")
    @Override
    public void execute() {
        if(checkMasterIsMe()) {
            log.trace(masterUtil.getServerIP() + ":" + masterUtil.getServerPort() + " is running jog------");
        }
    }
}
