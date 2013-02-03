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
@Table(name = "test_instance", uniqueConstraints = @UniqueConstraint(columnNames = "test, build_job"))
public class TestInstance implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public TestInstance() {
		
	}
	
	public TestInstance(BuildJob buildJob, Test test, Status testStatus, Long durationMs) {
		this.buildJob = buildJob;
		this.test = test;
		this.testStatus = testStatus;
		this.durationMs = durationMs;
	}

	@Id
	@JoinColumn(nullable = false, name = "test")
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Test.class, cascade=CascadeType.PERSIST)
	private Test test;

	@Id
	@JoinColumn(nullable = false, name = "build_job")
	@ManyToOne(targetEntity = BuildJob.class, fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	private BuildJob buildJob;

	@JoinColumn(nullable = false, name = "test_status")
	@ManyToOne(targetEntity = Status.class, fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Status testStatus;

	@Column(name = "duration_ms")
	private Long durationMs;

	public void setTest(Test test) {
		this.test = test;
	}

	public Test getTest() {
		return test;
	}

	public void setBuildJob(BuildJob buildJob) {
		this.buildJob = buildJob;
	}

	public BuildJob getBuildJob() {
		return buildJob;
	}

	public void setTestStatus(Status testStatus) {
		this.testStatus = testStatus;
	}

	public Status getTestStatus() {
		return testStatus;
	}

	public void setDurationMs(Long durationMs) {
		this.durationMs = durationMs;
	}

	public Long getDurationMs() {
		return durationMs;
	}

	@Override
	public String toString() {
		return "test:" + test.getId() + ", build_job:" + buildJob.getId()
				+ ", status:" + testStatus.getId() + ", duration(ms):"
				+ durationMs;
	}

}
