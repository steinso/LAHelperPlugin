package ntnu.stud.steinkso.learninganalytics.markerlogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

/**
 * Logger representation of markers.
 * A list of markers grouped by filename
 * 
 * @author stan
 */
public class LoggerMarkerList {

	// Filename -> list of markers
	// where list of markers is a list of attributes
	private HashMap<String,ArrayList<Map<String,Object>>> listOfMarkers;

	public LoggerMarkerList(IMarker[] markers) throws CoreException {
		listOfMarkers = new HashMap<String,ArrayList<Map<String,Object>>>();

		for(int i=0; i < markers.length;i++){
			String filePath = markers[i].getResource().getFullPath().toString();

			boolean isValidPath =LoggerPlugin.getDefault().isValidResourcePath(filePath);
			if(isValidPath){
                 addMarker(markers[i]);
			}
		}
	}

	private void addMarker(IMarker imarker) throws CoreException {

		String fileName = imarker.getResource().getName();
		fileName = imarker.getResource().getFullPath().toString();

		ArrayList<Map<String,Object>> markerList;

		if(!listOfMarkers.containsKey(fileName)){
			markerList = new ArrayList<Map<String,Object>>();
			listOfMarkers.put(fileName, markerList);
		}

		markerList = listOfMarkers.get(fileName);
		markerList.add(imarker.getAttributes());
	}
}
