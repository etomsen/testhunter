package it.unibz.testhunter;

import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.db.TestInstance;
import it.unibz.testhunter.db.SequenceItem;
import it.unibz.testhunter.shared.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PtfMetric {

	private Sequence sequence;
	private Map<Long, TestInstance> buildJobResults;
	private IStatusResolver statusResolver;
	private ITimeNormalizer timeNormalizer;
	private long testsCount;
	private long failedTestsCount;

	@Inject
	public PtfMetric(@Assisted Sequence sequnce,
			@Assisted List<TestInstance> buildJobResults,
			IStatusResolver statusResolver, ITimeNormalizer timeNormalizer) {
		this.buildJobResults = new HashMap<Long, TestInstance>();
		for (TestInstance ti : buildJobResults) {
			this.buildJobResults.put(ti.getTest().getId(), ti);
		}
		this.sequence = sequnce;
		this.statusResolver = statusResolver;
		this.timeNormalizer = timeNormalizer;
	}
	
	public long getTestsCount() {
		return testsCount;
	}
	
	public long getFailedTestCount() {
		return failedTestsCount;
	}

	public List<Pair<Float>> computePtfPerTests() throws TException {
		List<Pair<Float>> result = new ArrayList<Pair<Float>>();
		result.add(new PairFloat(0, 0));
		
		testsCount = 0;
		failedTestsCount = 0;
		
		for (SequenceItem tsi : sequence.getItems()) {
			TestInstance i = buildJobResults.get(tsi.getTestId());
			if (i != null) {
				testsCount ++;
				boolean failed = statusResolver.isStatusFailed(i.getTestStatus().getId());
				failedTestsCount += failed ? 1 : 0;
				result.add(new PairFloat(testsCount, failedTestsCount));
			}
		}
		for (Pair<Float> pair : result) {
			pair.setX(new Float(pair.getX().floatValue()/testsCount));
			pair.setY(new Float(pair.getY().floatValue()/failedTestsCount));
		}
		return result;
	}
	
	public List<Pair<Float>> computePtfPerTime() throws TException {
		List<Pair<Float>> result = new ArrayList<Pair<Float>>();
		result.add(new PairFloat(0, 0));
		
		float t = 0;
		float f = 0;
		
		for (SequenceItem tsi : sequence.getItems()) {
			TestInstance i = buildJobResults.get(tsi.getTestId());
			if (i != null) {
				t += timeNormalizer.normalize(i.getDurationMs());
				boolean failed = statusResolver.isStatusFailed(i.getTestStatus().getId());
				f += failed ? 1 : 0;
				result.add(new PairFloat(t, f));
			}
		}
		for (Pair<Float> pair : result) {
			pair.setX(new Float(pair.getX().floatValue()/t));
			pair.setY(new Float(pair.getY().floatValue()/f));
		}
		return result;
	}

}
