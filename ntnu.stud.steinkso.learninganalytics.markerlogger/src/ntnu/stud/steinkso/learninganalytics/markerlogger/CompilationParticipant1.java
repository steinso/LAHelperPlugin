package ntnu.stud.steinkso.learninganalytics.markerlogger;

import java.util.ArrayList;

import ntnu.stud.steinkso.learninganalytics.LearningAnalyticsPlugin;
import ntnu.stud.steinkso.logcollector.ErrorHandler;
import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.CompilationParticipant;


/**
 * This compilation participant runs when build has finished. It creates a
 * .markers.json file in the project directory where the markers of files are
 * stored in json format.
 * 
 * @author stan
 */

public class CompilationParticipant1 extends CompilationParticipant {

	// Specifies if the compilation participant should participate
	public boolean isActive(IJavaProject project) {
		return true;
	}

	// @override
	public void buildFinished(IJavaProject project) {

		//Don't do anything if logging is inactive
		if(!LearningAnalyticsPlugin.getDefault().getLoggingIsActive()){
			return;
		}

		try {
			sendCurrentWorkspaceMarkers(project);

		} catch (CoreException e) {
			ErrorHandler.logError("Could not save marker file",e);
			System.out.println("ERROR: Could not save marker file.");
		}
	}

	private void sendCurrentWorkspaceMarkers(IJavaProject project)
			throws CoreException {
		IResource resource = project.getResource();
		String markersJson;
		String markersFileName = LearningAnalyticsPlugin.getDefault().getPreferences().getMarkersFileName();

		LoggerMarkerList markerList = createMarkerList(resource);
		markersJson = convertMarkerListToJson(markerList);

		//LoggerPlugin.getDefault().saveFileInPluginDir(markersJson,markersFileName);
		LoggerResource loggerResource = new LoggerResource();
		loggerResource.setFileContents(markersJson);
		loggerResource.setName(markersFileName);
		loggerResource.setTypeOfChange(LoggerResource.CONTENT);
		loggerResource.setPath("/.markers.json");
		loggerResource.setType(LoggerResource.FILE);
		
		ArrayList<LoggerResource> loggerResourceList = new ArrayList<LoggerResource>();
		loggerResourceList.add(loggerResource);

		LearningAnalyticsPlugin.getDefault().addResourcesToCurrentLogState(loggerResourceList);
	}

	private String convertMarkerListToJson(LoggerMarkerList markerList) {
		String json = LoggerPlugin.getDefault().convertToJson(markerList); 
		return json;
	}

	private LoggerMarkerList createMarkerList(IResource resource) throws CoreException {

		IMarker[] markers = resource.findMarkers(null, true, -1);
		LoggerMarkerList markerList = new LoggerMarkerList(markers);
		return markerList;
		
	}

}