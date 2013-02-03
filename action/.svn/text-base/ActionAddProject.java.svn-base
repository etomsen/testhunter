package it.unibz.testhunter.action;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcProject;

import java.net.URL;
import java.util.UUID;

import com.google.inject.Inject;

public class ActionAddProject implements ICommandAction {

	private SvcProject svcProject;
	private SvcPlugin svcPlugin;

	@Inject
	public ActionAddProject(SvcProject svcProject, SvcPlugin svcPlugin) {
		this.svcProject = svcProject;
		this.svcPlugin = svcPlugin;
	}

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		String name = (String) command.getCmdOption("name");
		URL url = (URL) command.getCmdOption("url");
		UUID plugin = (UUID) command.getCmdOption("plugin");
		addProject(name, url, plugin);
	}

	private void addProject(String projectName, URL url, UUID pluginUUID)
			throws TException {
		System.out.println("Adding project...");
		System.out.flush();
		IPlugin plugin = svcPlugin.getPluginByUUID(pluginUUID);
		if (plugin == null) {
			throw new TException("user message").setUserMsg(String.format(
					"Plugin with name %s was not found!", pluginUUID));
		}

		svcProject.insertNew(projectName, url, pluginUUID);
		
		System.out.println("done.");
		System.out.flush();
	}

}
