package it.unibz.testhunter;

import it.unibz.testhunter.shared.TException;

public interface IStatusResolver {
	boolean isStatusFailed(Long testStatusId) throws TException;
}
