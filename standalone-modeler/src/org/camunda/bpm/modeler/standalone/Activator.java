package org.camunda.bpm.modeler.standalone;

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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

@SuppressWarnings("restriction")
public class Activator extends AbstractUIPlugin {

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	
		openProject();
	}
	
	private void openProject() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		if (hasVisibleProjects(root)) {
			return;
		}

		final NullProgressMonitor monitor = new NullProgressMonitor();
		
		final IProject project = root.getProject("Diagrams");
		if (!project.exists()) {
			try {
				workspace.run(new IWorkspaceRunnable() {
					@Override
					public void run(IProgressMonitor monitor) throws CoreException {
						ProjectDescription description = new ProjectDescription();
						description.setName("Diagrams");
						description.setComment("Default Project for diagram resources");
						
						project.create(description, monitor);
						project.open(monitor);
					}
				}, monitor);
			
			} catch (CoreException e) {
				org.camunda.bpm.modeler.core.Activator.logStatus(new Status(IStatus.WARNING, org.camunda.bpm.modeler.core.Activator.PLUGIN_ID, "Failed to create default project", e));
			}
		}
	}

	private boolean hasVisibleProjects(IWorkspaceRoot root) {
		IProject[] projects = root.getProjects();
		
		for (IProject project: projects) {
			if (!project.isHidden()) {
				return true;
			}
		}
		
		return false;
	}
}
