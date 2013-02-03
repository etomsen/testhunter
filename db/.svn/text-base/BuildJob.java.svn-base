package it.unibz.testhunter.db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.PrivateOwned;

@Entity
@Table(name = "build_job", uniqueConstraints = @UniqueConstraint(columnNames = "build_number, job_name"))
public class BuildJob implements Serializable {

	private static final long serialVersionUID = 1L;

	public BuildJob() {
		this.tests = new HashSet<TestInstance>();
	}

	public BuildJob(Long buildNumber, Timestamp buildStart, String jobName) {
		this.buildNumber = buildNumber;
		this.buildStart = buildStart;
		this.jobName = jobName;
		this.tests = new HashSet<TestInstance>();
	}

	@Id
	@GeneratedValue(generator = "seq_build_job_id", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "buld_number", nullable = false)
	private Long buildNumber;

	@Column(name = "buld_start", nullable = false)
	private Timestamp buildStart;

	@Column(name = "job_name", nullable = false, length = 255)
	private String jobName;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Project.class, cascade = CascadeType.ALL)
	private Project project;

	@OneToMany(targetEntity = TestInstance.class, mappedBy = "buildJob", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@PrivateOwned
	private java.util.Set<TestInstance> tests;

	public Long getId() {
		return id;
	}

	public void setBuildNumber(Long buildNumber) {
		this.buildNumber = buildNumber;
	}

	public Long getBuildNumber() {
		return buildNumber;
	}

	public void setBuildStart(Timestamp buildStart) {
		this.buildStart = buildStart;
	}

	public Timestamp getBuildStart() {
		return buildStart;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	@Override
	public String toString() {
		return id + ", #" + buildNumber + ", " + buildStart.toString() + ", "
				+ jobName;
	}

	public void setTests(Set<TestInstance> tests) {
		this.tests = tests;
	}

	public Set<TestInstance> getTests() {
		return tests;
	}

	public TestInstance addTest(TestInstance test) {
		this.tests.add(test);
		if (test.getBuildJob() != this) {
			test.setBuildJob(this);
		}
		return test;
	}

}
