package it.unibz.testhunter.cmd;

import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;
import jargs.gnu.CmdLineParser.Option;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CmdCheckProject extends Command {
	
	public final static String name = "chk-project";

	private Option idOption;

	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}


	private Long getOptionId() throws TException {
		Object o = paramsParser.getOptionValue(idOption);
		if (o != null) {
			return (Long) o;
		} else
			throw getOptionMissingException(idOption.longForm());
	}

	@Override
	public String getCmdHelp() {
		return "Checks whether new builds available for the project specified by --id (-i) parameter";
	}

	@Override
	public String getCmdName() {
		return name;
	}

	@Override
	protected void setParserOptions() {
		idOption = paramsParser.addLongOption('i', "id");
	}


	@Override
	protected Object getParserOption(String optionName) throws TException {
		if (optionName == idOption.longForm()) {
			return getOptionId();
		}
		return null;
	}

}
