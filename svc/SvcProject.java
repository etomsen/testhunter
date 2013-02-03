package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.db.Project_;
import it.unibz.testhunter.shared.TException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;

public class SvcProject {

	private final EntityManager em;
	
	@Inject
	public SvcProject(EntityManager em) {
		this.em = em;
	}
	
	@Transactional
	public void delete(Project prj) throws TException {
		try {
			em.remove(prj);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public List<Project> selectAll() throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Project> c = cb.createQuery(Project.class);
			TypedQuery<Project> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<Project>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public Project select(Long id) throws TException {
		try {
			return em.find(Project.class, id);
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public Collection<Project> selectByPlugin(UUID plugin) throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Project> c = cb.createQuery(Project.class);
			Root<Project> p = c.from(Project.class);
			c.where(cb.equal(p.get(Project_.plugin), plugin.toString()));
			TypedQuery<Project> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<Project>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public Project insertNew(String name, URL url, UUID plugin)
			throws TException {
		try {
			Project prj = new Project(name, url, plugin);
			em.persist(prj);
			return prj;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}

	}
	
	@Transactional
	public void clear(Project prj) throws TException {
		prj.ClearBuilds();
		em.persist(prj);
	}
	
	@Transactional
	public void clearAll() throws TException {
		List<Project> projects = selectAll();
		for (Project prj : projects) {
			delete(prj);
		}
	}

}
