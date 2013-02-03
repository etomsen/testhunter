package it.unibz.testhunter.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "test", uniqueConstraints = @UniqueConstraint(columnNames = "name, class, package"))
public class Test implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Test() {
		this.instances = new ArrayList<TestInstance>();
	}
	
	public Test(String testName, String testClass, String testPackage) {
		this.instances = new ArrayList<TestInstance>();
		this.testName = testName;
		this.testClass = testClass;
		this.testPackage = testPackage;
	}
	
	@Id
	@GeneratedValue(generator = "seq_test_id", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 255)
	private String testName;
	
	@Column(name = "class", nullable = false, length = 255)
	private String testClass;
	
	@Column(name = "package", nullable = false, length = 255)
	private String testPackage;
	
	@OneToMany(targetEntity=TestInstance.class, mappedBy="test", fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.PERSIST) 
	private Collection<TestInstance> instances;

	public Long getId() {
		return id;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestClass(String testClass) {
		this.testClass = testClass;
	}

	public String getTestClass() {
		return testClass;
	}

	public void setTestPackage(String testPackage) {
		this.testPackage = testPackage;
	}

	public String getTestPackage() {
		return testPackage;
	}

	public void setInstances(Collection<TestInstance> instances) {
		this.instances = instances;
	}

	public Collection<TestInstance> getInstances() {
		return instances;
	}
	
	public TestInstance addTestInstance(TestInstance t) {
		this.instances.add(t);
		if (t.getTest() != this) {
			t.setTest(this);
		}
		return t;
	}
	
	@Override
	public String toString() {
		return id + ", " + testName + ", " + testClass + ", " + testPackage;	
	}


}
