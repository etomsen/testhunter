package it.unibz.testhunter.svc;

import java.util.UUID;

import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.TException;

public interface SvcPlugin {

	public void reloadPlugins() throws TException;
	public Iterable<IPlugin> getPlugins();
	public IPlugin getPluginByUUID(UUID pluginUUID) throws TException;
}
