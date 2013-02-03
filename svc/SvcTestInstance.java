package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Status;
import it.unibz.testhunter.db.Test;
import it.unibz.testhunter.db.TestInstance;
import it.unibz.testhunter.db.TestInstance_;
import it.unibz.testhunter.shared.TException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;

public class SvcTestInstance {

	private EntityManager em;

	@Inject
	public SvcTestInstance(EntityManager em) {
		this.em = em;
	}

	public TestInstance selectByTestByBuild(Test test, BuildJob buildJob)
			throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestInstance> c = cb.createQuery(TestInstance.class);
			Root<TestInstance> p = c.from(TestInstance.class);
			c.where(cb.and(
					cb.equal(p.get(TestInstance_.test), test),
					cb.equal(p.get(TestInstance_.buildJob), buildJob)
			));
			TypedQuery<TestInstance> q = em.createQuery(c);
			return q.getSingleResult();
		} catch (NoResultException e1) {
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();

		}
	}

	public List<TestInstance> selectByBuildJob(BuildJob buildJob)
			throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestInstance> c = cb.createQuery(TestInstance.class);
			Root<TestInstance> p = c.from(TestInstance.class);
			c.where(cb.equal(p.get(TestInstance_.buildJob), buildJob));
			TypedQuery<TestInstance> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			return new ArrayList<TestInstance>();
		} catch (Exception e) {
			throw new TException(e.getMessage())
					.setUserMsg(String.format(
						"db select test instance by byuldjob: %d: query error",
						buildJob.getId())).setTerminateApp();
		}
	}

	@Transactional
	public TestInstance insert(Test test, BuildJob buildJob,
			Status testStatus, Long durationMs) throws TException {
		try {
			TestInstance instance = new TestInstance();
			instance.setTest(test);
			instance.setBuildJob(buildJob);
			instance.setTestStatus(testStatus);
			instance.setDurationMs(durationMs);

			em.persist(instance);
			return instance;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}

	}

	@Transactional
	public void delete(TestInstance ti) throws TException {
		try {
			em.remove(ti);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}

	}

	public List<TestInstance> selectAll() throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestInstance> c = cb.createQuery(TestInstance.class);
			TypedQuery<TestInstance> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<TestInstance>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void clearAll() throws TException {
		List<TestInstance> tiList = selectAll();
		for (TestInstance ti : tiList) {
			delete(ti);
		}
	}

}
