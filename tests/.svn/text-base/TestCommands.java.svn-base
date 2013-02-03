package it.unibz.testhunter.tests;

import it.unibz.testhunter.CommandFactory;
import it.unibz.testhunter.action.ActionAddProject;
import it.unibz.testhunter.action.ActionCheckProject;
import it.unibz.testhunter.action.ActionCommandHelp;
import it.unibz.testhunter.action.ActionHelp;
import it.unibz.testhunter.action.ActionListPlugins;
import it.unibz.testhunter.action.ActionListProjects;
import it.unibz.testhunter.action.ActionProject;
import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.cmd.CmdAddProject;
import it.unibz.testhunter.cmd.CmdCheckProject;
import it.unibz.testhunter.cmd.CmdListCommands;
import it.unibz.testhunter.cmd.CmdListPlugins;
import it.unibz.testhunter.cmd.CmdListProjects;
import it.unibz.testhunter.cmd.CmdProject;
import it.unibz.testhunter.cmd.Command;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.Transactional;

import java.net.URL;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

public class TestCommands {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Injector injector = Guice.createInjector(new AbstractModule() {
			// Actions Module
			@Override
			protected void configure() {
				bind(ICommandAction.class).annotatedWith(
						Names.named("cmd-help")).to(ActionCommandHelp.class);
				bind(ICommandAction.class).annotatedWith(
						Names.named(CmdListProjects.name)).to(
						ActionListProjects.class);
				bind(ICommandAction.class).annotatedWith(
						Names.named(CmdAddProject.name)).to(
						ActionAddProject.class);
				bind(ICommandAction.class).annotatedWith(
						Names.named(CmdListCommands.name)).to(ActionHelp.class);
				bind(ICommandAction.class).annotatedWith(
						Names.named(CmdCheckProject.name)).to(
						ActionCheckProject.class);
				bind(ICommandAction.class).annotatedWith(
						Names.named(CmdListPlugins.name)).to(
						ActionListPlugins.class);
				bind(ICommandAction.class).annotatedWith(
						Names.named(CmdProject.name)).to(ActionProject.class);

			}
		}, new AbstractModule() {
			// Module Services
			@Override
			protected void configure() {
				EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();
				bind(EntityManager.class).toInstance(
						Persistence.createEntityManagerFactory(
								"testhunter_test").createEntityManager());
				requestInjection(interceptor);
				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);
				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class),
						interceptor);
			}
		});

		CommandFactory.registerCommandProvider(CmdListCommands.name, injector
				.getInstance(CmdListCommands.class).getProvider());
		CommandFactory.registerCommandProvider(CmdListProjects.name, injector
				.getInstance(CmdListProjects.class).getProvider());
		CommandFactory.registerCommandProvider(CmdAddProject.name, injector
				.getInstance(CmdAddProject.class).getProvider());
		CommandFactory.registerCommandProvider(CmdListPlugins.name, injector
				.getInstance(CmdListPlugins.class).getProvider());
		CommandFactory.registerCommandProvider(CmdCheckProject.name, injector
				.getInstance(CmdCheckProject.class).getProvider());
		CommandFactory.registerCommandProvider(CmdProject.name, injector
				.getInstance(CmdProject.class).getProvider());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Test
	public void checkRegisteredCommands() {
		System.out.println("checkRegisteredCommands:");
		for (String cmd : CommandFactory.getRegisteredCommands()) {
			System.out.println(cmd);
		}
		System.out.flush();
	}

	@Test
	public void cmdHelp() throws Exception {
		Command cmd = CommandFactory.createCommand("help");
		cmd.execute();
	}

	@Test
	public void cmdAddProjectNoOptions() throws Exception {
		Command cmd = CommandFactory.createCommand("add-project");
		try {
			cmd.execute();
		} catch (TException e) {
			System.out.println(e.getUserMsg());
		}

	}

	@Test
	public void cmdAddProjectHelp() throws Exception {
		Command cmd = CommandFactory.createCommand("add-project");
		cmd.setHelpOption(true);
		cmd.execute();
	}

	@Test
	public void cmdAddProject() throws Exception {
		Command cmd = CommandFactory.createCommand("add-project");
		cmd.setCmdOption("name", "test project");
		cmd.setCmdOption("url", new URL(
				"http://hudson.testrun.org/job/pytest-xdist"));
		cmd.setCmdOption("plugin",
				UUID.fromString("b398d1ed-1251-471d-8fc3-e01f6169649e"));
		cmd.execute();
	}

	@Test
	public void cmdListProjects() throws Exception {
		Command cmd = CommandFactory.createCommand("list-projects");
		cmd.execute();
	}

	@Test
	public void cmdListPlugins() throws Exception {
		Command cmd = CommandFactory.createCommand("list-plugins");
		cmd.execute();
	}
	
	@Test
	public void cmdChkProject() throws Exception {
		Command cmd = CommandFactory.createCommand("chk-project");
		cmd.setCmdOption("id", new Long(1));
		cmd.execute();
	}

	@Test
	public void cmdGrabProject() throws Exception {
		Command cmd = CommandFactory.createCommand("project");
		cmd.setCmdOption("id", new Long(1));
		cmd.setCmdOption("clear", new Boolean(true));
		cmd.execute();
	}


}
