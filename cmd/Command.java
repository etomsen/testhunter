package it.unibz.testhunter.cmd;

import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import it.unibz.testhunter.CommandReceiver;
import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;
import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

public abstract class Command implements IAcionExecuteSink {

	protected CmdLineParser paramsParser;
	protected CommandReceiver cmdReceiver = null;
	private CmdLineParser.Option optHelp;

	protected ICommandAction helpAction;
	protected ICommandAction aciton;
	private Boolean helpOptionEnabled = false;
	private HashMap<String, Object> options;
	private String cmdOptionsString; 

	public Command() {
		options = new HashMap<String, Object>();
		paramsParser = new CmdLineParser();
		optHelp = paramsParser.addBooleanOption('h', "help");
		setParserOptions();
	}

	protected TException getOptionMissingException(String optionName) {
		return new TException("cmd options error").setUserMsg(String.format(
				"Required option missing: %s", optionName));
	}

	protected TException getCommandException() {
		return new TException("command general error").setUserMsg(String
				.format("unexpected error: command '%s' cannot be executed",
						getCmdName()));
	}

	protected Boolean getHelpOption() {
		return helpOptionEnabled
				|| (paramsParser.getOptionValue(optHelp) != null);
	}

	public void setHelpOption(Boolean value) {
		helpOptionEnabled = value;
	}

	protected abstract void setParserOptions();

	public void execute() throws TException {
		if (getHelpOption()) {
			helpAction.execute(this);
		} else {
			aciton.execute(this);
		}
	}

	public void setParams(String[] params) throws TException {		
		try {
			paramsParser.parse(params);
		} catch (IllegalOptionValueException e) {
			throw new TException(e.getMessage()).setUserMsg(e.getMessage());
		} catch (UnknownOptionException e) {
			throw new TException(e.getMessage()).setUserMsg(e.getMessage());
		}	
		cmdOptionsString = "";
		for (String string : params) { 
			cmdOptionsString += " "+string; 
		}
		System.out.println("options:"+cmdOptionsString);
	}

	public void setReceiver(CommandReceiver receiver) {
		cmdReceiver = receiver;
	}

	@Inject
	public void setHelpAction(@Named("cmd-help") ICommandAction helpAction) {
		this.helpAction = helpAction;
	}

	public Provider<Command> getProvider() throws TException {
		if (aciton == null) {
			String s = String.format("error: %s: command action not registered", getCmdName());
			throw new TException(s).setUserMsg(s).setTerminateApp();
		}
		@SuppressWarnings("rawtypes")
		final Class clazz = this.getClass();
		return new Provider<Command>() {

			@Override
			public Command get() {
				Command cmd;
				try {
					cmd = (Command) clazz.newInstance();
					cmd.aciton = aciton;
					cmd.helpAction = helpAction;
					return cmd;
				} catch (Exception e) {
					// command initialization error
					// will cause the CommandFactory to throw an error message
					return null;
				}
			}
		};
	}

	protected abstract Object getParserOption(String optionName) throws TException;
	
	@Override 
	public String getCmdOptions() throws TException {
		return cmdOptionsString;		
	}
	
	@Override
	public Object getCmdOption(String optionName) throws TException {
		Object option = options.get(optionName);
		if (option == null) {
			option = getParserOption(optionName);
		} 
		return option;
	}

	public void setCmdOption(String name, Object option) {
		options.put(name, option);
	}
}
