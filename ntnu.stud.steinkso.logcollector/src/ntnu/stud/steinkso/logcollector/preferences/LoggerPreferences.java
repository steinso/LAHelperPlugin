package ntnu.stud.steinkso.logcollector.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.backend.ServerCommuncation;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;

public class LoggerPreferences implements
		org.eclipse.jface.util.IPropertyChangeListener {

	public static final String CLIENT_ID_NAME = "CLIENT_ID_NAME";
	public static final String CLIENT_ID = "CLIENT_ID";
	public static final String MARKERS_FILENAME = "MARKERS_FILENAME";
	public static final String TEST_FILENAME = "TEST_FILENAME";
	public static final String SERVER_URL = "SERVER_URL";
	public static final String ROOT_LOGGING_DIRECTORY = "ROOT_LOGGING_DIRECTORY";
	public static final String LOGGING_IS_DEACTIVATED = "LOGGING_IS_DEACTIVATED";
	public static final String DISCLAIMER_ANSWERED = "DISCLAIMER_ANSWERED";
	public static final String USER_PARTICIPATING = "USER_PARTICIPATING ";
	public static final String LOGGING_IS_ACTIVATED = "LOGGING_IS_ACTIVATED";
	public static final String MESSAGE_PREFERENCE_DISCLAIMER = "MESSAGE_PREFERENCE_DISCLAIMER";
	public static final String MESSAGE_DIALOG_DISCLAIMER = "MESSAGE_DIALOG_DISCLAIMER";
	public static final String MESSAGES_LAST_UPDATE_TIMESTAMP = "MESSAGE_LAST_UPDATE_TIMESTAMP";

	private boolean clientNameChangedFromHere = false;

	private IPreferenceStore preferenceStore;
	private ArrayList<Pattern> folderPatterns;

	public LoggerPreferences(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
		preferenceStore.addPropertyChangeListener(this);
		folderPatterns = new ArrayList<Pattern>();
		createFolderPatterns();
	}

	public void setMessagePreferenceDisclaimer(String value) {
		preferenceStore.setValue(MESSAGE_PREFERENCE_DISCLAIMER, value);
	}

	public String getMessagePreferenceDisclaimer() {
		return preferenceStore.getString(MESSAGE_PREFERENCE_DISCLAIMER);
	}

	public void setMessageDialogDisclaimer(String value) {
		preferenceStore.setValue(MESSAGE_DIALOG_DISCLAIMER, value);
	}

	public String getMessageDialogDisclaimer() {
		return preferenceStore.getString(MESSAGE_DIALOG_DISCLAIMER);
	}
	
	public void setMessagesLastUpdateTimestamp(String value){
		preferenceStore.setValue(MESSAGES_LAST_UPDATE_TIMESTAMP, value);
	}
	
	public String getMessagesLastUpdateTimestamp(){
		return preferenceStore.getString(MESSAGES_LAST_UPDATE_TIMESTAMP);
	}

	public void setDisclaimerAnswered(boolean value) {
		System.out.println("Setting discalimer answered:" + value);
		preferenceStore.setValue(DISCLAIMER_ANSWERED, value);
	}

	public boolean getDisclaimerAnswered() {
		System.out.println("Getting discalimer answered:"
				+ preferenceStore.getBoolean(DISCLAIMER_ANSWERED));
		return preferenceStore.getBoolean(DISCLAIMER_ANSWERED);
	}

	public void setUserParticipating(boolean participating) {
		preferenceStore.setValue(USER_PARTICIPATING, participating);
		//ServerCommuncation.sendClientParticipating(participating);
	}

	public boolean getUserParticipating() {
		return preferenceStore.getBoolean(USER_PARTICIPATING);
	}

	public void setRootLoggingDirectory(String path) {
		preferenceStore.setValue(ROOT_LOGGING_DIRECTORY, path);
	}

	public String getRootLoggingDirectory() {
		return preferenceStore.getString(ROOT_LOGGING_DIRECTORY);
	}

	public void setLoggingIsActive(boolean active) {
		System.out.println("Setting: Logging is:" + active);
		if (active) {
			preferenceStore
					.setValue(LOGGING_IS_ACTIVATED, LOGGING_IS_ACTIVATED);
		} else {
			preferenceStore.setValue(LOGGING_IS_ACTIVATED,
					LOGGING_IS_DEACTIVATED);
		}
	}

	public boolean getLoggingIsActive() {
		String active = preferenceStore.getString(LOGGING_IS_ACTIVATED);
		if (active.equals(LOGGING_IS_ACTIVATED)) {
			return true;
		} else {
			return false;
		}
	}

	public String getClientId() {
		System.out.println("Got client Id:"
				+ preferenceStore.getString(CLIENT_ID));
		return preferenceStore.getString(CLIENT_ID);
	}

	public void setClientId(String clientId) {
		System.out.println("Setting client Id:" + clientId);
		preferenceStore.setValue(CLIENT_ID, clientId);
	}

	public String getClientName() {
		System.out.println("Got client Name:"
				+ preferenceStore.getString(CLIENT_ID_NAME));
		return preferenceStore.getString(CLIENT_ID_NAME);
	}

	public void setClientName(String clientName) {
		clientNameChangedFromHere = true;
		System.out.println("Setting client Name:" + clientName);
		preferenceStore.setValue(CLIENT_ID_NAME, clientName);
	}

	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty() == CLIENT_ID_NAME) {

			// Event did not happen from preference page, was set
			// programatically through this
			if (clientNameChangedFromHere) {
				clientNameChangedFromHere = false;
				System.out
						.println("STopping event propagation, coming from myself");
				return;
			}
			LoggerPlugin.getDefault().onClientNameChange(
					(String) event.getNewValue(), (String) event.getOldValue());
		}

		if (event.getProperty() == LOGGING_IS_ACTIVATED) {
			String loggingActivated = (String) event.getNewValue();
			System.out.println("EVENT: Logging activated:" + loggingActivated);
			ServerCommuncation.sendClientParticipating(getLoggingIsActive());
		}

		if (event.getProperty() == ROOT_LOGGING_DIRECTORY) {
			createFolderPatterns();
		}

	}

	public void setMarkerFileName(String markersFileName) {
		preferenceStore.setValue(MARKERS_FILENAME, markersFileName);
	}

	public String getMarkersFileName() {
		return preferenceStore.getString(MARKERS_FILENAME);
	}

	public void setServerUrl(String url) {
		preferenceStore.setValue(SERVER_URL, url);
	}

	public String getServerUrl() {
		return preferenceStore.getString(SERVER_URL);
	}

	public String getTestFileName() {
		return preferenceStore.getString(TEST_FILENAME);
	}

	public void setTestFileName(String testFileName) {
		preferenceStore.setValue(TEST_FILENAME, testFileName);
	}

	public boolean isValidResourcePath(String path) {
		Iterator<Pattern> iterator = folderPatterns.iterator();

		while (iterator.hasNext()) {
			Pattern pattern = iterator.next();
			Matcher m = pattern.matcher(path);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}

	private void createFolderPatterns() {
		String patterns = getRootLoggingDirectory();
		String[] seperatePatterns = patterns.split(",");
		folderPatterns.clear();
		for (int i = 0; i < seperatePatterns.length; i++) {
			Pattern pattern = Pattern
					.compile(".*" + seperatePatterns[i] + ".*");
			folderPatterns.add(pattern);
		}
	}
}
