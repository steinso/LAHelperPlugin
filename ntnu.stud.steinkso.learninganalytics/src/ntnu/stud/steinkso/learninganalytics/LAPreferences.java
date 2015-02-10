package ntnu.stud.steinkso.learninganalytics;

import org.eclipse.jface.preference.IPreferenceStore;

public class LAPreferences {

	public static final String MARKERS_FILENAME = "MARKERS_FILENAME";
	public static final String TEST_FILENAME = "TEST_FILENAME";


	private IPreferenceStore preferenceStore;

	public LAPreferences(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}
	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
	public void setMarkerFileName(String markersFileName) {
		preferenceStore.setValue(MARKERS_FILENAME, markersFileName);
	}

	public String getMarkersFileName() {
		return preferenceStore.getString(MARKERS_FILENAME);
	}

	public String getTestFileName() {
		return preferenceStore.getString(TEST_FILENAME);
	}

	public void setTestFileName(String testFileName) {
		preferenceStore.setValue(TEST_FILENAME, testFileName);
	}
}
