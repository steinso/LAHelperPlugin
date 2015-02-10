package ntnu.stud.steinkso.logcollector.backend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import ntnu.stud.steinkso.logcollector.ErrorHandler;
import ntnu.stud.steinkso.logcollector.internal.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPHelper {
	

	private static Queue<LoggerRequest> requestQueue = new LinkedList<LoggerRequest>();
	private static int maxRequestQueueSize = 80;
	private static boolean requestQueueSizeExceeded = false;
	private static int msRequestDelay = 100; 
	private static boolean performingRequestsLock = false;

	public static void post(String url, String body, HTTPListener listener) {
		LoggerRequest request = new LoggerRequest(url,body,listener);
		System.out.println("Performing post request [added to queue]:"+url);
		requestQueue.add(request);

		//Prevent several threads to race  
		if(performingRequestsLock){return;}

		performingRequestsLock = true;
		System.out.println("Queue not in lock, running requests");

		startRequestQueueThread();
	}
	
	private static void startRequestQueueThread(){

        Runnable r = new Runnable() {
			public void run() {
				try {
                    executeRequestsInQueue();
				} catch (Exception e) {
					System.out.println("ERROR: Could not connect to server");
				} finally{
					performingRequestsLock = false;
				}
			}
		};
		Thread t = new Thread(r, "runnable");
		t.start();	
	}

	private static void executeRequestsInQueue() throws Exception {

        Thread.sleep(msRequestDelay);

		LoggerRequest firstRequest = requestQueue.peek();
		if(firstRequest == null){
			System.out.println("Queue end!");
			return;
		}

		executeSingleRequest(firstRequest);
	}
		
	private static void executeSingleRequest(LoggerRequest request) throws Exception{
	
		String url = request.getUrl();
		String body = request.getBody();
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", "application/octet-stream");

		try {
			// Request parameters and other properties.
			ByteArrayEntity entitybody = new ByteArrayEntity(body.getBytes("UTF-8"));
			httppost.setEntity(entitybody);

			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Sent request to server: " + url);

			try {
                    handleResponse(response,request);
					httppost.abort();

			} catch (Exception ex) {
				httppost.abort();
			}

		} catch (Exception e) {

			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	private static void handleResponse(HttpResponse response, LoggerRequest request) throws Exception {

		int statusCode = response.getStatusLine().getStatusCode();

		if(statusCode ==HttpStatus.SC_OK){
			handleSuccess(response,request);
		}else{
			handleError(statusCode,request);
		}
	}
		
	private static void handleSuccess(HttpResponse response, LoggerRequest request) throws Exception{

		//Request went through!
		requestQueue.remove(request);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();

			try {
				String responseText = FileUtils.convertStreamToString(instream);
				
				if(request.getListener() !=null){
					request.getListener().onResponse(responseText);
				}

			} catch (Exception e) {
				throw e;
			} finally {
				instream.close();
				executeRequestsInQueue();
			}
		}
	}

	private static void handleError(int statusCode, LoggerRequest request) {
		
		if(requestQueue.size() >= maxRequestQueueSize){
			requestQueue.remove(request);
			request.getListener().handleError(statusCode);
			if(!requestQueueSizeExceeded){
				requestQueueSizeExceeded = true;
                ErrorHandler.logError("HTTP REQUEST QUEUE SIZE EXCEEDED on request to url:"+request.getUrl());
			}
		}
	}
}
