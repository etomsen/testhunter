package it.unibz.testhunter;

import it.unibz.testhunter.model.TestResultModel;

import java.util.ArrayList;
import java.util.List;

public class AperMetric implements IMetric {

	private List<TestResultModel> testSequence;
	private List<Float> infoSeq;
	private List<Float> timeSeq;
	private Float T;
	private Float I;
	
	public AperMetric(List<TestResultModel> testSequence) {
		this.testSequence = testSequence;
		infoSeq = new ArrayList<Float>();
		timeSeq = new ArrayList<Float>();
		T = null;
		I = null;
	}
	
	@Override
	public float compute() {
		infoSeq.add(new Float(0));
		timeSeq.add(new Float(0));
		
		float lastInfo = 0;
		float lastTime = 0;
		float aper = 0;
		for (TestResultModel tr : testSequence) {
			aper += (0.5*tr.getInfo()+lastInfo)*tr.getTime();
			lastInfo += tr.getInfo();
			lastTime += tr.getTime();
			infoSeq.add(new Float(lastInfo));
			timeSeq.add(new Float(lastTime));
		}
		I = lastInfo;
		T = lastTime;
		return (float) aper/(I*T);
	}

	@Override
	public String getName() {
		return "APER";
	}

}
