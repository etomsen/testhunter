package it.unibz.testhunter.db;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "test_sequence_item", uniqueConstraints=@UniqueConstraint(columnNames="sequence, index"))
public class SequenceItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@JoinColumn(name="sequence", nullable=false)
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Sequence.class, cascade = CascadeType.ALL)
	private Sequence sequence;
	
	@Id
	@Column(nullable=false)
	private Long index;
	
	@Column(nullable=false)
	private Float time;
	
	@Column(nullable=false)
	private Float fitness;
	
	@Column(name="test_id", nullable=false)
	private Long testId;
	
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	public Sequence getSequence() {
		return sequence;
	}
	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
	public Long getIndex() {
		return index;
	}
	public void setIndex(Long index) {
		this.index = index;
	}
	public Float getTime() {
		return time;
	}
	public void setTime(Float time) {
		this.time = time;
	}
	public Float getFitness() {
		return fitness;
	}
	public void setFitness(Float fitness) {
		this.fitness = fitness;
	}
	
}
