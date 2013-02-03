package it.unibz.testhunter.action;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.SvcPlugin;

import com.google.inject.Inject;

public class ActionListPlugins implements ICommandAction {

	private SvcPlugin svcPlugin;

	@Inject
	public ActionListPlugins(SvcPlugin svcPlugin) {
		this.svcPlugin = svcPlugin;
	}

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		System.out.println("Plugins loaded:");
		if (svcPlugin.getPlugins() != null) {
			for (IPlugin plugin : svcPlugin.getPlugins()) {				
				System.out.print(String.format("Name: %20s \t UUID: %s\n",
						plugin.getName(), plugin.getUUID().toString()));
			}
		} else {
			System.out.println("No plugins loaded!");
		}
		System.out.flush();
	}

}
