/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.web;

import com.simbest.cores.exceptions.NotAllowUploadFileTypeException;
import com.simbest.cores.utils.AppFileUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-04-13  16:59 
 */
public class CustomMultipartResolver extends CommonsMultipartResolver {
    public transient final Log log = LogFactory.getLog(getClass());

    private Set<String> fileTypes;

    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        List<FileItem> fileItems = checkContentType(request, encoding);
        return parseFileItems(fileItems, encoding);
    }

    public List<FileItem> checkContentType(HttpServletRequest request, String encoding){
        if(null != fileTypes && fileTypes.size() > 0){
            FileUpload fileUpload = prepareFileUpload(encoding);
            try {
                List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
                for (FileItem item : fileItems) {
                    log.debug("client want to upload file with type: "+item.getContentType());
                    if (!fileTypes.contains(AppFileUtils.getFileExtByName(item.getName()))) {
                        throw new NotAllowUploadFileTypeException("Not allow upload file type exception occur... \r\n");
                    }
                }
                return fileItems;
            } catch (FileUploadException e) {
                throw new NotAllowUploadFileTypeException("Not allow upload file type exception occur... \r\n" + e.getMessage());
            }
        }
        return null;
    }

    public Set<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(Set<String> fileTypes) {
        this.fileTypes = fileTypes;
    }
}
