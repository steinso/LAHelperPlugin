package ntnu.stud.steinkso.learninganalytics;

import java.util.ArrayList;
import java.util.Iterator;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LearningAnalyticsPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "ntnu.stud.steinkso.learninganalytics"; //$NON-NLS-1$

	private static LearningAnalyticsPlugin plugin;
	private ArrayList<LoggerResource> stateResources = new ArrayList<LoggerResource>();

	private LAPreferences preferences;
	
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
		//Remove current if already exist in the arrayList = overwritten
		ArrayList<LoggerResource> toBeRemoved;
		toBeRemoved = getDuplicateResourceStates(stateResources, loggerResources);

		stateResources.removeAll(toBeRemoved);
		stateResources.addAll(loggerResources);
	}
	
	private ArrayList<LoggerResource> getDuplicateResourceStates(ArrayList<LoggerResource> oldResources, 
                                                                 ArrayList<LoggerResource> newResources){
		Iterator<LoggerResource> iterator = newResources.iterator();
		ArrayList<LoggerResource> toBeRemoved = new ArrayList<LoggerResource>();

		while(iterator.hasNext()){
            LoggerResource newResource = iterator.next();

            Iterator<LoggerResource> currentIterator = oldResources.iterator();
            while(currentIterator.hasNext()){
            	LoggerResource resource = currentIterator.next();

            	if(resource.getPath().equals(newResource.getPath()) && resource.getName().equals(newResource.getName())){
            		toBeRemoved.add(resource);
            	}
            }
		}
		return toBeRemoved;
	}
	
	public void logCurrentState(){
		if(stateResources.size() > 0){
			//Create new arrayList to prevent race conditions
			ArrayList<LoggerResource> resources = new ArrayList<LoggerResource>(stateResources);
			stateResources.clear();
			LoggerPlugin.getDefault().logResources(resources);
		}
	}

	public boolean getLoggingIsActive() {
		return LoggerPlugin.getDefault().getPreferences().getLoggingIsActive();
	}

}
