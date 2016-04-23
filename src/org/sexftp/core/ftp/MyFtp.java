package org.sexftp.core.ftp;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.sexftp.core.exceptions.FtpCDFailedException;
import org.sexftp.core.exceptions.FtpConnectClosedException;
import org.sexftp.core.exceptions.FtpIOException;
import org.sexftp.core.exceptions.FtpNoSuchFileException;
import org.sexftp.core.ftp.bean.FtpFile;
import org.sexftp.core.utils.StringUtil;
import sexftp.uils.FtpExchangeOutputStream;
import sexftp.uils.LogUtil;
import sexftp.uils.PluginUtil;

public class MyFtp implements XFtp {
	FtpExchangeOutputStream ftpExchangeOutputStream = null;
	private FTPClient ftpclient = null;
	private String host;
	private int port;
	private String username;
	private String password;

	public MyFtp() {
		this.ftpclient = myInstance();
	}

	protected FTPClient myInstance() {
		return new FTPClient();
	}

	public void cd(String directory) {
		try {
			this.ftpExchangeOutputStream.setListenStr(new StringBuffer());
			boolean isOk = this.ftpclient.changeWorkingDirectory(iso88591(directory));
			if (!isOk) {
				String reseason = this.ftpExchangeOutputStream.getListenStr().toString();
				this.ftpExchangeOutputStream.setListenStr(null);
				if (reseason.toLowerCase().indexOf("No such file or directory".toLowerCase()) >= 0) {
					throw new FtpNoSuchFileException(reseason);
				}
				if (reseason.toLowerCase().indexOf("550".toLowerCase()) > 0) {
					throw new FtpNoSuchFileException(reseason);
				}

				throw new FtpCDFailedException(directory + " change failed." + reseason);
			}
		} catch (FTPConnectionClosedException e) {
			throw new FtpConnectClosedException(e.getMessage(), e);
		} catch (IOException e) {
			throw new FtpIOException("cd " + directory + " error!" + e.getMessage(), e);
		}
	}

	public void cdOrMakeIfNotExists(String directory) {
		try {
			if (directory.length() != 0) {
				cd(directory);
			}
		} catch (FtpNoSuchFileException localFtpNoSuchFileException) {
			try {
				mkdirs(directory);
			} catch (IOException e1) {
				throw new FtpIOException("mkdirs " + directory + " fail!" + e1, e1);
			}
		}
	}

	private void mkdirs(String directory) throws IOException {
		String[] dirs = directory.split("/");

		List<String> plist = new ArrayList();
		for (int i = 0; i < dirs.length; i++) {
			String path = "/";
			for (int j = 0; j <= i; j++) {
				if (dirs[j].length() != 0)
					path = path + dirs[j] + "/";
			}
			plist.add(path);
		}

		int i = 0;
		for (i = plist.size() - 1; i >= 0; i--) {
			try {
				cd((String) plist.get(i));
			} catch (FtpNoSuchFileException localFtpNoSuchFileException) {
			}
		}

		i++;
		for (; i < plist.size(); i++) {
			String dir = (String) plist.get(i);
			this.ftpclient.makeDirectory(iso88591(dir));
			cd(dir);
		}
	}

	private String iso88591(String str) {
		return StringUtil.iso88591(str, this.encode);
	}

