package org.camunda.bpm.modeler.standalone;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.IDEApplication;

@SuppressWarnings("restriction")
public class ModelerApplication extends IDEApplication {

	private static final String WORKSPACE = "workspace";
	private static final String SEPARATOR = File.separator;
	private static final String MODELER_APPLICATION_ID = ModelerApplication.class.getName();
	
	private static class FocusHandler implements MessageHandler {
		
		@Override
		public String handle(String args) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			
			Shell shell = window.getShell();
			if (shell != null) {
				if (shell.getMinimized()) {
					shell.setMinimized(false);
				}
				shell.forceActive();
			}
			
			return null;
		}
	};

	@Override
	public Object start(IApplicationContext appContext) throws Exception {
	
		try {
			lockApp();
		} catch (AlreadyLockedException e) {
			focusApp();
			
			return EXIT_OK;
		}

		assignPlatformLocation();
		
		try {
			return super.start(appContext);
		} finally {
			JUnique.releaseLock(MODELER_APPLICATION_ID);
		}
	}

	protected void assignPlatformLocation() {

		Location location = Platform.getInstanceLocation();
		
		if (location.isSet()) {
			return;
		}
		
		try {
			URL platformLocation = initializePlatformLocation("camundaModeler");
			
			// we use toURL here because it properly handles whitespaces
			// (in opposite to toURI())
			location.set(platformLocation, false);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to start Modeler", e);
		}
	}

	@SuppressWarnings("deprecation")
	public static URL initializePlatformLocation(String productName) throws IOException {
		
		File dataDirectory = getDataDirectory(productName);
		File modelerDir = getModelerDirectory();
		
		File workspaceDir = new File(dataDirectory, WORKSPACE);
		if (!workspaceDir.exists()) {
			
			FileUtils.forceMkdir(workspaceDir);
			
			File workspaceBlueprint = new File(modelerDir, WORKSPACE);
			
			if (workspaceBlueprint.exists()) {
				FileUtils.copyDirectory(workspaceBlueprint, workspaceDir);
			}
		}
		
		return workspaceDir.toURL();
	}

	private void focusApp() {
		JUnique.sendMessage("focus", null);		
	}

	private void lockApp() throws AlreadyLockedException {
		JUnique.acquireLock(MODELER_APPLICATION_ID, new FocusHandler());
	}
	

	public static File getModelerDirectory() {
		String home = System.getProperty("eclipse.home.location");
		return toFile(home);
	}

	public static File getDataDirectory(String name) {

		// try to assign an instance location manually
		
		String os = System.getProperty("os.name").toLowerCase();
		
		String path = null;
		
		if (os.contains("win")) {
			// perform custom bootstrap on windows
			
			String appData = System.getenv("APPDATA");
			
			path = appData + SEPARATOR + name;
		} else {
			String userHome = System.getProperty("user.home");
			path = userHome + SEPARATOR + "." + name;
		}
		
		return new File(path);
	}

	private static File toFile(String fileUri) {
		
		try {
			URL url = new URL(fileUri.replaceAll("\\s", "%20"));
			return new File(url.toURI());
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e); 
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e); 
		}
	}
}
