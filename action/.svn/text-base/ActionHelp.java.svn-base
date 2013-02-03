package it.unibz.testhunter.action;

import it.unibz.testhunter.CommandFactory;
import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.shared.TException;

public class ActionHelp implements ICommandAction {

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		System.out.println("Printing list of commands:");
		for (String cmd : CommandFactory.getRegisteredCommands()) {
			System.out.println(cmd);
		} 
		System.out.flush();
	}

}
