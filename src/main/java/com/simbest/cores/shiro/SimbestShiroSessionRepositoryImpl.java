/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.shiro;

import io.longyuan.shiro.redissession.service.impl.ShiroSessionRepositoryImpl;
import org.apache.shiro.session.Session;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-12-29  21:41 
 */
public class SimbestShiroSessionRepositoryImpl extends ShiroSessionRepositoryImpl {

    @Override
    public Collection<Session> getAllSessions() {
        Set<Session> sessions = new HashSet<Session>();
        Set<String> keys = getRedisTemplate().keys("shiro-session:*");
        if(keys != null && keys.size()>0){
            for(String key:keys){
                Session session = (Session)this.getRedisTemplate().boundValueOps(key).get();
                sessions.add(session);
            }
        }
        return sessions;
    }
}
