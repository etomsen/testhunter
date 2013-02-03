package it.unibz.testhunter.model;

import it.unibz.testhunter.ITestResultModelFacotry;
import it.unibz.testhunter.db.TestsResultItem;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ProjectResultsModel {

	public LinkedHashMap<Long, TestResultModel> getTestResults() {
		return testResults;
	}

	private Long prj;
	private LinkedHashMap<Long, TestResultModel> testResults;
	ITestResultModelFacotry testResultModelFactory;
	
	@Inject
	public ProjectResultsModel(@Assisted Long prj, ITestResultModelFacotry testResultModelFactory) {
		this.prj = prj;
		this.testResultModelFactory = testResultModelFactory;
		testResults = new LinkedHashMap<Long, TestResultModel>();
	}

	public void addTesetResultList(List<TestsResultItem> trList) {
		for (TestsResultItem tr : trList) {
			addTestResult(tr);
		}
	}

	public void addTestResult(TestsResultItem tr) {
		Long testId = tr.getTest();
		TestResultModel trModel;
		if (testResults.containsKey(testId)) {
			trModel = testResults.get(testId);
		} else {
			trModel = testResultModelFactory.create(testId);
			testResults.put(testId, trModel);
		}
		trModel.addTestResult(tr);
	}

	public Long getPrj() {
		return prj;
	}
}
