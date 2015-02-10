package ntnu.stud.steinkso.logcollector.internal;

import ntnu.stud.steinkso.logcollector.ErrorHandler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;


/**
 * Representation of resource that is transmitted to the server
 * 
 * @author stan
 */

public class LoggerResource {

	public static final String TYPE = "type";
	public static final String REMOVED = "removed";
	public static final String MOVED = "moved";
	public static final String CONTENT = "content";
	public static final String ADDED = "added";
	public static final String DERIVED = "derived";
	public static final String LOCAL = "local";
	public static final String FILE = "file";
	public static final String FOLDER = "folder";
	private String name;
	private String path;
	private String fileContents;
	private String type;
	private String typeOfChange;
	private String movedFrom;

	public LoggerResource() {

	}

	public LoggerResource(IResource resource, IResourceDelta resourceDelta)
			throws CoreException {

		defineTypeOfChange(resourceDelta);
		defineTypeOfResource(resource);

	}

	private void defineTypeOfChange(IResourceDelta resourceDelta) {

		if ((resourceDelta.getKind() & IResourceDelta.ADDED) != 0) {
			typeOfChange = ADDED;
		}
		if ((resourceDelta.getKind() & IResourceDelta.REMOVED) != 0) {
			typeOfChange = REMOVED;
		}
		if ((resourceDelta.getFlags() & IResourceDelta.CONTENT) != 0) {
			typeOfChange = CONTENT;
		}
		if ((resourceDelta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
			typeOfChange = MOVED;
			movedFrom = resourceDelta.getMovedFromPath().toString();
		}
		if ((resourceDelta.getFlags() & IResourceDelta.TYPE) != 0) {
			typeOfChange = TYPE;
		}
		if ((resourceDelta.getFlags() & IResourceDelta.DERIVED_CHANGED) != 0) {
			typeOfChange = DERIVED;
		}
		if ((resourceDelta.getFlags() & IResourceDelta.LOCAL_CHANGED) != 0) {
			typeOfChange = LOCAL;
		}
	}

	private void defineTypeOfResource(IResource resource) {

		if ((resource.getType() & IResource.FILE) != 0) {
			defineFile(resource);
		}
		if ((resource.getType() & IResource.FOLDER) != 0) {
			defineFolder(resource);
		}

	}

	private void defineFolder(IResource resource) {
		type = FOLDER;
		name = resource.getName();
		path = resource.getFullPath().toString();
	}

	private void defineFile(IResource resource) {
		type = FILE;
		name = resource.getName();
		path = resource.getFullPath().toString();
		defineContentsOfFile((IFile) resource);

	}

	private void defineContentsOfFile(IFile resource) {
		if (typeOfChange == REMOVED) {
			return;
		}

		try {
			fileContents = FileUtils.convertStreamToString(((IFile) resource)
					.getContents());
		} catch (CoreException e) {
			ErrorHandler.logError("Could not read contents of changed file", e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileContents() {
		return fileContents;
	}

	public void setFileContents(String fileContents) {
		this.fileContents = fileContents;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeOfChange() {
		return typeOfChange;
	}

	public void setTypeOfChange(String typeOfChange) {
		this.typeOfChange = typeOfChange;
	}

	public String getMovedFrom() {
		return movedFrom;
	}

	public void setMovedFrom(String movedFrom) {
		this.movedFrom = movedFrom;
	}

}
