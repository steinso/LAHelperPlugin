package ntnu.stud.steinkso.logcollector;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ntnu.stud.steinkso.logcollector.backend.HTTPListener;
import ntnu.stud.steinkso.logcollector.backend.ServerCommuncation;
import ntnu.stud.steinkso.logcollector.internal.FileUtils;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;
import ntnu.stud.steinkso.logcollector.preferences.LoggerPreferences;

import org.eclipse.core.resources.IWorkspace;
import org.osgi.framework.BundleContext;
import org.eclipse.core.resources.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.gson.Gson;

/**
 * Activator for the plugin. This is where the plugin is initialized. And the
 * common singleton
 * 
 * @author Stein
 *
 */
public class LoggerPlugin extends AbstractUIPlugin {
	/**
	 * TODO: Save participants
	 * 
	 * When plug-in writers implement their first resource change listener, they
	 * often encounter a dilemma caused by Eclipse's lazy plug-in loading
	 * behavior. Since plug-in activation may occur well after the workspace has
	 * started up, there is no opportunity add a resource change listener when
	 * the workspace is first started. This causes a "blind spot" for listeners,
	 * because they cannot process changes that occur between the time of
	 * workspace creation and the time when their plug-in is activated.
	 * 
	 * The solution to this problem is to take advantage of the save participant
	 * mechanism. Save participants implement the ISaveParticipant interface,
	 * and are installed using IWorkspace.addSaveParticipant. The main purpose
	 * of save participants is to allow plug-ins to save their important model
	 * state at the same time that the workspace saves its state. This ensures
	 * that the persisted workspace state stays synchronized with any domain
	 * model state that relies on it. Once a save participant is registered with
	 * the workspace, subsequent calls to addSaveParticipant will return an
	 * ISavedState object. By passing a resource change listener to
	 * ISavedState.processResourceChangeEvents, participants are given the
	 * opportunity to process the changes that have occurred since the last save
	 * occurred. This fills in the "blind spot" between workspace startup and
	 * activation of the plug-in that the listener belongs to. To find out about
	 * other facilities provided by the save participant mechanism, read the API
	 * Javadoc for ISaveParticipant, ISavedState, and ISaveContext.
	 * 
	 */

	public static final String PLUGIN_ID = "ntnu.stud.steinkso.logcollector"; //$NON-NLS-1
	private static LoggerPlugin plugin;

	private static BundleContext context;
	private LoggerPreferences preferences;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		LoggerPlugin.context = bundleContext;
		plugin = this;

		preferences = new LoggerPreferences(getPreferenceStore());
		System.out.println("Learning Analytics Logger started!");

		createClientIdIfNotExistAndInitializePlugin();
	}

	private void presentDisclaimerIfNotShown() {
		DisclaimerDialog DisclaimerDialog = new DisclaimerDialog();
	}

	public static LoggerPlugin getDefault() {
		return plugin;
	}

	private void createClientIdIfNotExistAndInitializePlugin() {

		String clientId = getPreferences().getClientId();

		if (clientId.equals("")) {
			ServerCommuncation.createUser(new HTTPListener() {
				@Override
				public void onResponse(String response) {
					onGotClientId(response);
				}

				@Override
				public void handleError(int statusCode) {
					ErrorHandler.logError("PLUGIN NOT INITIALIZED: Could not get client id from server:"
									+ statusCode);
				}
			});
		} else {
			System.out.println("UserID retrieved from preferences");
			initializePlugin();
		}
	}

	private void onGotClientId(String clientId) {

		if (validateClientId(clientId)) {
			preferences.setClientId(clientId);
			System.out.println("Got userid from server:" + clientId);
			initializePlugin();
		} else {
			ErrorHandler.logError("PLUGIN NOT INITIALIZED: Could not validate client id from server:"
							+ clientId);
		}
	}

	private void initializePlugin() {
		// The reporter is an onChange listener that runs if
		// changes happen on the files in the workspace
		// This also tells the CompilationParticipant whether
		// to create and send the markers file
		presentDisclaimerIfNotShown();
	}

	public String createServerUrl(String location) {
		String serverUrl = preferences.getServerUrl();
		if (location == "createUser") {
			return serverUrl + location;
		}
		
		if(location == "getMessage/dialogDisclaimer" || location == "getMessage/preferenceDisclaimer"){
			return serverUrl + location+"/"+preferences.getMessagesLastUpdateTimestamp();
		}

		return serverUrl + location + "/" + preferences.getClientId();
	}

	private boolean validateClientId(String clientId) {
		Pattern pattern = Pattern.compile("[A-z0-9]+");
		Matcher matcher = pattern.matcher(clientId);
		return matcher.matches();
	}

	public LoggerPreferences getPreferences() {
		return preferences;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		plugin = null;
		LoggerPlugin.context = null;
	}

	public void onClientNameChange(String newName, String oldName) {

		final String foldName = oldName;
		final String fnewName = newName;

		ServerCommuncation.setClientName(newName, new HTTPListener() {

			@Override
			public void onResponse(String response) {

			}

			@Override
			public void handleError(int statusCode) {
				preferences.setClientName(foldName);
				ErrorHandler.logError("Could not contact server for name change:" + statusCode);
			}
		});
	}
	
	public void logResources(ArrayList<LoggerResource> resourceList){

		if(preferences.getLoggingIsActive()){
                ServerCommuncation.SendResources(resourceList);
		}
	}
	
	public String convertToJson(Object obj){
		String json = new Gson().toJson(obj); 
		return json;
		
	}

	public boolean isValidResourcePath(String filePath) {
		return preferences.isValidResourcePath(filePath);
	}

/*	
	public void logEvents(Event[] event){
		
	}
	*/
}
