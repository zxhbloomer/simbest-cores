package com.simbest.cores.utils.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import com.simbest.cores.exceptions.Exceptions;

public class LocalHttpClient {

	protected static HttpClient httpClient = HttpClientFactory.createHttpClient(100,10);

	private static Map<String,HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();
	public static void init(int maxTotal,int maxPerRoute){
		httpClient = HttpClientFactory.createHttpClient(maxTotal,maxPerRoute);
	}

	/**
	 * 初始化   MCH HttpClient KeyStore
	 * @param mch_id
	 * @param keyStoreFilePath
	 */
	public static void initMchKeyStore(String mch_id, String keyStoreFilePath){
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			File keyStoreFile = new File(keyStoreFilePath);
			FileInputStream instream = new FileInputStream(keyStoreFile);
			keyStore.load(instream,mch_id.toCharArray());
			instream.close();
			HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id, new String[]{"TLSv1"});
			httpClient_mchKeyStore.put(mch_id, httpClient);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			Exceptions.printException(e);
		}
	}


	public static HttpResponse execute(HttpUriRequest request){
		try {
			return httpClient.execute(request);
		} catch (IOException e) {
			Exceptions.printException(e);
		} 
		return null;
	}

	public static <T> T execute(HttpUriRequest request,ResponseHandler<T> responseHandler){
		try {
			return httpClient.execute(request, responseHandler);
		} catch (IOException e) {
			Exceptions.printException(e);
		} 
		return null;
	}

	/**
	 * 数据返回自动JSON对象解析
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T executeJsonResult(HttpUriRequest request,Class<T> clazz){
		return execute(request, JsonResponseHandler.createResponseHandler(clazz));
	}

	/**
	 * 数据返回自动XML对象解析
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T executeXmlResult(HttpUriRequest request,Class<T> clazz){
		return execute(request,XmlResponseHandler.createResponseHandler(clazz));
	}

	/**
	 * 数据返回直接为响应字符串
	 * @param request
	 * @return
	 */
	public static String executeStringResult(HttpUriRequest request){
		return execute(request,StringResponseHandler.createResponseHandler());
	}
	
	/**
	 * MCH keyStore 请求 数据返回自动XML对象解析
	 * @param mch_id
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T keyStoreExecuteXmlResult(String mch_id,HttpUriRequest request,Class<T> clazz){
		try {
			return httpClient_mchKeyStore.get(mch_id).execute(request,XmlResponseHandler.createResponseHandler(clazz));
		} catch (ClientProtocolException e) {
			Exceptions.printException(e);;
		} catch (IOException e) {
			Exceptions.printException(e);;
		}
		return null;
	}
}
