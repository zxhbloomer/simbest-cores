/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.admin.task.distributed;

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

    @Autowired
    private DistributedMasterUtil masterUtil;

    @Scheduled(cron = "0/1 * * * * ?")
    @Override
    public void execute() {
        if(checkMasterIsMe()) {
            System.out.println(masterUtil.getServerIP() + ":" + masterUtil.getServerPort() + " running jog------");
        }
    }
}
