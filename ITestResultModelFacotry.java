package it.unibz.testhunter;

import it.unibz.testhunter.model.TestResultModel;

public interface ITestResultModelFacotry {
	public TestResultModel create(Long testId);
}
