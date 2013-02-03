package it.unibz.testhunter.action;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.db.TestsResult;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.SvcTestResults;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

public class ActionTestResults implements ICommandAction {

	private SvcTestResults svcTestResults;
	private SvcBuildJob svcBuildJob;
	private SvcProject svcProject;
		
	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		try {
			Long projectId = (Long) command.getCmdOption("project");			
			Project prj = svcProject.select(projectId);	
			if (prj == null) {
				throw new TException("user message").setUserMsg(String.format(
						"%s: %d: no project with such id", command.getCmdName(),
						projectId));
			}			
			String jobName = (String) command.getCmdOption("job-name");
			// TODO: add these options to the command
			Boolean includeZeroTimeToTime = null;//(Boolean) command.getCmdOption("zero-time-time");
			Boolean includeZeroTimeToCount = null;//(Boolean) command.getCmdOption("zero-time-count");
			generateAllTestResults(prj, jobName, includeZeroTimeToTime, includeZeroTimeToCount, command.getCmdOptions());

		} catch (TException te) {
			te.setUserMsg(String.format("%s: %s", command.getCmdName(), te.getUserMsg()));
			throw te;
		}
	}
		
	/**
	 * @param svcTestResults - injected service
	 * @param svcBuildJob - injected service
	 * @param svcTestSequence 
	 */
	@Inject
	public ActionTestResults(SvcTestResults svcTestResults, SvcBuildJob svcBuildJob, SvcProject svcProject) {
		this.svcTestResults = svcTestResults;
		this.svcBuildJob = svcBuildJob;
		this.svcProject = svcProject;
	}
	
	/**
	 * @param prj - project record
	 * @param jobName -  job name
	 * @param includeZeroTimeToTime - if not null, will consider 0 time tests when calculating average duration 
	 * @param includeZeroTimeToCount - if not null, will consider 0 time tests when calculating probabilities
	 * @throws TException 
	 */
	private void generateAllTestResults(Project prj, String jobName, Boolean includeZeroTimeToTime, Boolean includeZeroTimeToCount, String options) throws TException {
		List<BuildJob> prjBuildJobs = svcBuildJob.selectByProject(prj, jobName);
		Collections.sort(prjBuildJobs, new Comparator<BuildJob>() {

			@Override
			public int compare(BuildJob o1, BuildJob o2) {
				return o1.getBuildNumber().compareTo(o2.getBuildNumber());
			}
		});
		
		Long lastBuild = new Long(-1);		
		for (BuildJob buildJob : prjBuildJobs) {
			if (buildJob.getBuildNumber() <= lastBuild) {
				// Skip the job if build already processed 
				continue;
			} else {
				lastBuild = buildJob.getBuildNumber();
			}
			TestsResult tr = new TestsResult();
			tr.setDate(new Timestamp(new java.util.Date().getTime()));
			if (includeZeroTimeToTime != null) {
				tr.setIncludeZeroTimeToTime(includeZeroTimeToTime);
			}
			if (includeZeroTimeToCount != null) {
				tr.setIncludeZeroTimeToCount(includeZeroTimeToCount);
			}			
			
			tr.setProjectId(prj.getId());
			tr.setJobName(jobName);
			tr.setLastBuild(buildJob.getBuildNumber());
			tr.setOptions(options);
			tr = svcTestResults.saveTestResult(tr);
			svcTestResults.updateTestsResultItems(tr);			
		}		
	}
		
}
