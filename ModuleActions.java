package it.unibz.testhunter;

import it.unibz.testhunter.action.ActionAddProject;
import it.unibz.testhunter.action.ActionCheckProject;
import it.unibz.testhunter.action.ActionCommandHelp;
import it.unibz.testhunter.action.ActionCreateTestSequence;
import it.unibz.testhunter.action.ActionHelp;
import it.unibz.testhunter.action.ActionListPlugins;
import it.unibz.testhunter.action.ActionListProjects;
import it.unibz.testhunter.action.ActionProject;
import it.unibz.testhunter.action.ActionTestResults;
import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.cmd.CmdAddProject;
import it.unibz.testhunter.cmd.CmdCheckProject;
import it.unibz.testhunter.cmd.CmdCreateTestSequence;
import it.unibz.testhunter.cmd.CmdListCommands;
import it.unibz.testhunter.cmd.CmdListPlugins;
import it.unibz.testhunter.cmd.CmdListProjects;
import it.unibz.testhunter.cmd.CmdProject;
import it.unibz.testhunter.cmd.CmdTestResults;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ModuleActions extends AbstractModule {

	@Override
	protected void configure() {
		bind(ICommandAction.class).annotatedWith(Names.named("cmd-help")).to(ActionCommandHelp.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdListProjects.name)).to(ActionListProjects.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdAddProject.name)).to(ActionAddProject.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdListCommands.name)).to(ActionHelp.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdCheckProject.name)).to(ActionCheckProject.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdListPlugins.name)).to(ActionListPlugins.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdProject.name)).to(ActionProject.class);
		bind(ICommandAction.class).annotatedWith(Names.named(CmdTestResults.name)).to(ActionTestResults.class);	
		bind(ICommandAction.class).annotatedWith(Names.named(CmdCreateTestSequence.name)).to(ActionCreateTestSequence.class);
	}

}
