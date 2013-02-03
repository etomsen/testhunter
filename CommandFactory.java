package it.unibz.testhunter;

import it.unibz.testhunter.cmd.Command;
import it.unibz.testhunter.shared.TException;

import java.util.HashMap;
import java.util.Set;

import com.google.inject.Provider;

public class CommandFactory {

	private static HashMap<String, Provider<Command>> registeregProviders = new HashMap<String, Provider<Command>>();

	public static void registerCommandProvider(String commandName,
			Provider<Command> provider) {
		registeregProviders.put(commandName, provider);
	}

	public static Set<String> getRegisteredCommands() {
		return registeregProviders.keySet();
	}

	public static Command createCommand(String name) throws TException {
		Provider<Command> provider = registeregProviders.get(name);
		if (provider == null) {
			// unable to find command provider
			throw new TException("user message").setUserMsg(String.format(
					"%s: command not found", name));
		}
		Command cmd = null;
		try {
			cmd = provider.get();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg(String.format(
					"%s: unable to load command", name));
		}
		if (cmd == null) {
			throw new TException(String.format("command provider %s corrupted",
					provider.getClass().getName())).setTerminateApp()
					.setUserMsg(
							String.format("%s: unable to load command", name));
		}
		return cmd;
	}
}
