package org.camunda.bpm.modeler.standalone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.camunda.bpm.modeler.core.files.FileService;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2Editor;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2EditorUpdateBehavior;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.ResourceInfo;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.commands.ExpressionContext;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.part.IMultiPageEditorSiteHolder;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.WorkbenchPart;

@SuppressWarnings("restriction")
public class SaveAsHandler extends AbstractHandler {
	
	Workspace workspace;
	Bpmn2Editor bpmnEditor;
	Shell shell;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// get context
		ExpressionContext context = (ExpressionContext) event.getApplicationContext();
		IEclipseContext eclipseContext = context.eclipseContext;
		IMultiPageEditorSiteHolder editorSiteHolder = eclipseContext.getLocal(org.eclipse.ui.internal.part.IMultiPageEditorSiteHolder.class);
		MultiPageEditorSite site = editorSiteHolder.getSite();
		bpmnEditor = (Bpmn2Editor) site.getEditor();
		Bpmn2Resource bpmnResource = bpmnEditor.getModelResource();	
		workspace = (Workspace) ResourcesPlugin.getWorkspace();
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			
		// use standard doSaveAs for files within the workspace
		if (!bpmnResource.getURI().segment(1).equals(".internal")){
			bpmnEditor.doSaveAs();
			return null;
		}

		// get new file name
		FileDialog fileDialog = new FileDialog(site.getShell(), SWT.SAVE);
		fileDialog.setText("Save As...");
        String[] filterExt = { "*.bpmn", "*.bpmn2" };
        fileDialog.setFilterExtensions(filterExt);
        String oldFile = null;
		try {
			oldFile = getOldFile(bpmnResource);
		} catch (CoreException e) {
			MessageDialog.openError(shell, "Error", "Save As not successful.");
			e.printStackTrace();
			return null;
		}
        fileDialog.setFileName(oldFile);
        String dir = oldFile.substring(0, oldFile.lastIndexOf("/"));
        fileDialog.setFilterPath(dir);
		String newFilePath = fileDialog.open();
		
		if (newFilePath == null)
			return null;
		
		// Confirm overwrite if file exists
		File newFile = new File(newFilePath);
		if (newFile.exists()) {
			boolean overwrite = MessageDialog.openQuestion(shell,
					"Overwrite?", "The file " + newFilePath +  " already exists. Overwrite?");
			if (overwrite == false)
				return null;
		}		
		
		// save new file
		OutputStream out = null;
		try {
			out = new FileOutputStream(newFilePath);
			System.out.println(newFilePath);
		} catch (FileNotFoundException e) {
			MessageDialog.openError(shell, "Error", "Save As not successful.");
			e.printStackTrace();
			return null;
		}
		try {
			bpmnResource.save(out, null);
		} catch (IOException e) {
			MessageDialog.openError(shell, "Error", "Save As not successful.");
			e.printStackTrace();
			return null;
		}

		// get new URI
		URI newUri = URI.createFileURI(newFilePath);
		try {
			newUri = FileService.resolveAsWorkspaceResource(newUri);
		} catch (CoreException e) {
			MessageDialog.openError(shell, "Error", "Save As not successful.");
			e.printStackTrace();
			return null;
		}
		
		// update resource URI
		Bpmn2EditorUpdateBehavior updateBehavior = (Bpmn2EditorUpdateBehavior) bpmnEditor.getDiagramBehavior().getUpdateBehavior();
		updateBehavior.getWorkspaceSynchronizerDelegate().handleResourceMoved(bpmnResource, newUri);

		// update title
		String newTitle = newFile.getName();
		if (newTitle.indexOf(".") > 0) {
			newTitle = newTitle.substring(0, newTitle.lastIndexOf("."));
		}
		setTitle(newTitle);
				
		return null;
	}

	private String getOldFile(Bpmn2Resource bpmnResource) throws CoreException{
		IFile oldFile = WorkspaceSynchronizer.getUnderlyingFile(bpmnResource);
		ResourceInfo info = workspace.getResourceInfo(oldFile.getFullPath(), true, false);
		java.net.URI uri = info.getFileStoreRoot().computeURI(oldFile.getFullPath());
		return uri.getPath();
	}
	
	private void setTitle(String newTitle){
		WorkbenchPart part = (WorkbenchPart) bpmnEditor.getWorkbenchPart();
		Method method;
		try {
			method = WorkbenchPart.class.getDeclaredMethod("setPartName", String.class);
			method.setAccessible(true); 
			method.invoke(part, newTitle);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}