/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.exceptions;

import org.springframework.web.multipart.MultipartException;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-04-13  16:56 
 */
public class NotAllowUploadFileTypeException extends MultipartException {
    public NotAllowUploadFileTypeException(String msg) {
        super(msg);
    }
}
