package it.unibz.testhunter.tests;

import it.unibz.testhunter.action.ActionPrioritize;
import it.unibz.testhunter.action.ICommandAction;
import it.unibz.testhunter.cmd.IAcionExecuteSink;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcPrioritization;
import it.unibz.testhunter.svc.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class TestActionPrioritize {

	private static SvcPrioritization svcPrioritization;
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

		svcPrioritization = injector.getInstance(SvcPrioritization.class);
		svcBuildJob = injector.getInstance(SvcBuildJob.class);
	}

	@Test
	public void test() throws Exception {
		ICommandAction actionPrioritize = new ActionPrioritize(svcPrioritization, svcBuildJob, null, null);
		actionPrioritize.execute(new IAcionExecuteSink() {

			@Override
			public Object getCmdOption(String optionName) throws TException {
				if (optionName == "last-build-number") {
					return new Long(140);
				}
				if (optionName == "project") {
					return new Long(1);
				}
				if (optionName == "job-name") {
//					return "TOXENV=py27,label=win";
				}
				if (optionName == "last-job-id") {
//					return 711;
				}
				if (optionName == "exclude-build-number") {
					List<Long> excludedBuilds = new ArrayList<Long>();
//					excludedBuilds.add(new Long(96));
					return excludedBuilds;
				}

				return null;
			}

			@Override
			public String getCmdName() {
				return "prioritize";
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
