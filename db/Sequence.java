package it.unibz.testhunter.db;

import java.io.Serializable;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.PrivateOwned;

@Entity
@Table(name = "test_sequence")
public class Sequence implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Sequence() {
		items = new HashSet<SequenceItem>();
	}
	
	public java.util.Set<SequenceItem> getItems() {
		return items;
	}
	public void setItems(java.util.Set<SequenceItem> items) {
		this.items = items;
	}
	public void addItem(Float fitness, Float time, Long testId) {
		SequenceItem item = new SequenceItem();
		item.setSequence(this);
		item.setIndex(new Long(items.size()));
		item.setFitness(fitness);
		item.setTime(time);
		item.setTestId(testId);
		items.add(item);
	}

	@Id
	@GeneratedValue(generator = "seq_test_sequence_id", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "project_id", nullable = false)
	private Long projectId;
	
	@JoinColumn(name = "test_result", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TestsResult.class, cascade = CascadeType.ALL)
	private TestsResult testResult;
	
	@Column(name = "min_time_ms", nullable = false)
	private Long minTimeMs;
	
	@Column(name = "max_time_ms", nullable = false)
	private Long maxTimeMs;
		
	@Column(name = "options", length = 255)
	private String options;
	
	@Column(name = "avg_value")
	private Float avgValue;

	@OneToMany(targetEntity = SequenceItem.class, mappedBy = "sequence", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@PrivateOwned
	private java.util.Set<SequenceItem> items;	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getMinTimeMs() {
		return minTimeMs;
	}
	public void setMinTimeMs(Long minTimeMs) {
		this.minTimeMs = minTimeMs;
	}
	public Long getMaxTimeMs() {
		return maxTimeMs;
	}
	public void setMaxTimeMs(Long maxTimeMs) {
		this.maxTimeMs = maxTimeMs;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}

	public void setAvgValue(Float avgValue) {
		this.avgValue = avgValue;
	}

	public Float getAvgValue() {
		return avgValue;
	}

	public void setTestResult(TestsResult testResult) {
		this.testResult = testResult;
	}

	public TestsResult getTestResult() {
		return testResult;
	}}
