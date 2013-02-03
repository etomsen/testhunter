package it.unibz.testhunter.tests;

import it.unibz.testhunter.action.ActionComputePtf;
import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcTestInstance;
import it.unibz.testhunter.svc.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class TestActionComputePtf {
	private static SvcTestInstance svcTestInstance;
	private static SvcBuildJob svcBuildJob;

	@BeforeClass
	public static void beforeClass() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			// Module Services
			@Override
			protected void configure() {
				EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();
				bind(EntityManager.class).toInstance(
						Persistence.createEntityManagerFactory("testhunter")
								.createEntityManager());
				requestInjection(interceptor);
				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);

				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class),
						interceptor);
			}
		});		
		svcBuildJob = injector.getInstance(SvcBuildJob.class);
		svcTestInstance = injector.getInstance(SvcTestInstance.class);
	}

	@Test
	public void test() throws Exception {
		ICommandAction actionComputePtf = new ActionComputePtf(svcTestInstance, svcBuildJob, null);
		actionComputePtf.execute(new IAcionExecuteSink() {

			@Override
			public Object getCmdOption(String optionName) throws TException {
				if (optionName == "sequence") {
					return new Long(1);
				}
				if (optionName == "build-job") {
					return new Long(703);
				}
				return null;
			}

			@Override
			public String getCmdName() {
				return "compute-ptf";
			}
			
			@Override
			public String getCmdHelp() {
				return "";
			}

			@Override
			public String getCmdOptions() throws TException {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
}
