/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 非法字符过滤器，用来处理request.getParamater中的非法字符。如<script>alert('123');</script>
 *
 * @author 单红宇(365384722)
 * @myblog http://blog.csdn.net/catoop/
 * @create 2015年9月18日
 */
public class IllegalCharacterFilter implements Filter {

    public transient final Log log = LogFactory.getLog(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        log.debug(((HttpServletRequest) req).getRequestURI());
        request = new MHttpServletRequest(request);
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {

    }

}
