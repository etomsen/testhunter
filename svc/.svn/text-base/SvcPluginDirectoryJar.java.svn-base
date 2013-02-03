package it.unibz.testhunter.svc;

import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.IPluginProvider;
import it.unibz.testhunter.shared.TException;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

class JarFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.endsWith(".jar"));
	}
}

public class SvcPluginDirectoryJar implements SvcPlugin {

	private static final Class<?>[] parameters = new Class[] { URL.class };

	private final HashMap<UUID, IPlugin> pluginHash;

	public SvcPluginDirectoryJar() throws TException {
		pluginHash = new HashMap<UUID, IPlugin>(5);
		reloadPlugins();
	}

	private void addPluginToHash(File f, IPluginProvider pr) throws TException {
		IPlugin pl = pr.get();
		try {
			pluginHash.put(pl.getUUID(), pl);
		} catch (IllegalArgumentException e) {

			throw new TException(e.getMessage())
				.setTerminateApp()
				.setUserMsg(
						"plugin UDDI " + pl.getUUID() +
						" from plugin jar" + f.getName() + 
						" is not well-formed");
		}
	}

	private void loadClassPlugins(File f, Class<?> clazz) throws TException {
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> c : interfaces) {
			// look for the IPluginProvider interface
			if (c.getSimpleName().equals("IPluginProvider")) {
				IPluginProvider pr = null;
				try {
					pr = (IPluginProvider) clazz.newInstance();
				} catch (InstantiationException e1) {
					throw new TException(e1.getMessage())
						.setTerminateApp()
						.setUserMsg("problem loading jar plugin" + f.getName());
				} catch (IllegalAccessException e2) {
					throw new TException(e2.getMessage())
					.setTerminateApp()
					.setUserMsg("problem loading jar plugin" + f.getName());
				}
				addPluginToHash(f, pr);
				break;
			}
		}

	}

	private void loadPluginsFromFile(File f) throws TException {
		List<String> classNames = null;
		try {
			classNames = getClassNames(f.getAbsolutePath());

			for (String className : classNames) {
				// skip private classes
				if (className.contains("$"))
					continue;
				// Remove the ".class" at the back
				String name = className.substring(0, className.length() - 6);
				Class<?> clazz = getClass(f, name);
				loadClassPlugins(f, clazz);

			}
		} catch (IOException e1) {
			// for getClassNames
			throw new TException(e1.getMessage()).setTerminateApp().setUserMsg(
					"problem loading jar plugin" + f.getName());
		} catch (Exception e2) {
			// fro getClass(f, Name)
			throw new TException(e2.getMessage()).setTerminateApp().setUserMsg(
					"problem loading jar plugin" + f.getName());
		}
	}

	private void searchPlugins(String directory) throws TException {
		File dir = new File(directory);
		if (dir.isFile()) {
			dir.getAbsolutePath();
			return;
		}
		File[] files = dir.listFiles(new JarFilter());
		for (File f : files) {
			loadPluginsFromFile(f);
		}
	}

	private List<String> getClassNames(String jarName) throws IOException {
		ArrayList<String> classes = new ArrayList<String>(10);
		JarInputStream jarFile = new JarInputStream(
				new FileInputStream(jarName));
		JarEntry jarEntry;
		while (true) {
			jarEntry = jarFile.getNextJarEntry();
			if (jarEntry == null) {
				break;
			}
			if (jarEntry.getName().endsWith(".class")) {
				classes.add(jarEntry.getName().replaceAll("/", "\\."));
			}
		}

		return classes;
	}

	private Class<?> getClass(File file, String name) throws Exception {
		addURL(file.toURI().toURL());

		URLClassLoader clazzLoader;
		Class<?> clazz;
		String filePath = file.getAbsolutePath();
		filePath = "jar:file://" + filePath + "!/";
		URL url = new File(filePath).toURI().toURL();
		clazzLoader = new URLClassLoader(new URL[] { url });
		clazz = clazzLoader.loadClass(name);
		return clazz;

	}

	private void addURL(URL u) throws IOException {
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL urls[] = sysLoader.getURLs();
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].toString().equalsIgnoreCase(u.toString())) {
				return;
			}
		}
		Class<?> sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException(
					"Error, could not add URL to system classloader");
		}
	}

	@Override
	public Collection<IPlugin> getPlugins() {
		return pluginHash.values();
	}

	@Override
	public IPlugin getPluginByUUID(UUID pluginUUID) throws TException {
		return pluginHash.get(pluginUUID);
	}

	@Override
	public void reloadPlugins() throws TException {
		try {
			searchPlugins("plugins");
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg(
					"plugin finder initialization failed").setTerminateApp();
		}
	}

}
