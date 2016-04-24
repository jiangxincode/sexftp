package sexftp.views;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.desy.common.util.DateTimeUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.sexftp.core.exceptions.AbortException;
import org.sexftp.core.exceptions.BizException;
import org.sexftp.core.ftp.FtpPools;
import org.sexftp.core.ftp.XFtp;
import org.sexftp.core.ftp.bean.FtpConf;
import org.sexftp.core.ftp.bean.FtpDownloadPro;
import org.sexftp.core.ftp.bean.FtpFile;
import org.sexftp.core.ftp.bean.FtpUploadConf;
import org.sexftp.core.utils.Cpdetector;
import org.sexftp.core.utils.ExistFtpFile;
import org.sexftp.core.utils.FileUtil;
import org.sexftp.core.utils.SearchCallback;
import org.sexftp.core.utils.StringUtil;
import org.sexftp.core.utils.TreeViewUtil;
import org.sexftp.core.utils.TreeViewUtil.ThisYourFind;
import sexftp.SexftpJob;
import sexftp.SexftpRun;
import sexftp.SrcViewable;
import sexftp.editors.inner.SfTextEditor;
import sexftp.uils.PluginUtil;
import sexftp.views.savelisteners.ServerInnnerEditSaveListener;

public class SexftpServerView extends SexftpMainView {
	protected void actionPrepare() {
		this.actionCompare.setText("Compare With Local(&C)");
		super.actionPrepare();
	}

	protected String getDefaultPathToLocation(Object selectO) {
		if ((selectO instanceof FtpDownloadPro)) {
			return ((FtpDownloadPro) selectO).getFtpUploadConf().getServerPath();
		}
		return super.getDefaultPathToLocation(selectO);
	}

	protected void actionCompare_actionPerformed() throws Exception {
		Object[] os = getSelectionObjects();
		innerCompare(os);
		super.actionCompare_actionPerformed();
	}

	public void innerCompare(Object[] os) throws Exception {
		final IWorkbenchPage page = PluginUtil.getActivePage();
		if ((os != null) && (os.length == 1) && ((os[0] instanceof FtpDownloadPro))) {
			FtpDownloadPro dpro = (FtpDownloadPro) os[0];

			IFile ifile = PluginUtil.createSexftpIFileFromPath(
					"/.seredittemp/" + System.currentTimeMillis() + "/" + dpro.getFtpfile().getName());
			final String tmpeditpath = ifile.getLocation().toFile().getAbsolutePath();
			innerDownload(os, new MySexftpServerDownload() {
				public String trustFolder(FtpDownloadPro dpro) {
					return tmpeditpath;
				}

				public boolean exceptionNotExits(FtpDownloadPro dpro) throws Exception {
					return true;
				}

				public void afterDownload(FtpDownloadPro dpro) throws Exception {
					String client = dpro.getFtpUploadConf().getClientPath();
					File clentFile = new File(client);
					if (!clentFile.exists())
						throw new BizException("[" + clentFile.getAbsolutePath() + "] Not Exists!");
					if (clentFile.isDirectory()) {
						client = client + "/" + dpro.getFtpfile().getName();
					}

					File f = new File(tmpeditpath);
					if (f.length() > 10000000L) {
						throw new BizException(String.format("[%s] 文件共 %s ，超过了 %s，不能继续操作。", new Object[] { f.getName(),
								FileUtils.byteCountToDisplaySize(f.length()), FileUtils.byteCountToDisplaySize(10000000L) }));
					}
					f = new File(client);
					if (f.length() > 10000000L) {
						throw new BizException(String.format("[%s] 文件共 %s ，超过了 %s，不能继续操作。", new Object[] { f.getName(),
								FileUtils.byteCountToDisplaySize(f.length()), FileUtils.byteCountToDisplaySize(10000000L) }));
					}
					Charset cs = Cpdetector.richencode(new FileInputStream(tmpeditpath));
					Charset cc = Cpdetector.richencode(new FileInputStream(client));

					String stext = FileUtil.getTextFromFile(tmpeditpath, cs != null ? cs.toString() : "gbk");
					String ctext = FileUtil.getTextFromFile(client, cc != null ? cc.toString() : "gbk");
					FileUtils.deleteDirectory(new File(tmpeditpath).getParentFile());
					PluginUtil.openCompareEditor(page, ctext, stext, String.format("%s - %s", new Object[] {
							new File(client).getAbsolutePath(), dpro.getFtpUploadConf().getServerPath() }));
				}

			}, "Compare Process");
		}
	}

