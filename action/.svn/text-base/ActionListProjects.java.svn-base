package it.unibz.testhunter.action;

import com.google.inject.Inject;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcProject;

public class ActionListProjects implements ICommandAction {

	private SvcProject svcProject; 
	
	@Inject
	public ActionListProjects(SvcProject svcProject) {
		this.svcProject = svcProject;
	}
	
	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		System.out.println("Project list:");
		for (Project prj : svcProject.selectAll()) {
			System.out.println(prj.toString());
		}
		System.out.flush();
	}

}
