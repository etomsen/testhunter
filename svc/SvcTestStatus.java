package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.Status;
import it.unibz.testhunter.db.Status_;
import it.unibz.testhunter.shared.TException;

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

public class SvcTestStatus {

	private EntityManager em;

	@Inject
	public SvcTestStatus(EntityManager em) {
		this.em = em;
	}

	public Status selectByNameByPlugin(String name, UUID plugin)
			throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Status> c = cb.createQuery(Status.class);
			Root<Status> p = c.from(Status.class);
			c.where(cb.and(cb.equal(p.get(Status_.name), name),
					cb.equal(p.get(Status_.plugin), plugin.toString())));
			TypedQuery<Status> q = em.createQuery(c);
			return q.setMaxResults(1).getSingleResult();
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public Collection<Status> selectByPlugin(UUID plugin) throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Status> c = cb.createQuery(Status.class);
			Root<Status> p = c.from(Status.class);
			c.where(cb.equal(p.get(Status_.plugin), plugin.toString()));
			TypedQuery<Status> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<Status>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public Status insertIfNotExists(String name, UUID plugin)
			throws TException {
		Status ts = this.selectByNameByPlugin(name, plugin);
		if (ts == null) {
			ts = this.insert(name, plugin);
		}
		return ts;
	}

	@Transactional
	public Status insert(String name, UUID plugin) throws TException {
		try {
			Status testStatus = new Status(name, plugin);
			em.persist(testStatus);
			return testStatus;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public Status select(Long id) throws TException {
		try {
			return em.find(Status.class, id);
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void delete(Status ts) throws TException {
		try {
			em.remove(ts);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}

	}
	
	public List<Status> selectAll() throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Status> c = cb.createQuery(Status.class);
			TypedQuery<Status> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<Status>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void clearAll() throws TException {
		List<Status> tsList = selectAll();
		for (Status ts : tsList) {
			delete(ts);
		}
	}


}
