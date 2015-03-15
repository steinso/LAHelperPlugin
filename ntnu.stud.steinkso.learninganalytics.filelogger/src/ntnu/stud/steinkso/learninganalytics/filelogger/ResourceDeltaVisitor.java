package ntnu.stud.steinkso.learninganalytics.filelogger;

import java.util.ArrayList;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class ResourceDeltaVisitor implements IResourceDeltaVisitor {

	private ArrayList<IResource> suitableResources;
	private ArrayList<IResourceDelta> deltas;

	public ResourceDeltaVisitor() {

		suitableResources = new ArrayList<IResource>();
		deltas = new ArrayList<IResourceDelta>();
	}

	public ArrayList<IResource> getSuitableResources() {
		return suitableResources;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		boolean isCorrectTypeOfChange = isCorrectTypeOfChange(delta);
		boolean isCorrectTypeOfResource = isCorrectTypeOfResource(delta);
		boolean isCorrectLocation = isCorrectLocation(delta);

		if (isCorrectLocation && isCorrectTypeOfChange && isCorrectTypeOfResource) {
			IResource resource = delta.getResource();
			suitableResources.add(resource);
			deltas.add(delta);
		}

		return true;
	}

	private boolean isCorrectLocation(IResourceDelta delta) {
		IResource resource = delta.getResource();
		String filePath = resource.getFullPath().toString();

		if(LoggerPlugin.getDefault().getPreferences().isValidResourcePath(filePath)){
			return true;
		}

		System.out.println("File does not match:"+filePath+" --> "+LoggerPlugin.getDefault().getPreferences().getRootLoggingDirectory());
		return false;
	}

	private boolean isCorrectTypeOfResource(IResourceDelta delta) {
		IResource resource = delta.getResource();
		

		if ("java".equalsIgnoreCase(resource.getFileExtension())) {
			return true;
		}

		if((resource.getType() & IResource.FOLDER) != 0 || (resource.getType() & IResource.PROJECT) != 0){
			return true;
		}

		return false;
	}

	private boolean isCorrectTypeOfChange(IResourceDelta delta) {

		// Resource has changed content or moved/copied 
		if ((delta.getKind() & IResourceDelta.CHANGED) != 0){ 
            if ( (delta.getFlags() & IResourceDelta.CONTENT) != 0 ||
				 (delta.getFlags() & IResourceDelta.COPIED_FROM) != 0 || 
				 (delta.getFlags() & IResourceDelta.MOVED_FROM) !=0 ) {
            	 return true;
            }
		}
		
		//Resource is added or removed
		if( (delta.getKind() & IResourceDelta.ADDED) != 0 ||
			(delta.getKind() & IResourceDelta.REMOVED) != 0){
			return true;
		}

		return false;
	}

	public ArrayList<IResourceDelta> getDeltas() {
		return deltas;
	}

}
