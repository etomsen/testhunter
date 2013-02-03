package it.unibz.testhunter.cmd;

import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;
import jargs.gnu.CmdLineParser.Option;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CmdCreateTestSequence extends Command {

	public final static String name = "create-test-sequence";
	public final static String optionTestResultMin = "tr-min";
	public final static String optionTestResultMax = "tr-max";
	public final static String optionZeroTime = "zero-time";
	
	private Option oMinTestResult;
	private Option oMaxTestResult;
	private Option oZeroTime;
	
	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}
	
	@Override
	public String getCmdName() {
		return "create-test-sequence";
	}

	@Override
	public String getCmdHelp() {
		return "creates tests sequence based on given test-result (tr option)";
	}

	@Override
	protected void setParserOptions() {
		oMinTestResult = paramsParser.addLongOption('i', optionTestResultMin);
		oMaxTestResult = paramsParser.addLongOption('a', optionTestResultMax);
		oZeroTime = paramsParser.addBooleanOption('z', optionZeroTime);
	}

	@Override
	protected Object getParserOption(String optionName) throws TException {
		if (optionName == oMinTestResult.longForm()) {
			return getOptionTestResultsMin();
		}
		if (optionName == oMaxTestResult.longForm()) {
			return getOptionTestResultsMax();
		}
		if (optionName == oZeroTime.longForm()) {
			return getOptionZeroTime();
		}
		return null;
	}
	
	private Long getOptionTestResultsMin() throws TException {
		Object o = paramsParser.getOptionValue(oMinTestResult);
		if (o != null) {
			return (Long) o;
		}
		throw getOptionMissingException(oMinTestResult.longForm());						
	}
	
	private Long getOptionTestResultsMax() throws TException {
		Object o = paramsParser.getOptionValue(oMaxTestResult);
		if (o != null) {
			return (Long) o;
		}
		throw getOptionMissingException(oMaxTestResult.longForm());						
						
	}
	
	private Boolean getOptionZeroTime() throws TException {
		Object o = paramsParser.getOptionValue(oZeroTime);
		if (o != null) {
			return (Boolean) o;
		} else {
			return new Boolean(false);
		}		
	}
	

}
