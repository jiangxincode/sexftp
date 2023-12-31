package sexftp.mainbar.actions;

import java.io.File;
import java.lang.reflect.Method;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.sexftp.core.exceptions.AbortException;
import sexftp.uils.LogUtil;
import sexftp.views.SexftpMainView;

public class SexftpMainAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {
	private IWorkbenchWindow window;
	protected IPath path = null;

	public void run(IAction action) {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("sexftp.views.MainView");
		} catch (PartInitException localPartInitException) {
		}
		if (this.path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(this.path);

			SexftpMainView mainView = (SexftpMainView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView("sexftp.views.MainView");
			try {
				mainView.directTo(file.getLocation().toFile().getAbsolutePath(), null);
			} catch (AbortException e) {
				LogUtil.info("abort:" + e.getMessage());
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if ((selection instanceof TreeSelection)) {
			this.path = null;
			TreeSelection treeSelect = (TreeSelection) selection;
			try {
				this.path = ((IPath) treeSelect.getFirstElement().getClass().getMethod("getPath", new Class[0])
						.invoke(treeSelect.getFirstElement(), new Object[0]));
			} catch (Exception localException1) {
			}
			try {
				this.path = ((IPath) treeSelect.getFirstElement().getClass().getMethod("getFullPath", new Class[0])
						.invoke(treeSelect.getFirstElement(), new Object[0]));
			} catch (Exception localException2) {
			}
		}
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
	}
}
