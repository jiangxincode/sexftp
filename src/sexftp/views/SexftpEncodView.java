package sexftp.views;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ide.IDE;
import org.sexftp.core.exceptions.AbortException;
import org.sexftp.core.exceptions.BizException;
import org.sexftp.core.ftp.bean.FtpUploadConf;
import org.sexftp.core.ftp.bean.FtpUploadPro;
import org.sexftp.core.utils.ByteUtils;
import org.sexftp.core.utils.Cpdetector;
import org.sexftp.core.utils.FileUtil;
import org.sexftp.core.utils.StringUtil;
import sexftp.SexftpJob;
import sexftp.SexftpRun;
import sexftp.SrcViewable;
import sexftp.uils.PluginUtil;
import sexftp.uils.PluginUtil.RunAsDisplayThread;

public class SexftpEncodView extends AbstractSexftpEncodView {
	private String getProjectPath(IProject prject) {
		if (prject == null)
			return null;
		return prject.getFile("a.text").getLocation().toFile().getParentFile().getAbsolutePath();
	}

	private static Set<String> innerTextSet = new HashSet();

	static {
		innerTextSet.addAll(Arrays.asList(new String[] { ".java", ".txt", ".jsp", ".htm", ".html", ".shtm", ".shtml",
				".asp", ".php", ".aspx", ".properties", ".bat", ".sh", ".css", ".js", ".sql" }));
	}

	private boolean isText(File sfile, IProject iproject) throws CoreException {
		int dotindex = sfile.getName().lastIndexOf(".");
		if (dotindex < 0)
			return false;
		String substring = sfile.getName().substring(dotindex);
		if (innerTextSet.contains(substring)) {
			return true;
		}
		IFile ifiletype = iproject.getFile("/types/t" + substring);
		File ftp = ifiletype.getLocation().toFile().getParentFile();
		File ft = ifiletype.getLocation().toFile();
		if (!ftp.exists()) {
			ftp.mkdirs();
		}

		if (!ft.exists()) {
			FileUtil.writeByte2File(ft.getAbsolutePath(), new byte[1]);
			ifiletype.refreshLocal(1, null);
		}

		IContentType contentType = IDE.getContentType(ifiletype);
		if ((contentType != null) && (contentType.getBaseType() != null)) {
			String basename = contentType.getBaseType().getName();
			if ("text".equalsIgnoreCase(basename)) {
				return true;
			}
		}
		return false;
	}

	private String asssetToString(Set<String> fileassoSet) {
		StringBuffer sb = new StringBuffer();
		for (String ass : fileassoSet) {
			if (ass.trim().length() > 0)
				sb.append("*" + ass.trim());
			sb.append("\r\n");
		}
		return sb.toString().trim();
	}

	private void stringToasset(String assos, Set<String> fileassoSet) {
		fileassoSet.clear();
		String[] arrayOfString;
		int j = (arrayOfString = assos.split("\n")).length;
		for (int i = 0; i < j; i++) {
			String ass = arrayOfString[i];

			if (ass.trim().length() > 0)
				fileassoSet.add(ass.trim());
		}
	}

	Set<String> fileassoSet = null;
	String path = null;

	public void checkAndView(String path) {
		checkAndView(path, null);
	}