	private String gbfrom88591(String str) {
		try {
			return new String(str.getBytes("iso-8859-1"), this.encode);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public void connect() {
		try {
			int serverTimeout = PluginUtil.getServerTimeout();
			this.ftpclient.setDefaultTimeout(serverTimeout);
			this.ftpclient.setConnectTimeout(serverTimeout);
			this.ftpclient.setDataTimeout(serverTimeout);
			this.ftpExchangeOutputStream = new FtpExchangeOutputStream(LogUtil.initSexftpChangeConsole(), this.encode);
			this.ftpExchangeOutputStream.setPreStr(
					String.format("%s:%d@%s", new Object[] { this.host, Integer.valueOf(this.port), this.username }));
			this.ftpclient.addProtocolCommandListener(new PrintCommandListener(this.ftpExchangeOutputStream, true));
			this.ftpclient.connect(this.host, this.port);
			if (!FTPReply.isPositiveCompletion(this.ftpclient.getReplyCode())) {
				this.ftpclient.disconnect();
				return;
			}
			boolean loginSuc = this.ftpclient.login(this.username, this.password);
			if (!loginSuc) {
				throw new FtpIOException(
						String.format("Login Failed Using %s : %s!", new Object[] { this.username, this.password }));
			}
			this.ftpclient.setFileType(2);
			this.ftpclient.enterLocalPassiveMode();

			LogUtil.info("connected.");
		} catch (Exception e) {
			disconnect();
			throw new FtpIOException(e);
		}
	}

	private String encode = "gbk";

	public void prepareConnect(String host, int port, String username, String password, String encode) {
		this.host = host;
		this.port = port;
		this.password = password;
		this.username = username;
		this.encode = (encode != null ? encode : "gbk");
	}

	public void delete(String deleteFile) {
		try {
			this.ftpclient.deleteFile(deleteFile);
		} catch (IOException e) {
			throw new FtpIOException("delete " + deleteFile + " fail!" + e, e);
		}
	}

	/* Error */
	public void disconnect() {
		// Byte code:
		// 0: aload_0
		// 1: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 4: invokevirtual 312 org/apache/commons/net/ftp/FTPClient:logout ()Z
		// 7: pop
		// 8: goto +4 -> 12
		// 11: pop
		// 12: aload_0
		// 13: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 16: invokevirtual 274 org/apache/commons/net/ftp/FTPClient:disconnect
		// ()V
		// 19: goto +38 -> 57
		// 22: pop
		// 23: aload_0
		// 24: aload_0
		// 25: invokevirtual 31 org/sexftp/core/ftp/MyFtp:myInstance
		// ()Lorg/apache/commons/net/ftp/FTPClient;
		// 28: putfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 31: ldc_w 316
		// 34: invokestatic 294 sexftp/uils/LogUtil:info (Ljava/lang/String;)V
		// 37: goto +34 -> 71
		// 40: astore_1
		// 41: aload_0
		// 42: aload_0
		// 43: invokevirtual 31 org/sexftp/core/ftp/MyFtp:myInstance
		// ()Lorg/apache/commons/net/ftp/FTPClient;
		// 46: putfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 49: ldc_w 316
		// 52: invokestatic 294 sexftp/uils/LogUtil:info (Ljava/lang/String;)V
		// 55: aload_1
		// 56: athrow
		// 57: aload_0
		// 58: aload_0
		// 59: invokevirtual 31 org/sexftp/core/ftp/MyFtp:myInstance
		// ()Lorg/apache/commons/net/ftp/FTPClient;
		// 62: putfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 65: ldc_w 316
		// 68: invokestatic 294 sexftp/uils/LogUtil:info (Ljava/lang/String;)V
		// 71: return
		// Line number table:
		// Java source line #233 -> byte code offset #0
		// Java source line #234 -> byte code offset #11
		// Java source line #238 -> byte code offset #12
		// Java source line #239 -> byte code offset #22
		// Java source line #243 -> byte code offset #23
		// Java source line #245 -> byte code offset #31
		// Java source line #242 -> byte code offset #40
		// Java source line #243 -> byte code offset #41
		// Java source line #245 -> byte code offset #49
		// Java source line #246 -> byte code offset #55
		// Java source line #243 -> byte code offset #57
		// Java source line #245 -> byte code offset #65
		// Java source line #247 -> byte code offset #71
		// Local variable table:
		// start length slot name signature
		// 0 72 0 this MyFtp
		// 40 16 1 localObject Object
		// 11 1 2 localException1 Exception
		// 22 1 3 localException2 Exception
		// Exception table:
		// from to target type
		// 0 8 11 java/lang/Exception
		// 12 19 22 java/lang/Exception
		// 12 23 40 finally
	}

	/* Error */
	public void download(String downloadFile, String saveFile, sexftp.views.IFtpStreamMonitor monitor) {
		// Byte code:
		// 0: aconst_null
		// 1: astore 4
		// 3: aconst_null
		// 4: astore 5
		// 6: iconst_0
		// 7: istore 6
		// 9: new 320 java/io/File
		// 12: dup
		// 13: aload_2
		// 14: invokespecial 322 java/io/File:<init> (Ljava/lang/String;)V
		// 17: astore 7
		// 19: aload 7
		// 21: invokevirtual 323 java/io/File:getParentFile ()Ljava/io/File;
		// 24: invokevirtual 327 java/io/File:exists ()Z
		// 27: ifne +12 -> 39
		// 30: aload 7
		// 32: invokevirtual 323 java/io/File:getParentFile ()Ljava/io/File;
		// 35: invokevirtual 330 java/io/File:mkdirs ()Z
		// 38: pop
		// 39: aload 7
		// 41: invokevirtual 327 java/io/File:exists ()Z
		// 44: ifeq +33 -> 77
		// 47: new 88 java/lang/StringBuilder
		// 50: dup
		// 51: ldc_w 332
		// 54: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 57: aload 7
		// 59: invokevirtual 334 java/io/File:getName ()Ljava/lang/String;
		// 62: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 65: ldc_w 337
		// 68: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 71: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 74: goto +6 -> 80
		// 77: ldc_w 339
		// 80: astore 8
		// 82: aload_0
		// 83: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 86: aload_0
		// 87: aload_1
		// 88: invokespecial 53 org/sexftp/core/ftp/MyFtp:iso88591
		// (Ljava/lang/String;)Ljava/lang/String;
		// 91: invokevirtual 341
		// org/apache/commons/net/ftp/FTPClient:retrieveFileStream
		// (Ljava/lang/String;)Ljava/io/InputStream;
		// 94: astore 4
		// 96: aload 4
		// 98: ifnonnull +31 -> 129
		// 101: new 345 org/sexftp/core/exceptions/NeedRetryException
		// 104: dup
		// 105: new 88 java/lang/StringBuilder
		// 108: dup
		// 109: aload_1
		// 110: invokestatic 90 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 113: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 116: ldc_w 347
		// 119: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 122: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 125: invokespecial 349
		// org/sexftp/core/exceptions/NeedRetryException:<init>
		// (Ljava/lang/String;)V
		// 128: athrow
		// 129: new 350 java/io/FileOutputStream
		// 132: dup
		// 133: aload_2
		// 134: invokespecial 352 java/io/FileOutputStream:<init>
		// (Ljava/lang/String;)V
		// 137: astore 5
		// 139: iconst_0
		// 140: istore 9
		// 142: sipush 1024
		// 145: newarray <illegal type>
		// 147: astore 10
		// 149: iconst_0
		// 150: istore 6
		// 152: goto +66 -> 218
		// 155: aload 5
		// 157: aload 10
		// 159: iconst_0
		// 160: iload 11
		// 162: invokevirtual 353 java/io/OutputStream:write ([BII)V
		// 165: goto +30 -> 195
		// 168: pop
		// 169: new 200 java/lang/RuntimeException
		// 172: dup
		// 173: new 88 java/lang/StringBuilder
		// 176: dup
		// 177: ldc_w 359
		// 180: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 183: iload 6
		// 185: invokevirtual 361 java/lang/StringBuilder:append
		// (I)Ljava/lang/StringBuilder;
		// 188: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 191: invokespecial 364 java/lang/RuntimeException:<init>
		// (Ljava/lang/String;)V
		// 194: athrow
		// 195: iload 6
		// 197: iload 11
		// 199: iadd
		// 200: istore 6
		// 202: aload_3
		// 203: aconst_null
		// 204: iload 6
		// 206: i2l
		// 207: iload 9
		// 209: i2l
		// 210: ldc_w 339
		// 213: invokeinterface 365 7 0
		// 218: aload 4
		// 220: aload 10
		// 222: invokevirtual 371 java/io/InputStream:read ([B)I
		// 225: dup
		// 226: istore 11
		// 228: iconst_m1
		// 229: if_icmpne -74 -> 155
		// 232: aload_3
		// 233: new 88 java/lang/StringBuilder
		// 236: dup
		// 237: ldc_w 377
		// 240: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 243: aload_1
		// 244: aload 7
		// 246: invokevirtual 334 java/io/File:getName ()Ljava/lang/String;
		// 249: invokevirtual 379 java/lang/String:equals (Ljava/lang/Object;)Z
		// 252: ifne +26 -> 278
		// 255: new 88 java/lang/StringBuilder
		// 258: dup
		// 259: aload_1
		// 260: invokestatic 90 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 263: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 266: ldc_w 382
		// 269: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 272: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 275: goto +6 -> 281
		// 278: ldc_w 339
		// 281: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 284: aload 7
		// 286: invokevirtual 334 java/io/File:getName ()Ljava/lang/String;
		// 289: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 292: ldc_w 384
		// 295: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 298: aload 8
		// 300: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 303: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 306: invokeinterface 386 2 0
		// 311: goto +105 -> 416
		// 314: astore 7
		// 316: aload 7
		// 318: athrow
		// 319: astore 7
		// 321: aload_0
		// 322: invokevirtual 297 org/sexftp/core/ftp/MyFtp:disconnect ()V
		// 325: goto +4 -> 329
		// 328: pop
		// 329: new 113 org/sexftp/core/exceptions/FtpIOException
		// 332: dup
		// 333: new 88 java/lang/StringBuilder
		// 336: dup
		// 337: ldc_w 389
		// 340: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 343: iload 6
		// 345: i2l
		// 346: invokestatic 391 org/sexftp/core/utils/StringUtil:getHumanSize
		// (J)Ljava/lang/String;
		// 349: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 352: aload 7
		// 354: invokevirtual 144 java/lang/StringBuilder:append
		// (Ljava/lang/Object;)Ljava/lang/StringBuilder;
		// 357: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 360: aload 7
		// 362: invokespecial 122
		// org/sexftp/core/exceptions/FtpIOException:<init>
		// (Ljava/lang/String;Ljava/lang/Throwable;)V
		// 365: athrow
		// 366: astore 12
		// 368: aload 4
		// 370: ifnull +12 -> 382
		// 373: aload 4
		// 375: invokevirtual 395 java/io/InputStream:close ()V
		// 378: goto +4 -> 382
		// 381: pop
		// 382: aload 5
		// 384: invokevirtual 398 java/io/OutputStream:close ()V
		// 387: goto +4 -> 391
		// 390: pop
		// 391: aload_0
		// 392: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 395: invokevirtual 399
		// org/apache/commons/net/ftp/FTPClient:isConnected ()Z
		// 398: ifeq +15 -> 413
		// 401: aload_0
		// 402: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 405: invokevirtual 402
		// org/apache/commons/net/ftp/FTPClient:completePendingCommand ()Z
		// 408: pop
		// 409: goto +4 -> 413
		// 412: pop
		// 413: aload 12
		// 415: athrow
		// 416: aload 4
		// 418: ifnull +12 -> 430
		// 421: aload 4
		// 423: invokevirtual 395 java/io/InputStream:close ()V
		// 426: goto +4 -> 430
		// 429: pop
		// 430: aload 5
		// 432: invokevirtual 398 java/io/OutputStream:close ()V
		// 435: goto +4 -> 439
		// 438: pop
		// 439: aload_0
		// 440: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 443: invokevirtual 399
		// org/apache/commons/net/ftp/FTPClient:isConnected ()Z
		// 446: ifeq +15 -> 461
		// 449: aload_0
		// 450: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 453: invokevirtual 402
		// org/apache/commons/net/ftp/FTPClient:completePendingCommand ()Z
		// 456: pop
		// 457: goto +4 -> 461
		// 460: pop
		// 461: return
		// Line number table:
		// Java source line #250 -> byte code offset #0
		// Java source line #251 -> byte code offset #3
		// Java source line #252 -> byte code offset #6
		// Java source line #254 -> byte code offset #9
		// Java source line #255 -> byte code offset #19
		// Java source line #256 -> byte code offset #30
		// Java source line #258 -> byte code offset #39
		// Java source line #260 -> byte code offset #82
		// Java source line #261 -> byte code offset #96
		// Java source line #262 -> byte code offset #101
		// Java source line #264 -> byte code offset #129
		// Java source line #266 -> byte code offset #139
		// Java source line #267 -> byte code offset #142
		// Java source line #270 -> byte code offset #149
		// Java source line #271 -> byte code offset #152
		// Java source line #273 -> byte code offset #155
		// Java source line #274 -> byte code offset #168
		// Java source line #275 -> byte code offset #169
		// Java source line #277 -> byte code offset #195
		// Java source line #278 -> byte code offset #202
		// Java source line #271 -> byte code offset #218
		// Java source line #280 -> byte code offset #232
		// Java source line #281 -> byte code offset #243
		// Java source line #282 -> byte code offset #284
		// Java source line #283 -> byte code offset #292
		// Java source line #284 -> byte code offset #298
		// Java source line #280 -> byte code offset #306
		// Java source line #287 -> byte code offset #314
		// Java source line #288 -> byte code offset #316
		// Java source line #290 -> byte code offset #319
		// Java source line #292 -> byte code offset #321
		// Java source line #293 -> byte code offset #328
		// Java source line #295 -> byte code offset #329
		// Java source line #298 -> byte code offset #366
		// Java source line #300 -> byte code offset #368
		// Java source line #301 -> byte code offset #373
		// Java source line #303 -> byte code offset #381
		// Java source line #306 -> byte code offset #382
		// Java source line #307 -> byte code offset #390
		// Java source line #311 -> byte code offset #391
		// Java source line #312 -> byte code offset #401
		// Java source line #313 -> byte code offset #412
		// Java source line #316 -> byte code offset #413
		// Java source line #300 -> byte code offset #416
		// Java source line #301 -> byte code offset #421
		// Java source line #303 -> byte code offset #429
		// Java source line #306 -> byte code offset #430
		// Java source line #307 -> byte code offset #438
		// Java source line #311 -> byte code offset #439
		// Java source line #312 -> byte code offset #449
		// Java source line #313 -> byte code offset #460
		// Java source line #317 -> byte code offset #461
		// Local variable table:
		// start length slot name signature
		// 0 462 0 this MyFtp
		// 0 462 1 downloadFile String
		// 0 462 2 saveFile String
		// 0 462 3 monitor sexftp.views.IFtpStreamMonitor
		// 1 421 4 is java.io.InputStream
		// 4 427 5 os java.io.OutputStream
		// 7 337 6 t int
		// 17 268 7 sf java.io.File
		// 314 3 7 e org.sexftp.core.exceptions.AbortException
		// 319 42 7 e Exception
		// 80 219 8 exists String
		// 140 68 9 avi int
		// 147 74 10 bytes byte[]
		// 155 43 11 c int
		// 226 3 11 c int
		// 366 48 12 localObject Object
		// 168 1 16 localException1 Exception
		// 328 1 17 localException2 Exception
		// 381 1 18 localException3 Exception
		// 390 1 19 localException4 Exception
		// 412 1 20 localException5 Exception
		// 429 1 21 localException6 Exception
		// 438 1 22 localException7 Exception
		// 460 1 23 localException8 Exception
		// Exception table:
		// from to target type
		// 155 165 168 java/lang/Exception
		// 9 311 314 org/sexftp/core/exceptions/AbortException
		// 9 311 319 java/lang/Exception
		// 321 325 328 java/lang/Exception
		// 9 366 366 finally
		// 368 378 381 java/lang/Exception
		// 382 387 390 java/lang/Exception
		// 391 409 412 java/lang/Exception
		// 416 426 429 java/lang/Exception
		// 430 435 438 java/lang/Exception
		// 439 457 460 java/lang/Exception
	}

	public boolean isConnect() {
		if (this.ftpclient.isConnected()) {
			try {
				this.ftpclient.noop();
				return true;

			} catch (FTPConnectionClosedException localFTPConnectionClosedException) {
			} catch (SocketException localSocketException) {
			} catch (IOException e) {

				throw new FtpIOException(e);
			}
			disconnect();
		}
		return false;
	}

	public List<FtpFile> listFiles() {
		List<FtpFile> ftpfileLIst = new ArrayList();

		try {
			FTPFile[] listFiles = this.ftpclient.listFiles();
			FTPFile[] arrayOfFTPFile1;
			int j = (arrayOfFTPFile1 = listFiles).length;
			for (int i = 0; i < j; i++) {
				FTPFile ftpFile = arrayOfFTPFile1[i];

				if ((!".".equals(ftpFile.getName())) && (!"..".equals(ftpFile.getName()))) {

					boolean isfolder = ftpFile.isDirectory();

					FtpFile newftpfile = new FtpFile(gbfrom88591(ftpFile.getName()), isfolder, ftpFile.getSize(),
							ftpFile.getTimestamp());
					ftpfileLIst.add(newftpfile);
				}
			}
		} catch (IOException e) {
			throw new FtpIOException(e);
		}
		return ftpfileLIst;
	}

	/* Error */
	public void upload(String uploadFile, sexftp.views.IFtpStreamMonitor monitor) {
		// Byte code:
		// 0: aconst_null
		// 1: astore_3
		// 2: aconst_null
		// 3: astore 4
		// 5: new 320 java/io/File
		// 8: dup
		// 9: aload_1
		// 10: invokespecial 322 java/io/File:<init> (Ljava/lang/String;)V
		// 13: astore 5
		// 15: aload_0
		// 16: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 19: aload_0
		// 20: aload 5
		// 22: invokevirtual 334 java/io/File:getName ()Ljava/lang/String;
		// 25: invokespecial 53 org/sexftp/core/ftp/MyFtp:iso88591
		// (Ljava/lang/String;)Ljava/lang/String;
		// 28: invokevirtual 471
		// org/apache/commons/net/ftp/FTPClient:storeFileStream
		// (Ljava/lang/String;)Ljava/io/OutputStream;
		// 31: astore 4
		// 33: new 475 java/io/FileInputStream
		// 36: dup
		// 37: aload_1
		// 38: invokespecial 477 java/io/FileInputStream:<init>
		// (Ljava/lang/String;)V
		// 41: astore_3
		// 42: aload 5
		// 44: invokevirtual 478 java/io/File:length ()J
		// 47: lstore 6
		// 49: sipush 1024
		// 52: newarray <illegal type>
		// 54: astore 8
		// 56: lconst_0
		// 57: lstore 10
		// 59: goto +68 -> 127
		// 62: aload 4
		// 64: aload 8
		// 66: iconst_0
		// 67: iload 9
		// 69: invokevirtual 353 java/io/OutputStream:write ([BII)V
		// 72: goto +33 -> 105
		// 75: astore 12
		// 77: new 200 java/lang/RuntimeException
		// 80: dup
		// 81: new 88 java/lang/StringBuilder
		// 84: dup
		// 85: ldc_w 359
		// 88: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 91: lload 10
		// 93: invokevirtual 480 java/lang/StringBuilder:append
		// (J)Ljava/lang/StringBuilder;
		// 96: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 99: aload 12
		// 101: invokespecial 483 java/lang/RuntimeException:<init>
		// (Ljava/lang/String;Ljava/lang/Throwable;)V
		// 104: athrow
		// 105: lload 10
		// 107: iload 9
		// 109: i2l
		// 110: ladd
		// 111: lstore 10
		// 113: aload_2
		// 114: aconst_null
		// 115: lload 10
		// 117: lload 6
		// 119: ldc_w 339
		// 122: invokeinterface 365 7 0
		// 127: aload_3
		// 128: aload 8
		// 130: invokevirtual 484 java/io/FileInputStream:read ([B)I
		// 133: dup
		// 134: istore 9
		// 136: iconst_m1
		// 137: if_icmpne -75 -> 62
		// 140: aload_2
		// 141: new 88 java/lang/StringBuilder
		// 144: dup
		// 145: ldc_w 485
		// 148: invokespecial 94 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 151: aload 5
		// 153: invokevirtual 334 java/io/File:getName ()Ljava/lang/String;
		// 156: invokevirtual 97 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 159: invokevirtual 101 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 162: invokeinterface 386 2 0
		// 167: goto +78 -> 245
		// 170: astore 5
		// 172: aload_0
		// 173: invokevirtual 297 org/sexftp/core/ftp/MyFtp:disconnect ()V
		// 176: goto +4 -> 180
		// 179: pop
		// 180: new 113 org/sexftp/core/exceptions/FtpIOException
		// 183: dup
		// 184: aload 5
		// 186: invokespecial 298
		// org/sexftp/core/exceptions/FtpIOException:<init>
		// (Ljava/lang/Throwable;)V
		// 189: athrow
		// 190: astore 13
		// 192: aload_3
		// 193: invokevirtual 487 java/io/FileInputStream:close ()V
		// 196: goto +4 -> 200
		// 199: pop
		// 200: aload 4
		// 202: invokevirtual 398 java/io/OutputStream:close ()V
		// 205: goto +4 -> 209
		// 208: pop
		// 209: aload_0
		// 210: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 213: invokevirtual 399
		// org/apache/commons/net/ftp/FTPClient:isConnected ()Z
		// 216: ifeq +26 -> 242
		// 219: aload_0
		// 220: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 223: invokevirtual 402
		// org/apache/commons/net/ftp/FTPClient:completePendingCommand ()Z
		// 226: pop
		// 227: goto +15 -> 242
		// 230: astore 14
		// 232: new 113 org/sexftp/core/exceptions/FtpIOException
		// 235: dup
		// 236: aload 14
		// 238: invokespecial 298
		// org/sexftp/core/exceptions/FtpIOException:<init>
		// (Ljava/lang/Throwable;)V
		// 241: athrow
		// 242: aload 13
		// 244: athrow
		// 245: aload_3
		// 246: invokevirtual 487 java/io/FileInputStream:close ()V
		// 249: goto +4 -> 253
		// 252: pop
		// 253: aload 4
		// 255: invokevirtual 398 java/io/OutputStream:close ()V
		// 258: goto +4 -> 262
		// 261: pop
		// 262: aload_0
		// 263: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 266: invokevirtual 399
		// org/apache/commons/net/ftp/FTPClient:isConnected ()Z
		// 269: ifeq +26 -> 295
		// 272: aload_0
		// 273: getfield 25 org/sexftp/core/ftp/MyFtp:ftpclient
		// Lorg/apache/commons/net/ftp/FTPClient;
		// 276: invokevirtual 402
		// org/apache/commons/net/ftp/FTPClient:completePendingCommand ()Z
		// 279: pop
		// 280: goto +15 -> 295
		// 283: astore 14
		// 285: new 113 org/sexftp/core/exceptions/FtpIOException
		// 288: dup
		// 289: aload 14
		// 291: invokespecial 298
		// org/sexftp/core/exceptions/FtpIOException:<init>
		// (Ljava/lang/Throwable;)V
		// 294: athrow
		// 295: return
		// Line number table:
		// Java source line #366 -> byte code offset #0
		// Java source line #367 -> byte code offset #2
		// Java source line #369 -> byte code offset #5
		// Java source line #373 -> byte code offset #15
		// Java source line #375 -> byte code offset #33
		// Java source line #376 -> byte code offset #42
		// Java source line #378 -> byte code offset #49
		// Java source line #381 -> byte code offset #56
		// Java source line #382 -> byte code offset #59
		// Java source line #384 -> byte code offset #62
		// Java source line #385 -> byte code offset #75
		// Java source line #386 -> byte code offset #77
		// Java source line #388 -> byte code offset #105
		// Java source line #389 -> byte code offset #113
		// Java source line #382 -> byte code offset #127
		// Java source line #391 -> byte code offset #140
		// Java source line #394 -> byte code offset #170
		// Java source line #396 -> byte code offset #172
		// Java source line #397 -> byte code offset #179
		// Java source line #399 -> byte code offset #180
		// Java source line #401 -> byte code offset #190
		// Java source line #403 -> byte code offset #192
		// Java source line #404 -> byte code offset #199
		// Java source line #408 -> byte code offset #200
		// Java source line #409 -> byte code offset #208
		// Java source line #414 -> byte code offset #209
		// Java source line #415 -> byte code offset #219
		// Java source line #416 -> byte code offset #230
		// Java source line #417 -> byte code offset #232
		// Java source line #420 -> byte code offset #242
		// Java source line #403 -> byte code offset #245
		// Java source line #404 -> byte code offset #252
		// Java source line #408 -> byte code offset #253
		// Java source line #409 -> byte code offset #261
		// Java source line #414 -> byte code offset #262
		// Java source line #415 -> byte code offset #272
		// Java source line #416 -> byte code offset #283
		// Java source line #417 -> byte code offset #285
		// Java source line #422 -> byte code offset #295
		// Local variable table:
		// start length slot name signature
		// 0 296 0 this MyFtp
		// 0 296 1 uploadFile String
		// 0 296 2 monitor sexftp.views.IFtpStreamMonitor
		// 1 245 3 is java.io.FileInputStream
		// 3 251 4 os java.io.OutputStream
		// 13 139 5 file java.io.File
		// 170 15 5 e IOException
		// 47 71 6 avi long
		// 54 75 8 bytes byte[]
		// 62 46 9 c int
		// 134 3 9 c int
		// 57 59 10 t long
		// 75 25 12 e Exception
		// 190 53 13 localObject Object
		// 230 7 14 e IOException
		// 283 7 14 e IOException
		// 179 1 16 localException1 Exception
		// 199 1 17 localException2 Exception
		// 208 1 18 localException3 Exception
		// 252 1 19 localException4 Exception
		// 261 1 20 localException5 Exception
		// Exception table:
		// from to target type
		// 62 72 75 java/lang/Exception
		// 5 167 170 java/io/IOException
		// 172 176 179 java/lang/Exception
		// 5 190 190 finally
		// 192 196 199 java/lang/Exception
		// 200 205 208 java/lang/Exception
		// 209 227 230 java/io/IOException
		// 245 249 252 java/lang/Exception
		// 253 258 261 java/lang/Exception
		// 262 280 283 java/io/IOException
	}

	public void completed() {
	}

	public static void main(String[] args) {
		System.out.println("coy".substring(3));
	}
}
