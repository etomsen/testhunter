package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.BuildJob_;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.shared.TException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;

public class SvcBuildJob {

	private EntityManager em;

	@Inject
	public SvcBuildJob(EntityManager em) {
		this.em = em;
	}

	public List<BuildJob> selectByProject(Project prj, String jobName) throws TException {
		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BuildJob> c = cb.createQuery(BuildJob.class);
			Root<BuildJob> p = c.from(BuildJob.class);			
			if (jobName != null) {
				c.where(cb.and(
						cb.equal(p.get(BuildJob_.project), prj),
						cb.equal(p.get(BuildJob_.jobName), jobName)
				));			
			} else {
				c.where(cb.equal(p.get(BuildJob_.project), prj));
			}
			c.orderBy(cb.desc(p.get(BuildJob_.buildNumber)));
			TypedQuery<BuildJob> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			// no matches found
			return new ArrayList<BuildJob>();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public BuildJob selectLastByProject(Project prj) throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BuildJob> c = cb.createQuery(BuildJob.class);
			Root<BuildJob> p = c.from(BuildJob.class);
			c.where(cb.equal(p.get(BuildJob_.project), prj));
			c.orderBy(cb.desc(p.get(BuildJob_.buildNumber)));
			TypedQuery<BuildJob> q = em.createQuery(c);
			return q.setMaxResults(1).getSingleResult();
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}

	}

	@Transactional
	public BuildJob insert(Project prj, Long buildNumber,
			Timestamp buildStart, String jobName) throws TException {
		try {
			BuildJob bj = new BuildJob();
			bj.setBuildNumber(buildNumber);
			bj.setBuildStart(buildStart);
			bj.setJobName(jobName);
			bj.setProject(prj);
			prj.getBuilds().add(bj);
			em.persist(bj);
			return bj;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public BuildJob select(Long id) throws TException {
		try {
			return em.find(BuildJob.class, id);
		} catch (NoResultException e1) {
			// no matches found
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void deleteBuildJob(BuildJob b) throws TException {
		try {
			b.getProject().deleteBuild(b);
			em.remove(b);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void clearByProject(Project prj) throws TException {
		try {
			prj.ClearBuilds();
			em.persist(prj);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	public List<BuildJob> selectAll() throws TException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BuildJob> c = cb.createQuery(BuildJob.class);
			c.from(BuildJob.class);
			TypedQuery<BuildJob> q = em.createQuery(c);
			return q.getResultList();
		} catch (NoResultException e1) {
			return null;
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

	@Transactional
	public void clearAll() throws TException {
		try {
			List<BuildJob> bjs = selectAll();
			for (BuildJob bj : bjs) {
				deleteBuildJob(bj);
			}
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("db query error")
					.setTerminateApp();
		}
	}

}
