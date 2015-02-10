package ntnu.stud.steinkso.learninganalytics;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;

public class CompilationParticipant1 extends CompilationParticipant{

	// Specifies if the compilation participant should participate
	public boolean isActive(IJavaProject project) {
		return true;
	}

	//@override
	public void reconcile(ReconcileContext context){
		LearningAnalyticsPlugin.getDefault().logCurrentState();
	}
}
