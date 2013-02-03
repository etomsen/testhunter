package it.unibz.testhunter.db;

import it.unibz.testhunter.shared.TException;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
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

import org.eclipse.persistence.annotations.PrivateOwned;


@Entity
@Table(name = "project")
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	public Project() {
		builds = new HashSet<BuildJob>();
	}

	public Project(String name, URL url, UUID plugin) {
		this.builds = new HashSet<BuildJob>();
		this.name = name;
		this.url = url.toString();
		this.plugin = plugin.toString();
	}

	@Id
	@GeneratedValue(generator = "seq_project_id", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "plugin", nullable = false, length = 36)
	private String plugin;

	@OneToMany(targetEntity = BuildJob.class, mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@PrivateOwned
	private java.util.Set<BuildJob> builds;

	public Long getId() {
		return id;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return name;
	}

	public void setPlugin(UUID value) {
		this.plugin = value.toString();
	}

	public UUID getPlugin() throws TException {
		try {
			return UUID.fromString(plugin);
		} catch (IllegalArgumentException e) {
			throw new TException(e.getMessage()).setUserMsg(plugin
					+ " is not well-formed UUID!");
		}
	}

	public void setUrl(URL value) {
		this.url = value.toString();
	}

	public URL getUrl() throws TException {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new TException(e.getMessage()).setUserMsg(url
					+ " is not well-formed URL!");
		}
	}

	public void setBuilds(Set<BuildJob> builds) {
		this.builds = builds;
	}

	public Set<BuildJob> getBuilds() {
		return builds;
	}
	
	public void deleteBuild(BuildJob build) {
		builds.remove(build);
	}

	public BuildJob addBuildJob(BuildJob b) {
		this.builds.add(b);
		if (b.getProject() != this) {
			b.setProject(this);
		}
		return b;
	}

	public void ClearBuilds() {
		builds.clear();
	}

	@Override
	public String toString() {
		return id + ", " + name + ", " + url + ", " + plugin;
	}

}
