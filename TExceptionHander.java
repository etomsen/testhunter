package it.unibz.testhunter;

import it.unibz.testhunter.shared.TException;

import java.lang.Thread.UncaughtExceptionHandler;

public class TExceptionHander implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (e instanceof TException) {
			TException appException = (TException) e;
			System.out.println(appException.getUserMsg());
		} else {
			System.out.println(e.getMessage());
		}
		System.exit(2);
	}

}
