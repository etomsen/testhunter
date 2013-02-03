package it.unibz.testhunter.cmd;

import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CmdListCommands extends Command {
	
	public final static String name = "help";
	
	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}
	
	
	@Override
	protected void setParserOptions() {
		// no options required
	}

	@Override
	public String getCmdName() {
		return name;
	}

	@Override
	public String getCmdHelp() {
		return "Prints command list.";
	}


	@Override
	protected Object getParserOption(String optionName) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
