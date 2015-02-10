package ntnu.stud.steinkso.logcollector.backend;

public class LoggerRequest {
	private String url;
	private String body;
	private HTTPListener listener;
	private boolean inTransit = false;

	public LoggerRequest(String url, String body, HTTPListener listener) {
		this.url = url; 
		this.body = body;
		this.listener = listener;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public HTTPListener getListener() {
		return listener;
	}

	public void setListener(HTTPListener listener) {
		this.listener = listener;
	}

	public boolean isInTransit() {
		return inTransit;
	}

	public void setInTransit(boolean inTransit) {
		this.inTransit = inTransit;
	}
	

}
