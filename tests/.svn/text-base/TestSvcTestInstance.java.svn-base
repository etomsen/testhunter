package it.unibz.testhunter.tests;

import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.db.Status;
import it.unibz.testhunter.db.TestInstance;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.SvcTest;
import it.unibz.testhunter.svc.SvcTestInstance;
import it.unibz.testhunter.svc.SvcTestStatus;
import it.unibz.testhunter.svc.Transactional;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class TestSvcTestInstance {

	private static SvcTestInstance svcTestInstance;
	private static SvcTestStatus svcTestStatus;
	private static SvcTest svcTest;
	private static SvcBuildJob svcBuildJob;
	private static SvcProject svcProject;

	private static URL url;
	private static UUID plugin;

	private static Project prj;
	private static BuildJob build;
	private static it.unibz.testhunter.db.Test test;
	private static Status testStatus;

	private TestInstance ti;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Injector injector = Guice.createInjector(new AbstractModule() {
			// Module Services
			@Override
			protected void configure() {
				EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();
				bind(EntityManager.class).toInstance(
						Persistence.createEntityManagerFactory(
								"testhunter_test1").createEntityManager());
				requestInjection(interceptor);

				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);

				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class),
						interceptor);
			}
		});

		svcTest = injector.getInstance(SvcTest.class);
		svcTestStatus = injector.getInstance(SvcTestStatus.class);
		svcTestInstance = injector.getInstance(SvcTestInstance.class);
		svcBuildJob = injector.getInstance(SvcBuildJob.class);
		svcProject = injector.getInstance(SvcProject.class);

		plugin = UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963FF");
		url = new URL("http://hudson.testrun.org/job/anyvc/");

		svcProject.clearAll();
		svcTest.clearAll();
		svcTestStatus.clearAll();

		prj = svcProject.insertNew("testrun", url, plugin);
		build = svcBuildJob.insert(prj, new Long(1),
				new Timestamp(new Date().getTime()), "build #1");
		test = svcTest.insertIfNotExists("test name #1", "test class #1",
				"test package #1");
		testStatus = svcTestStatus.insertIfNotExists("test status #1", plugin);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void before() throws Exception {
		svcTestInstance.clearAll();
		ti = svcTestInstance.insert(test, build, testStatus, new Long(10000));
	}

	@Test
	public void insertCheck() throws Exception {
		TestInstance ti1 = svcTestInstance.selectByTestByBuild(test, build);
		Assert.assertEquals(ti, ti1);
	}

	@Test
	public void delete() throws Exception {
		svcTestInstance.delete(ti);
		TestInstance ti1 = svcTestInstance.selectByTestByBuild(test, build);
		Assert.assertNull(ti1);
	}

}
