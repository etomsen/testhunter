package it.unibz.testhunter.cmd;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jargs.gnu.CmdLineParser.Option;
import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;

public class CmdTestResults extends Command {

	public final static String name = "test-results";
	private Option oProjectId;
	private Option oJobName;
	
	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}
	
	private Long getOptionId() throws TException {
		Object o = paramsParser.getOptionValue(oProjectId);
		if (o != null) {
			return (Long) o;
		} else
			throw getOptionMissingException(oProjectId.longForm());
	}
	
	private String getOptionJobName() throws TException {
		Object o = paramsParser.getOptionValue(oJobName);
		if (o != null) {
			return (String) o;
		} else {
			return null;
		}
	}
	
	@Override
	public String getCmdName() {
		return name;
	}

	@Override
	public String getCmdHelp() {
		return "Calcs test-tesults table for the project specified by --project (-p) parameter. Options: -j job_name";
	}

	@Override
	protected void setParserOptions() {
		oProjectId = paramsParser.addLongOption('p', "project");
		oJobName = paramsParser.addStringOption('j', "job-name");
	}

	@Override
	protected Object getParserOption(String optionName) throws TException {
		if (optionName == oProjectId.longForm()) {
			return getOptionId();
		}
		if (optionName == oJobName.longForm()) {
			return getOptionJobName();
		}
		return null;
	}

}
