package org.sexftp.core.ftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.sexftp.core.exceptions.FtpNoSuchFileException;
import org.sexftp.core.exceptions.SRuntimeException;
import org.sexftp.core.ftp.bean.FtpFile;
import sexftp.uils.PluginUtil;
import sexftp.views.IFtpStreamMonitor;

public class MySFTP implements XFtp {
	private ChannelSftp channelSftp;
	private String host;
	private int port;
	private String username;
	private String password;
	private String encode = "gbk";

	public void connect() {
		ChannelSftp sftp = null;
		try {
			int serverTimeout = PluginUtil.getServerTimeout();
			JSch jsch = new JSch();

			Session sshSession = jsch.getSession(this.username, this.host, this.port);
			sshSession.setPassword(this.password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setTimeout(serverTimeout);
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (Exception e) {
			throw new SRuntimeException(String.format("%s \r\n using %s : %s",
					new Object[] { e.getMessage(), this.username, this.password }), e);
		}
		this.channelSftp = sftp;
	}

	public void prepareConnect(String host, int port, String username, String password, String encode) {
		this.host = host;
		this.port = port;
		this.password = password;
		this.username = username;
		this.encode = (encode != null ? encode : "gbk");
	}

	public void disconnect() {
		this.channelSftp.disconnect();
		this.channelSftp.exit();
	}

	public void cd(String directory) {
		ChannelSftp sftp = this.channelSftp;
		try {
			sftp.cd(directory);
		} catch (SftpException e) {
			if ((e.getMessage() != null) && (e.getMessage().toLowerCase().startsWith("No such file".toLowerCase()))) {
				throw new FtpNoSuchFileException(e.toString());
			}
			if (e.id == 2) {
				throw new FtpNoSuchFileException(e.toString());
			}

			throw new RuntimeException(directory + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException(directory + e.getMessage(), e);
		}
	}

	public void cdOrMakeIfNotExists(String directory) {
		ChannelSftp sftp = this.channelSftp;
		try {
			sftp.cd(directory);
		} catch (Exception localException1) {
			try {
				mkdirs(directory);
			} catch (Exception e2) {
				throw new RuntimeException(directory, e2);
			}
		}
	}

	private void mkdirs(String directory) throws Exception {
		ChannelSftp sftp = this.channelSftp;
		String[] dirs = directory.split("/");
		if (directory.startsWith("/")) {
			sftp.cd("/");
		}
		String[] arrayOfString1;
		int j = (arrayOfString1 = dirs).length;
		for (int i = 0; i < j; i++) {
			String dir = arrayOfString1[i];
			if (dir.length() > 0) {
				try {
					sftp.cd(dir);
				} catch (Exception localException1) {
					try {
						sftp.mkdir(dir);
						sftp.cd(dir);
					} catch (Exception e1) {
						throw new SRuntimeException("Make directory " + directory + " Failed! (" + dir + ")", e1);
					}
				}
			}
		}
	}

	/* Error */
	public void upload(String uploadFile, final IFtpStreamMonitor monitor) {
		// Byte code:
		// 0: aload_0
		// 1: getfield 108 org/sexftp/core/ftp/MySFTP:channelSftp
		// Lcom/jcraft/jsch/ChannelSftp;
		// 4: astore_3
		// 5: new 201 java/io/File
		// 8: dup
		// 9: aload_1
		// 10: invokespecial 203 java/io/File:<init> (Ljava/lang/String;)V
		// 13: astore 4
		// 15: aconst_null
		// 16: astore 5
		// 18: new 204 java/io/FileInputStream
		// 21: dup
		// 22: aload 4
		// 24: invokespecial 206 java/io/FileInputStream:<init>
		// (Ljava/io/File;)V
		// 27: astore 5
		// 29: aload 4
		// 31: invokevirtual 209 java/io/File:length ()J
		// 34: lstore 6
		// 36: aload 5
		// 38: invokevirtual 212 java/io/FileInputStream:close ()V
		// 41: new 215 org/sexftp/core/ftp/MySFTP$1
		// 44: dup
		// 45: aload_0
		// 46: aload 4
		// 48: aload_2
		// 49: lload 6
		// 51: invokespecial 217 org/sexftp/core/ftp/MySFTP$1:<init>
		// (Lorg/sexftp/core/ftp/MySFTP;Ljava/io/File;Lsexftp/views/IFtpStreamMonitor;J)V
		// 54: astore 8
		// 56: aload 8
		// 58: astore 5
		// 60: aload_3
		// 61: aload 8
		// 63: aload 4
		// 65: invokevirtual 220 java/io/File:getName ()Ljava/lang/String;
		// 68: invokevirtual 223 com/jcraft/jsch/ChannelSftp:put
		// (Ljava/io/InputStream;Ljava/lang/String;)V
		// 71: goto +54 -> 125
		// 74: astore 6
		// 76: aload 6
		// 78: athrow
		// 79: astore 6
		// 81: aload 6
		// 83: invokevirtual 226 java/lang/Exception:getCause
		// ()Ljava/lang/Throwable;
		// 86: instanceof 230
		// 89: ifeq +12 -> 101
		// 92: aload 6
		// 94: invokevirtual 226 java/lang/Exception:getCause
		// ()Ljava/lang/Throwable;
		// 97: checkcast 230 org/sexftp/core/exceptions/AbortException
		// 100: athrow
		// 101: new 154 java/lang/RuntimeException
		// 104: dup
		// 105: aload 6
		// 107: invokespecial 232 java/lang/RuntimeException:<init>
		// (Ljava/lang/Throwable;)V
		// 110: athrow
		// 111: astore 9
		// 113: aload 5
		// 115: invokevirtual 212 java/io/FileInputStream:close ()V
		// 118: goto +4 -> 122
		// 121: pop
		// 122: aload 9
		// 124: athrow
		// 125: aload 5
		// 127: invokevirtual 212 java/io/FileInputStream:close ()V
		// 130: goto +4 -> 134
		// 133: pop
		// 134: aload_2
		// 135: new 156 java/lang/StringBuilder
		// 138: dup
		// 139: ldc -21
		// 141: invokespecial 162 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 144: new 201 java/io/File
		// 147: dup
		// 148: aload_1
		// 149: invokespecial 203 java/io/File:<init> (Ljava/lang/String;)V
		// 152: invokevirtual 220 java/io/File:getName ()Ljava/lang/String;
		// 155: invokevirtual 163 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 158: invokevirtual 167 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 161: invokeinterface 237 2 0
		// 166: return
		// Line number table:
		// Java source line #150 -> byte code offset #0
		// Java source line #151 -> byte code offset #5
		// Java source line #152 -> byte code offset #15
		// Java source line #154 -> byte code offset #18
		// Java source line #155 -> byte code offset #29
		// Java source line #156 -> byte code offset #36
		// Java source line #157 -> byte code offset #41
		// Java source line #182 -> byte code offset #56
		// Java source line #183 -> byte code offset #60
		// Java source line #185 -> byte code offset #74
		// Java source line #186 -> byte code offset #76
		// Java source line #188 -> byte code offset #79
		// Java source line #189 -> byte code offset #81
		// Java source line #191 -> byte code offset #92
		// Java source line #193 -> byte code offset #101
		// Java source line #195 -> byte code offset #111
		// Java source line #197 -> byte code offset #113
		// Java source line #198 -> byte code offset #121
		// Java source line #200 -> byte code offset #122
		// Java source line #197 -> byte code offset #125
		// Java source line #198 -> byte code offset #133
		// Java source line #201 -> byte code offset #134
		// Java source line #203 -> byte code offset #166
		// Local variable table:
		// start length slot name signature
		// 0 167 0 this MySFTP
		// 0 167 1 uploadFile String
		// 0 167 2 monitor IFtpStreamMonitor
		// 4 57 3 sftp ChannelSftp
		// 13 51 4 file File
		// 16 110 5 useIns FileInputStream
		// 34 16 6 avili long
		// 74 3 6 e org.sexftp.core.exceptions.AbortException
		// 79 27 6 e Exception
		// 54 8 8 ins FileInputStream
		// 111 12 9 localObject Object
		// 121 1 11 localException1 Exception
		// 133 1 12 localException2 Exception
		// Exception table:
		// from to target type
		// 18 71 74 org/sexftp/core/exceptions/AbortException
		// 18 71 79 java/lang/Exception
		// 18 111 111 finally
		// 113 118 121 java/lang/Exception
		// 125 130 133 java/lang/Exception
	}

	public void download(String downloadFile, String saveFile, final IFtpStreamMonitor monitor) {
		ChannelSftp sftp = this.channelSftp;
		try {
			File file = new File(saveFile);
			String exists = file.exists() ? " (overwrite exists:" + file.getName() + ")" : "";
			FileOutputStream fos = new FileOutputStream(file) {
				int t = 0;

				public void write(byte[] b, int off, int len) throws IOException {
					if (len >= 0) {
						this.t += len;
						monitor.printStreamString(null, this.t, 0L, "");
					}
					super.write(b, off, len);
				}
			};
			sftp.get(downloadFile, fos);
			monitor.printSimple("      download success :" + new File(downloadFile).getName() + exists);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(String deleteFile) {
		ChannelSftp sftp = this.channelSftp;
		try {
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<FtpFile> listFiles() {
		List<FtpFile> listfiles = new ArrayList();
		ChannelSftp sftp = this.channelSftp;
		try {
			Vector ls = sftp.ls(sftp.pwd());
			for (Object f : ls) {
				ChannelSftp.LsEntry lsentry = (ChannelSftp.LsEntry) f;
				if ((!lsentry.getFilename().equals(".")) && (!lsentry.getFilename().equals(".."))) {
					StringTokenizer s = new StringTokenizer(lsentry.getLongname(), "\t ");
					boolean isFolder = false;
					while (s.hasMoreElements()) {
						String i = (String) s.nextElement();
						try {
							Integer valueOf = Integer.valueOf(i);
							isFolder = valueOf.intValue() != 1;
						} catch (NumberFormatException localNumberFormatException) {
						}
					}

					Calendar instance = Calendar.getInstance();

					instance.setTime(new Date(lsentry.getAttrs().getMTime() * 1000L));
					FtpFile file = new FtpFile(lsentry.getFilename(), isFolder, lsentry.getAttrs().getSize(), instance);
					listfiles.add(file);
				}
			}
			return listfiles;
		} catch (SftpException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		String[] arrayOfString = args;
		int j = args.length;
		for (int i = 0; i < j; i++) {
			String arg = arrayOfString[i];
			System.out.println(arg);
		}

		XFtp sf = new MySFTP();
		String host = args[1];
		String port = args[2];
		String username = args[3];
		String password = args[4];
		String directory = args[5];

		sf.prepareConnect(host, new Integer(port).intValue(), username, password, null);
		sf.cdOrMakeIfNotExists(directory);
		try {
			sf.disconnect();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConnect() {
		return (this.channelSftp.isConnected()) && (!this.channelSftp.isClosed()) && (!this.channelSftp.isEOF());
	}
}