	public void checkAndView(String path, final Set<String> deffileassoSet) {
		this.path = path;
		final List<AbstractSexftpEncodView.CorCod> corcodeList = new ArrayList();
		final File file = new File(path);
		final IProject iproject = PluginUtil.createSexftpIFileFromPath("/filetype/a.t").getProject();

		final List<File> fileList = new ArrayList();

		Job job = new SexftpJob("Anyasising", this) {
			protected IStatus srun(IProgressMonitor monitor) throws Exception {
				SexftpEncodView.this.viewSubFile(file, fileList, monitor);
				monitor.beginTask("Anyasising", fileList.size());
				final Set<String> fileassoSet = deffileassoSet != null ? deffileassoSet : new LinkedHashSet();
				fileassoSet.add(".txt");
				fileassoSet.add(".java");
				final Set<String> ignorefileassoSet = new LinkedHashSet();

				if (fileList.size() == 1) {
					fileassoSet.add(((File) fileList.get(0)).getName());
				} else if ((fileList.size() > 1) && (deffileassoSet == null)) {
					String asso;
					for (File sfile : fileList) {
						if (monitor.isCanceled())
							throw new AbortException();
						monitor.subTask("Anyasising " + sfile.getAbsolutePath());
						int index = sfile.getName().lastIndexOf(".");
						asso = index >= 0 ? sfile.getName().substring(index) : "";
						if (asso.length() != 0) {
							boolean istext = SexftpEncodView.this.isText(sfile, iproject);
							if (istext) {
								fileassoSet.add(asso);
							} else {
								ignorefileassoSet.add(asso);
							}
						}
					}

					// int index = (ass =
					// (String[])ignorefileassoSet.toArray(new
					// String[0])).length;
					int index = 0;
					for (int i = 0; i < index; i++) {
						// String ass = asso[i];
						String ass = null;

						IFile ifiletype = iproject.getFile("/types/t" + ass);
						File ftp = ifiletype.getLocation().toFile().getParentFile();
						File ft = ifiletype.getLocation().toFile();
						if (!ftp.exists()) {
							ftp.mkdirs();
						}

						if (!ft.exists()) {
							FileUtil.writeByte2File(ft.getAbsolutePath(), new byte[1]);
							ifiletype.refreshLocal(1, null);
						}
						IContentType contentType = IDE.getContentType(ifiletype);
						if (contentType != null) {
							ignorefileassoSet.remove(ass);
							ignorefileassoSet.add(ass);
						}
					}
					PluginUtil.runAsDisplayThread(new PluginUtil.RunAsDisplayThread() {
						public Object run() throws Exception {
							ConfirmSexftpEncoderDialog d = new ConfirmSexftpEncoderDialog(
									SexftpEncodView.this.viewer.getTree().getShell(), file.getAbsolutePath(),
									SexftpEncodView.this.asssetToString(fileassoSet),
									SexftpEncodView.this.asssetToString(ignorefileassoSet));
							int r = d.open();
							if (r != 0)
								throw new AbortException();
							SexftpEncodView.this.stringToasset(d.getFileAssociations(), fileassoSet);
							SexftpEncodView.this.stringToasset(d.getFileNoAssociations(), ignorefileassoSet);
							return null;
						}
					});
				}
				SexftpEncodView.this.fileassoSet = fileassoSet;
				for (File sfile : fileList) {
					monitor.subTask("Anyasising " + sfile.getAbsolutePath());
					AbstractSexftpEncodView.CorCod corCod = new AbstractSexftpEncodView.CorCod("");
					corCod.setParentFolder(file.getAbsolutePath());
					corCod.setEndexten(sfile.getAbsolutePath());

					boolean oktext = StringUtil.fileStyleMatch(sfile.getName(),
							(String[]) fileassoSet.toArray(new String[0]));
					if (oktext) {

						String encode = null;
						FileInputStream ascfilinput = new FileInputStream(sfile);

						if (sfile.length() < 10000000L) {
							byte[] ascdatas = FileUtil.readBytesFromInStream(ascfilinput);
							String isallasc = Cpdetector.isOnlyASC(ascdatas);
							if (isallasc.length() > 0) {
								if (isallasc.equals("US-ASCII")) {
									encode = "US-ASCII";
									corCod.setChengeDes("ASCII Text File Do not Need Change Charset Encoding.");
									corCod.setDescIcon("stop.gif");
								} else if (isallasc.startsWith("US-ASCII_")) {
									encode = isallasc.replace("US-ASCII_", "");
									corCod.setChengeDes("ASCII File,Only Use SBC case!");
									corCod.setDescIcon("hprio_tsk.gif");
								}
							}
							if (encode == null) {
								Charset c = Cpdetector.encode(new FileInputStream(sfile));
								encode = c != null ? c.toString() : null;
							}

							if ((encode != null) && (!"void".equals(encode))) {
								if (encode.startsWith("UTF-16")) {
									corCod.setChengeDes("You Must Check Samples!");
									corCod.setDescIcon("hprio_tsk.gif");
								}
								byte[] newdata = Cpdetector.delASCIIdata(ascdatas);
								if (("x-EUC-TW".equalsIgnoreCase(encode)) || ("windows-1252".equalsIgnoreCase(encode))
										|| ("EUC-KR".equalsIgnoreCase(encode))) {
									Charset c = Cpdetector.encode(new ByteArrayInputStream(newdata));
									String newcode = c != null ? c.toString() : null;
									if (newcode != null) {
										if (newcode.startsWith("GB")) {
											corCod.setChengeDes(
													"Need Check [" + newcode + " or " + encode + "]  Samples");
											encode = newcode;
										} else {
											encode = "GB18030";
											corCod.setChengeDes("We Just Guess,You Must Check  Samples!");
										}
										corCod.setDescIcon("hprio_tsk.gif");
									}
								}

								corCod.setFileencode(encode);
								String sample = Cpdetector.onlyNoneASCII(new String(ascdatas, encode));

								if (sample.length() > 50) {
									sample = sample.substring(0, 50) + "...";
								}
								corCod.setSamples(sample);
							} else {
								corCod.setFileencode("<Unkown>");
								corCod.setDescIcon("hprio_tsk.gif");
							}
						} else {
							corCod.setFileencode("File Too Large!");
							corCod.setEclipseeditorencode("File Too Large!");
							corCod.setDescIcon("stop.gif");
							continue;
						}

					} else {
						corCod.setFileencode("");
						corCod.setDescIcon("hprio_tsk.gif");
						continue;
					}

					corCod.setOldfileencode(corCod.getFileencode());
					corcodeList.add(corCod);
					monitor.worked(1);
				}
				SexftpEncodView.this.refreshData(SexftpEncodView.this.comin(corcodeList));
				SexftpEncodView.this.applyChanges.setEnabled(true);

				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	private List<AbstractSexftpEncodView.ParentCorCod> comin(List<AbstractSexftpEncodView.CorCod> corcodeList) {
		Map<AbstractSexftpEncodView.ParentCorCod, AbstractSexftpEncodView.ParentCorCod> okMap = new HashMap();
		for (AbstractSexftpEncodView.CorCod corCod : corcodeList) {
			String path = corCod.getEndexten();
			if (path.endsWith("odi-table.js")) {
				System.out.println("");
			}
			path = path.replace('\\', '/');
			int findex = path.lastIndexOf("/");
			int ptIndex = path.lastIndexOf(".");
			if (ptIndex > findex) {
				AbstractSexftpEncodView.ParentCorCod key = new AbstractSexftpEncodView.ParentCorCod("");
				key.setEndexten("*" + path.substring(ptIndex));
				key.setFileencode(corCod.getFileencode());
				key.setEclipseeditorencode(corCod.getEclipseeditorencode());
				key.setParentFolder(corCod.getParentFolder());
				key.setOldfileencode(corCod.getOldfileencode());
				key.setChengeDes(corCod.getChengeDes());
				key.setDescIcon(corCod.getDescIcon());
				if (!okMap.containsKey(key)) {

					okMap.put(key, key);
				}

				hindex(hash(key.hashCode()), 16);
				((AbstractSexftpEncodView.ParentCorCod) okMap.get(key)).addChild(corCod);
			}
		}

		for (AbstractSexftpEncodView.ParentCorCod p : okMap.keySet()) {
			int len = p.getChildren().length;
			int index = new Random().nextInt(len);
			p.setSamples(p.getChildren()[index].getSamples());
		}
		return new ArrayList(okMap.keySet());
	}

	private int hash(int h) {
		h ^= h >>> 20 ^ h >>> 12;
		return h ^ h >>> 7 ^ h >>> 4;
	}

	private int hindex(int h, int length) {
		return h & length - 1;
	}

	private void viewSubFile(File file, List<File> fileList, IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			throw new AbortException();
		}
		if (file.isDirectory()) {
			monitor.subTask("Scaning.. " + file.getAbsolutePath());
			File[] arrayOfFile;
			int j = (arrayOfFile = file.listFiles()).length;
			for (int i = 0; i < j; i++) {
				File subfile = arrayOfFile[i];

				viewSubFile(subfile, fileList, monitor);
			}
		} else if (file.isFile()) {
			fileList.add(file);
		}
	}

	protected void oldEncodeChanged(AbstractSexftpEncodView.CorCod c, String encode, final String newencode) {
		final AbstractSexftpEncodView.ParentCorCod p = (AbstractSexftpEncodView.ParentCorCod) c;
		if (!encode.equals(newencode)) {
			if (showQuestion("Refresh Samples Use [" + newencode + "]?")) {
				Job job = new SexftpJob("Refreshing", this) {
					protected IStatus srun(IProgressMonitor monitor) throws Exception {
						monitor.beginTask("Refreshing", p.getChildren().length);
						AbstractSexftpEncodView.CorCod[] arrayOfCorCod;
						int j = (arrayOfCorCod = p.getChildren()).length;
						for (int i = 0; i < j; i++) {
							AbstractSexftpEncodView.CorCod co = arrayOfCorCod[i];

							monitor.subTask("refresh of " + co.getEndexten());
							String sample = Cpdetector
									.onlyNoneASCII(FileUtil.getTextFromFile(co.getEndexten(), newencode));
							if (sample.length() > 10) {
								sample = sample.substring(0, 10) + "...";
							}
							co.setSamples(sample);
							monitor.worked(1);
						}
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
		}
	}

	protected void applyChanges_actionPerformed() throws Exception {
		getActPage();
		applyEncodeChange();
	}

	public void applyEncodeChange() {
		Job job = new SexftpJob("Anyasising", this) {
			protected IStatus srun(IProgressMonitor monitor) throws Exception {
				int total = 0;
				for (AbstractSexftpEncodView.ParentCorCod c : SexftpEncodView.this.corcodeList) {

					if (!c.getOldfileencode().equals(c.getFileencode())) {
						try {
							"test".getBytes(c.getOldfileencode());
							"test".getBytes(c.getFileencode());
						} catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
							continue;
						}
						if (c.getFileencode().indexOf("ASCII") >= 0) {
							throw new BizException("Error:[" + c.getEndexten() + "] Cann't Changed To ASCII!");
						}

						total += c.getChildren().length;
					}
				}
				if (total == 0) {
					throw new BizException("No Files To Change Encoding!");
				}
				monitor.beginTask("Change Encoding...", total);
				boolean changed = false;
				for (AbstractSexftpEncodView.ParentCorCod c : SexftpEncodView.this.corcodeList) {
					if (!c.getOldfileencode().equals(c.getFileencode())) {
						try {
							"test".getBytes(c.getOldfileencode());
							"test".getBytes(c.getFileencode());
						} catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
							continue;
						}
						AbstractSexftpEncodView.CorCod[] arrayOfCorCod;
						int j = (arrayOfCorCod = c.getChildren()).length;
						for (int i = 0; i < j; i++) {
							AbstractSexftpEncodView.CorCod detCod = arrayOfCorCod[i];

							Cpdetector.encode(new FileInputStream(detCod.getEndexten()));

							String subtask = String.format("Change Encoding From %s to %s :%s",
									new Object[] { c.getOldfileencode(), c.getFileencode(), detCod.getEndexten() });
							monitor.subTask(subtask);
							SexftpEncodView.this.console(subtask);
							String text = FileUtil.getTextFromFile(detCod.getEndexten(), c.getOldfileencode());
							ByteUtils.writeByte2Stream(text.getBytes(c.getFileencode()),
									new FileOutputStream(detCod.getEndexten()));
							monitor.worked(1);
							changed = true;
						}
					}
				}
				if (changed) {
					SexftpEncodView.this.showMessage("[" + total + "] Files Charset Encoding Changed Success!");
					final Object fs = SexftpEncodView.this.fileassoSet;
					final String npath = SexftpEncodView.this.path;
					Display.getDefault().asyncExec(new SexftpRun(SexftpEncodView.this) {
						public void srun() throws Exception {
							checkAndView(npath, (Set) fs);
						}
					});
					SexftpEncodView.this.applyChanges.setEnabled(false);

				} else {
					SexftpEncodView.this.showMessage("No Changed!");
				}
				SexftpEncodView.this.fileassoSet = null;
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
	}

	protected void doubleClick_actionPerformed() throws Exception {
		ISelection selection = this.viewer.getSelection();
		IStructuredSelection selection2 = (IStructuredSelection) selection;

		Object obj = selection2.getFirstElement();
		if (!(obj instanceof AbstractSexftpEncodView.ParentCorCod)) {

			FtpUploadConf fu = new FtpUploadConf();
			fu.setClientPath(((AbstractSexftpEncodView.CorCod) obj).getEndexten());
			FtpUploadPro fpro = new FtpUploadPro(fu, null);
			PluginUtil.findLocalView(getActPage()).innerEditLocalFile(fpro);
		}
	}

	protected void actionCopyQualifiedName_actionPerformed() throws Exception {
		AbstractSexftpEncodView.CorCod[] objes = getSelectionObjects();
		StringBuffer sb = new StringBuffer();
		AbstractSexftpEncodView.CorCod[] arrayOfCorCod1;
		int j = (arrayOfCorCod1 = objes).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpEncodView.CorCod o = arrayOfCorCod1[i];
			if ((o instanceof AbstractSexftpEncodView.ParentCorCod)) {
				AbstractSexftpEncodView.ParentCorCod po = (AbstractSexftpEncodView.ParentCorCod) o;
				AbstractSexftpEncodView.CorCod[] arrayOfCorCod2;
				int m = (arrayOfCorCod2 = po.getChildren()).length;
				for (int k = 0; k < m; k++) {
					AbstractSexftpEncodView.CorCod corCod = arrayOfCorCod2[k];
					sb.append(corCod);
					sb.append("\r\n");
				}
			}
		}
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Object tText = new StringSelection(sb.toString().trim());
		systemClipboard.setContents((Transferable) tText, null);
	}

	protected void actionCopy_actionPerformed() throws Exception {
		AbstractSexftpEncodView.CorCod[] selectionObjects = getSelectionObjects();
		StringBuffer sb = new StringBuffer();
		AbstractSexftpEncodView.CorCod[] arrayOfCorCod1;
		int j = (arrayOfCorCod1 = selectionObjects).length;
		for (int i = 0; i < j; i++) {
			AbstractSexftpEncodView.CorCod corCod = arrayOfCorCod1[i];
			sb.append(corCod);
			sb.append("\r\n");
		}
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Object tText = new StringSelection(sb.toString().trim());
		systemClipboard.setContents((Transferable) tText, null);
	}

	protected void filedillAction_actionPerformed() throws Exception {
		DirectoryDialog d = new DirectoryDialog(this.viewer.getControl().getShell());
		d.open();
		String filterPath = d.getFilterPath();
		if ((filterPath != null) && (filterPath.length() > 0)) {
			checkAndView(filterPath);
		}
	}

	protected AbstractSexftpEncodView.CorCod[] getSelectionObjects() {
		List<AbstractSexftpEncodView.CorCod> r = new ArrayList();
		ISelection selection = this.viewer.getSelection();
		Iterator<AbstractSexftpEncodView.CorCod> it = ((IStructuredSelection) selection).iterator();
		while (it.hasNext()) {
			AbstractSexftpEncodView.CorCod o = (AbstractSexftpEncodView.CorCod) it.next();
			r.add(o);
		}

		return (AbstractSexftpEncodView.CorCod[]) r.toArray(new AbstractSexftpEncodView.CorCod[0]);
	}
}
