package it.unibz.testhunter.tests;

import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.db.Status;
import it.unibz.testhunter.db.TestsResultItem;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcPrioritization;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.SvcTest;
import it.unibz.testhunter.svc.SvcTestStatus;
import it.unibz.testhunter.svc.Transactional;

import java.net.URL;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class TestSvcTestResults {

	private static SvcPrioritization svcTestResult;
	private static SvcTestStatus svcTestStatus;
	private static SvcTest svcTest;
	private static SvcProject svcProject;

	private static URL url;
	private static UUID plugin;

	private static Project prj;
	private static it.unibz.testhunter.db.Test test;
	private static Status testStatus1;
	private static Status testStatus2;

	private TestsResultItem tr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Injector injector = Guice.createInjector(new AbstractModule() {
			// Module Services
			@Override
			protected void configure() {
				EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();
				bind(EntityManager.class).toInstance(
						Persistence.createEntityManagerFactory(
								"testhunter_test").createEntityManager());
				requestInjection(interceptor);

				bind(SvcPrioritization.class);
				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);

				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class),
						interceptor);
			}
		});

		svcTest = injector.getInstance(SvcTest.class);
		svcTestStatus = injector.getInstance(SvcTestStatus.class);
		svcTestResult = injector.getInstance(SvcPrioritization.class);
		svcProject = injector.getInstance(SvcProject.class);

		plugin = UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963FF");
		url = new URL("http://hudson.testrun.org/job/anyvc/");

		svcProject.clearAll();
		svcTest.clearAll();
		svcTestStatus.clearAll();

		
		prj = svcProject.insertNew("testrun", url, plugin);
		test = svcTest.insertIfNotExists("test name #1", "test class #1",
				"test package #1");
		testStatus1 = svcTestStatus.insertIfNotExists("test status #1", plugin);
		testStatus2 = svcTestStatus.insertIfNotExists("test status #2", plugin);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void before() throws Exception {
	}

	
	
}
