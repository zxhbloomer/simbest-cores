/**
 * 
 */
package com.simbest.cores.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.ListObjectsRequest;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.configs.CoreConfig;

/**
 * 文件上传工具类
 * 
 * @author lishuyi
 *
 */
@Component
public class AppFileUtils {
	private static transient final Log log = LogFactory.getLog(AppFileUtils.class);
	
	public enum StoreLocation {Cloud, Disk}

    @Autowired
	private CoreConfig coreConfig;
    
    private static String host = "http://bj.bcebos.com";
    private String apikey = null;
    private String secretKey = null;
    private String bucket = null;
    private StoreLocation location = null;
    private BosClient bosClient = null;

    @PostConstruct
	public void init(){	
    	apikey = coreConfig.getValue("bae.app.apikey");
    	secretKey = coreConfig.getValue("bae.app.secretkey");
    	bucket = coreConfig.getValue("bae.bcs.bucket");
    	location = Enum.valueOf(StoreLocation.class, coreConfig.getValue("app.upload.file.store"));		
	    BosClientConfiguration config = new BosClientConfiguration();
	    config.setCredentials(new DefaultBceCredentials(apikey, secretKey));
	    config.setEndpoint(host);
	    // config.setEndpoint(coreConfig.getValue("app.domain"));
	    // 设置HTTP最大连接数为10
	    config.setMaxConnections(10);
	    bosClient = new BosClient(config);	  
	}
    
    public static void main(String[] args) {
    	AppFileUtils utils = new AppFileUtils();
    	utils.apikey = "e46c7e6c281a4d3da7545c685bb5db1c";
    	utils.secretKey = "89d1c208bee145f49ca88ef1bbd12302";
    	utils.bucket = "onegymbucket";
    	utils.location = StoreLocation.Cloud;
    	BosClientConfiguration config = new BosClientConfiguration();
  	    config.setCredentials(new DefaultBceCredentials(utils.apikey, utils.secretKey));
  	    config.setEndpoint(host);
  	    // config.setEndpoint(coreConfig.getValue("app.domain"));
  	    // 设置HTTP最大连接数为10
  	    config.setMaxConnections(10);
  	    BosClient bosClient = new BosClient(config);	
  	    // 构造ListObjectsRequest请求
  	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest(utils.bucket);
  	    // 递归列出fun目录下的所有文件
  	    listObjectsRequest.setPrefix("static/uploadFiles/manager1/2015-11-12");
  	    ListObjectsResponse listing = bosClient.listObjects(listObjectsRequest);
  	    // 遍历所有Object
  	    System.out.println("Objects:");
  	    for (BosObjectSummary objectSummary : listing.getContents()) {
  	    	System.out.println(objectSummary.getKey());
  	    }  	  
  	    
  	    System.out.println(StringUtils.substringAfterLast("http://bj.bcebos.com/v1/onegymbucket/static/uploadFiles/manager1/2015-11-12/483bdc2b9ee8413d99e91293237615c9/ccc.jpg", "onegymbucket/"));
	}

