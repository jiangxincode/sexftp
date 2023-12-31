package sexftp.views;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.desy.common.util.DateTimeUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.sexftp.core.exceptions.AbortException;
import org.sexftp.core.exceptions.SRuntimeException;
import org.sexftp.core.ftp.FileMd5;
import org.sexftp.core.ftp.FtpUtil;
import org.sexftp.core.ftp.bean.FtpConf;
import org.sexftp.core.ftp.bean.FtpDownloadPro;
import org.sexftp.core.ftp.bean.FtpFile;
import org.sexftp.core.ftp.bean.FtpUploadConf;
import org.sexftp.core.ftp.bean.FtpUploadPro;
import org.sexftp.core.utils.FileUtil;
import org.sexftp.core.utils.SearchCallback;
import org.sexftp.core.utils.StringUtil;
import org.sexftp.core.utils.TreeViewUtil;
import sexftp.SexftpJob;
import sexftp.SexftpRun;
import sexftp.editors.inner.SfTextEditor;
import sexftp.editors.viewlis.IDoSaveListener;
import sexftp.uils.PluginUtil;

public class SexftpLocalView extends SexftpMainView {
	protected void actionPrepare() {
		this.actionCompare.setText("Compare With Server(&C)");
		super.actionPrepare();
	}

	protected void actionPrepareUpload_actionPerformed() throws Exception {
		final FtpConf[] ftpconfs = getFtpConfsSelected();
		final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();

		doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro() {
			public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
				innerPrepareUpload_actionPerformed(selectOs, ftpconfs, selectFtpUploadConfNodes);
			}
		});
	}

	protected void innerPrepareUpload_actionPerformed(final Object[] selectos, FtpConf[] ftpconfs,
			final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes) throws Exception {
		if (ftpconfs.length > 1) {
			throw new AbortException();
		}
		FtpConf[] arrayOfFtpConf;
		int j = (arrayOfFtpConf = ftpconfs).length;
		for (int i = 0; i < j; i++) {
			FtpConf ftpconf = arrayOfFtpConf[i];

			String lastModify = workspaceWkPath + ftpconf.getName() + "/lastModMap.d";
			if (!new File(lastModify).exists()) {
				showMessage("No Formated Files,Need Format First,And You Can Get Modified Files Next Time!");
				actionFormat_innerPerofrmed(ftpconf.getName(), true, null);
				return;
			}
		}
		final FtpConf ftpconf = ftpconfs[0];

		Object job = new SexftpJob("Prepare Local New Modified File", this) {
			protected IStatus srun(IProgressMonitor monitor) throws Exception {
				monitor.subTask("Preparing Files...");

				Object[] os = selectos;

				monitor.beginTask("Prepare Local New Modified File,doing ...", os.length);

				for (int i = 0; i < selectFtpUploadConfNodes.length; i++) {
					AbstractSexftpView.TreeParent tp = selectFtpUploadConfNodes[i];

					AbstractSexftpView.TreeParent newtp = new AbstractSexftpView.TreeParent(tp.getName(),
							StringUtil.deepClone(tp.getO()));
					newtp.setParent(tp.getParent());
					selectFtpUploadConfNodes[i] = newtp;
				}

				Map<String, String> lastModMap = FtpUtil
						.readLastModMap(SexftpLocalView.workspacePath + ".work/" + ftpconf.getName());
				final List<FtpUploadPro> difList = new ArrayList<FtpUploadPro>();
				final List<FtpUploadPro> notExistList = new ArrayList<FtpUploadPro>();
				Object[] arrayOfObject1;
				int j = (arrayOfObject1 = os).length;
				for (int i = 0; i < j; i++) {
					Object o = arrayOfObject1[i];
					if ((o instanceof FtpUploadPro)) {
						FtpUploadPro fupro = (FtpUploadPro) o;
						String client = fupro.getFtpUploadConf().getClientPath();
						monitor.subTask("Comparing.. " + client);
						File clientfile = new File(client);
						if ((clientfile.exists()) && (clientfile.isFile())) {
							String md5 = FileMd5.getMD5(clientfile, monitor);
							if (lastModMap.containsKey(client)) {
								if (!((String) lastModMap.get(client)).equals(md5)) {
									difList.add(fupro);
								}

							} else {
								notExistList.add(fupro);
							}
						}
						monitor.subTask("waiting...");
					}
					monitor.worked(1);
				}

				Display.getDefault().asyncExec(new SexftpRun(SexftpLocalView.this) {
					public void srun() throws Exception {
						List<FtpUploadPro> pathList = new ArrayList();
						pathList.addAll(difList);
						pathList.addAll(notExistList);
						boolean syncViewShowed = PluginUtil.isSyncViewShowed(SexftpLocalView.this.activePage);
						if (pathList.size() == 0) {
							SexftpLocalView.this.showMessage(
									"No Different Or Modified Files With The Status Of Last Format Or Upload!");
							if (!syncViewShowed) {
								return;
							}
						}
						SexftpLocalView.this.handleSyncTreeNode(pathList, selectFtpUploadConfNodes, ftpconf);

						if (!syncViewShowed) {
							SexftpLocalView.this.showMessage("The Result Will Show In [Sexftp Synchronize View]");
						}
						SexftpSyncView syncView = PluginUtil.findAndShowSyncView(SexftpLocalView.this.activePage);
						syncView.showDifView(selectFtpUploadConfNodes,
								SexftpLocalView.this.anyaCustomizedImgMap(new ArrayList(), difList, notExistList));
					}

				});
				return Status.OK_STATUS;
			}
		};
		((Job) job).setUser(true);
		((Job) job).schedule();
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
			} else if ((to.getO() instanceof FtpUploadPro)) {
				selectOs.add(uPro2DownPro((FtpUploadPro) to.getO()));
			}
		}

		PluginUtil.findAndShowServerView(PluginUtil.getActivePage()).innerDownload(selectOs.toArray(), null);
	}

	public void innerCompareFiles(final IInnerCompareCallback callback, Object[] os) throws Exception {
		if (os != null) {
			final List<FtpDownloadPro> dproList = new ArrayList();
			boolean hasfolder = false;
			Object[] arrayOfObject;
			int j = (arrayOfObject = os).length;
			for (int i = 0; i < j; i++) {
				Object o = arrayOfObject[i];

				if ((o instanceof FtpUploadPro)) {
					FtpUploadPro upro = (FtpUploadPro) o;
					File file = new File(upro.getFtpUploadConf().getClientPath());

					if (file.isFile()) {
						String filename = file.getName();
						FtpFile ftpfile = new FtpFile(filename, false, 0L, null);
						FtpUploadConf fconf = (FtpUploadConf) StringUtil.deepClone(upro.getFtpUploadConf());
						fconf.setServerPath(fconf.getServerPath() + filename);
						fconf.setClientPath(file.getParent());
						FtpDownloadPro dpro = new FtpDownloadPro(fconf, upro.getFtpConf(), ftpfile);
						dproList.add(dpro);
					}
					if (file.isDirectory()) {
						hasfolder = true;
					}
				} else {
					hasfolder = true;
				}
			}

			if ((dproList.size() == 1) && (!hasfolder)) {

				FtpDownloadPro dpro = (FtpDownloadPro) dproList.get(0);
				PluginUtil.findServerView(getWorkbenchPage()).innerCompare(new Object[] { dpro });
			} else if ((dproList.size() > 1) || (hasfolder)) {
				IFile ifile = PluginUtil
						.createSexftpIFileFromPath("/.seredittemp/" + System.currentTimeMillis() + "/comparetemp.v");
				final String tmpeditpath = ifile.getLocation().toFile().getAbsolutePath();
				PluginUtil.findServerView(getWorkbenchPage()).innerDownload(dproList.toArray(),
						new MySexftpServerDownload() {
							private int downcount = 0;
							private List<FtpUploadPro> equalsList = new ArrayList();
							private List<FtpUploadPro> difList = new ArrayList();
							private List<FtpUploadPro> notExistList = new ArrayList();

							public String trustFolder(FtpDownloadPro dpro) throws Exception {
								return tmpeditpath;
							}

							public boolean exceptionNotExits(FtpDownloadPro dpro) throws Exception {
								this.notExistList.add(SexftpLocalView.this.downPro2UpPro(dpro));
								this.downcount += 1;
								if (this.downcount >= dproList.size()) {
									callback.afterCompareEnd(this.equalsList, this.difList, this.notExistList);
								}
								return false;
							}

							public void afterDownload(FtpDownloadPro dpro) throws Exception {
								String serverMd5 = FileMd5.getMD5(new File(tmpeditpath), null, "");

								File clientfile = new File(
										dpro.getFtpUploadConf().getClientPath() + "/" + dpro.getFtpfile().getName());

								String clientMd5 = FileMd5.getMD5(clientfile, null, "");
								if (clientMd5.equals(serverMd5)) {
									this.equalsList.add(SexftpLocalView.this.downPro2UpPro(dpro));
								} else {
									this.difList.add(SexftpLocalView.this.downPro2UpPro(dpro));
								}

								this.downcount += 1;
								if (this.downcount >= dproList.size()) {
									callback.afterCompareEnd(this.equalsList, this.difList, this.notExistList);
								}
							}
						}, "Prepaer Files Which Different From Server");
			}
		}
	}

	protected void actionCompare_actionPerformed() throws Exception {
		doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro() {
			public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
				SexftpLocalView.this.innerCompare_actionPerformed(selectOs);
			}
		});
	}

	protected void innerCompare_actionPerformed(Object[] os) throws Exception {
		innerCompareFiles(new IInnerCompareCallback() {
			public void afterCompareEnd(final List<FtpUploadPro> equalsList, final List<FtpUploadPro> difList,
					final List<FtpUploadPro> notExistList) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						SexftpLocalView.this.customizedImgMap = SexftpLocalView.this.anyaCustomizedImgMap(equalsList,
								difList, notExistList);
						SexftpLocalView.this.viewer.refresh();
					}

				});
			}
		}, os);
		super.actionCompare_actionPerformed();
	}

	private Map<String, String> anyaCustomizedImgMap(List<FtpUploadPro> equalsList, List<FtpUploadPro> difList,
			List<FtpUploadPro> notExistList) {
		Map<String, String> map = new HashMap();
		File cfile = null;
		for (Iterator localIterator = notExistList.iterator(); localIterator.hasNext() && (cfile != null);) {
			FtpUploadPro notExists = (FtpUploadPro) localIterator.next();

			String clianetpah = notExists.getFtpUploadConf().getClientPath();
			map.put(clianetpah, "new_con2.gif");
			map.put(new File(clianetpah).getParent(), "foldermodified_pending.gif");

			cfile = new File(clianetpah).getParentFile();

			map.put(cfile.getAbsolutePath(), "addtoworkset.gif");
			cfile = cfile.getParentFile();
			continue;
		}

		for (Iterator localIterator = difList.iterator(); localIterator.hasNext() && (cfile != null);) {
			FtpUploadPro dif = (FtpUploadPro) localIterator.next();

			String clianetpah = dif.getFtpUploadConf().getClientPath();
			map.put(clianetpah, "filemodified_pending.gif");

			cfile = new File(clianetpah).getParentFile();

			map.put(cfile.getAbsolutePath(), "foldermodified_pending.gif");
			cfile = cfile.getParentFile();
			continue;
		}

		for (Iterator localIterator = equalsList.iterator(); localIterator.hasNext() && (cfile != null);) {
			FtpUploadPro eq = (FtpUploadPro) localIterator.next();

			String clianetpah = eq.getFtpUploadConf().getClientPath();
			map.put(clianetpah, "interceptor-stack.gif");

			cfile = new File(clianetpah).getParentFile();

			if (map.containsKey(cfile.getAbsolutePath())) {
				map.put(cfile.getAbsolutePath(), "foldermodified_pending.gif");
			}
			cfile = cfile.getParentFile();
			continue;

		}

		return map;
	}

	protected boolean copyTreeNodeIndoAfterSelectAndAddChildUploadPro() {
		return false;
	}

	public void doAfterSelectAndAddChildUploadPro(final DoAfterSelectAndAddChildUploadPro run) throws Exception {
		getWorkbenchPage();
		final List selectObjs = new ArrayList();
		final AbstractSexftpView.TreeObject[] selectFtpUploadConfNodes = getUpNodes(getSelectNodes(false));
		Job job = new SexftpJob("Prepare Local File Process", this) {
			protected IStatus srun(IProgressMonitor monitor) throws Exception {
				monitor.beginTask("Prepare Local File Process", -1);
				AbstractSexftpView.TreeObject[] arrayOfTreeObject1;
				int j = (arrayOfTreeObject1 = selectFtpUploadConfNodes).length;
				for (int i = 0; i < j; i++) {
					AbstractSexftpView.TreeObject treeObject = arrayOfTreeObject1[i];
					AbstractSexftpView.TreeObject[] oktos = (AbstractSexftpView.TreeObject[]) null;
					if ((treeObject instanceof AbstractSexftpView.TreeParent)) {
						if ((treeObject.getO() instanceof FtpConf)) {
							AbstractSexftpView.TreeParent treeParent = (AbstractSexftpView.TreeParent) treeObject;
							oktos = treeParent.getChildren();
						} else {
							oktos = new AbstractSexftpView.TreeObject[] { treeObject };
						}
					} else {
						selectObjs.add(treeObject.getO());
						continue;
					}
					AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
					int m = (arrayOfTreeObject2 = oktos).length;
					for (int k = 0; k < m; k++) {
						AbstractSexftpView.TreeObject oktree = arrayOfTreeObject2[k];

						if ((oktree instanceof AbstractSexftpView.TreeParent)) {
							AbstractSexftpView.TreeParent treeParent = (AbstractSexftpView.TreeParent) oktree;
							AbstractSexftpView.TreeParent newtp = treeParent;
							if (SexftpLocalView.this.copyTreeNodeIndoAfterSelectAndAddChildUploadPro()) {
								newtp = new AbstractSexftpView.TreeParent(treeParent.getName(),
										StringUtil.deepClone(treeParent.getO()));
								newtp.setParent(new AbstractSexftpView.TreeParent("", treeParent.getParent().getO()));
							}

							SexftpLocalView.this.addChildUploadPro(newtp, monitor, true);
							selectObjs.add(newtp.getO());
							selectObjs.addAll(Arrays.asList(SexftpLocalView.this.getChildObjects(newtp, monitor)));
							SexftpLocalView.this.refreshTreeView(newtp);
						}
					}
				}

				run.doafter(selectObjs.toArray(), monitor);
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	protected void actionPrepareServUpload_actionPerformed() throws Exception {
		final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
		final FtpConf ftpconf = getFtpConfsSelected()[0];
		final IWorkbenchPage activePage = getWorkbenchPage();
		doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro() {
			public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
				SexftpLocalView.this.innerPrepareServUpload_actionPerformed(selectOs, selectFtpUploadConfNodes, ftpconf,
						activePage);
			}
		});
	}

	public void innerPrepareServUpload_actionPerformed(Object[] selectOs,
			final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes, final FtpConf ftpconf,
			final IWorkbenchPage activePage) throws Exception {
		if (!showQuestion(
				"This Operation Will Compare Data With Server,It's May Take a Long Time,Sure?\r\nWe Suggest You To Use <View Or Upload Local New Modified Files>,It's Only Compare With Last File Upload Point At Local.")) {

			return;
		}

		Object[] os = selectOs;

		innerCompareFiles(new IInnerCompareCallback() {
			public void afterCompareEnd(final List<FtpUploadPro> equalsList, final List<FtpUploadPro> difList,
					final List<FtpUploadPro> notExistList) {
				for (int i = 0; i < selectFtpUploadConfNodes.length; i++) {
					AbstractSexftpView.TreeParent tp = selectFtpUploadConfNodes[i];

					AbstractSexftpView.TreeParent newtp = new AbstractSexftpView.TreeParent(tp.getName(),
							StringUtil.deepClone(tp.getO()));
					newtp.setParent(tp.getParent());
					selectFtpUploadConfNodes[i] = newtp;
				}
				Display.getDefault().asyncExec(new SexftpRun(SexftpLocalView.this) {
					public void srun() throws Exception {
						List<FtpUploadPro> pathList = new ArrayList();
						pathList.addAll(difList);
						pathList.addAll(notExistList);
						if (pathList.size() == 0) {
							SexftpLocalView.this.showMessage("No Different Or Modified Files With Server!");
							return;
						}

						SexftpLocalView.this.handleSyncTreeNode(pathList, selectFtpUploadConfNodes, ftpconf);
						if (!PluginUtil.isSyncViewShowed(activePage)) {
							SexftpLocalView.this.showMessage("The Result Will Show In [Sexftp Synchronize View]");
						}
						SexftpSyncView syncView = PluginUtil.findAndShowSyncView(activePage);
						syncView.showDifView(selectFtpUploadConfNodes,
								SexftpLocalView.this.anyaCustomizedImgMap(equalsList, difList, notExistList));
					}

				});
			}
		}, os);
		super.actionPrepareServUpload_actionPerformed();
	}

	private void handleSyncTreeNode(List<FtpUploadPro> pathList,
			AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes, FtpConf ftpconf) {
		Map<String, AbstractSexftpView.TreeParent> folderMap = new HashMap();
		for (FtpUploadPro upro : pathList) {
			String path = new File(upro.getFtpUploadConf().getClientPath()).getAbsolutePath();
			File folder = new File(path).getParentFile();
			if (!folderMap.containsKey(folder.getAbsolutePath())) {
				AbstractSexftpView.TreeParent[] arrayOfTreeParent;
				int j = (arrayOfTreeParent = selectFtpUploadConfNodes).length;
				for (int i = 0; i < j; i++) {
					AbstractSexftpView.TreeParent tp = arrayOfTreeParent[i];

					FtpUploadConf ftpUp = (FtpUploadConf) tp.getO();
					String ftpUpClientPath = new File(ftpUp.getClientPath()).getAbsolutePath();
					if (path.startsWith(ftpUpClientPath)) {

						FtpUploadConf foldUp = (FtpUploadConf) StringUtil.deepClone(ftpUp);
						foldUp.setClientPath(folder.getAbsolutePath());

						String xd = folder.getAbsolutePath().substring(ftpUpClientPath.length()).replace("\\", "/");
						if (xd.length() > 0) {
							if (!xd.endsWith("/"))
								xd = xd + "/";
							if (xd.startsWith("/"))
								xd = xd.substring(1);
							foldUp.setServerPath(ftpUp.getServerPath() + xd);

							FtpUploadPro ftpUpro = new FtpUploadPro(foldUp, ftpconf);
							AbstractSexftpView.TreeParent foldertp = new AbstractSexftpView.TreeParent(
									xd.substring(0, xd.length() - 1), ftpUpro);
							tp.addChild(foldertp);
							folderMap.put(folder.getAbsolutePath(), foldertp);
							break;
						}

						folderMap.put(folder.getAbsolutePath(), tp);

						break;
					}
				}
			}
			AbstractSexftpView.TreeParent foldertp = (AbstractSexftpView.TreeParent) folderMap
					.get(folder.getAbsolutePath());
			if (foldertp != null) {
				FtpUploadConf ftpUploadConf = null;
				if ((foldertp.getO() instanceof FtpUploadConf)) {
					ftpUploadConf = (FtpUploadConf) foldertp.getO();
				} else {
					FtpUploadPro fupro = (FtpUploadPro) foldertp.getO();
					ftpUploadConf = fupro.getFtpUploadConf();
				}
				FtpUploadConf fuconf = (FtpUploadConf) StringUtil.deepClone(ftpUploadConf);
				fuconf.setClientPath(path);
				FtpUploadPro ffupro = new FtpUploadPro(fuconf, ftpconf);
				AbstractSexftpView.TreeObject fto = new AbstractSexftpView.TreeObject(new File(path).getName(), ffupro);
				foldertp.addChild(fto);
			}
		}
	}

	protected void treeExpanded_actionPerformed(TreeExpansionEvent e) throws Exception {
		Object elem = e.getElement();
		if ((elem instanceof AbstractSexftpView.TreeParent)) {
			AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent) elem;
			if ((((parent.getO() instanceof FtpUploadConf)) || ((parent.getO() instanceof FtpUploadPro)))
					&& (parent.getChildren().length == 0)) {
				addChildUploadPro(parent, null, false);
				refreshTreeView(parent);
			}
		}

		super.treeExpanded_actionPerformed(e);
	}

	protected void treeCollapsed_actionPerformed(TreeExpansionEvent e) throws Exception {
		super.treeCollapsed_actionPerformed(e);
		Object elem = e.getElement();
		if ((elem instanceof AbstractSexftpView.TreeParent)) {
			AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent) elem;
			if ((parent.getO() instanceof FtpUploadConf)) {
				parent.removeAll();
				this.viewer.refresh();
			}
		}
	}

	protected void actionLocalEdit_actionPerformed() throws Exception {
		Object[] selectO = getSelectionObjects();
		if ((selectO.length == 1) && ((selectO[0] instanceof FtpUploadPro))) {
			FtpUploadPro dpro = (FtpUploadPro) selectO[0];
			innerEditLocalFile(dpro);
		}
	}

	protected void doubleClickAction_actionPerformed() throws Exception {
		super.doubleClickAction_actionPerformed();
		actionLocalEdit_actionPerformed();
	}

	protected boolean okPopActionDownload() {
		return true;
	}

	protected void actionEdit_actionPerformed() throws Exception {
		super.actionEdit_actionPerformed();
		Object[] selectO = getSelectionObjects();
		if ((selectO.length == 1) && ((selectO[0] instanceof FtpUploadPro))) {
			FtpUploadPro upro = (FtpUploadPro) selectO[0];
			FtpFile ftfile = new FtpFile(new File(upro.getFtpUploadConf().getClientPath()).getName(), false, 0L,
					Calendar.getInstance());
			XStream xstream = new XStream();
			FtpUploadConf up = (FtpUploadConf) xstream.fromXML(xstream.toXML(upro.getFtpUploadConf()));
			up.setServerPath(up.getServerPath() + ftfile.getName());
			FtpDownloadPro dpro = new FtpDownloadPro(up, upro.getFtpConf(), ftfile);
			SexftpServerView mv = PluginUtil.findServerView(getWorkbenchPage());
			mv.innerEditServerFile(dpro);
		}
	}

	public void innerEditLocalFile(final FtpUploadPro dpro) throws Exception {
		String clientPath = dpro.getFtpUploadConf().getClientPath();
		File f = new File(clientPath);
		if (!f.isFile())
			return;
		IProject[] arrayOfIProject;
		int j = (arrayOfIProject = PluginUtil.getAllOpenedProjects()).length;
		for (int i = 0; i < j; i++) {
			IProject p = arrayOfIProject[i];

			String projectPath = p.getFile("/a.txt").getLocation().toFile().getParent();
			if (f.getAbsolutePath().startsWith(projectPath)) {
				String ipath = f.getAbsolutePath().substring(projectPath.length());
				IFile ifile = p.getFile(ipath);

				ifile.refreshLocal(1, null);
				if (ifile.exists()) {
					IDE.openEditor(getWorkbenchPage(), ifile);
					return;
				}
			}
		}
		IFile ifile = PluginUtil
				.createSexftpIFileFromPath("/.seredittemp/" + System.currentTimeMillis() + "/" + f.getName());
		File profile = ifile.getLocation().toFile();
		if (!profile.getParentFile().exists()) {
			profile.getParentFile().mkdirs();
		}
		IFile inewfile = ifile;
		FileUtil.copyFile(f.getAbsolutePath(), profile.getAbsolutePath());
		ifile.refreshLocal(1, null);
		Charset c = org.sexftp.core.utils.Cpdetector.richencode(new FileInputStream(profile));
		if ((c != null) && (c.toString().indexOf("ASCII") < 0)) {
			if ((IDE.getContentType(inewfile) == null) || (IDE.getContentType(inewfile).getDefaultCharset() == null)
					|| (!IDE.getContentType(inewfile).getDefaultCharset().equalsIgnoreCase(c.toString()))) {
				inewfile = PluginUtil.rename(ifile, ifile.getName() + ".sf" + c);
				if (IDE.getContentType(inewfile) != null) {
					IDE.getContentType(inewfile).setDefaultCharset(c.toString());
				} else {
					showMessage("You May Need Set The Text file encoding (" + c + ")");
				}
			}
		}
		final IFile editfile = inewfile;
		org.eclipse.ui.IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.openEditor(new FileEditorInput(inewfile), "sexftp.editors.inner.SfTextEditor");
		SfTextEditor stextEditor = (SfTextEditor) openEditor;
		stextEditor.setDoSaveListener(new IDoSaveListener() {
			public void dosave() {
				FileUtil.copyFile(editfile.getLocation().toFile().getAbsolutePath(),
						dpro.getFtpUploadConf().getClientPath());
			}

			public void dispose() {
				File file = editfile.getLocation().toFile();
				File[] arrayOfFile;
				int j = (arrayOfFile = file.getParentFile().listFiles()).length;
				for (int i = 0; i < j; i++) {
					File subfile = arrayOfFile[i];

					subfile.delete();
				}
				file.getParentFile().delete();
			}
		});
	}

	protected void actionEnableHandle() {
		super.actionEnableHandle();
		Object[] os = getSelectionObjects();
		if ((os.length == 1) && ((os[0] instanceof FtpUploadPro))
				&& (new File(((FtpUploadPro) os[0]).getFtpUploadConf().getClientPath()).isFile())) {
			this.actionLocalEdit.setEnabled(true);
			this.actionEdit.setEnabled(true);
		}
		this.actionDirectSLocal.setEnabled(false);
		this.actionDirectSServer.setEnabled(true);
	}

	protected void actionDirectSServer_actionPerformed() throws Exception {
		ISelection selection = this.viewer.getSelection();
		AbstractSexftpView.TreeObject obj = (AbstractSexftpView.TreeObject) ((IStructuredSelection) selection)
				.getFirstElement();
		AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
		SexftpServerView mv = PluginUtil.findAndShowServerView(PluginUtil.getActivePage());
		if (selectFtpUploadConfNodes.length >= 1) {
			AbstractSexftpView.TreeParent selectObj = selectFtpUploadConfNodes[0];
			if ((obj.getO() instanceof FtpUploadPro)) {
				FtpUploadPro dpro = (FtpUploadPro) obj.getO();
				String clilentPath = dpro.getFtpUploadConf().getClientPath();
				String serverPath = dpro.getFtpUploadConf().getServerPath();
				if (new File(clilentPath).isFile()) {
					serverPath = serverPath + new File(clilentPath).getName();
				}

				AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
				for (int i = 0; i < allFtpUploadConfNodes.length; i++) {
					if (allFtpUploadConfNodes[i] == selectObj) {
						mv.directTo(serverPath, Integer.valueOf(i));
						break;
					}
				}
			}
		}
	}

	protected boolean canEnableUpload() {
		boolean can = false;
		Object[] arrayOfObject;
		int j = (arrayOfObject = getSelectionObjects()).length;
		for (int i = 0; i < j; i++) {
			Object o = arrayOfObject[i];

			if (((o instanceof FtpConf)) || ((o instanceof FtpUploadPro)) || ((o instanceof FtpUploadConf))) {
				can = true;
				break;
			}
		}

		return can;
	}

	private void addChildUploadPro(AbstractSexftpView.TreeParent p, IProgressMonitor monitor, boolean includeChild) {
		addChildUploadPro(p, monitor, includeChild, null);
	}

	private void addChildUploadPro(AbstractSexftpView.TreeParent p, IProgressMonitor monitor, boolean includeChild,
			String onlyPath) {
		if ((monitor != null) && (monitor.isCanceled())) {
			throw new AbortException();
		}
		FtpUploadConf ftpUploadConf = null;
		FtpConf ftpConf = null;
		if ((p.getO() instanceof FtpUploadPro)) {
			FtpUploadPro ftpPro = (FtpUploadPro) p.getO();
			ftpUploadConf = ftpPro.getFtpUploadConf();
			ftpConf = ftpPro.getFtpConf();
		} else if ((p.getO() instanceof FtpUploadConf)) {
			ftpUploadConf = (FtpUploadConf) p.getO();
			ftpConf = (FtpConf) p.getParent().getO();
		} else {
			return;
		}
		File file = new File(ftpUploadConf.getClientPath());
		if ((file.isHidden()) && (file.getParentFile() != null))
			return;
		File[] subfiles = file.listFiles();
		if ((file.isDirectory()) && (subfiles != null)) {
			if (monitor != null)
				monitor.subTask("scanning " + file.getAbsolutePath());
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = subfiles).length;
			for (int i = 0; i < j; i++) {
				File subfile = arrayOfFile1[i];
				if (!subfile.isHidden()) {
					if (subfile.isDirectory()) {
						FtpUploadConf canFtpUploadConf = new FtpUploadConf();
						canFtpUploadConf.setClientPath(subfile.getAbsolutePath());
						canFtpUploadConf.setServerPath(ftpUploadConf.getServerPath() + subfile.getName() + "/");
						canFtpUploadConf.setExcludes(ftpUploadConf.getExcludes());
						canFtpUploadConf.setIncludes(ftpUploadConf.getIncludes());

						if (StringUtil.fileStyleEIMatch(subfile.getAbsolutePath(), ftpUploadConf.getExcludes(),
								ftpUploadConf.getIncludes())) {

							AbstractSexftpView.TreeObject exists = existFtpuploadPro(p,
									canFtpUploadConf.getClientPath());
							AbstractSexftpView.TreeParent newP = new AbstractSexftpView.TreeParent(getFileInfo(subfile),
									new FtpUploadPro(canFtpUploadConf, ftpConf));
							if (exists != null) {
								newP = (AbstractSexftpView.TreeParent) exists;
							} else {
								p.addChild(newP);
							}
							if (includeChild) {
								if ((onlyPath == null) || (onlyPath.startsWith(subfile.getAbsolutePath()))) {
									addChildUploadPro(newP, monitor, includeChild, onlyPath);
								}
							}
						}
					} else {
						FtpUploadConf canFtpUploadConf = new FtpUploadConf();
						canFtpUploadConf.setClientPath(subfile.getAbsolutePath());
						canFtpUploadConf.setServerPath(ftpUploadConf.getServerPath());
						canFtpUploadConf.setExcludes(ftpUploadConf.getExcludes());
						canFtpUploadConf.setIncludes(ftpUploadConf.getIncludes());

						if (StringUtil.fileStyleEIMatch(subfile.getAbsolutePath(), ftpUploadConf.getExcludes(),
								ftpUploadConf.getIncludes())) {
							if (existFtpuploadPro(p, canFtpUploadConf.getClientPath()) == null) {
								AbstractSexftpView.TreeObject newP = new AbstractSexftpView.TreeObject(
										getFileInfo(subfile), new FtpUploadPro(canFtpUploadConf, ftpConf));
								p.addChild(newP);
							}
						}
					}
				}
			}
		}
	}

	private String getFileInfo(File file) {
		if (!file.isFile()) {
			return file.getName();
		}
		try {
			long size = file.length();
			return String.format("%s ( %s %s )", new Object[] { file.getName(), FileUtils.byteCountToDisplaySize(size),
					DateTimeUtils.format(new Date(file.lastModified())) });
		} catch (Exception e) {
			throw new SRuntimeException(e);
		}
	}

	private AbstractSexftpView.TreeObject existFtpuploadPro(AbstractSexftpView.TreeParent p, String clientPath) {
		AbstractSexftpView.TreeObject[] arrayOfTreeObject;
		int j = (arrayOfTreeObject = p.getChildren()).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpView.TreeObject to = arrayOfTreeObject[i];

			if ((to.getO() instanceof FtpUploadPro)) {
				FtpUploadPro fupro = (FtpUploadPro) to.getO();
				if (fupro.getFtpUploadConf().getClientPath().equals(clientPath)) {
					return to;
				}
			}
		}

		return null;
	}

	protected boolean okPopActionUpload() {
		Object[] os = getSelectionObjects();
		if (os.length == 0)
			return false;
		Object[] arrayOfObject1;
		int j = (arrayOfObject1 = os).length;
		for (int i = 0; i < j; i++) {
			Object o = arrayOfObject1[i];
			if (!(o instanceof FtpConf)) {

				if (!(o instanceof FtpUploadConf)) {

					if (!(o instanceof FtpUploadPro)) {

						return false;
					}
				}
			}
		}
		return true;
	}

	protected void actionUpload_actionPerformed() throws Exception {
		doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro() {
			public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
				SexftpLocalView.this.innerUpload_actionPerformed(selectOs);
			}
		});
	}

	public void innerUpload_actionPerformed(Object[] selectOs) {
		FtpConf ftpConf = null;
		final List<FtpUploadConf> okFtpUploadConfList = new ArrayList();
		Object[] arrayOfObject;
		int j = (arrayOfObject = selectOs).length;
		for (int i = 0; i < j; i++) {
			Object o = arrayOfObject[i];
			if ((o instanceof FtpUploadPro)) {
				FtpUploadPro ftpUploadPro = (FtpUploadPro) o;
				if ((ftpConf != null) && (!ftpConf.equals(ftpUploadPro.getFtpConf()))) {
					showMessage("Ftp Config Only Support One Upload At The Same Time,But you Chose More as :["
							+ ftpConf.getName() + "," + ftpUploadPro.getFtpConf().getName() + "]");
					return;
				}
				ftpConf = ftpUploadPro.getFtpConf();
				if (new File(ftpUploadPro.getFtpUploadConf().getClientPath()).isFile())
					okFtpUploadConfList.add(ftpUploadPro.getFtpUploadConf());
			}
		}
		if (okFtpUploadConfList.size() == 0) {
			showMessage(
					"Nothing For Upload,You Can Try Thes Options Before as :\r\nPrepare Modified Files  For Upload\r\nPrepare All Files For Upload");
			return;
		}
		final FtpConf ftpConfOk = ftpConf;
		int r = ((Integer) PluginUtil.runAsDisplayThread(new PluginUtil.RunAsDisplayThread() {
			public Object run() throws Exception {
				UploadConfirmDialog ul = new UploadConfirmDialog(SexftpLocalView.this.getShell(),
						"Upload To:[" + ftpConfOk.toString() + "]",
						"Confirm To Upload These [" + okFtpUploadConfList.size() + "] Files:", okFtpUploadConfList,
						ftpConfOk);
				int r = ul.open();
				return Integer.valueOf(r);
			}
		})).intValue();

		if (r == 0) {
			Object job = new SexftpJob("Uploading", this) {
				protected IStatus srun(final IProgressMonitor monitor) {
					try {
						monitor.beginTask("Uploading", okFtpUploadConfList.size());
						SexftpLocalView.this.openConsole();
						FtpUtil.executeUpload(ftpConfOk, okFtpUploadConfList, new IFtpStreamMonitor() {
							private FtpUploadConf curftpUploadConf;
							long calSize = 0L;
							long timsta = 0L;
							long speed = 0L;
							boolean smallLeftCompleted = true;
							boolean okCancel = false;

							public void printStreamString(FtpUploadConf ftpUploadConf, long uploadedSize,
									long totalSize, String info) {
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

								monitor.subTask(String.format("(%s in %s) %s \r\n uploading %s%s",
										new Object[] { FileUtils.byteCountToDisplaySize(uploadedSize),
												FileUtils.byteCountToDisplaySize(totalSize),
												(float) this.speed > 1.0E-4F
														? FileUtils.byteCountToDisplaySize(this.speed) + "/s" : "",
										this.curftpUploadConf.getServerPath(),
										new File(this.curftpUploadConf.getClientPath()).getName() }));

								this.okCancel = monitor.isCanceled();

								if (totalSize == uploadedSize) {
									monitor.worked(1);

								} else if ((this.okCancel) && (totalSize - uploadedSize < 102400L)) {
									if (this.smallLeftCompleted) {
										SexftpLocalView.this.console("Canncled But Go Ahead Little Left Files!");
										this.okCancel = false;
									}
								}

								if (this.okCancel) {
									SexftpLocalView.this.console("Operation Canceled!");
									SexftpLocalView.this
											.console(
													String.format("Last Uploaded %s Of %s .",
															new Object[] {
																	FileUtils.byteCountToDisplaySize(uploadedSize),
																	FileUtils.byteCountToDisplaySize(totalSize) }));
									if (totalSize > uploadedSize) {
										SexftpLocalView.this
												.console(String.format("Warning:Incomplete Upload %s%s %s of %s!",
														new Object[] { this.curftpUploadConf.getServerPath(),
																new File(this.curftpUploadConf.getClientPath())
																		.getName(),
														FileUtils.byteCountToDisplaySize(uploadedSize),
														FileUtils.byteCountToDisplaySize(totalSize) }));
									}
									throw new AbortException();
								}
							}

							public void printSimple(String info) {
								SexftpLocalView.this.console(info);
							}
						}, SexftpLocalView.this);
						if (!monitor.isCanceled()) {
							monitor.subTask("Upload Success!Now ReFormatting...");
							SexftpLocalView.this.console("Upload Success!Now ReFormatting...");
							String path = SexftpLocalView.workspacePath + ftpConfOk.getName();
							FtpUtil.formaterSel(SexftpLocalView.workspaceWkPath, path, okFtpUploadConfList);
							SexftpLocalView.this.console("Upload Task Finished!");
						}
					} catch (Exception e) {
						SexftpLocalView.this.handleException(e);
					}
					return Status.OK_STATUS;
				}

			};
			((Job) job).setUser(true);
			((Job) job).schedule();
		}
	}

	public void directTo(final String expandClientPath, Integer ftpUploadTreeNodesIndex) {
		AbstractSexftpView.TreeParent r = null;
		if (ftpUploadTreeNodesIndex != null) {
			r = getAllFtpUploadConfNodes()[ftpUploadTreeNodesIndex.intValue()];
		} else {
			r = getRoot();
		}
		TreeViewUtil.serchTreeData(r, new SearchCallback() {
			public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject o) {
				if ((o.getO() instanceof FtpUploadConf)) {
					String client = new File(((FtpUploadConf) o.getO()).getClientPath()).getAbsolutePath();
					if (expandClientPath.startsWith(client)) {
						((AbstractSexftpView.TreeParent) o).getChildren();

						SexftpLocalView.this.addChildUploadPro((AbstractSexftpView.TreeParent) o, null, true,
								new File(expandClientPath).getAbsolutePath());

						SexftpLocalView.this.directExpand(expandClientPath, o);

						throw new AbortException();
					}
				}
				return new TreeViewUtil.ThisYourFind(false, true);
			}
		});
	}

	private void directExpand(final String explandClientPath, final AbstractSexftpView.TreeObject to) {
		final AbstractSexftpView.TreeObject serchto = TreeViewUtil.serchTreeData(to, new SearchCallback() {
			public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject newo) {
				if ((newo.getO() instanceof FtpUploadPro)) {
					String sclient = ((FtpUploadPro) newo.getO()).getFtpUploadConf().getClientPath();
					String dclient = explandClientPath;
					sclient = new File(sclient).getAbsolutePath();
					dclient = new File(dclient).getAbsolutePath();
					if (sclient.equals(dclient)) {
						return new TreeViewUtil.ThisYourFind(true, false);
					}
					if (!dclient.startsWith(sclient)) {
						return new TreeViewUtil.ThisYourFind(false, false);
					}

					return new TreeViewUtil.ThisYourFind(false, true);
				}

				return new TreeViewUtil.ThisYourFind(false, true);

			}

		});
		new Thread(new SexftpRun(this) {
			public void srun() throws Exception {
				Thread.sleep(100L);
				Display.getDefault().asyncExec(new SexftpRun(SexftpLocalView.this) {
					public void srun() throws Exception {
						if (serchto != null) {
							SexftpLocalView.this.viewer.refresh();
							TreePath tpath = TreeViewUtil.changeTreePath(serchto);
							TreeSelection t = new TreeSelection(tpath);
							SexftpLocalView.this.viewer.setSelection(t);
							SexftpLocalView.this.console("Direct To Ok:" + serchto);

						} else {
							TreePath tpath = TreeViewUtil.changeTreePath(to);
							TreeSelection t = new TreeSelection(tpath);
							SexftpLocalView.this.viewer.setSelection(t);
						}
					}
				});
			}
		})

		.start();
	}

	protected String getDefaultPathToLocation(Object selectO) {
		if ((selectO instanceof FtpUploadPro)) {
			return ((FtpUploadPro) selectO).getFtpUploadConf().getClientPath();
		}
		return super.getDefaultPathToLocation(selectO);
	}
}
