package ntnu.stud.steinkso.learninganalytics.testlogger;

import java.util.ArrayList;

import ntnu.stud.steinkso.learninganalytics.LearningAnalyticsPlugin;
import ntnu.stud.steinkso.logcollector.LoggerPlugin;
import ntnu.stud.steinkso.logcollector.internal.LoggerResource;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;


public class TestRunListener1 extends TestRunListener {

	ArrayList<LoggerTest> testResults = new ArrayList<LoggerTest>();
		
	public void testCaseFinished(ITestCaseElement testCaseElement){
		
		// Get test result
		Result result = testCaseElement.getTestResult(true);
		String className = testCaseElement.getTestClassName();
		String methodName = testCaseElement.getTestMethodName();

		// Create data structure
		LoggerTest test= new LoggerTest();
		test.setClassName(className);
		test.setMethodName(methodName);
		test.setResult(result.toString());
		
		testResults.add(test);
	}

	public void sessionFinished(ITestRunSession session){

		//Don't do anything if logging is inactive
		if(!LearningAnalyticsPlugin.getDefault().getLoggingIsActive()){
			testResults.clear();
			return;
		}

		String json = LoggerPlugin.getDefault().convertToJson(testResults);
		String testFileName = LearningAnalyticsPlugin.getDefault().getPreferences().getTestFileName();
		
		try {
                LoggerResource loggerResource = new LoggerResource();
                loggerResource.setFileContents(json);
                loggerResource.setName(testFileName);
                loggerResource.setTypeOfChange(LoggerResource.CONTENT);
                loggerResource.setPath("/.tests.json");
                loggerResource.setType(LoggerResource.FILE);
                
                ArrayList<LoggerResource> loggerResourceList = new ArrayList<LoggerResource>();
                loggerResourceList.add(loggerResource);

                LoggerPlugin.getDefault().logResources(loggerResourceList);

		}finally{
            testResults.clear();
		}
	}
}
