package it.unibz.testhunter.action;

import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.shared.TException;

public class ActionCommandHelp implements ICommandAction {

	@Override
	public void execute(IAcionExecuteSink command) throws TException {
		System.out.println(command.getCmdHelp());
		System.out.flush();
	}

}
