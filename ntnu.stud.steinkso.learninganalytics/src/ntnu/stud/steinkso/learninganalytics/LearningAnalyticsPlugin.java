package ntnu.stud.steinkso.learninganalytics;

import java.util.ArrayList;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LearningAnalyticsPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ntnu.stud.steinkso.learninganalytics"; //$NON-NLS-1$

	// The shared instance
	private static LearningAnalyticsPlugin plugin;
	private ArrayList<LoggerResource> stateResources = new ArrayList<LoggerResource>();

	private LAPreferences preferences;
	
	/**
	 * The constructor
	 */
	public LearningAnalyticsPlugin() {
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		preferences = new LAPreferences(getPreferenceStore());
	}

	public LAPreferences getPreferences() {
		return preferences;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static LearningAnalyticsPlugin getDefault() {
		return plugin;
	}
	
	public void addResourcesToCurrentLogState(ArrayList<LoggerResource> loggerResources) {
		stateResources.addAll(loggerResources);
		//logCurrentState();
	}
	
	public void logCurrentState(){
		if(stateResources.size() > 0){
			LoggerPlugin.getDefault().logResources(stateResources);
			stateResources.clear();
		}
	}


	public boolean getLoggingIsActive() {
		return true;
	}

}
