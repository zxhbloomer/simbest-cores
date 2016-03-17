package com.simbest.cores.utils.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.simbest.cores.utils.Constants;

public class XmlResponseHandler{
	private static transient final Log log = LogFactory.getLog(XmlResponseHandler.class);

	private static Map<String, ResponseHandler<?>> map = new HashMap<String, ResponseHandler<?>>();

	@SuppressWarnings("unchecked")
	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz){
		if(map.containsKey(clazz.getName())){
			return (ResponseHandler<T>)map.get(clazz.getName());
		}else{
			ResponseHandler<T> responseHandler = new ResponseHandler<T>() {
				@Override
				public T handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    String str = EntityUtils.toString(entity);
	                    log.debug(clazz.getSimpleName()+"  Serializable String value is:"+str);
	                    T o = XMLConverUtil.convertToObject(clazz,new String(str.getBytes("iso-8859-1"), Constants.CHARSET));
	                    log.debug(clazz.getSimpleName()+"  Deserializable Object value is:"+o);
	                   return o;
	                } else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }

				}
			};
			map.put(clazz.getName(), responseHandler);
			return responseHandler;
		}
	}

}
