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
@Table(name = "tests_result_item", uniqueConstraints = @UniqueConstraint(columnNames = "tests_result, test, status"))
public class TestsResultItem  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@JoinColumn(name = "tests_result", nullable=false)
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=TestsResult.class, cascade=CascadeType.ALL)
	private TestsResult testsResult;

	@Id
	@Column(nullable = false, name = "test")
	private Long test;
	
	@Id
	@Column(nullable = false, name = "status")
	private Long status;
	
	@Column(name="avg_time")
	private Long avgTime;
	
	@Column(name="count")
	private Long count;
	
	public Long getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(Long time) {
		this.avgTime = time;
	}
	
	public TestsResult getTestsResult() {
		return testsResult;
	}

	public void setTestsResult(TestsResult testsResult) {
		this.testsResult = testsResult;
	}

	public Long getTest() {
		return test;
	}

	public void setTest(Long test) {
		this.test = test;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}


}