	protected void treeExpanded_actionPerformed(TreeExpansionEvent e) throws Exception {
		Object elem = e.getElement();
		if ((elem instanceof AbstractSexftpView.TreeParent)) {
			AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent) elem;
			expandTreeData(parent, null);
		}
		super.treeExpanded_actionPerformed(e);
	}

	private void expandTreeData(final AbstractSexftpView.TreeParent parent, final String onlyPath) {
		boolean isftpuploadconf = parent.getO() instanceof FtpUploadConf;
		boolean isftpdownpro = parent.getO() instanceof FtpDownloadPro;
		final FtpConf ftpconf = isftpdownpro ? ((FtpDownloadPro) parent.getO()).getFtpConf()
				: isftpuploadconf ? (FtpConf) parent.getParent().getO() : null;
		final FtpUploadConf ftpUploadConf = isftpdownpro ? ((FtpDownloadPro) parent.getO()).getFtpUploadConf()
				: isftpuploadconf ? (FtpUploadConf) parent.getO() : null;
		if ((parent.getChildren().length == 0) && (ftpUploadConf != null)) {
			refreshPendingTree(parent, new SexftpRun(this) {
				public void srun() {
					FtpPools ftppool = new FtpPools(ftpconf, SexftpServerView.this);
					XFtp ftp = ftppool.getFtp();
					synchronized (ftp) {
						ftp = ftppool.getConnectedFtp();

						FtpPools ftppool2 = new FtpPools(ftpconf, SexftpServerView.this);
						ftppool2.getFtp();
						AbstractSexftpView.TreeObject onlyTo = SexftpServerView.this.expandServerFile(parent, ftpconf,
								ftpUploadConf, ftp, onlyPath);
						setReturnObject(onlyTo);
						if ((onlyPath != null) && (onlyTo == null)) {
							SexftpServerView.this
									.showMessage("Cann't Find [" + onlyPath + "] In [Sexftp Server View]!");
						}
					}
				}
			});
		}
	}

	private AbstractSexftpView.TreeObject expandServerFile(AbstractSexftpView.TreeParent parent, FtpConf ftpconf,
			FtpUploadConf ftpUploadConf, XFtp ftp, String onlyPath) {
		AbstractSexftpView.TreeObject onlyTreeObject = null;
		List<FtpDownloadPro> listDownloadPro = null;
		listDownloadPro = listDownloadPro(ftpUploadConf, ftpconf, ftp);
		for (FtpDownloadPro ftpDownloadPro : listDownloadPro) {
			String thisServer = ftpDownloadPro.getFtpUploadConf().getServerPath();

			String serverPath = thisServer;
			String teeNodeName = String.format("%s ( %s %s )",
					new Object[] { ftpDownloadPro.getFtpfile().getName(),
							FileUtils.byteCountToDisplaySize(ftpDownloadPro.getFtpfile().getSize()),
							DateTimeUtils.format(ftpDownloadPro.getFtpfile().getTimeStamp().getTime()) });
			if (serverPath.endsWith("/")) {
				AbstractSexftpView.TreeObject exists = existFtpDownloadPro(parent, thisServer);
				AbstractSexftpView.TreeParent child = new AbstractSexftpView.TreeParent(
						ftpDownloadPro.getFtpfile().getName(), ftpDownloadPro);
				if (exists != null) {
					child = (AbstractSexftpView.TreeParent) exists;

				} else {

					parent.addChild(child);
				}

				if (thisServer.equals(onlyPath)) {
					onlyTreeObject = child;

				} else if ((onlyPath != null) && (onlyPath.startsWith(thisServer))) {
					onlyTreeObject = onlyTreeObject == null
							? expandServerFile(child, ftpconf, ftpDownloadPro.getFtpUploadConf(), ftp, onlyPath)
							: onlyTreeObject;
				}

			} else {
				AbstractSexftpView.TreeObject to = new AbstractSexftpView.TreeObject(teeNodeName, ftpDownloadPro);
				if (existFtpDownloadPro(parent, thisServer) == null) {

					parent.addChild(to);
				}
				if (thisServer.equals(onlyPath)) {
					onlyTreeObject = to;
				}
			}
		}

		return onlyTreeObject;
	}

	private AbstractSexftpView.TreeObject existFtpDownloadPro(AbstractSexftpView.TreeParent p, String serverPath) {
		AbstractSexftpView.TreeObject[] arrayOfTreeObject;
		int j = (arrayOfTreeObject = p.getChildren()).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpView.TreeObject to = arrayOfTreeObject[i];

			if ((to.getO() instanceof FtpDownloadPro)) {
				FtpDownloadPro fupro = (FtpDownloadPro) to.getO();
				if (fupro.getFtpUploadConf().getServerPath().equals(serverPath)) {
					return to;
				}
			}
		}

		return null;
	}

	public void directTo(final String expandServerPath, Integer ftpUploadTreeNodesIndex) {
		final AbstractSexftpView.TreeParent r = ftpUploadTreeNodesIndex != null
				? getAllFtpUploadConfNodes()[ftpUploadTreeNodesIndex.intValue()] : getRoot();
		if (r.getChildren().length == 0) {
			expandTreeData(r, expandServerPath);
			return;
		}

		TreeViewUtil.serchTreeData(r, new SearchCallback() {
			public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject o) {
				if ((o.getO() instanceof FtpDownloadPro)) {
					String serverPath = ((FtpDownloadPro) o.getO()).getFtpUploadConf().getServerPath();
					if (expandServerPath.equals(serverPath)) {
						SexftpServerView.this.refreshTreeView(r, o);
						throw new AbortException();
					}
					if ((expandServerPath.startsWith(serverPath)) && ((o instanceof AbstractSexftpView.TreeParent))) {
						AbstractSexftpView.TreeObject[] children = ((AbstractSexftpView.TreeParent) o).getChildren();

						if (children.length == 0) {
							SexftpServerView.this.expandTreeData((AbstractSexftpView.TreeParent) o, expandServerPath);
							throw new AbortException();
						}
					}
				}

				return new TreeViewUtil.ThisYourFind(false, true);
			}

		});
		showMessage("Cann't Find [" + expandServerPath + "] In [Sexftp Server View]!");
	}

	protected void actionDirectSLocal_actionPerformed() throws Exception {
		ISelection selection = this.viewer.getSelection();
		AbstractSexftpView.TreeObject obj = (AbstractSexftpView.TreeObject) ((IStructuredSelection) selection)
				.getFirstElement();
		AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
		SexftpLocalView mv = PluginUtil.findAndShowLocalView(PluginUtil.getActivePage());
		if (selectFtpUploadConfNodes.length >= 1) {
			AbstractSexftpView.TreeParent selectObj = selectFtpUploadConfNodes[0];
			if ((obj.getO() instanceof FtpDownloadPro)) {

				FtpDownloadPro dpro = (FtpDownloadPro) obj.getO();
				String clilentPath = dpro.getFtpUploadConf().getClientPath();
				if (!dpro.getFtpUploadConf().getServerPath().endsWith("/")) {
					clilentPath = clilentPath + "/" + new File(dpro.getFtpUploadConf().getServerPath()).getName();
					clilentPath = new File(clilentPath).getAbsolutePath();
				}
				AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
				for (int i = 0; i < allFtpUploadConfNodes.length; i++) {
					if (allFtpUploadConfNodes[i] == selectObj) {
						mv.directTo(clilentPath, Integer.valueOf(i));
						break;
					}
				}
			}
		}

		super.actionDirectSLocal_actionPerformed();
	}

	protected boolean isShowProjectView() {
		return false;
	}

	protected void doubleClickAction_actionPerformed() throws Exception {
		super.doubleClickAction_actionPerformed();
		actionEdit_actionPerformed();
	}

	public List getHiddenActions() {
		return Arrays.asList(new Object[] { this.actionDirectSServer, this.actionUpload, this.actionPrepareServUpload,
				this.actionPrepareUpload, this.actionLocalEdit });
	}

	protected void menuAboutToShow_event(IMenuManager manager) {
		super.menuAboutToShow_event(manager);
	}

	protected void actionEnableHandle() {
		super.actionEnableHandle();
		Object[] os = getSelectionObjects();
		if ((os.length == 1) && ((os[0] instanceof FtpDownloadPro))
				&& (!((FtpDownloadPro) os[0]).getFtpUploadConf().getServerPath().endsWith("/"))) {
			this.actionEdit.setEnabled(true);
		} else {
			this.actionCompare.setEnabled(false);
		}

		this.actionDirectSLocal.setEnabled(true);
		this.actionDirectSServer.setEnabled(false);
	}

	protected void actionEdit_actionPerformed() throws Exception {
		Object[] selectO = getSelectionObjects();
		if ((selectO.length == 1) && ((selectO[0] instanceof FtpDownloadPro))) {
			FtpDownloadPro dpro = (FtpDownloadPro) selectO[0];
			if (!dpro.getFtpUploadConf().getServerPath().endsWith("/")) {
				innerEditServerFile(dpro);
			}
		}

		super.actionEdit_actionPerformed();
	}

	public void innerEditServerFile(FtpDownloadPro dpro) throws Exception {
		final IFile ifile = PluginUtil.createSexftpIFileFromPath(
				"/.seredittemp/" + System.currentTimeMillis() + "/" + dpro.getFtpfile().getName());
		final String tmpeditpath = ifile.getLocation().toFile().getAbsolutePath();

		innerDownload(new Object[] { dpro }, new MySexftpServerDownload() {
			public String trustFolder(FtpDownloadPro dpro) {
				return tmpeditpath;
			}

			public boolean exceptionNotExits(FtpDownloadPro dpro) throws Exception {
				return true;
			}

			public void afterDownload(final FtpDownloadPro dpro) {
				Display.getDefault().asyncExec(new SexftpRun(SexftpServerView.this) {
					public void srun() throws Exception {
						ifile.refreshLocal(1, null);
						IFile inewfile = ifile;

						Charset c = Cpdetector.richencode(new FileInputStream(ifile.getLocation().toFile()));
						if ((c != null) && (c.toString().indexOf("ASCII") < 0)) {
							IContentType contentType = IDE.getContentType(inewfile);
							if ((contentType == null) || (contentType.getBaseType() == null)
									|| (!"text".equalsIgnoreCase(contentType.getBaseType().getName()))) {

								if (c.toString().startsWith("UTF-16")) {
									c = Charset.forName("gbk");
								}
							}
							inewfile = PluginUtil.rename(ifile, ifile.getName() + ".sf" + c);
							if (contentType != null) {
								contentType.setDefaultCharset(c.toString());
							} else {
								SexftpServerView.this
										.showMessage("You May Need Set The Text file encoding (" + c + ")");
							}
						}
						FtpConf ftpconf = dpro.getFtpConf();
						FtpPools ftppool = new FtpPools(ftpconf, SexftpServerView.this);
						String serverDirPath = new File(dpro.getFtpUploadConf().getServerPath()).getParent()
								.replace('\\', '/');
						String serverFileName = dpro.getFtpfile().getName();
						IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.openEditor(new FileEditorInput(inewfile), "sexftp.editors.inner.SfTextEditor");
						SfTextEditor stextEditor = (SfTextEditor) openEditor;
						stextEditor.setDoSaveListener(new ServerInnnerEditSaveListener(inewfile, serverDirPath,
								serverFileName, SexftpServerView.this, ftppool));
					}
				});
			}
		});
	}

	protected void actionDisconnect_actionPerformed() throws Exception {
		super.actionDisconnect_actionPerformed();
	}

	private List<FtpDownloadPro> listDownloadPro(FtpUploadConf ftpUploadConf, FtpConf ftpconf, XFtp ftp) {
		List<FtpDownloadPro> downloadProList = new ArrayList();
		ftp.cd(ftpUploadConf.getServerPath());
		List<FtpFile> listFiles = ftp.listFiles();
		for (FtpFile ftpfile : listFiles) {
			String filename = ftpfile.getName();
			if ((!filename.equals(".")) && (!filename.equals(".."))) {
				FtpUploadConf expandFtpUploadInf = new FtpUploadConf();
				String serverPath = ftpUploadConf.getServerPath();
				String clientPath = ftpUploadConf.getClientPath();

				serverPath = serverPath + filename;
				if (ftpfile.isIsfolder()) {
					serverPath = serverPath + "/";
					clientPath = clientPath + "/" + filename;
				}
				expandFtpUploadInf.setServerPath(serverPath);
				expandFtpUploadInf.setClientPath(clientPath);
				expandFtpUploadInf.setIncludes(ftpUploadConf.getIncludes());
				expandFtpUploadInf.setExcludes(ftpUploadConf.getExcludes());
				FtpDownloadPro dpro = new FtpDownloadPro(expandFtpUploadInf, ftpconf, ftpfile);

				downloadProList.add(dpro);
			}
		}
		return downloadProList;
	}

	protected boolean okPopActionDownload() {
		Object[] os = getSelectionObjects();
		if (os.length == 0)
			return false;
		Object[] arrayOfObject1;
		int j = (arrayOfObject1 = os).length;
		for (int i = 0; i < j; i++) {
			Object o = arrayOfObject1[i];
			if (!(o instanceof FtpConf)) {

				if (!(o instanceof FtpUploadConf)) {

					if (!(o instanceof FtpDownloadPro)) {

						return false;
					}
				}
			}
		}
		return true;
	}

	protected void actionDownload_actionPerformed() throws Exception {
		AbstractSexftpView.TreeObject[] selectTreeObj = getUpNodes(getSelectNodes(false));
		List selectOs = new ArrayList();
		AbstractSexftpView.TreeObject[] arrayOfTreeObject1;
		int j = (arrayOfTreeObject1 = selectTreeObj).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpView.TreeObject to = arrayOfTreeObject1[i];

			if ((to.getO() instanceof FtpUploadConf)) {
				FtpDownloadPro dpro = new FtpDownloadPro((FtpUploadConf) to.getO(), (FtpConf) to.getParent().getO(),
						new FtpFile("/", true, 0L, Calendar.getInstance()));
				selectOs.add(dpro);
			} else if ((to.getO() instanceof FtpConf)) {
				AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
				int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent) to).getChildren()).length;
				for (int k = 0; k < m; k++) {
					AbstractSexftpView.TreeObject suto = arrayOfTreeObject2[k];

					FtpDownloadPro dpro = new FtpDownloadPro((FtpUploadConf) suto.getO(),
							(FtpConf) suto.getParent().getO(), new FtpFile("/", true, 0L, Calendar.getInstance()));
					selectOs.add(dpro);
				}
			} else {
				selectOs.add(to.getO());
			}
		}
		innerDownload(selectOs.toArray(), null);
	}

	public void innerDownload(Object[] os, MySexftpServerDownload mydownload) throws Exception {
		innerDownload(os, mydownload, null);
	}

	public void innerDownload(final Object[] os, final MySexftpServerDownload mydownload, String title)
			throws Exception {
		Job job = new SexftpJob(title != null ? title : "Download Process", this) {
			private List<FtpDownloadPro> list;
			private boolean finisedPrepare = false;
			private int workedcont = 0;
			private double workedpercents = 800.0D;
			private boolean corrTime = false;
			private Integer overwritecode = null;
			private boolean okdowncurrent = true;

			protected IStatus srun(final IProgressMonitor monitor) throws Exception {
				FtpPools ftppool = null;
				XFtp ftp = null;
				FtpDownloadPro pdpro = null;
				Object[] arrayOfObject1;
				int j = (arrayOfObject1 = os).length;
				FtpConf ftpconf;
				for (int i = 0; i < j; i++) {
					Object o = arrayOfObject1[i];
					if ((o instanceof FtpDownloadPro)) {
						pdpro = (FtpDownloadPro) o;
						ftpconf = ((FtpDownloadPro) o).getFtpConf();
						ftppool = new FtpPools(ftpconf, SexftpServerView.this);
						ftp = ftppool.getFtp();
						break;
					}
				}

				monitor.beginTask("Getting Data From Server...", 1000);
				if (ftp != null) {
					synchronized (ftp) {
						ftppool.getConnectedFtp();
						this.list = new Vector();
						ExistFtpFile existf = new ExistFtpFile(ftp);
						Object[] arrayOfObject2;
						int temp = (arrayOfObject2 = os).length;
						for (int localFtpConf1 = 0; localFtpConf1 < temp; localFtpConf1++) {
							Object o = arrayOfObject2[localFtpConf1];
							if ((o instanceof FtpDownloadPro)) {
								pdpro = (FtpDownloadPro) o;
								String cserverPath = pdpro.getFtpUploadConf().getServerPath();
								if (!cserverPath.endsWith("/")) {
									monitor.subTask("Checking " + cserverPath);
									if (existf.existsFtpFile(cserverPath) == null) {

										if ((mydownload == null) || (mydownload.exceptionNotExits(pdpro))) {
											throw new BizException("[" + pdpro.getFtpUploadConf().getServerPath()
													+ "] Not Exists On Server!");
										}
									} else
										pdpro.setFtpfile(existf.existsFtpFile(cserverPath));
								} else {
									SexftpServerView.this.innerDownladRetriList(pdpro, monitor, this.list, ftp);
								}
							}
						}
						this.finisedPrepare = true;

						String dir = "";
						for (int i = 0; i < 10; i++) {
							if ((!this.finisedPrepare) && (this.list.size() == 0)) {

								for (int t = 0; t < 30; t++) {
									Thread.sleep(1000L);
									if (this.list.size() > 0)
										break;
								}
							}
							if (this.list.size() == 0) {
								if (this.finisedPrepare) {
									break;
								}
								throw new BizException("Download Time Out!");
							}

							if ((this.finisedPrepare) && (!this.corrTime)) {
								this.list.size();

								if (1000 > this.list.size()) {
									monitor.worked(1000 - this.list.size());
								} else if (1000 < this.list.size()) {
									this.workedpercents = ((1000 - this.workedcont) * 100
											/ (this.list.size() - this.workedcont));
									this.workedpercents = (this.workedpercents * 70.0D / 100.0D);
								}
								this.corrTime = true;
							}

							FtpDownloadPro dpro = (FtpDownloadPro) this.list.get(0);

							final FtpDownloadPro fdpro = dpro;
							String serverPath = dpro.getFtpUploadConf().getServerPath();
							String wkdir = serverPath.substring(0, serverPath.lastIndexOf("/") + 1);
							String fielname = serverPath.substring(serverPath.lastIndexOf("/") + 1);
							if (!dir.equals(wkdir)) {
								ftp.cd(wkdir);
								dir = wkdir;
								SexftpServerView.this.console("working in " + wkdir);
							}
							IFtpStreamMonitor fmon = new IFtpStreamMonitor() {
								private FtpUploadConf curftpUploadConf;
								long calSize = 0L;
								long timsta = 0L;
								long speed = 0L;
								boolean smallLeftCompleted = true;
								boolean okCancel = false;

								public void printStreamString(FtpUploadConf ftpUploadConf, long uploadedSize,
										long totalSize, String info) {
									totalSize = (int) fdpro.getFtpfile().getSize();

									if (ftpUploadConf != null) {
										this.curftpUploadConf = ftpUploadConf;
										this.calSize = 0L;
										this.timsta = System.currentTimeMillis();
										return;
									}
									long secds = System.currentTimeMillis() - this.timsta;
									if (secds > 1000L) {
										this.speed = (long) (((float) (uploadedSize - this.calSize)
												/ ((float) secds / 1000.0F)));
										this.calSize = uploadedSize;
										this.timsta = System.currentTimeMillis();
									}

									monitor.subTask(String.format("(%s in %s) %s \r\n getting %s",
											new Object[] { FileUtils.byteCountToDisplaySize(uploadedSize),
													FileUtils.byteCountToDisplaySize(totalSize),
													(float) this.speed > 1.0E-4F
															? FileUtils.byteCountToDisplaySize(this.speed) + "/s" : "",
											this.curftpUploadConf.getServerPath() }));

									this.okCancel = monitor.isCanceled();

									if (totalSize == uploadedSize) {
										int r = new Random().nextInt(100);

										if (workedpercents > r) {
											monitor.worked(1);
											workedcont += 1;
										}

									} else if ((this.okCancel) && (totalSize - uploadedSize < 102400L)) {
										if (this.smallLeftCompleted) {
											SexftpServerView.this.console("Canncled But Go Ahead Little Left Files! "
													+ fdpro.getFtpfile().getName() + " "
													+ FileUtils.byteCountToDisplaySize(uploadedSize));
											this.okCancel = false;
										}
									}

									if (this.okCancel) {
										SexftpServerView.this.console("Operation Canceled!");
										SexftpServerView.this.console(String.format("Last Downloaded %s Of %s .",
												new Object[] { FileUtils.byteCountToDisplaySize(uploadedSize),
														FileUtils.byteCountToDisplaySize(totalSize) }));
										if (totalSize > uploadedSize) {
											SexftpServerView.this
													.console(String.format("Warning:Incomplete Download %s %s of %s!",
															new Object[] { this.curftpUploadConf.getServerPath(),
																	FileUtils.byteCountToDisplaySize(uploadedSize),
																	FileUtils.byteCountToDisplaySize(totalSize) }));
										}
										list = null;
										throw new AbortException();
									}
								}

								public void printSimple(String info) {
									SexftpServerView.this.console(info);
								}
							};
							fmon.printStreamString(dpro.getFtpUploadConf(), 0L, 0L, null);
							String trustPath = mydownload != null ? mydownload.trustFolder(fdpro) : null;
							final String cpath = dpro.getFtpUploadConf().getClientPath() + "/" + fielname;
							this.okdowncurrent = true;
							if ((PluginUtil.overwriteTips().booleanValue()) && (trustPath == null)
									&& (new File(cpath).exists())) {
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										int oretur = 0;
										if (overwritecode == null) {
											MessageDialogWithToggle q = SexftpServerView.this.showQuestion(
													"File [" + cpath + "] Exists,Overwirte?",
													"Remember Me In This Operation!");
											if (q.getToggleState()) {
												overwritecode = Integer.valueOf(q.getReturnCode());
											}
											oretur = q.getReturnCode();
										} else {
											oretur = overwritecode.intValue();
										}
										if (oretur != 2) {

											if (oretur == 1) {
												list = null;
												okdowncurrent = false;
											} else if (oretur == 3) {
												okdowncurrent = false;
											} else {
												okdowncurrent = false;
											}
										}
									}
								});
							}
							if (this.list == null)
								throw new AbortException();
							if ((this.okdowncurrent) && (!monitor.isCanceled())) {
								ftp.download(fielname, cpath, fmon);
								if (mydownload != null) {
									mydownload.afterDownload(dpro);
								}
							}

							this.list.remove(0);
							if ((this.finisedPrepare) && (this.list.size() == 0))
								break;
							if ((this.list.size() > 0) && (i >= 9)) {
								i = 0;
							}
						}
					}
				}

				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
	}

	private List<FtpDownloadPro> innerDownladRetriList(FtpDownloadPro dpro, IProgressMonitor monitor,
			List<FtpDownloadPro> list, XFtp ftp) {
		if ((monitor.isCanceled()) || (list == null)) {
			throw new AbortException();
		}
		String serverPath = dpro.getFtpUploadConf().getServerPath();
		if (serverPath.endsWith("/")) {
			monitor.subTask("Prepareing " + serverPath);
			List<FtpDownloadPro> listDownloadPro = listDownloadPro(dpro.getFtpUploadConf(), dpro.getFtpConf(), ftp);
			for (FtpDownloadPro ftpDownloadPro : listDownloadPro) {
				innerDownladRetriList(ftpDownloadPro, monitor, list, ftp);
			}

		} else if (list != null) {
			list.add(dpro);
		}
		return list;
	}
}
