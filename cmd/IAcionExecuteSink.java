package it.unibz.testhunter.cmd;

import it.unibz.testhunter.shared.TException;


public interface IAcionExecuteSink {
	
	public String getCmdOptions() throws TException;
	public Object getCmdOption(String optionName) throws TException;
	public String getCmdName();
	public String getCmdHelp();
}
