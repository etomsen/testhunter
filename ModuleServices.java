package it.unibz.testhunter;

import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class ModuleServices extends AbstractModule {

	@Override
	protected void configure() {
		EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();
		
		bind(EntityManager.class).toInstance(
				Persistence.createEntityManagerFactory("testhunter")
						.createEntityManager());
		
        requestInjection(interceptor); 
        
		bind(SvcProject.class);

		
		bindInterceptor(Matchers.any(),
				Matchers.annotatedWith(Transactional.class), interceptor);

		bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);

	}

}
