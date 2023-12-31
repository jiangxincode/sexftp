package sexftp.views;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.sexftp.core.ftp.bean.FtpConf;
import org.sexftp.core.ftp.bean.FtpUploadConf;
import org.sexftp.core.ftp.bean.FtpUploadPro;
import sexftp.uils.PluginUtil;

public class SexftpSyncView extends SexftpLocalView {
	protected void actionEnableHandle() {
		super.actionEnableHandle();
		this.actionDirectSLocal.setEnabled(true);
		Object[] selectOs = getSelectionObjects(true);
		boolean hasFtpUploadPro = false;
		boolean hasNoFtpUploadPro = false;
		Object[] arrayOfObject1;
		int j = (arrayOfObject1 = selectOs).length;
		for (int i = 0; i < j; i++) {
			Object object = arrayOfObject1[i];
			if ((object instanceof FtpUploadPro)) {
				hasFtpUploadPro = true;
			} else {
				hasNoFtpUploadPro = true;
			}
			if ((hasFtpUploadPro) && (hasNoFtpUploadPro)) {
				break;
			}
		}
		if (!hasFtpUploadPro) {
			this.actionUpload.setEnabled(false);
			this.actionCompare.setEnabled(false);
		}

		if (hasNoFtpUploadPro) {
			this.actionCompare.setEnabled(false);
		}
	}

	protected void doubleClickAction_actionPerformed() throws Exception {
		ISelection selection = this.viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if ((obj instanceof AbstractSexftpView.TreeParent)) {
			super.doubleClickAction_actionPerformed();
		} else {
			actionCompare_actionPerformed();
		}
	}

	public List getHiddenActions() {
		return Arrays.asList(new Object[] { this.actionRefreshFile });
	}

	protected void actionDirectSLocal_actionPerformed() throws Exception {
		Object[] os = getSelectionObjects();
		AbstractSexftpView.TreeParent selectObj = getSelectFtpUploadConfNodes()[0];
		SexftpLocalView mv = PluginUtil.findAndShowLocalView(PluginUtil.getActivePage());
		if ((os.length == 1) && ((os[0] instanceof FtpUploadPro))) {
			FtpUploadPro fupro = (FtpUploadPro) os[0];
			String client = fupro.getFtpUploadConf().getClientPath();
			AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
			for (int i = 0; i < allFtpUploadConfNodes.length; i++) {
				if (allFtpUploadConfNodes[i] == selectObj) {
					mv.directTo(client, Integer.valueOf(i));
					break;
				}
			}
		}
	}

	protected void actionUpload_actionPerformed() throws Exception {
		innerUpload_actionPerformed(getSelectionObjects(true));
	}

	protected void actionCompare_actionPerformed() throws Exception {
		innerCompare_actionPerformed(getSelectionObjects(true));
	}

	public void doAfterSelectAndAddChildUploadPro(DoAfterSelectAndAddChildUploadPro run) throws Exception {
		super.doAfterSelectAndAddChildUploadPro(run);
	}

	protected boolean copyTreeNodeIndoAfterSelectAndAddChildUploadPro() {
		return true;
	}

	private boolean treeVisibleCtrl = true;

	protected void treeExpanded_actionPerformed(TreeExpansionEvent e) throws Exception {
	}

	protected void treeCollapsed_actionPerformed(TreeExpansionEvent e) throws Exception {
	}

	public void refreshTreeViewData() {
		super.refreshTreeViewData();
		AbstractSexftpView.TreeObject[] arrayOfTreeObject1;
		int j = (arrayOfTreeObject1 = this.invisibleRoot.getChildren()).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpView.TreeObject ftpstart = arrayOfTreeObject1[i];
			AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
			int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent) ftpstart).getChildren()).length;
			for (int k = 0; k < m; k++) {
				AbstractSexftpView.TreeObject ftpConfNode = arrayOfTreeObject2[k];

				ftpConfNode.setVisible(false);
			}
		}
	}

	protected void actionPrepareServUpload_actionPerformed() throws Exception {
		super.actionPrepareServUpload_actionPerformed();
	}

	public void showDifView(AbstractSexftpView.TreeParent[] viewtps, Map<String, String> cusImgMap) {
		actionRefreshSexftp_actionPerformed();

		this.customizedImgMap = cusImgMap;
		AbstractSexftpView.TreeObject[] arrayOfTreeObject1;
		int j = (arrayOfTreeObject1 = this.invisibleRoot.getChildren()).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpView.TreeObject ftpstart = arrayOfTreeObject1[i];
			AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
			int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent) ftpstart).getChildren()).length;
			for (int k = 0; k < m; k++) {
				AbstractSexftpView.TreeObject ftpConfNode = arrayOfTreeObject2[k];

				AbstractSexftpView.TreeParent ftpConfNodeIn = (AbstractSexftpView.TreeParent) ftpConfNode;
				AbstractSexftpView.TreeObject[] arrayOfTreeObject3;
				int i1 = (arrayOfTreeObject3 = ftpConfNodeIn.getChildren()).length;
				for (int n = 0; n < i1; n++) {
					AbstractSexftpView.TreeObject ftpUploadConfNode = arrayOfTreeObject3[n];

					AbstractSexftpView.TreeParent ftpUpNode = (AbstractSexftpView.TreeParent) ftpUploadConfNode;
					if ((ftpUpNode.getO() instanceof FtpUploadConf)) {
						if (this.treeVisibleCtrl)
							ftpUpNode.setVisible(false);
						AbstractSexftpView.TreeParent[] arrayOfTreeParent;
						int i3 = (arrayOfTreeParent = viewtps).length;
						for (int i2 = 0; i2 < i3; i2++) {
							AbstractSexftpView.TreeParent viewtp = arrayOfTreeParent[i2];

							if ((((FtpUploadConf) ftpUpNode.getO()).getClientPath()
									.equals(((FtpUploadConf) viewtp.getO()).getClientPath()))
									&& (((FtpConf) ftpUpNode.getParent().getO()).getName()
											.equals(((FtpConf) viewtp.getParent().getO()).getName()))) {
								ftpConfNodeIn.setVisible(true);
								ftpConfNodeIn.removeChild(ftpUpNode);
								ftpConfNodeIn.addChild(viewtp);
								refreshTreeView(ftpConfNodeIn);
								refreshTreeView(viewtp);
								break;
							}
						}
					}
				}

				this.viewer.refresh(ftpConfNodeIn);
			}
		}
		this.treeVisibleCtrl = true;
	}
}
