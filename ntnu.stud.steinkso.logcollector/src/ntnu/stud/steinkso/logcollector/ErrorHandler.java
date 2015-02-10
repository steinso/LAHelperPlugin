package ntnu.stud.steinkso.logcollector;

import com.google.gson.Gson;

import ntnu.stud.steinkso.logcollector.backend.ErrorRepresentation;
import ntnu.stud.steinkso.logcollector.backend.ServerCommuncation;

/**
 * This is a helper class for sending error logs to the server.
 * @author stan
 *
 */
public class ErrorHandler {

	public static void logError(String msg, Exception e){
		ErrorRepresentation error = new ErrorRepresentation(msg,e);
		sendErrorMessageToServer(error);
	}

	public static void logError(String msg) {
		ErrorRepresentation error = new ErrorRepresentation(msg);
		sendErrorMessageToServer(error);
	}

	public void logEvent(String msg){
		ServerCommuncation.sendEventLog(msg);
	}
	
	private static void sendErrorMessageToServer(ErrorRepresentation error) {
		printErrorMessage(error);
		ServerCommuncation.sendErrorLog(error);
	}

	private static void printErrorMessage(ErrorRepresentation error) {
		final String body = new Gson().toJson(error); 
		System.out.println(body);
	}



}
