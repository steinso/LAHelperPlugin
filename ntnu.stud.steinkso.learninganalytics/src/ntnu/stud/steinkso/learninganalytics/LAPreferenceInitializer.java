package ntnu.stud.steinkso.learninganalytics;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class LAPreferenceInitializer extends
		AbstractPreferenceInitializer{

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = LearningAnalyticsPlugin.getDefault().getPreferenceStore();

		store.setDefault(LAPreferences.MARKERS_FILENAME,".markers.json");
        store.setDefault(LAPreferences.TEST_FILENAME,".tests.json");
	}
}