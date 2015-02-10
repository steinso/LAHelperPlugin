package ntnu.stud.steinkso.logcollector.internal;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

import ntnu.stud.steinkso.logcollector.ErrorHandler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class FileUtils {

	/**
	 * Saves (and creates) a file to the project root.
	 * 
	 * @param contents
	 * @param filename
	 * @param project
	 * @throws CoreException
	 */
	public static void saveFile(String contents, String filename,
			IProject project) throws CoreException {

		IFile file = project.getFile(filename);
		InputStream stream = new ByteArrayInputStream(contents.getBytes());

		if (!file.exists()) {
			file.create(stream, true, null);
		} else {
			file.setContents(stream, true, false, null);
		}
	}

	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static void saveFile(File file,String contents) {

		Writer writer = null;
		try {
			

			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file.getPath()), "utf-8"));
			writer.write(contents);

		} catch (Exception e) {
			ErrorHandler.logError("Could not write plugin meta data file"
					+ file.getPath(), e);
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

		// file.setContents(stream, true, false, null);
	}
}
