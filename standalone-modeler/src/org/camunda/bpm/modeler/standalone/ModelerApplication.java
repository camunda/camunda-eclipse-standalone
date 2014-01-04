package org.camunda.bpm.modeler.standalone;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;

import org.camunda.bpm.modeler.core.Activator;
import org.eclipse.core.internal.resources.ProjectDescription;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.IDEApplication;

@SuppressWarnings("restriction")
public class ModelerApplication extends IDEApplication {

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
		
		try {
			return super.start(appContext);
		} finally {
			JUnique.releaseLock(MODELER_APPLICATION_ID);
		}
	}

	private void focusApp() {
		JUnique.sendMessage("focus", null);		
	}

	private void lockApp() throws AlreadyLockedException {
		JUnique.acquireLock(MODELER_APPLICATION_ID, new FocusHandler());
	}
}
