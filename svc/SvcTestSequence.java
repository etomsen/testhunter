package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.db.Sequence_;
import it.unibz.testhunter.shared.TException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;

public class SvcTestSequence {
	private EntityManager em;

	@Inject
	public SvcTestSequence(EntityManager em) {
		this.em = em;
	}
	
	@Transactional
	public void save(Sequence sequence) throws TException {
		try {			
			em.persist(sequence);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg(
					"db: unable to save test sequence").setTerminateApp();
		}
	}
	public Object[] getMinMaxInstanceTime(Long projectId, Long lastBuild) throws TException {
		try {
			String sql = "select min(duration_ms), max(duration_ms) "
					+ "from test_instance join build_job on (test_instance.build_job = build_job.id) "
					+ "where build_job.project_id=#project and "
					+ "test_instance.duration_ms >0";
			if ((lastBuild != null) && (lastBuild.longValue() > 0)) {
				sql += " and build_job.buld_number <= #build_number;";
			} else {
				sql += ";";
			}
			Query q = em.createNativeQuery(sql);
			q.setParameter("project", projectId);
			q.setParameter("build_number", lastBuild);
			return (Object[]) q.getSingleResult();
		} catch (NoResultException e1) {
			// no project test results data found
			throw new TException("user message")
					.setUserMsg("message: no test instances found for the project");
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
}

public Sequence getSequence(Long seqId) throws TException {
	try {
		return em.find(Sequence.class, seqId);
	
	} catch (Exception e) {
		throw new TException(e.getMessage()).setUserMsg(
				String.format("db get sequence: %d db query error", seqId))
				.setTerminateApp();
	}
}

public List<Sequence> listSequences(Long projectId) throws TException {
	try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sequence> c = cb.createQuery(Sequence.class);
		TypedQuery<Sequence> q = em.createQuery(c);
		Root<Sequence> p = c.from(Sequence.class);
		c.where(cb.equal(p.get(Sequence_.projectId), projectId));
		return q.getResultList();
	} catch (NoResultException e1) {
		return new ArrayList<Sequence>();
	} catch (Exception e) {
		throw new TException(e.getMessage()).setUserMsg(
				String.format(
						"db list sequences by project: %d: query error",
						projectId)).setTerminateApp();
	}
}

}
