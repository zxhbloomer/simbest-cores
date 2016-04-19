/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：验证HTTP Referer字段防御CSRF攻击
 * 作者: lishuyi 
 * 时间: 2016-04-20  0:11
 * 参考：
 * https://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/
 * http://blog.csdn.net/u012228718/article/details/39521677
 */
public class RefererFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        // 必须的
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 链接来源地址，通过获取请求头 referer 得到
        String referer = request.getHeader("referer");

        if (referer != null && !referer.contains(request.getServerName())) {//本站点访问，则有效
            /**
             * 如果 链接地址来自其他网站，则返回错误图片
             */
            request.getRequestDispatcher("/html/errors/404.html").forward(request, response);
        } else {
            /**
             * 图片正常显示
             */
            chain.doFilter(request, response);
        }

    }

    public void destroy() {
    }
}
