package ntnu.stud.steinkso.logcollector.backend;

public interface HTTPListener {
	
	public void onResponse(String response);

	public void handleError(int statusCode);
}
