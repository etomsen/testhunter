package it.unibz.testhunter.action;

import it.unibz.testhunter.CsvPrinter;
import it.unibz.testhunter.IPtfMetricProvider;
import it.unibz.testhunter.IStatusResolver;
import it.unibz.testhunter.ITimeNormalizer;
import it.unibz.testhunter.PtfMetric;
import it.unibz.testhunter.TimeNormalizerImpl;
import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.db.TestInstance;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcTestInstance;
import it.unibz.testhunter.svc.SvcTestSequence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryProvider;

public class ActionComputePtf implements ICommandAction {

	private SvcTestInstance svcTestInstance;
	private SvcBuildJob svcBuildJob;
	private SvcTestSequence svcTestSequence;

	@Inject
	public ActionComputePtf(SvcTestInstance svcTestInstance, SvcBuildJob svcBuildJob, SvcTestSequence svcTestSequence) {

		this.svcTestInstance = svcTestInstance;
		this.svcBuildJob = svcBuildJob;
		this.svcTestSequence = svcTestSequence;
	}

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		try {
			Long seqId = (Long) command.getCmdOption("sequence");
			Long buildJobId = (Long) command.getCmdOption("build-job");
			BuildJob buildJob = svcBuildJob.select(buildJobId);
			List<TestInstance> buildJobResults = svcTestInstance
					.selectByBuildJob(buildJob);
			Sequence ts = svcTestSequence.getSequence(seqId);

			final ITimeNormalizer timeNormalizer = new TimeNormalizerImpl(
					ts.getMinTimeMs(), ts.getMaxTimeMs());
			Injector injector = Guice.createInjector(new AbstractModule() {

				@Override
				protected void configure() {
					bind(ITimeNormalizer.class).toInstance(timeNormalizer);

					bind(IStatusResolver.class).toInstance(
							new IStatusResolver() {

								@Override
								public boolean isStatusFailed(Long testStatusId)
										throws TException {
									switch (testStatusId.intValue()) {
									case 1:
										return false;
									case 2:
										return true;
									case 3:
										return true;
									case 4:
										return true;
									case 5:
										return false;
									default:
										throw new TException("");
									}
								}
							});

					bind(IPtfMetricProvider.class).toProvider(
							FactoryProvider.newFactory(
									IPtfMetricProvider.class, PtfMetric.class));

				}
			});
			PtfMetric ptf = injector.getInstance(IPtfMetricProvider.class).create(ts, buildJobResults);
			OutputStreamWriter writer;
			
			writer = new FileWriter(String.format("ptf-per-time[sequence-%d][job-%d].csv", seqId, buildJobId));
			CsvPrinter.printPairList(ptf.computePtfPerTime(), "percent of total suite time spent",
					"percent of failed test discovered", writer);
			writer.close();
			
			writer = new FileWriter(String.format("ptf-per-tests[sequence-%d][job-%d].csv", seqId, buildJobId));
			CsvPrinter.printPairList(ptf.computePtfPerTests(), "percent of tests in suite executed", "percent of failed test discovered", writer);
			writer.close();
			
			writer = new FileWriter(String.format("ptf-data[sequence-%d][job-%d].csv", seqId, buildJobId));
			writer.append("total tests count, failed tests count, percent failed, aptf");
			writer.append("\n");
			writer.append(String.format("%d, %d, %f", 
					ptf.getTestsCount(), ptf.getFailedTestCount(), 
					(float)ptf.getFailedTestCount()/ptf.getTestsCount()));
			writer.close();
			
		} catch (IOException e) {
			throw new TException(e.getMessage()).setUserMsg("action compute PTF: unable to print");
		} catch (TException te) {
			te.setUserMsg("action compute PTF: unexpected error");
		}
	}
	
	
	public void checkDuration() {
		// select test_instance.build_job, job_name, build_job.buld_number, sum(duration_ms) from test_instance left join build_job on test_instance.build_job = build_job.id where build_job.project_id =1 group by build_job.buld_number, job_name, test_instance.build_job order by buld_number;
		
	}
}
