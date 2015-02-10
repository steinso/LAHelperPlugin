package ntnu.stud.steinkso.logcollector.backend;

import java.util.ArrayList;

import ntnu.stud.steinkso.logcollector.ErrorHandler;
import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;
import ntnu.stud.steinkso.logcollector.internal.ResourceHelper;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

import com.google.gson.Gson;

public class ServerCommuncation {

	public static void SendResources(ArrayList<LoggerResource> resources) {


		// Create new thread for posting file to server
		try {

            String body = new Gson().toJson(resources); 
			HTTPHelper.post(LoggerPlugin.getDefault().createServerUrl("files"), body, null);

		} catch (Exception e) {
			ErrorHandler.logError("Could not convert resource to JSON", e);

		}

	}

	public static void sendErrorLog(ErrorRepresentation error) {

		final String body = new Gson().toJson(error);

		HTTPHelper.post(LoggerPlugin.getDefault().createServerUrl("errorLog"),
				body, null);

	}

	public static void sendEventLog(String msg) {

		final String body = msg;
		HTTPHelper.post(LoggerPlugin.getDefault().createServerUrl("eventLog"),
				body, null);
	}

	public static void createUser(HTTPListener listener) {
		HTTPHelper.post(
				LoggerPlugin.getDefault().createServerUrl("createUser"), "",
				listener);
	}

	public static void setClientName(String newName, HTTPListener listener) {

		String body = newName;
		HTTPHelper.post(
				LoggerPlugin.getDefault().createServerUrl("setClientName"),
				body, listener);
	}

	public static void sendClientParticipating(boolean participating) {
		String body = participating + "";
		HTTPHelper.post(
				LoggerPlugin.getDefault().createServerUrl(
						"setClientParticipating"), body, null);
	}

	public static void getDialogDisclaimer(HTTPListener listener) {
		HTTPHelper.post(
				LoggerPlugin.getDefault().createServerUrl(
						"getMessage/dialogDisclaimer"), "", listener);
	}

	public static void getPreferenceDisclaimer(HTTPListener listener) {
		HTTPHelper.post(
				LoggerPlugin.getDefault().createServerUrl(
						"getMessage/preferenceDisclaimer"), "", listener);
	}
}
