package it.unibz.testhunter.tests;

import it.unibz.testhunter.db.BuildJob;
import it.unibz.testhunter.db.Project;
import it.unibz.testhunter.svc.EntityManagerTransactionalInterceptor;
import it.unibz.testhunter.svc.SvcBuildJob;
import it.unibz.testhunter.svc.SvcPlugin;
import it.unibz.testhunter.svc.SvcPluginDirectoryJar;
import it.unibz.testhunter.svc.SvcProject;
import it.unibz.testhunter.svc.Transactional;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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

public class TestSvcBuild {

	private static SvcBuildJob svcBuildJob;
	private static SvcProject svcProject;
	private static URL url;
	private static UUID plugin;
	private static Project prj;
	
	private BuildJob b;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		Injector injector = Guice.createInjector(new AbstractModule() {
			//Module Services
			@Override
			protected void configure() {
				EntityManagerTransactionalInterceptor interceptor = new EntityManagerTransactionalInterceptor();				
				bind(EntityManager.class).toInstance(
						Persistence.createEntityManagerFactory("testhunter_test")
								.createEntityManager());
		        requestInjection(interceptor); 
				bind(SvcPlugin.class).to(SvcPluginDirectoryJar.class);
				
				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(Transactional.class), interceptor);
			}
		});
				
		svcBuildJob = injector.getInstance(SvcBuildJob.class);
		svcProject = injector.getInstance(SvcProject.class);
		svcProject.clearAll();
		
		plugin = UUID.fromString("20A55906-67B1-478F-9CD5-2E1CC1F963FF");
		url = new URL("http://hudson.testrun.org/job/anyvc/");
		prj = svcProject.insertNew("testrun", url, plugin);

	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	public void before() throws Exception {
		svcBuildJob.clearAll();
		b = svcBuildJob.insert(prj, new Long(1), new Timestamp(new Date().getTime()), "job #1");
	}
	
	@Test
	public void insert() throws Exception {
		Assert.assertNotNull(b);
		
	}
	
	@Test
	public void select() throws Exception {
		BuildJob b1 = svcBuildJob.select(b.getId());
		Assert.assertEquals(b1, b);
		
	}
		
	@Test
	public void selectByProject() throws Exception {
		List<BuildJob> projectBuilds = svcBuildJob.selectByProject(prj, null);
		Assert.assertEquals(projectBuilds.size(), 1);
		Assert.assertEquals(projectBuilds.get(0), b);
	}
	
	@Test
	public void selectFromProject() throws Exception {
		svcBuildJob.insert(prj, new Long(2), new Timestamp(new Date().getTime()), "job #2");
		Assert.assertEquals(prj.getBuilds().size(), 2);
	}
	
	@Test 
	public void selectLastByProject() throws Exception {
		svcBuildJob.insert(prj, new Long(2), new Timestamp(new Date().getTime()), "job #2");
		svcBuildJob.insert(prj, new Long(3), new Timestamp(new Date().getTime()), "job #3");
		svcBuildJob.insert(prj, new Long(1), new Timestamp(new Date().getTime()), "job #1");
		
		BuildJob b = svcBuildJob.selectLastByProject(prj);
		Assert.assertEquals(b.getBuildNumber(), new Long(3));
	}
	
	@Test
	public void deleteBuildJob() throws Exception {
		svcBuildJob.deleteBuildJob(b);
		BuildJob b1 = svcBuildJob.select(b.getId());
		Assert.assertNull(b1);
		Assert.assertEquals(prj.getBuilds().size(), 0);
		Assert.assertEquals(svcBuildJob.selectByProject(prj, null).size(), 0);
	}

}
