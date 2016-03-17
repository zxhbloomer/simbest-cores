package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.simbest.cores.utils.AppFileUtils;
import com.simbest.cores.utils.Encodes;

public class BCE {
	private static final String ACCESS_KEY_ID = "e46c7e6c281a4d3da7545c685bb5db1c";
	private static final String SECRET_ACCESS_KEY = "89d1c208bee145f49ca88ef1bbd12302";
	private static final String BUCKET = "onegymbucket";

	public static void main(String[] args) throws IOException {
	    String ENDPOINT = "http://bj.bcebos.com";
	    BosClientConfiguration config = new BosClientConfiguration();
	    config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
	    config.setEndpoint(ENDPOINT);
	    BosClient client = new BosClient(config);	    
	    File targetFile = new File("C:/Users/Li/Desktop/onegym/aaa.jpg");
	    String storePath = "/static/test/";
	    putObject(client, targetFile, storePath);
	    deleteObject(client, "http://bj.bcebos.com/v1/onegymbucket/static/test/aaa.jpg");
	}
	
	public static void putObject(BosClient client,File targetFile, String storePath) throws FileNotFoundException{		
		storePath += targetFile.getName();
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(targetFile.length());
		meta.setContentType(AppFileUtils.getContentType(targetFile.getAbsolutePath()));
		client.putObject(BUCKET, storePath, new FileInputStream(targetFile), meta);												
		String savePath = client.generatePresignedUrl(BUCKET, storePath, -1).toString()+"&responseContentDisposition=attachment";
    	int index = savePath.indexOf("?authorization");
    	savePath = savePath.substring(0, index);
    	System.out.println(savePath);
	}

	public static void listObject(BosClient client){
	    ListObjectsResponse listing = client.listObjects(BUCKET);
	    // 遍历所有Object
	    for (BosObjectSummary objectSummary : listing.getContents()) {
	        System.out.println("ObjectKey: " + objectSummary.getKey());
	    }
	}
	
	public static void deleteObject(BosClient client, String fileUrl){
		String remoteUrl = Encodes.urlDecode(fileUrl);	
		try{
			String url = StringUtils.substringAfterLast(remoteUrl, BUCKET+"/");
			BosObject object = client.getObject(BUCKET, url);
			client.deleteObject(BUCKET, object.getKey());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
