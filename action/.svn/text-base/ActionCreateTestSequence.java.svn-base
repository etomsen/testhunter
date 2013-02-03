package it.unibz.testhunter.action;

import it.unibz.testhunter.AperMetric;
import it.unibz.testhunter.IProjectResultModelFactory;
import it.unibz.testhunter.IStatusNormalizer;
import it.unibz.testhunter.ITestResultModelFacotry;
import it.unibz.testhunter.ITimeNormalizer;
import it.unibz.testhunter.TimeNormalizerImpl;
import it.unibz.testhunter.cmd.CmdCreateTestSequence;
import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.db.TestsResult;
import it.unibz.testhunter.db.TestsResultItem;
import it.unibz.testhunter.model.ProjectResultsModel;
import it.unibz.testhunter.model.TestResultModel;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcTestResults;
import it.unibz.testhunter.svc.SvcTestSequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryProvider;

public class ActionCreateTestSequence implements ICommandAction {

	private SvcTestSequence  svcTestSequence;
	private SvcTestResults svcTestResults;

	@Inject
	public ActionCreateTestSequence(SvcTestResults svcTestResults, SvcTestSequence svcTestSequence) {
		this.svcTestResults = svcTestResults;
		this.svcTestSequence = svcTestSequence;
	}
	@Override
	public void execute(IAcionExecuteSink command) throws TException {
			Long minTrId = (Long) command.getCmdOption(CmdCreateTestSequence.optionTestResultMin);
			Long maxTrId = (Long) command.getCmdOption(CmdCreateTestSequence.optionTestResultMax);
			List<TestsResult> trList = svcTestResults.select(minTrId, maxTrId);
			System.out.println(String.format("%d test result found", trList.size()));
			for (TestsResult testsResult : trList) {
				System.out.println(String.format("Sequence for test result: %d", testsResult.getId()));
				computeSequenceForTr(testsResult, command);
				System.out.println("done.");
			}		
		
	}
	
	private void computeSequenceForTr(TestsResult tr, IAcionExecuteSink command) throws TException {
		// define min/max time for tests in order to scale duration
		final Object[] testMinMaxTime = svcTestSequence.getMinMaxInstanceTime(tr.getProjectId(), null);
		
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IProjectResultModelFactory.class).toProvider(
						FactoryProvider.newFactory(
								IProjectResultModelFactory.class,
								ProjectResultsModel.class));

				bind(ITestResultModelFacotry.class).toProvider(
						FactoryProvider.newFactory(
								ITestResultModelFacotry.class,
								TestResultModel.class));
				bind(ITimeNormalizer.class).toInstance(new TimeNormalizerImpl((Long) testMinMaxTime[0], (Long) testMinMaxTime[1]));
				
				bind(IStatusNormalizer.class).toInstance(new IStatusNormalizer() {
					
					@Override
					public Long normalize(Long statusId) { 
						return statusId;
					}
				});

			}
		});
		Boolean zeroTime = (Boolean) command.getCmdOption("zero-time");
		ProjectResultsModel prm = injector.getInstance(IProjectResultModelFactory.class).create(tr.getProjectId());
		List<TestsResultItem> trList = new ArrayList<TestsResultItem>(tr.getItems());
		prm.addTesetResultList(trList);
		List<TestResultModel> trmList = new ArrayList<TestResultModel>(prm.getTestResults().values());
		// sort by tangent
		Collections.shuffle(trmList);		
		Collections.sort(trmList);
		
		// save sorted sequence
		Sequence ts = new Sequence();
		ts.setMinTimeMs((Long) testMinMaxTime[0]);
		ts.setMaxTimeMs((Long) testMinMaxTime[1]);
		ts.setProjectId(tr.getProjectId());		
		ts.setAvgValue(new AperMetric(trmList).compute());		
		ts.setTestResult(tr);		
		for (TestResultModel trm : trmList) {
			if (trm.getTime() == 0 && !zeroTime.booleanValue()) {
				// skip zero-time tests if there is no zero-time flag set up.  
				continue;
			}
			ts.addItem(trm.getInfo(), trm.getTime(), trm.getTest());
		}
		ts.setOptions(command.getCmdOptions()+String.format("--tr_count %d --ts_count %d --last_build %d", tr.getItems().size(), ts.getItems().size(), tr.getLastBuild()));		
		svcTestSequence.save(ts);		

	}
	
}
