/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.web.filter;

import com.google.common.collect.Sets;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.Constants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * 用途：验证HTTP Referer字段防御CSRF攻击
 * 作者: lishuyi 
 * 时间: 2016-04-20  0:11
 * 参考：
 * https://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/
 * http://blog.csdn.net/u012228718/article/details/39521677
 */
public class RefererFilter implements Filter {

    private Set<String> whiteHostList = Sets.newHashSet();

    public void init(FilterConfig config) throws ServletException {
        String whiteHosts = config.getInitParameter("whiteHosts");
        if(StringUtils.isNotEmpty(whiteHosts)){
            String[] whiteHostss = whiteHosts.split(Constants.COMMA);
            for(String s:whiteHostss){
                whiteHostList.add(s.toLowerCase());
            }
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 链接来源地址，通过获取请求头 referer 得到
        String referer = request.getHeader("referer");
        try {
            if (referer != null) {
                URI referUri = new URI(referer);
                if (whiteHostList.size()>0 && !whiteHostList.contains(referUri.getHost().toLowerCase())) {
                    request.getRequestDispatcher("/html/errors/404.html").forward(request, response);
                } else {
                    chain.doFilter(request, response);
                }
            }else {
                    chain.doFilter(request, response);
            }
        } catch (URISyntaxException e) {
            Exceptions.printException(e);
        }

    }

    public void destroy() {
    }
}
