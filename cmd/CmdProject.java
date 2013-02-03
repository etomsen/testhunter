package it.unibz.testhunter.cmd;

import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;
import jargs.gnu.CmdLineParser.Option;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CmdProject extends Command {

	public static final String name = "project";

	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}

	private Option optionId;
	private Option optionGrab;
	private Option optionClear;
	private Option optionComputeTestsResult;

	@Override
	public String getCmdHelp() {
		StringBuilder b = new StringBuilder();
		b.append("if --clear option specified delete all builds for the project specified by --id (-i) option \n");
		b.append("if --grab option specified loads all new builds and test for the project specified by --id (-i) option \n");
		b.append("if --tests-result option tests' execution probabilities and average durations are recomputed for the project specified by --id (-i) option \n");
		b.append("if both --grab AND --clear specified does first --clear then --grab \n");
		return b.toString();
	}

	@Override
	public String getCmdName() {
		return name;
	}

	@Override
	protected void setParserOptions() {
		optionId = paramsParser.addLongOption('i', "id");
		optionGrab = paramsParser.addBooleanOption('g', "grab");
		optionClear = paramsParser.addBooleanOption('c', "clear");
		optionComputeTestsResult = paramsParser.addBooleanOption('r', "tests-result");

	}

	private Long getOptionId() throws TException {
		Object o = paramsParser.getOptionValue(optionId);
		if (o != null) {
			return (Long) o;
		} else
			throw getOptionMissingException(optionId.longForm());
	}
	
	private Boolean getOptionGrab() throws TException {
		return (paramsParser.getOptionValue(optionGrab) != null);
	}

	private Boolean getOptionClear() throws TException {
		return (paramsParser.getOptionValue(optionClear) != null);
	}

	private Boolean getOptionComputeProbs() throws TException {
		return (paramsParser.getOptionValue(optionComputeTestsResult) != null);
	}

	@Override
	protected Object getParserOption(String optionName) throws TException {
		if (optionName == optionId.longForm()) {
			return getOptionId();
		}
		if (optionName == optionGrab.longForm()) {
			return getOptionGrab();
		}
		if (optionName == optionClear.longForm()) {
			return getOptionClear();
		}
		if (optionName == optionComputeTestsResult.longForm()) {
			return getOptionComputeProbs();
		}
		return null;
	}


}
