package it.unibz.testhunter;

import it.unibz.testhunter.cmd.CmdAddProject;
import it.unibz.testhunter.cmd.CmdCheckProject;
import it.unibz.testhunter.cmd.CmdCreateTestSequence;
import it.unibz.testhunter.cmd.CmdListCommands;
import it.unibz.testhunter.cmd.CmdListPlugins;
import it.unibz.testhunter.cmd.CmdListProjects;
import it.unibz.testhunter.cmd.CmdProject;
import it.unibz.testhunter.cmd.CmdTestResults;
import it.unibz.testhunter.logging.Log;
import it.unibz.testhunter.shared.TException;

import java.util.LinkedList;
import java.util.List;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	/**
	 * @param args
	 */
	static {
		try {
			Log.setup();
		} catch (Exception e) {
			System.out.println("Unable to setup logger.");
		} 
	}
	
	private static void registerCommands(Injector injector) throws TException {
		CommandFactory.registerCommandProvider(CmdListCommands.name, injector
				.getInstance(CmdListCommands.class).getProvider());
		CommandFactory.registerCommandProvider(CmdListProjects.name, injector
				.getInstance(CmdListProjects.class).getProvider());
		CommandFactory.registerCommandProvider(CmdAddProject.name, injector
				.getInstance(CmdAddProject.class).getProvider());		
		CommandFactory.registerCommandProvider(CmdListPlugins.name, injector
				.getInstance(CmdListPlugins.class).getProvider());
		CommandFactory.registerCommandProvider(CmdCheckProject.name, injector
				.getInstance(CmdCheckProject.class).getProvider());
		CommandFactory.registerCommandProvider(CmdProject.name, injector
				.getInstance(CmdProject.class).getProvider());
		CommandFactory.registerCommandProvider(CmdTestResults.name, injector
				.getInstance(CmdTestResults.class).getProvider());
		CommandFactory.registerCommandProvider(CmdCreateTestSequence.name, injector
				.getInstance(CmdCreateTestSequence.class).getProvider());
	}
		
	public static void main(String[] args) throws Exception {
		// set global handler for critical and unhandled exceptions
		TExceptionHander h = new TExceptionHander();
		Thread.setDefaultUncaughtExceptionHandler(h);
		
		ConsoleReader reader = new ConsoleReader();
		reader.setBellEnabled(false);
		
		Injector injector = Guice.createInjector(new ModuleActions(), new ModuleServices());
		registerCommands(injector);
		CommandReceiver receiver = injector.getInstance(CommandReceiver.class);
		
		CommandInvoker invoker = new CommandInvoker();
		
		// command completer 
		List<Completor> completors = new LinkedList<Completor>();
		String[] commands = CommandFactory.getRegisteredCommands().toArray(new String[0]);	
		completors.add(new SimpleCompletor(commands));
		reader.addCompletor(new ArgumentCompletor(completors));
	
		
		String line;
		while ((line = reader.readLine("test-hunter> ")) != null) {
			if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
				break;
			}
			String[] cmdParts = line.trim().split(" ");
			if (cmdParts.length > 0) {
				String cmdName = cmdParts[0];
				String[] cmdOptions = new String[cmdParts.length-1];
				System.arraycopy(cmdParts, 1, cmdOptions, 0, cmdParts.length-1);
				try {
					invoker.queueCmd(cmdName, cmdOptions, receiver);
				} catch (TException e) {
					// if error is critical propagate it to global handler
					if (e.getTerminateApp()) {
						throw e;
					} else {
						System.out.println(e.getUserMsg());
						System.out.flush();
					}
				}	
			}
		}
	}
}
