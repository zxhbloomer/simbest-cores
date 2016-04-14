/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.web.filter;

import com.simbest.cores.utils.XssShieldUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 参数特殊字符过滤
 *
 * @author   单红宇(365384722)
 * @myblog  http://blog.csdn.net/catoop/
 * @create    2015年9月18日
 */
public class MHttpServletRequest extends HttpServletRequestWrapper {

    public MHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        /* 返回值之前 先进行过滤 */

        return XssShieldUtil.stripXss(super.getParameter(XssShieldUtil.stripXss(name)));
    }

    @Override
    public String[] getParameterValues(String name) {
        // 返回值之前 先进行过滤
        String[] values = super.getParameterValues(XssShieldUtil.stripXss(name));
        if(values != null){
            for (int i = 0; i < values.length; i++) {
                values[i] = XssShieldUtil.stripXss(values[i]);
            }
        }
        return values;
    }

}
