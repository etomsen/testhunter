package it.unibz.testhunter.cmd;

import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.shared.TException;
import jargs.gnu.CmdLineParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CmdAddProject extends Command {

	public final static String name = "add-project";

	@Inject
	public void setAction(@Named(name) ICommandAction action) {
		this.aciton = action;
	}

	private CmdLineParser.Option urlOption;
	private CmdLineParser.Option pluginOption;
	private CmdLineParser.Option nameOption;

	private UUID getPlugin() throws TException {
		Object o = paramsParser.getOptionValue(pluginOption);
		if (o != null) {
			String s = (String) o;
			try {
				return UUID.fromString(s);
			} catch (IllegalArgumentException e) {
				throw new TException("cmd options error").setUserMsg(s
						+ " is not a valid UUID");
			}
		} else
			throw getOptionMissingException(pluginOption.longForm());

	}

	private URL getUrl() throws TException {
		Object o = paramsParser.getOptionValue(urlOption);
		if (o != null) {
			String s = (String) o;
			try {
				return new URL(s);
			} catch (MalformedURLException e) {
				throw new TException("cmd options error").setUserMsg(s
						+ "is not a valid URL");
			}
		} else {
			return null;
		}
	}

	private String getProjectName() throws TException {
		Object o = paramsParser.getOptionValue(nameOption);
		if (o != null) {
			return (String) o;
		} else
			throw getOptionMissingException(nameOption.longForm());
	}

	@Override
	protected void setParserOptions() {
		urlOption = paramsParser.addStringOption('u', "url");
		pluginOption = paramsParser.addStringOption('p', "plugin");
		nameOption = paramsParser.addStringOption('n', "name");
	}

	@Override
	public String getCmdName() {
		return name;
	}

	@Override
	public String getCmdHelp() {
		StringBuilder b = new StringBuilder();
		b.append("Adds project record to db\n");

		b.append("Usage (long form): \n");
		b.append(String.format("%s --%s=string --%s=string --%s=string\n",
				name, nameOption.longForm(), urlOption.longForm(),
				pluginOption.longForm()));
		b.append("Usage (short form): \n");
		b.append(String.format("%s -%s string -%s string -%s string\n", name,
				nameOption.shortForm(), urlOption.shortForm(),
				pluginOption.shortForm()));
		return b.toString();
	}

	@Override
	protected Object getParserOption(String optionName) throws TException {
		if (optionName == "url") {
			return getUrl();
		} 
		if (optionName == "plugin") {
			return getPlugin();
		} 
		if (optionName == "name") {
			return getProjectName();
		}
		return null;	
	}

}
