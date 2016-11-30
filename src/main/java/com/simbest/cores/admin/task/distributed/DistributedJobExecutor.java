/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.admin.task.distributed;

import com.simbest.cores.admin.task.schedule.QuartzJob;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-11-30  18:47 
 */
public abstract class DistributedJobExecutor implements QuartzJob {

    @Autowired
    private DistributedMasterUtil masterUtil;

    public boolean checkMasterIsMe() {
        return masterUtil.checkMasterIsMe();
    }
}
