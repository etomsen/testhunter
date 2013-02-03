package it.unibz.testhunter;

public class TimeNormalizerImpl implements ITimeNormalizer {
	
	long minTime, maxTime;

	public TimeNormalizerImpl(long minTime, long maxTime) {
		this.minTime = minTime;
		this.maxTime = maxTime;
	}
	
	@Override
	public float normalize(long timeMsec) {
		long time = prepare(timeMsec);
		return (float) (time - minTime) / (maxTime - minTime);
	}

	@Override
	public Long prepare(long timeMsec) {
		long time = (timeMsec <= minTime) ? minTime : timeMsec;
		time = (time > maxTime) ? maxTime : time;	
		return new Long(time);
	}
}
