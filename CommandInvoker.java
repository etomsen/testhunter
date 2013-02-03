package it.unibz.testhunter;

import it.unibz.testhunter.cmd.Command;
import it.unibz.testhunter.shared.TException;

import java.util.concurrent.ArrayBlockingQueue;

public class CommandInvoker {

	final static int MAX_CMD_QUEUE = 20;
	
	private ArrayBlockingQueue<Command> cmdQueue = new ArrayBlockingQueue<Command>(MAX_CMD_QUEUE);

	public void queueCmd(String cmdName, String[] cmdParams, CommandReceiver receiver) throws TException {
		Command cmd = CommandFactory.createCommand(cmdName);
		try {
			cmd.setParams(cmdParams);
			cmd.setReceiver(receiver);
			cmdQueue.put(cmd);
			cmdQueue.take().execute();
		} catch (InterruptedException e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(String.format("%s: unable to process command"));
		}
	}

}
