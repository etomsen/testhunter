package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.Test;
import it.unibz.testhunter.db.Test_;
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

public class SvcTest {

	private EntityManager em;

	@Inject
	public SvcTest(EntityManager em) {
		this.em = em;
	}

	public Test selectByNameByClassByPackage(String testName, String testClass,
			String testPackage) throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Test> c = cb.createQuery(Test.class);
			Root<Test> p = c.from(Test.class);
			c.where(cb.and(cb.equal(p.get(Test_.testName), testName),
					cb.equal(p.get(Test_.testClass), testClass),
					cb.equal(p.get(Test_.testPackage), testPackage)));
			TypedQuery<Test> q = em.createQuery(c);
			return q.getSingleResult();
		} catch (NoResultException e1) {
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public Test insert(String testName, String testClass, String testPackage)
			throws TException {
		Test test = new Test(testName, testClass, testPackage);
		try {
			em.persist(test);
			return test;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public Test insertIfNotExists(String testName, String testClass,
			String testPackage) throws TException {
		Test test = selectByNameByClassByPackage(testName, testClass,
				testPackage);
		if (test == null) {
			test = insert(testName, testClass, testPackage);
		}
		return test;
	}

	@Transactional
	public void delete(Test ts) throws TException {
		try {
			// TODO: delete all connected TestInstance
			em.remove(ts);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}

	}

	public List<Test> selectAll() throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Test> c = cb.createQuery(Test.class);
			TypedQuery<Test> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<Test>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void clearAll() throws TException {
		List<Test> tests = selectAll();
		for (Test ts : tests) {
			delete(ts);
		}
	}
	
	public Test select(Long id) throws TException {
		try {
			return em.find(Test.class, id);
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

}
