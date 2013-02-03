package it.unibz.testhunter.db;

import it.unibz.testhunter.shared.TException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

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
@Table(name = "test_status", uniqueConstraints = @UniqueConstraint(columnNames = "name, plugin"))
public class Status implements Serializable {

	private static final long serialVersionUID = 1L;

	public Status() {
		this.tests = new ArrayList<TestInstance>();
	}

	public Status(String name, UUID plugin) {
		this.tests = new ArrayList<TestInstance>();
		this.name = name;
		this.plugin = plugin.toString();
	}

	@Id
	@GeneratedValue(generator = "seq_test_status_id", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "plugin", nullable = false, length = 36)
	private String plugin;

	@OneToMany(targetEntity = TestInstance.class, mappedBy = "testStatus", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
	Collection<TestInstance> tests;

	public Long getId() {
		return id;
	}

	public UUID getPlugin() throws TException {
		try {
			return UUID.fromString(plugin);
		} catch (IllegalArgumentException e) {
			throw new TException(e.getMessage()).setUserMsg(plugin
					+ " is not well-formed UUID!");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public void setPlugin(UUID value) {
		plugin = value.toString();
	}

	public TestInstance addTest(TestInstance test) {
		this.tests.add(test);
		if (test.getTestStatus() != this) {
			test.setTestStatus(this);
		}
		return test;
	}

	@Override
	public String toString() {
		return id + ", " + name + ", " + plugin;
	}

}