    public File downloadFromUrl(String fileUrl, String fileName, String storePath){
        File targetFile = null;
        HttpURLConnection conn = null;
        try{
            URL url = new URL(fileUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod(Constants.HTTPGET);
            conn.connect();
            if(conn.getContentLengthLong() != 0) {
                //存储时，存放应用磁盘Context路径
                String savePath = getFileLocation() + storePath + fileName + getFileExtByContentType(conn.getHeaderField(Constants.CONTENT_TYPE));
                targetFile = new File(savePath);
                FileUtils.copyURLToFile(url, targetFile);
                conn.disconnect();
                log.debug(String.format("Downloaded file from url: %s, and save at: %s", fileUrl, targetFile.getAbsolutePath()));
            }else{
                log.error(String.format("Want to download file from %s, but get nothing......",fileUrl));
            }
        }catch(Exception e){
            if(conn != null){
                conn.disconnect();
                conn = null;
            }
            log.error(Exceptions.getStackTraceAsString(e));
        }finally{
            if(conn != null){
                conn.disconnect();
            }
        }
        return targetFile;
    }

	/**
	 * 将文件由远程URL读取后，再上传至云存储或应用系统
	 * 
	 * @param fileUrl  远程文件URL
	 * @param fileName 存储文件名称
	 * @param storePath 云存储或应用Context路径下的相对路径，以/开头，并且以/结束
	 * @return 返回存储路径
	 */
	public String uploadFromUrl(String fileUrl, String fileName, String storePath){
		//fileName = AppCodeGenerator.cn2En(fileName);
		String savePath = null;
		HttpURLConnection conn = null;
		try{
			URL url = new URL(fileUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod(Constants.HTTPGET);				
			conn.connect();
			switch(location){
				case Cloud:
					storePath += fileName+getFileExtByContentType(conn.getHeaderField(Constants.CONTENT_TYPE));
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(conn.getContentLengthLong());
					meta.setContentType(conn.getContentType());
					bosClient.putObject(bucket, storePath, conn.getInputStream(), meta);												
					savePath = bosClient.generatePresignedUrl(bucket, storePath, -1).toString()+"&responseContentDisposition=attachment";
			    	int index = savePath.indexOf("?authorization");
			    	savePath = savePath.substring(0, index);
					break;
				case Disk:			
					//存储时，存放应用磁盘Context路径
					savePath = getFileLocation()+storePath+fileName+getFileExtByContentType(conn.getHeaderField(Constants.CONTENT_TYPE));
					FileUtils.copyURLToFile(url, new File(savePath));
					//访问时，访问应用URL路径
					String baseUrl = getBaseUrl();
					savePath = baseUrl+storePath+fileName+getFileExtByContentType(conn.getHeaderField(Constants.CONTENT_TYPE));
					break;
				default:
					break;
			}
			conn.disconnect();
		} catch(Exception e){
			if(conn != null){
				conn.disconnect();
				conn = null;
			}
			log.error(Exceptions.getStackTraceAsString(e));
		}finally{
			if(conn != null){
				conn.disconnect();
                conn = null;
			}
		}
		savePath = StringUtils.replace(savePath, Constants.SEPARATOR, "/");
		log.debug("savePath:"+savePath);
		return savePath;
	}
	
	/**
	 * 客户端通过应用系统上传附件至云存储或系统应用
	 * 
	 * @param file 浏览器接收文件
	 * @param fileName 存储文件名称
	 * @param storePath 云存储或应用Context路径下的相对路径，以/开头，并且以/结束
	 * @return 返回存储路径
	 */
	public String uploadFromClient(MultipartFile file, String fileName, String storePath){
		//fileName = AppCodeGenerator.cn2En(fileName);
		String savePath = null;
		File targetFile;
		try{
			switch(location){
				case Cloud:							
					storePath += fileName;
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(file.getSize());
					meta.setContentType(file.getContentType());
					bosClient.putObject(bucket, storePath, file.getInputStream(), meta);												
					savePath = bosClient.generatePresignedUrl(bucket, storePath, -1).toString()+"&responseContentDisposition=attachment";
			    	int index = savePath.indexOf("?authorization");
			    	savePath = savePath.substring(0, index);
					break;
				case Disk:					
					//存储时，存放应用磁盘Context路径
					targetFile = new File(getFileLocation()+storePath+fileName);
					FileUtils.touch(targetFile); //覆盖文件
					file.transferTo(targetFile);
					//访问时，访问应用URL路径
					String baseUrl = getBaseUrl();
					savePath = baseUrl+storePath+fileName;
					break;
				default:
					break;
			}
		} catch(Exception e){
			log.error(Exceptions.getStackTraceAsString(e));
		}
		savePath = StringUtils.replace(savePath, Constants.SEPARATOR, "/");
		log.debug("savePath:"+savePath);
		return savePath;
	}
	
	/**
	 * 将应用系统Context路径下的文件上传至云存储，或者直接将Context路径下的文件转换为Http访问路径
	 * 
	 * @param targetFile 应用系统Context路径下的文件
	 * @param storePath  云存储或应用Context路径下的相对路径，以/开头，并且以/结束
	 * @return 返回存储路径
	 */
	public String uploadFromLocal(File targetFile, String storePath){
		String savePath = null;
        FileInputStream fis = null;
        try{
			switch(location){
				case Cloud:
					storePath += targetFile.getName();
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(targetFile.length());
					meta.setContentType(getContentType(targetFile.getAbsolutePath()));
                    fis = new FileInputStream(targetFile);
					bosClient.putObject(bucket, storePath, fis, meta);
					savePath = bosClient.generatePresignedUrl(bucket, storePath, -1).toString()+"&responseContentDisposition=attachment";
			    	int index = savePath.indexOf("?authorization");
			    	savePath = savePath.substring(0, index);
					break;
				case Disk:	
					//直接将Context路径下的文件转换为Http访问路径
					String baseUrl = getBaseUrl();
					savePath = baseUrl+storePath+targetFile.getName();
				default:
					break;
			}
		}catch(FileNotFoundException e){
			log.error(Exceptions.getStackTraceAsString(e));
		}finally {
            if(null != fis)
                try {
                    fis.close();
                } catch (IOException e) {
                }
        }
        savePath = StringUtils.replace(savePath, Constants.SEPARATOR, "/");
		log.debug("savePath:"+savePath);
		return savePath;
	}
	
	/**
	 * 将图片生成文件，并存放在应用系统Context路径下，或者上传至云存储
	 * 
	 * @param image 图片
	 * @param storePath  云存储或应用Context路径下的相对路径，以/开头，并且以/结束
	 * @return 返回存储路径
	 */
	public String uploadBufferedImage(BufferedImage image, String storePath){
		String savePath = null;
		String fileName = AppCodeGenerator.nextSystemUUID()+".png";
		try {
			File targetFile = new File(getFileLocation()+storePath+fileName);
			FileUtils.touch(targetFile); //创建空文件
			ImageIO.write(image, "png", targetFile);
			String baseUrl = getBaseUrl();
			savePath = baseUrl+storePath+fileName;
		} catch (IOException e) {
			Exceptions.printException(e);
		}
		log.debug("savePath:"+savePath);
		return savePath;
	}
	
	/**
	 * 
	 * 将应用系统Context路径下的图片压缩后，再上传至云存储
	 * 
	 * 图片高质量压缩参考：http://www.lac.inpe.br/JIPCookbook/6040-howto-compressimages.jsp
	 * 
	 * @param imageFile 应用系统Context路径下的图片
	 * @param quality   压缩质量
	 * @param storePath 云存储或应用Context路径下的相对路径，以/开头，并且以/结束
	 * @return 返回存储路径
	 */
	public String uploadCompressImage(File imageFile, float quality, String storePath){
		String savePath = null;
		ByteArrayOutputStream baos = null;
		ImageOutputStream ios = null;
		ByteArrayInputStream bais = null;
		ImageWriter writer;
		try{
			BufferedImage image = ImageIO.read(imageFile);
			Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpg");
			if (!writers.hasNext())
				throw new IllegalStateException("No writers found");
			writer = (ImageWriter) writers.next();
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(quality);
			baos = new ByteArrayOutputStream(32768);
			switch(location){
				case Cloud:
					storePath += "compressed"+imageFile.getName();
                    bais = new ByteArrayInputStream(baos.toByteArray());
					bosClient.putObject(bucket, storePath, bais);					
					savePath = bosClient.generatePresignedUrl(bucket, storePath, -1).toString()+"&responseContentDisposition=attachment";																
			    	int index = savePath.indexOf("?authorization");
			    	savePath = savePath.substring(0, index);
                    if(bais != null)
                        try {
                            bais.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
					break;
				case Disk:
					//存储时，存放应用磁盘Context路径
					ios = ImageIO.createImageOutputStream(baos);
					File compressedFile = new File(getFileLocation()+storePath+"compressed"+imageFile.getName());
					FileImageOutputStream output = new FileImageOutputStream(compressedFile);
					writer.setOutput(output);
					writer.write(null, new IIOImage(image, null, null), param);
					ios.flush(); 
					//访问时，访问应用URL路径
					String baseUrl = getBaseUrl();
					savePath = baseUrl+storePath+compressedFile.getName();
					break;
				default:
					break;
			}
		}catch(IOException e){
            Exceptions.printException(e);
            if(baos != null)
                try {
                    baos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			if(ios != null)
                try {
                    ios.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
		savePath = StringUtils.replace(savePath, Constants.SEPARATOR, "/");
		log.debug("savePath:"+savePath);
		return savePath;
	}
	
	/**
	 * 根据文件访问URL, 将文件从云存储或应用系统Context路径下的文件删除
	 * @param fileUrl
	 */
	public void deleteFile(String fileUrl){
		if(!StringUtils.isEmpty(fileUrl)){
			switch(location){
				case Cloud:
					final String remoteUrl = Encodes.urlDecode(fileUrl);	
					String objectKey = null;
					try{
						objectKey = StringUtils.substringAfterLast(remoteUrl, coreConfig.getValue("bae.bcs.bucket")+"/");
						BosObject object = bosClient.getObject(bucket, objectKey);
						//bosClient.deleteObject(bucket, object.getKey());
					}catch(Exception e){
						log.error(String.format("Delete File failed with objectKey: %s remoteUrl: %s ......", objectKey, remoteUrl));
					}
					break;
				case Disk:				
					String filePath = null;
					try {
						filePath = getFileLocation()+fileUrl.substring(fileUrl.indexOf("/static"));
						//FileUtils.forceDelete(new File(filePath));
                        log.debug(filePath);
					} catch (Exception e) {
						log.error(String.format("Delete File failed with filePath: %s ......", filePath));
					}
					break;
				default:
					break;
			}		
		}
	}
	
	/**
	 * 根据文件访问URL列表, 将文件从云存储或应用系统Context路径下的文件删除
	 * @param fileUrls
	 * @return 返回存储路径
	 */
	public Integer deleteFiles(Collection<String> fileUrls){
		int count = 0;
		AsyncTaskExecutor executor = new SimpleAsyncTaskExecutor(); 
		for(String url:fileUrls){
			final String fileUrl = StringUtils.substringAfterLast(url, coreConfig.getValue("bae.bcs.bucket")+"/");			
			try {
				Future<Integer> future = executor.submit(new Callable<Integer>(){
					@Override
					public Integer call() throws Exception {
						deleteFile(fileUrl);
						return 1;
					}});  
				count += future.get();
			} catch (InterruptedException | ExecutionException e) {
				Exceptions.printException(e);
			} 
		}
		return count;
	}
	
	/**
	 * 根据文件返回文件后缀
	 * @param fileName
	 * @return 返回存储路径
	 */
	public static String getFileExtByName(String fileName){
		return fileName.substring(fileName.lastIndexOf(Constants.DOT)+1);
	}
	
	/**
	 * 根据内容类型返回文件后缀
	 * 
	 * @param contentType 内容类型
	 * @return 返回存储路径
	 */
	public static String getFileExtByContentType(String contentType) {
		String fileEndWitsh = "";
		if ("image/jpeg".equals(contentType))
			fileEndWitsh = ".jpg";
		else if ("image/png".equals(contentType))
			fileEndWitsh = ".png";
		else if ("audio/mpeg".equals(contentType))
			fileEndWitsh = ".mp3";
		else if ("audio/amr".equals(contentType))
			fileEndWitsh = ".amr";
		else if ("video/mp4".equals(contentType))
			fileEndWitsh = ".mp4";
		else if ("video/mpeg4".equals(contentType))
			fileEndWitsh = ".mp4";
		return fileEndWitsh;
	}
    
	/**
	 * 根据文件路径获取文件contentType
	 * @param pathToFile 文件路径
	 * @return 返回存储路径
	 */
    public static String getContentType(String pathToFile) {        
        Path path = Paths.get(pathToFile);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
            if(StringUtils.isEmpty(contentType))
                contentType = getFileExtByName(pathToFile);
        } catch (IOException e) {
        	Exceptions.printException(e);
        }
        return contentType;      
    }

    public static boolean isWord(String pathToFile){
        String contentType = getContentType(pathToFile);
        if("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType))
            return true;
        else if("application/msword".equals(contentType))
            return true;
        else if("doc".equals(contentType))
            return true;
        else if("docx".equals(contentType))
            return true;
        else
            return false;
    }

    public static boolean isExcel(String pathToFile){
        String contentType = getContentType(pathToFile);
        if("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType))
            return true;
        else if("application/vnd.ms-excel".equals(contentType))
            return true;
        else if("xls".equals(contentType))
            return true;
        else if("xlsx".equals(contentType))
            return true;
        else
            return false;
    }

    public static boolean isOfficeFile(String pathToFile){
        if(isExcel(pathToFile) || isWord(pathToFile))
            return true;
        else
            return false;
    }

    public static boolean isImage(String pathToFile){
        String contentType = getContentType(pathToFile);
        if("image/jpeg".equals(contentType))
            return true;
        else if("image/gif".equals(contentType))
            return true;
        else if("image/png".equals(contentType))
            return true;
        else if("image/bmp".equals(contentType))
            return true;
        else if("jpeg".equals(contentType))
            return true;
        else if("jpg".equals(contentType))
            return true;
        else if("gif".equals(contentType))
            return true;
        else if("png".equals(contentType))
            return true;
        else if("bmp".equals(contentType))
            return true;
        else
            return false;
    }

    public static boolean isTxt(String pathToFile){
        String contentType = getContentType(pathToFile);
        if("text/plain".equals(contentType))
            return true;
        else if("txt".equals(contentType))
            return true;
        else
            return false;
    }

    public static boolean isCompressFile(String pathToFile){
        String contentType = getContentType(pathToFile);
        if("application/x-zip-compressed".equals(contentType) || "application/zip".equals(contentType))
            return true;
        else if("rar".equals(contentType))
            return true;
        else if("zip".equals(contentType))
            return true;
        else
            return false;
    }

    /**
     * 如果设置了绝对路径app.disk.location，则以绝对路径为主，否则使用应用目录下的相对路径
     * 结合Tomcat的Context的别名设置
     * @return 文件存储路径
     */
    public String getFileLocation(){
        String diskLocation = coreConfig.getValue("app.disk.location");
        if(StringUtils.isEmpty(diskLocation)){
            diskLocation = System.getProperty(coreConfig.getValue("app.root"));
        }
        return diskLocation;
    }

    /**
     * 若合Tomcat的Context的别名设置了文件存放的绝对路径，文件访问使用别名访问，否则使用应用路程
     * @return 文件访问前缀
     */
    public String getBaseUrl(){
        String baseUrl = coreConfig.getValue("app.path.alias");
        if(StringUtils.isEmpty(baseUrl)){
            baseUrl = "/"+coreConfig.getCtx();
        }
        return baseUrl;
    }
}
