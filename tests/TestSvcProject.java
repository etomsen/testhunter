package it.unibz.testhunter.tests;

import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.Transactional;

import java.net.URL;
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

public class TestSvcProject {

	private static SvcProject svcProject;
	private static URL url;
	private static UUID plugin;

	private Project prj;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			public void configure() {
				EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();
				bind(EntityManager.class).toInstance(
						Persistence.createEntityManagerFactory(
								"testhunter_test").createEntityManager());
				requestInjection(interceptor);
				bind(SvcProject.class);
				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);

				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class),
						interceptor);

			}
		});

		plugin = UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963FF");
		url = new URL("http://hudson.testrun.org/job/anyvc/");
		svcProject = injector.getInstance(SvcProject.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void before() throws Exception {
		svcProject.clearAll();
		prj = svcProject.insertNew("test project", url, plugin);
	}

	@Test
	public void insert() throws Exception {
		Assert.assertNotNull(prj);
	}

	@Test
	public void select() throws Exception {
		Project prj1 = svcProject.select(prj.getId());
		Assert.assertEquals(prj1, prj);
	}

	@Test
	public void delete() throws Exception {
		svcProject.delete(prj);
		Project prj1 = svcProject.select(prj.getId());
		Assert.assertNull(prj1);
	}

	@Test
	public void selectAll() throws Exception {
		svcProject.insertNew("test project #1", url, plugin);
		Assert.assertEquals(svcProject.selectAll().size(), 2);
	}

	@Test
	public void selectByPlugin() throws Exception {
		svcProject.insertNew("test project #1", url, plugin);
		Assert.assertEquals(svcProject.selectByPlugin(plugin).size(), 2);

		UUID plugin1 = UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963F0");
		Assert.assertEquals(svcProject.selectByPlugin(plugin1).size(), 0);
	}

}
