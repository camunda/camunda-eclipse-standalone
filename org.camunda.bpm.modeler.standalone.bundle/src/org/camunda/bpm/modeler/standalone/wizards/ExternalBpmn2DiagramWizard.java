package org.camunda.bpm.modeler.standalone.wizards;

import java.io.IOException;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.ui.wizards.Bpmn2DiagramCreator;
import org.camunda.bpm.modeler.ui.wizards.Bpmn2DiagramWizard;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.FileStoreEditorInput;

/**
 * Wizard with no page that creates an external bpmn 2.0 file
 * and opens the diagram editor.
 * 
 * @author nico.rehwaldt
 */
public class ExternalBpmn2DiagramWizard extends Wizard implements INewWizard {

	public boolean performFinish() {
		
		String fileName = selectFileName();
		if (fileName == null) {
			return false;
		}
	
		URI diagramUri = URI.createFileURI(fileName);
		
		try {
			Resource resource = createBpmnDiagram(diagramUri);
			IEditorInput input = createEditorInput(diagramUri);

			Bpmn2DiagramWizard.openEditor(input);
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), "Failed to create file", e.getMessage());
			return false;
		}

		return true;
	}

	private IEditorInput createEditorInput(URI diagramUri) throws CoreException {
		return new FileStoreEditorInput(EFS.getStore(java.net.URI.create(diagramUri.toString())));
	}

	private Resource createBpmnDiagram(URI diagramUri) throws CoreException {
		final Bpmn2Resource resource = Bpmn2DiagramCreator.createBpmn2Resource(diagramUri);
		
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					resource.save(null);
				} catch (IOException e) {
					throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to save file", e));
				}
			}
		}, new NullProgressMonitor());
		
		return resource;
	}
	
	private String selectFileName() {
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setText("Create new BPMN 2.0 file");
		
		dialog.setOverwrite(true);
		dialog.setFilterExtensions(new String[] { ".bpmn" });
		
		return dialog.open();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
}
