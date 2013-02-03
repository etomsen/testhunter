package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.TestsResult;
import it.unibz.testhunter.db.TestsResult_;
import it.unibz.testhunter.shared.TException;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;

public class SvcTestResults {
	private EntityManager em;

	@Inject
	public SvcTestResults(EntityManager em) {
		this.em = em;
	}

	public TestsResult select(Long id) throws TException {
		try {
			return em.find(TestsResult.class, id);
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error").setTerminateApp();
		}
	}

	public List<TestsResult> selectTestsResult(TestsResult tr) throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestsResult> c = cb.createQuery(TestsResult.class);
			Root<TestsResult> from = c.from(TestsResult.class);
			Predicate where = cb.equal(from.get(TestsResult_.projectId), tr.getProjectId());
			if (tr.getLastBuild() != null) {
				where = cb.and(where, cb.equal(from.get(TestsResult_.lastBuild), tr.getLastBuild()));
			} else {
				where = cb.and(where, cb.isNull(from.get(TestsResult_.lastBuild)));
			}
			if (tr.getJobName() != null) {
				where = cb.and(where, cb.equal(from.get(TestsResult_.jobName), tr.getJobName()));
			} else {
				where = cb.and(where, cb.isNull(from.get(TestsResult_.jobName)));
			}			
			c.where(where);
			TypedQuery<TestsResult> q = em.createQuery(c);
			return q.getResultList();
			
		} catch (NoResultException e1) {
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();

		}
	}
	
	public List<TestsResult> select(Long minId, Long maxId) throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestsResult> c = cb.createQuery(TestsResult.class);
			Root<TestsResult> from = c.from(TestsResult.class);
			Predicate where = cb.between(from.get(TestsResult_.id), minId, maxId);
			c.where(where);
			TypedQuery<TestsResult> q = em.createQuery(c);
			return q.getResultList();			
		} catch (NoResultException e1) {
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error").setTerminateApp();
		}
	}


	@Transactional
	public void deleteTestResult(TestsResult tr) throws TException {
		try {
			em.remove(tr);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error").setTerminateApp();
		}
	}
	
	@Transactional 
	public void deleteTestsResults(List<TestsResult> trList) throws TException {
		for (TestsResult testResult : trList) {
			deleteTestResult(testResult);
		}
	}
	
	public void deleteTestsResults(TestsResult tr) throws TException {
		try {
			List<TestsResult> list = selectTestsResult(tr);
			for (TestsResult testsResult : list) {
				em.remove(testsResult);
			}
		} catch (NoResultException e1) {
			
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error").setTerminateApp();

		}
	}

	@Transactional
	public void updateTestsResultItems(TestsResult tr) throws TException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO tests_result_item(tests_result, test, status, count, avg_time) ");
			sb.append("SELECT #tests-result, exec_counts.test, exec_counts.status, exec_counts.count, avg_exec_times.time ");
			sb.append("FROM ( ");
			sb.append("(SELECT test_instance.test AS test, test_instance.test_status AS status, count(1) AS count ");
			sb.append("FROM test_instance JOIN build_job ON (test_instance.build_job = build_job.id) ");
			sb.append("WHERE build_job.project_id= #project ");			
			if (tr.getLastBuild() != null) {
				sb.append("AND build_job.buld_number <= #build_number ");
			}
			if (tr.getJobName() != null) {
				sb.append("AND build_job.job_name = '#job_name' ");
			}
			if (!tr.getIncludeZeroTimeToCount()) {
				sb.append("AND test_instance.duration_ms > 0 ");
			}
			sb.append("GROUP BY test, test_status) as exec_counts ");
			sb.append("LEFT OUTER JOIN ");
			sb.append("(SELECT test_instance.test as test, test_instance.test_status as status, avg(duration_ms) as time ");
			sb.append("FROM test_instance JOIN build_job ON (test_instance.build_job = build_job.id) ");
			sb.append("WHERE build_job.project_id= #project ");
			if (tr.getLastBuild() != null) {
				sb.append("AND build_job.buld_number <= #build_number ");
			}
			if (tr.getJobName() != null) {
				sb.append("AND build_job.job_name = '#job_name' ");
			}
			if (!tr.getIncludeZeroTimeToTime()) {
				sb.append("AND test_instance.duration_ms > 0 ");
			}
			sb.append("GROUP BY test, test_status ");
			sb.append(") AS avg_exec_times ");
			sb.append("ON exec_counts.test = avg_exec_times.test AND exec_counts.status= avg_exec_times.status ");
			sb.append(") WHERE time IS NOT NULL ORDER BY test, status;");

			Query q = em.createNativeQuery(sb.toString());
			q.setParameter("tests-result", tr.getId());
			q.setParameter("project", tr.getProjectId());
			q.setParameter("build_number", tr.getLastBuild());
			q.setParameter("job_name", tr.getJobName());
			q.executeUpdate();
			em.refresh(tr);
		} catch (NoResultException e1) {
			throw new TException("user message").setUserMsg(
					"update tests result: db query error").setTerminateApp();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg(
					"update tests result: db query error").setTerminateApp();
		}
	}
	
	@Transactional
	public TestsResult saveTestResult(TestsResult tr) throws TException {
		deleteTestsResults(tr);
		em.persist(tr);
		return tr;
	}

}
