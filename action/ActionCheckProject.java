package it.unibz.testhunter.action;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.shared.FilteredList;
import it.unibz.testhunter.shared.IBuild;
import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.IProject;
import it.unibz.testhunter.shared.Predicate;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcProject;

import java.util.List;

import com.google.inject.Inject;

public class ActionCheckProject implements ICommandAction {

	private SvcProject svcProject;
	private SvcPlugin svcPlugin;
	private SvcBuildJob svcBuildJob;

	@Inject
	public ActionCheckProject(SvcProject svcProject, SvcBuildJob svcBuildJob,
			SvcPlugin svcPlugin) {
		this.svcProject = svcProject;
		this.svcPlugin = svcPlugin;
		this.svcBuildJob = svcBuildJob;
	}

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		Long id = (Long) command.getCmdOption("id");

		System.out.print("Loading project record drom db...");
		System.out.flush();

		Project dbPrj = svcProject.select(id);
		if (dbPrj == null) {
			throw new TException("user message").setUserMsg(String.format(
					"%s: %d: No such project", command.getCmdName(), id));
		} else {
			System.out.println("Project found: " + dbPrj.toString());
		}
		BuildJob lastProjectBuild = svcBuildJob.selectLastByProject(dbPrj);
		final Long number;
		if (lastProjectBuild != null) {
			System.out.print("Last fetched project build is: "
					+ lastProjectBuild.toString());
			number = lastProjectBuild.getBuildNumber();
		} else {
			number = new Long(-1);
		}

		System.out.println("done.");
		System.out.print("Loading project plugin...");
		System.out.flush();

		IPlugin plugin = svcPlugin.getPluginByUUID(dbPrj.getPlugin());
		if (plugin == null) {
			throw new TException("user message").setUserMsg(String.format(
					"%s: %s: Unable to load plugin", dbPrj.getPlugin()
							.toString()));
		}
		System.out.println("done.");
		System.out.flush();

		System.out.println("Start checking project...");
		System.out.flush();

		IProject project = plugin.getProject(dbPrj.getUrl());
		System.out.println("done.");
		System.out.flush();

		List<IBuild> newBuilds = FilteredList.filter(project.getAllBuilds(),
				new Predicate<IBuild>() {

					@Override
					public boolean apply(IBuild type) throws TException {
						return type.getNumber() > number;
					}
				});

		System.out.println(String.format(
				"Number of ALL builds available... %d ",
				project.getBuildCount()));
		System.out.println(String.format(
				"Number of NEW builds available... %d ", newBuilds.size()));
		if (newBuilds.size() > 0) {
			System.out.print("New builds: ");
			for (IBuild b : newBuilds) {
				System.out.print(b.getNumber() + " ");
			}
			System.out.println();
		}
		System.out.flush();
	}

}
