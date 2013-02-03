package it.unibz.testhunter;

public class PairFloat implements Pair<Float> {
	
	private float x = 0;
	private float y = 0;
	
	public PairFloat(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Float getX() {
		return new Float(x);
	}

	@Override
	public Float getY() {
		return new Float(y);
	}

	@Override
	public void setX(Float x) {
		this.x = x.floatValue();
	}

	@Override
	public void setY(Float y) {
		this.y = y.floatValue();
	}

}
