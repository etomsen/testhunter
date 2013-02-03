package it.unibz.testhunter.action;

import it.unibz.testhunter.AperMetric;
import it.unibz.testhunter.CsvPrinter;
import it.unibz.testhunter.IProjectResultModelFactory;
import it.unibz.testhunter.IStatusNormalizer;
import it.unibz.testhunter.ITestResultModelFacotry;
import it.unibz.testhunter.ITimeNormalizer;
import it.unibz.testhunter.TimeNormalizerImpl;
import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.db.TestsResult;
import it.unibz.testhunter.db.TestsResultItem;
import it.unibz.testhunter.model.ProjectResultsModel;
import it.unibz.testhunter.model.TestResultModel;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPrioritization;
import it.unibz.testhunter.svc.SvcTestResults;
import it.unibz.testhunter.svc.SvcTestSequence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryProvider;

public class ActionPrioritize implements ICommandAction {

	private SvcPrioritization svcPrioritization;
	private SvcBuildJob svcBuildJob;
	private SvcTestResults svcTestResults;
	private SvcTestSequence svcTestSequence;
	
	@Inject
	public ActionPrioritize(SvcPrioritization svcPrioritization, SvcBuildJob svcBuildJob, SvcTestResults svcTestResults, SvcTestSequence svcTestSequence) {
		this.svcPrioritization = svcPrioritization;
		this.svcBuildJob = svcBuildJob;
		this.svcTestResults = svcTestResults;
		this.svcTestSequence = svcTestSequence;
	}

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		// --project
		// AND
		// --test-result (id of testsResult)
		// OR
		// --last-job-id AND [--zero-time-time] AND [--zero-time-time]
		// OR
		// -- last-build AND [--job-name] AND [--zero-time-time] AND [--zero-time-time]

		try {
			Long projectId = (Long) command.getCmdOption("project");
			if (projectId == null) {
				throw new TException("action parameter missing: project")
						.setUserMsg("action parameter missing: project");
			}
			
			Long testsResult = (Long) command.getCmdOption("tests-result");
			Long lastJobId = (Long) command.getCmdOption("last-job-id");
			Long lastBuildNumber = (Long) command.getCmdOption("last-build-number");			
			TestsResult tr;
			if (testsResult == null) {
				if (lastBuildNumber == null && lastJobId == null) {
					throw new TException(
							"action parameter missing: specify last-build or last-job")
							.setUserMsg("action parameter missing: specify last-build or last-job");
				}
				String jobName = (String) command
						.getCmdOption("job-name");
				Boolean includeZeroTimeToTime = (Boolean) command
						.getCmdOption("zero-time-time");
				Boolean includeZeroTimeToCount = (Boolean) command
						.getCmdOption("zero-time-count");

				tr = new TestsResult();
				tr.setDate(new Timestamp(new java.util.Date().getTime()));
				if (includeZeroTimeToTime != null) {
					tr.setIncludeZeroTimeToTime(includeZeroTimeToTime);
				}
				if (includeZeroTimeToCount != null) {
					tr.setIncludeZeroTimeToCount(includeZeroTimeToCount);
				}
				BuildJob lastJob;
				if (lastJobId != null) {
					lastJob = svcBuildJob.select(lastJobId);
					if (lastJob == null) {
						throw new TException(
									String.format("prioritize: %d: no job found", lastJobId))
								.setUserMsg(
										String.format("prioritize: %d: no job found",lastJobId));
					}
					tr.setJobName(lastJob.getJobName());
					tr.setLastBuild(lastJob.getBuildNumber());
				} else {
					tr.setJobName(jobName);
					tr.setLastBuild(lastBuildNumber);
				}
				tr.setProjectId(projectId);				
				tr = svcTestResults.saveTestResult(tr);
				svcTestResults.updateTestsResultItems(tr);
			} else {
				tr = svcTestResults.select(testsResult);
			}
			Object[] testMinMaxTime = svcTestSequence.getMinMaxInstanceTime(
					projectId, new Long(0));
			final ITimeNormalizer timeNormalizer = new TimeNormalizerImpl(
					(Long) testMinMaxTime[0], (Long) testMinMaxTime[1]);
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
					bind(ITimeNormalizer.class).toInstance(timeNormalizer);
					
					bind(IStatusNormalizer.class).toInstance(new IStatusNormalizer() {
						
						@Override
						public Long normalize(Long statusId) { 
							return statusId;
						}
					});

				}
			});

			ProjectResultsModel prm = injector.getInstance(
					IProjectResultModelFactory.class).create(projectId);

			List<TestsResultItem> trList = new ArrayList<TestsResultItem>(tr.getItems());
			prm.addTesetResultList(trList);

			List<TestResultModel> trmList = new ArrayList<TestResultModel>(prm.getTestResults().values());

			Collections.shuffle(trmList);
			System.out.println("APER (initial): "+ new AperMetric(trmList).compute());
			String s;
			s = String.format("sequence-original[lastbuild-%d].csv", lastBuildNumber);
			printSequenceToCsv(trmList, s);
			s = String.format("sequence-graphic-original[lastbuild-%d].csv", lastBuildNumber);
			printGraphicToCsv(trmList, s);

			Collections.sort(trmList);
			Sequence ts = new Sequence();
			ts.setMinTimeMs((Long) testMinMaxTime[0]);
			ts.setMaxTimeMs((Long) testMinMaxTime[1]);
			ts.setProjectId(projectId);
			ts.setOptions(command.getCmdOptions()+" --fitness percent_of_total_entropy_reduction --time percent_of_total_suite_time");
			svcPrioritization.savePrioritization(ts, trmList);
			System.out.println("APER (prioritized): "
					+ new AperMetric(trmList).compute());
			s = String.format("sequence[lastbuild-%d].csv", lastBuildNumber);
			printSequenceToCsv(trmList, s);
			s = String.format("sequence-graphic[lastbuild-%d].csv", lastBuildNumber);
			printGraphicToCsv(trmList, s);

		} catch (TException te) {
			te.setUserMsg(String.format("%s: %s", command.getCmdName(),
					te.getUserMsg()));
			throw te;
		}

	}

	private void printSequenceToCsv(List<TestResultModel> seq, String fileName)
			throws TException {
		try {
			OutputStreamWriter writer = new FileWriter(fileName);
			CsvPrinter.printSequence(seq, writer);
		} catch (IOException e) {
			throw new TException(e.getMessage()).setUserMsg("unable to print");
		} catch (TException te) {
			te.setUserMsg(String.format("print sequnce to csv: %s: %s",
					fileName, te.getUserMsg()));
			throw te;
		}
	}

	private void printGraphicToCsv(List<TestResultModel> seq, String fileName)
			throws TException {
		try {
			OutputStreamWriter writer = new FileWriter(fileName);
			CsvPrinter.printGraphic(seq, writer);
		} catch (IOException e) {
			throw new TException(e.getMessage()).setUserMsg("unable to print");
		} catch (TException te) {
			te.setUserMsg(String.format("print graphic to csv: %s: %s",
					fileName, te.getUserMsg()));
			throw te;
		}
	}

}
