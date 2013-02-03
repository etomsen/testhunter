package it.unibz.testhunter;

import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.db.TestInstance;

import java.util.List;

public interface IPtfMetricProvider {
	PtfMetric create(Sequence sequnce, List<TestInstance> results);
}
