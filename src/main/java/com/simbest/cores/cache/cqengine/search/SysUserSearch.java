/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.cache.cqengine.search;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.configs.CoreConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import com.simbest.cores.admin.authority.model.DynamicUserTreeNode;
import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysOrgAdvanceService;
import com.simbest.cores.admin.authority.service.ISysUserAdvanceService;
import com.simbest.cores.cache.cqengine.index.SysUserIndex;
import com.simbest.cores.utils.Constants;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-06-04  10:30
 *
 * 参考：
 * http://vishnu667.github.io/using-cqengine/
 * https://github.com/npgall/cqengine
 */
@Component
@CacheConfig(cacheNames = {"runtime:"}, cacheResolver="genericCacheResolver", keyGenerator="genericKeyGenerator")
public class SysUserSearch {
    public transient final Log log = LogFactory.getLog(getClass());

    private ISysOrgAdvanceService sysOrgAdvanceService;
    private ISysUserAdvanceService sysUserAdvanceService;

    private IndexedCollection<SysUserIndex> sysUserData = new ConcurrentIndexedCollection<SysUserIndex>();

    private SQLParser<SysUserIndex> parser = SQLParser.forPojoWithAttributes(SysUserIndex.class, createAttributes(SysUserIndex.class));

    @Autowired
    protected CoreConfig config;

    @Autowired
    public SysUserSearch(ISysOrgAdvanceService sysOrgAdvanceService, ISysUserAdvanceService sysUserAdvanceService) {
        this.sysOrgAdvanceService = sysOrgAdvanceService;
        this.sysUserAdvanceService = sysUserAdvanceService;
    }

    @PostConstruct
    public void init(){
        if(Boolean.valueOf(config.getValue("app.enable.cqengine"))) {
            //建立索引
            sysUserData.addIndex(HashIndex.onAttribute(SysUserIndex.LOGIN_NAME));
            sysUserData.addIndex(HashIndex.onAttribute(SysUserIndex.MP_NAME));
            sysUserData.addIndex(HashIndex.onAttribute(SysUserIndex.POSITION));

            //注册查询解析器
            parser.registerAttribute(SysUserIndex.LOGIN_NAME);
            parser.registerAttribute(SysUserIndex.MP_NAME);
            parser.registerAttribute(SysUserIndex.POSITION);

            AsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
            Future<String> future = executor.submit(new IndexTask(sysUserAdvanceService));
            try {
                log.debug(future.get());
            } catch (InterruptedException | ExecutionException e) {
                Exceptions.printException(e);
            }
        }

    }


     /**
     * 添加用户至CQEngine内存中
     * @param u
     */
    public boolean addSysUser(SysUser u){
        String parentIds = sysUserAdvanceService.getAllParentIdString(u.getSysOrg().getId());
        String[] parentIdArray = StringUtils.split(StringUtils.trim(parentIds), Constants.SPACE);
        List<SysOrg> parentSysOrgs = Lists.newArrayList();
        for(String parentId:parentIdArray){
            parentSysOrgs.add(sysOrgAdvanceService.loadByKey(Integer.valueOf(parentId)));
        }
        return addSysUserIndex(u, parentSysOrgs);
    }

    private boolean addSysUserIndex(SysUser u, List<SysOrg> parents){
        //建立索引的字段不可为空，否则忽略数据
        if(StringUtils.isNotEmpty(u.getLoginName()) && StringUtils.isNotEmpty(u.getMpNum()) && StringUtils.isNotEmpty(u.getPosition())){
            sysUserData.add(new SysUserIndex(u.getLoginName(), u.getMpNum(), u.getPosition(), u, u.getSysOrg(), parents));
            return true;
        }
        return false;
    }

    private ResultSet<SysUserIndex> searchQuery(SysUser u){
        String sql = "SELECT * FROM sysUserData WHERE (";
        if(StringUtils.isNotEmpty(u.getLoginName()))
            sql += " AND loginName='"+u.getLoginName()+"\'";
        if(StringUtils.isNotEmpty(u.getMpNum()))
            sql += " AND mpNum='"+u.getMpNum()+"\'";
        if(StringUtils.isNotEmpty(u.getPosition()))
            sql += " AND position='"+u.getPosition()+"\'";
        sql = StringUtils.replace(sql, "WHERE ( AND", "WHERE (" )+")";
        log.debug(sql);
        return parser.retrieve(sysUserData, sql);
    }

    /**
     * 根据所建立的索引字段搜索，并构建树
     * @param loginName
     * @param mpNum
     * @param position
     * @return
     */
    @Cacheable
    public List<DynamicUserTreeNode> searchDynamicUserTree(String loginName, String mpNum, String position){
        SysUser u = new SysUser();
        u.setLoginName(loginName);
        u.setMpNum(mpNum);
        u.setPosition(position);
        List<DynamicUserTreeNode> resultList = Lists.newArrayList();
        Set<SysOrg> unduplicatedOrgSet = Sets.newHashSet();
        ResultSet<SysUserIndex> list = searchQuery(u);
        for (SysUserIndex user : list) {
            DynamicUserTreeNode userNode = new DynamicUserTreeNode();
            userNode.setType("user");
            userNode.setChild(false);
            userNode.setId(user.getSysUser().getId());
            userNode.setPid(user.getSysOrg().getId());
            userNode.setTitle(user.getSysUser().getUsername());
            unduplicatedOrgSet.addAll(user.getParents());
            resultList.add(userNode);
        }
        for(SysOrg org : unduplicatedOrgSet){
            DynamicUserTreeNode orgNode = new DynamicUserTreeNode();
            Integer children = sysOrgAdvanceService.countByParent(org.getId());
            if(children != null && children>0)
                orgNode.setChild(true);
            else
                orgNode.setChild(false);
            orgNode.setType("org");
            orgNode.setId(org.getId());
            if(null != org.getParent())
                orgNode.setPid(org.getParent().getId());
            orgNode.setTitle(org.getOrgName());
            resultList.add(orgNode);
        }
        return resultList;
    }


    class IndexTask implements Callable<String> {
        private ISysUserAdvanceService sysUserAdvanceService;

        int count = 0;
        public IndexTask(ISysUserAdvanceService sysUserAdvanceService) {
            super();
            this.sysUserAdvanceService = sysUserAdvanceService;
        }

        @Override
        public String call() throws Exception {
            Collection<SysUser> users = sysUserAdvanceService.getAll();
            for(SysUser u:users){
                if(addSysUser(u))
                    count++;
            }
            return "Find "+users.size()+" records in redis, and indexed "+count+" records to the Google CQEngine !";
        }
    }
}
