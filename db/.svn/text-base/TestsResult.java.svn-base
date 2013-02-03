package it.unibz.testhunter.db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.PrivateOwned;

@Entity
@Table(name = "tests_result")
public class TestsResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public TestsResult() {
		items = new HashSet<TestsResultItem>();
	}
	
	@Id
	@GeneratedValue(generator = "seq_test_sequence_id", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "project", nullable = false)
	private Long projectId;
	
	@Column(name = "last_build")
	private Long lastBuild;
	
	@Column(name = "job")
	private String jobName;
		
	@Column(name = "date", nullable = false)
	private Timestamp date;
	
	@Column(name ="options")
	private String options;
	
	@OneToMany(targetEntity = TestsResultItem.class, mappedBy = "testsResult", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@PrivateOwned
	private java.util.Set<TestsResultItem> items;
	
	@Transient
	private boolean includeZeroTimeToCount = true;
	
	@Transient
	private boolean includeZeroTimeToTime = false;
	
	public boolean getIncludeZeroTimeToCount() {
		return includeZeroTimeToCount;
	}
	
	public boolean getIncludeZeroTimeToTime() {
		return includeZeroTimeToTime;
	}
	
	public void setIncludeZeroTimeToCount(boolean value) {
		this.includeZeroTimeToCount = value;
	}
	
	public void setIncludeZeroTimeToTime(boolean value) {
		this.includeZeroTimeToTime = value;
	}

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

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getLastBuild() {
		return lastBuild;
	}

	public void setLastBuild(Long lastBuild) {
		this.lastBuild = lastBuild;
	}

	public java.util.Set<TestsResultItem> getItems() {
		return items;
	}

	public void setItems(java.util.Set<TestsResultItem> items) {
		this.items = items;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getOptions() {
		return options;
	}
	
}
