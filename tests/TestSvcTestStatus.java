package it.unibz.testhunter.tests;

import it.unibz.testhunter.db.Status;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcTestStatus;
import it.unibz.testhunter.svc.Transactional;

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

public class TestSvcTestStatus {

	private static UUID plugin;
	private static SvcTestStatus svcTestStatus;

	private Status ts;

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

		plugin = UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963FF");
		svcTestStatus = injector.getInstance(SvcTestStatus.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void before() throws Exception {
		svcTestStatus.clearAll();
		ts = svcTestStatus.insert("test status", plugin);
	}

	@Test
	public void insert() throws Exception {
		Assert.assertNotNull(ts);
	}

	@Test
	public void select() throws Exception {
		Status ts1 = svcTestStatus.select(ts.getId());
		Assert.assertNotNull(ts1);
	}

	@Test
	public void selectByNameByPlugin() throws Exception {
		// positive case
		Status ts1 = svcTestStatus.selectByNameByPlugin(ts.getName(),
				ts.getPlugin());
		Assert.assertNotNull(ts1);
		Assert.assertEquals(ts, ts1);

		// negative case: change status name
		Status ts2 = svcTestStatus.selectByNameByPlugin(ts.getName()
				+ "another name", ts.getPlugin());
		Assert.assertNull(ts2);

		// negative case: change plugin
		Status ts3 = svcTestStatus.selectByNameByPlugin(ts.getName(),
				UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963F0"));
		Assert.assertNull(ts3);
	}

	@Test
	public void insertIfNotExists() throws Exception {
		Status ts1 = svcTestStatus.insertIfNotExists(ts.getName()+"#1", plugin);
		Assert.assertNotNull(ts1);
		Assert.assertNotSame(ts1, ts);
		
		Status ts2 = svcTestStatus.insertIfNotExists(ts.getName(), plugin);
		Assert.assertNotNull(ts2);
		Assert.assertEquals(ts, ts2);
	}

	@Test
	public void selectByPlugin() throws Exception {
		Assert.assertEquals(svcTestStatus.selectByPlugin(plugin).size(), 1);
	}

	@Test
	public void delete() throws Exception {
		svcTestStatus.delete(ts);
		Status ts1 = svcTestStatus.select(ts.getId());
		Assert.assertNull(ts1);
	}
}
