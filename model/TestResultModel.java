package it.unibz.testhunter.model;

import it.unibz.testhunter.IStatusNormalizer;
import it.unibz.testhunter.ITimeNormalizer;
import it.unibz.testhunter.db.TestsResultItem;

import java.util.LinkedHashMap;
import java.util.Vector;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TestResultModel implements Comparable<TestResultModel> {

	private ITimeNormalizer timeNormalizer;
	private IStatusNormalizer statusNormalizer;
	private Long test;
	private long totalCount;
	private Float info;
	private Float time;
	private LinkedHashMap<Long, Vector<Long>> statusCountTimesMerges;

	@Inject
	public TestResultModel(@Assisted Long test, ITimeNormalizer timeNormalizer, IStatusNormalizer statusNormalizer) {
		this.timeNormalizer = timeNormalizer;
		this.statusNormalizer = statusNormalizer;
		
		this.test = test;
		totalCount = 0;
		info = null;
		time = null;
		statusCountTimesMerges = new LinkedHashMap<Long, Vector<Long>>();
	}

	public void addTestResult(TestsResultItem tr) {
		if (tr.getAvgTime() <= 0) {
			// exit if average time is zero - those test wont be considered
			return;
		}
		Long status = statusNormalizer.normalize(tr.getStatus());
		Long count = tr.getCount();

		Long t = timeNormalizer.prepare(tr.getAvgTime().longValue());

		totalCount += count;

		if (statusCountTimesMerges.get(status) != null) {
			Vector<Long> v = statusCountTimesMerges.get(status);
			Long oldCount = v.get(0);
			Long oldTime = v.get(1);
			Long oldMerges = v.get(2);

			Long newCount = count + oldCount;
			Long newTime = new Long(Math.round((float) (oldTime * oldMerges + t)/ (oldMerges + 1)));
			Long newMerges = oldMerges++;

			v.clear();
			v.add(newCount);
			v.add(newTime);
			v.add(newMerges);
		} else {
			Vector<Long> v = new Vector<Long>(3);
			v.add(count);
			v.add(t);
			v.add(new Long(1));
			statusCountTimesMerges.put(status, v);
		}
	}

	public Long getTest() {
		return test;
	}

	private void computeInfoTime() {
		long avgTime = 0;
		float sumInfo = 0;

		for (Vector<Long> v : statusCountTimesMerges.values()) {
			long count = v.get(0).longValue();
			long t = v.get(1).longValue();
			float p = (float) count / totalCount;

			sumInfo -= p * Math.log(p);
			avgTime += Math.round((t * count) / totalCount);
		}

		float normalizedAvgTime = timeNormalizer.normalize(avgTime);
		// remember info and time values
		info = new Float(sumInfo);
		time = new Float(normalizedAvgTime);
		// end
	}

	public Double getTangent() {
		if (info == null || time == null) {
			computeInfoTime();
		}
		if (time != 0) {
			double d = (double) (info / time);
			return new Double(d);
		} else {
			return new Double(0);
		}
	}

	public Float getInfo() {
		if (info == null) {
			computeInfoTime();
		}
		return info;
	}

	public Float getTime() {
		if (time == null) {
			computeInfoTime();
		}
		return time;
	}

	@Override
	public int compareTo(TestResultModel o) {
		// time case
		if (this.getTime() == 0 && o.getTime() == 0) {
			return o.getInfo().compareTo(this.info);
		}
		if (this.getTime() == 0) {
			return -1;
		}
		if (o.getTime() == 0) {
			return 1;
		}
		// tangents case
		if (this.getTangent() == 0 && o.getTangent() == 0) {
			return o.getInfo().compareTo(this.info);
		}

		return o.getTangent().compareTo(this.getTangent());
	}
}
