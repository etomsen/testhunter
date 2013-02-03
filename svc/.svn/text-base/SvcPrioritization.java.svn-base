package it.unibz.testhunter.svc;

import it.unibz.testhunter.db.Sequence;
import it.unibz.testhunter.model.TestResultModel;
import it.unibz.testhunter.shared.TException;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

public class SvcPrioritization {
	private EntityManager em;

	@Inject
	public SvcPrioritization(EntityManager em) {
		this.em = em;
	}


	@Transactional
	public void savePrioritization(Sequence sequence,
			List<TestResultModel> trmList) throws TException {
		try {
			sequence.getItems().clear();
			em.persist(sequence);
			for (TestResultModel trm : trmList) {
				sequence.addItem(trm.getInfo(), trm.getTime(), trm.getTest());
			}
			em.merge(sequence);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg(
					"db: unable to save prioritization sequence")
					.setTerminateApp();
		}
	}	
}
