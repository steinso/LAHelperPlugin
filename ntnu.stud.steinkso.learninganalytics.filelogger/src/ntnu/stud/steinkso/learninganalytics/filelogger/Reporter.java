package ntnu.stud.steinkso.learninganalytics.filelogger;

import java.util.ArrayList;
import java.util.Iterator;

import ntnu.stud.steinkso.learninganalytics.LearningAnalyticsPlugin;
import ntnu.stud.steinkso.logcollector.ErrorHandler;
import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

/**
 * This is the resource change listener. When it finds a .java file that has
 * changed, or a markers file that has changed, it will send it to the server
 * 
 * @author stein
 *
 */

public class Reporter implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		//Don't do anything if logging is inactive
		LearningAnalyticsPlugin learningAnalytics = LearningAnalyticsPlugin.getDefault();

			System.out.println("EVENT: resource changed, is logging active: "+learningAnalytics.getLoggingIsActive());
		if(!learningAnalytics.getLoggingIsActive()){
			System.out.println("EVENT: resource changed, but logging not active");
			return;
		}

		if (!isCorrectEvent(event))
			return;

		registerSendMarkers();
		traverseChangedResources(event);
	}

	private boolean isCorrectEvent(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			return true;
		}
		return false;
	}

	private void registerSendMarkers() {
		LoggerPlugin.getDefault().setSendMarkers(true);
	}

	private void traverseChangedResources(IResourceChangeEvent event) {
		IResourceDelta rootDelta = event.getDelta();
		ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();

		try {
			rootDelta.accept(visitor);
			ArrayList<IResource> resources = visitor.getSuitableResources();
			ArrayList<IResourceDelta> deltas = visitor.getDeltas();
			ArrayList<LoggerResource> resourceList = new ArrayList<LoggerResource>();

            Iterator<IResource> iterator = resources.iterator();
		    Iterator<IResourceDelta> deltaiterator = deltas.iterator();

		    while(iterator.hasNext()){
		    	resourceList.add(new LoggerResource((IResource) iterator.next(),deltaiterator.next()));
            }

			if (resources.size() > 0) {
				LearningAnalyticsPlugin.getDefault().addResourcesToCurrentLogState(resourceList);
			}

		} catch (CoreException e) {
			ErrorHandler.logError("Error running visitor on file change deltas",e);
			System.out.println("Error running visitor on file change deltas");
		}
	}
}
