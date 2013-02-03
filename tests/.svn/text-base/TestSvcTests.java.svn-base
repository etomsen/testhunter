package it.unibz.testhunter.tests;

import it.unibz.testhunter.db.Test;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcTest;
import it.unibz.testhunter.svc.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class TestSvcTests {

	private static SvcTest svcTest;

	private it.unibz.testhunter.db.Test t;

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

				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);

				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class),
						interceptor);
			}
		});

		svcTest = injector.getInstance(SvcTest.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void before() throws Exception {
		svcTest.clearAll();
		t = svcTest.insertIfNotExists("test name #1", "test class #1",
				"test package #1");
	}

	@org.junit.Test
	public void insert() throws Exception {
		Assert.assertNotNull(t);
	}

	@org.junit.Test
	public void select() throws Exception {
		it.unibz.testhunter.db.Test t1 = svcTest.select(t.getId());
		Assert.assertNotNull(t1);
	}

	@org.junit.Test
	public void insertIfNotExists() throws Exception {
		Test t1 = svcTest.insertIfNotExists("test name #2", "test class #1",
				"test package #1");
		Assert.assertNotNull(t1);

		Test t2 = svcTest.insertIfNotExists("test name #2", "test class #1",
		"test package #1");
		
		Assert.assertNotNull(t2);

		Assert.assertEquals(t2, t1);
	}

	@org.junit.Test
	public void selectByNameByClassByPackage() throws Exception {

		Test t1 = svcTest.selectByNameByClassByPackage(
				t.getTestName(), t.getTestClass(), t.getTestPackage());
		Assert.assertNotNull(t1);
		Assert.assertEquals(t, t1);
	}

}
