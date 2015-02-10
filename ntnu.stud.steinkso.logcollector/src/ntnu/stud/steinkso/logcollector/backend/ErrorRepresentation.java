package ntnu.stud.steinkso.logcollector.backend;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;


public class ErrorRepresentation {
	private String errorMessage;
	private String exceptionMessage;
	private String localExceptionMessage;
 	private String stackTrace;
	private String exceptionType;
	private String timeStamp;

	public ErrorRepresentation(String errorMessage, Exception e) {
		this.errorMessage = errorMessage;
		this.stackTrace = stringifyStackTrace(e);
		this.exceptionType = e.getClass().getName();
		this.exceptionMessage=e.getMessage();
		this.localExceptionMessage=e.getLocalizedMessage();
		this.timeStamp = new Timestamp(System.currentTimeMillis()).toString();
	}
	
	public ErrorRepresentation(String errorMessage) {
		this.errorMessage = errorMessage;
		this.timeStamp = new Timestamp(System.currentTimeMillis()).toString();
	}

	private String stringifyStackTrace(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();	
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getLocalExceptionMessage() {
		return localExceptionMessage;
	}

	public void setLocalExceptionMessage(String localExceptionMessage) {
		this.localExceptionMessage = localExceptionMessage;
	}
}
