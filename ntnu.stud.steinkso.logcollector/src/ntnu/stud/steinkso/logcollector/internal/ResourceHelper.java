package ntnu.stud.steinkso.logcollector.internal;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import com.google.gson.Gson;

/**
 *  Helper for handling resources.
 *  Mainly to convert to JSON
 * @author stan
 *
 */
public class ResourceHelper {

	public static String resourceToJson(ArrayList<IResource> resources,ArrayList<IResourceDelta> finaldeltas) throws CoreException{
		String json;

		Iterator<IResource> iterator = resources.iterator();
		Iterator<IResourceDelta> deltaiterator = finaldeltas.iterator();
		ArrayList<LoggerResource> fileArray = new ArrayList<LoggerResource>();

		while(iterator.hasNext()){
			fileArray.add( new LoggerResource((IResource) iterator.next(),deltaiterator.next()));
		}

		json = new Gson().toJson(fileArray); 

		return json;
	}
}
