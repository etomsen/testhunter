package it.unibz.testhunter.action;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.db.Status;
import it.unibz.testhunter.db.Test;
import it.unibz.testhunter.db.TestInstance;
import it.unibz.testhunter.db.TestsResult;
import it.unibz.testhunter.shared.FilteredList;
import it.unibz.testhunter.shared.IBuild;
import it.unibz.testhunter.shared.IJob;
import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.IProject;
import it.unibz.testhunter.shared.Predicate;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.shared.TestInstanceModel;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.SvcTest;
import it.unibz.testhunter.svc.SvcTestInstance;
import it.unibz.testhunter.svc.SvcTestResults;
import it.unibz.testhunter.svc.SvcTestStatus;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

public class ActionProject implements ICommandAction {
	
	private SvcTestResults svcTestResults;
	private SvcProject svcProject;
	private SvcPlugin svcPlugin;
	private SvcBuildJob svcBuildJob;
	private SvcTest svcTest;
	private SvcTestStatus svcTestStatus;
	private SvcTestInstance svcTestInstance;
	private IAcionExecuteSink sink;
	private EntityManager em;

	@Inject
	public ActionProject(SvcProject svcProject, SvcBuildJob svcBuildJob,
			SvcPlugin svcPlugin, SvcTest svcTest, SvcTestStatus svcTestStatus,
			SvcTestInstance svcTestInstance, EntityManager em, SvcTestResults svcTestResults) {
		this.svcProject = svcProject;
		this.svcPlugin = svcPlugin;
		this.svcBuildJob = svcBuildJob;
		this.svcTest = svcTest;
		this.svcTestStatus = svcTestStatus;
		this.svcTestInstance = svcTestInstance;
		this.em = em;

	}

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		this.sink = command;

		Long projectId = (Long) command.getCmdOption("id");
		if (projectId == null) {
			throw new TException("action parameter missing: id").setUserMsg("cmd parameter missing: id"); 
		}
		
		Object clear = (Boolean) command.getCmdOption("clear");
		if (clear != null && (Boolean)clear) {
			clear(projectId);
		}

		Object grab = command.getCmdOption("grab");
		if (grab != null && (Boolean) grab) {
			grab(projectId);
		}

		Object testsResult = command.getCmdOption("tests-result");
		if ( testsResult != null && (Boolean)testsResult) {
			Long lastBuild = (Long) command.getCmdOption("last-build");
			String jobName = (String) command.getCmdOption("job-name");
			Boolean includeZeroTimeToTime = (Boolean) command.getCmdOption("zero-time-time");
			Boolean includeZeroTimeToCount = (Boolean) command.getCmdOption("zero-time-count");
			TestsResult tr = new TestsResult();
			tr.setDate(new Timestamp(new java.util.Date().getTime()));
			tr.setOptions(command.getCmdOptions());
			if (includeZeroTimeToTime != null) {
				tr.setIncludeZeroTimeToTime(includeZeroTimeToTime.booleanValue());
			}
			if (includeZeroTimeToCount != null) {
				tr.setIncludeZeroTimeToCount(includeZeroTimeToCount.booleanValue());
			}
			tr.setJobName(jobName);
			tr.setLastBuild(lastBuild);
			tr.setProjectId(projectId);
			svcTestResults.updateTestsResultItems(tr);
		}
		

	}
	
	private void clear(Long projectId) throws TException {
		System.out.print("Loading project record drom db...");
		System.out.flush();
		Project prj = svcProject.select(projectId);
		if (prj == null) {
			throw new TException("user message").setUserMsg(String.format(
					"%s: %d: no project with such id", sink.getCmdName(),
					projectId));
		}
		System.out.println("done.");
		System.out.print("Start cleaning the project...");
		System.out.flush();

		svcProject.clear(prj);

		System.out.println("done.");
		System.out.flush();
	}

	private void grab(Long projectId) throws TException {
		Project prj = loadProject(projectId);
		IPlugin plugin = getProjectsPlugin(prj);
		Long lastBuildNumber = getLastBuildNumber(prj);
		fetchPrjNewBuilds(prj, plugin, lastBuildNumber);
	}

	private Long getLastBuildNumber(Project prj) throws TException {
		BuildJob lastProjectBuild = svcBuildJob.selectLastByProject(prj);
		final Long number;
		if (lastProjectBuild != null) {
			System.out.print("Last fetched project build is: "
					+ lastProjectBuild.toString());
			number = lastProjectBuild.getBuildNumber();
		} else {
			number = new Long(-1);
		}
		System.out.println("done.");
		return number;
	}

	private Project loadProject(Long projectId) throws TException {
		System.out.print("Loading project record drom db...");
		System.out.flush();

		Project prj = svcProject.select(projectId);
		if (prj == null) {
			throw new TException("user message").setUserMsg(String.format(
					"%s: %d: no project with such id", sink.getCmdName(),
					projectId));
		} else {
			System.out.println("Project found: " + prj.toString());
		}
		return prj;
	}

	private IPlugin getProjectsPlugin(Project prj) throws TException {
		System.out.print("Loading project plugin...");
		System.out.flush();
		IPlugin plugin = svcPlugin.getPluginByUUID(prj.getPlugin());
		if (plugin == null) {
			throw new TException("user message").setUserMsg(String.format(
					"%s: %s: unable to load plugin", sink.getCmdName(), prj
							.getPlugin().toString()));
		}
		System.out.println("done.");
		System.out.flush();
		return plugin;
	}

	private void fetchPrjNewBuilds(Project prj, IPlugin plugin,
			final Long lastBuildNumber) throws TException {

		System.out.println("Start grabbing project...");
		System.out.flush();

		IProject pluginPrj = plugin.getProject(prj.getUrl());

		List<IBuild> newBuilds = FilteredList.filter(pluginPrj.getAllBuilds(),
				new Predicate<IBuild>() {

					@Override
					public boolean apply(IBuild type) throws TException {
						return type.getNumber() > lastBuildNumber;
					}
				});
		// sort builds by number
		Collections.sort(newBuilds);
		System.out.println(String.format(
				"Total number of builds available... %d ",
				pluginPrj.getBuildCount()));
		System.out.flush();
		System.out.println(String.format(
				"Number of new unfetched builds available... %d ",
				newBuilds.size()));
		System.out.flush();

		for (IBuild b : newBuilds) {
			System.out
					.println("Start fetching build #" + b.getNumber() + "...");
			System.out.flush();

			em.getTransaction().begin();
			for (IJob j : b.getAllJobs()) {

				System.out.print(".");
				System.out.flush();

				BuildJob bj = svcBuildJob.insert(prj,
						new Long(b.getNumber()), b.getStart(), j.getName());

				for (TestInstanceModel t : j.getAllTest()) {

					Test test = svcTest.insertIfNotExists(t.getName(),
							t.getClassName(), t.getPackageName());

					Status testStatus = svcTestStatus.insertIfNotExists(
							t.getStatus(), prj.getPlugin());

					TestInstance ti = svcTestInstance.selectByTestByBuild(test,
							bj);
					if (ti == null) {
						svcTestInstance.insert(test, bj, testStatus,
								new Long(t.getDuration()));
					}
					// else omitting test insertion
				}
			}
			em.getTransaction().commit();
			System.out.println("done.");
			System.out.flush();
		}

		System.out.println("done.");
		System.out.flush();

	}
}
