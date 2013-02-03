package it.unibz.testhunter.cmd;

import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CmdListPlugins extends Command {

	public static final String name="list-plugins";
	
	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}


	@Override
	public String getCmdHelp() {
		return "Print list of available plugins from jars within 'plugins' folder";
	}

	@Override
	public String getCmdName() {
		return name;
	}

	@Override
	protected void setParserOptions() {
		// no options required
	}


	@Override
	protected Object getParserOption(String optionName) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
