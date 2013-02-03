package it.unibz.testhunter.svc;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;

public class EntityManagerTransactionalInterceptor implements MethodInterceptor {

	@Inject
	private EntityManager em;

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object o;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
			o = invocation.proceed(); 
			em.getTransaction().commit();			
		} else {
			o = invocation.proceed();
		}
		return o;
	}

}
