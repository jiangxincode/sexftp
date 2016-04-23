package org.sexftp.core.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.desy.xbean.XbeanUtil;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Display;
import org.sexftp.core.exceptions.BizException;
import org.sexftp.core.exceptions.SRuntimeException;
import org.sexftp.core.ftp.bean.FtpConf;
import org.sexftp.core.ftp.bean.FtpUploadConf;
import org.sexftp.core.utils.FileUtil;
import sexftp.SexftpRun;
import sexftp.SrcViewable;
import sexftp.views.AbstractSexftpView;
import sexftp.views.IFtpStreamMonitor;

public class FtpUtil {
	public static Map<String, XFtp> FTP_MAP = new HashMap();

	static {
		FTP_MAP.put("ftp", new MyFtp());
		FTP_MAP.put("file", new MyFile());
		FTP_MAP.put("sftp", new MySFTP());
	}

	public static void main(String[] args) {
		XFtp f = new MySFTP();
		f.prepareConnect("10.0.0.250", 2010, "root", "20101207kkfunserver!@#$%^&*()_+", null);
	}

	public static List<FtpConf> getAllConf(String confFoloder) {
		List<FtpConf> ftpConfList = new ArrayList();
		File FileCurDir = new File(confFoloder);
		File[] confFiles = FileCurDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml"))
					return true;
				return false;
			}
		});
		Arrays.sort(confFiles);
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = confFiles).length;
		for (int i = 0; i < j; i++) {
			File file = arrayOfFile1[i];
			String xmlconf = FileUtil.getTextFromFile(file.getAbsolutePath(), "utf-8");
			FtpConf conf = null;
			try {
				conf = (FtpConf) XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
			} catch (Exception e) {
				conf = new FtpConf();
				conf.setHost("Load Config File Error :" + e.getMessage());
			}
			conf.setName(file.getName());
			ftpConfList.add(conf);
		}
		return ftpConfList;
	}

	public static void initWkDir(String workBaseDir, String configFilePath) {
		String wkdir = workBaseDir + "/" + new File(configFilePath).getName();
		File wkdirFile = new File(wkdir);
		if (!wkdirFile.exists()) {
			wkdirFile.mkdirs();
		}
	}

	public static List<FtpUploadConf> expandFtpUploadConf(FtpConf ftpconf, FtpUploadConf ftpUploadConf) {
		List<FtpUploadConf> expandFtpUploadConfList = new ArrayList();
		if (ftpUploadConf != null) {
			expandFtpUploadConfList.addAll(expandFtpUploadConf(ftpUploadConf));
		} else {
			for (FtpUploadConf tftpUploadConf : ftpconf.getFtpUploadConfList()) {
				expandFtpUploadConfList.addAll(expandFtpUploadConf(tftpUploadConf));
			}
		}
		return expandFtpUploadConfList;
	}

	public static void formater(String workBaseDir, String configFilePath) throws Exception {
		String wkdir = workBaseDir + "/" + new File(configFilePath).getName();
		initWkDir(workBaseDir, configFilePath);

		String xmlconf = FileUtil.getTextFromFile(configFilePath, "utf-8");
		FtpConf conf = (FtpConf) XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
		List<FtpUploadConf> expandFtpUploadConfList = expandFtpUploadConf(conf, null);
		Map<String, String> lastModMap = new HashMap();
		for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
			lastModMap.put(expandFtpUploadConf.getClientPath(), expandFtpUploadConf.getFileMd5());
		}

		writeLastModMap(wkdir, lastModMap);
	}

	public static void formaterSel(String workBaseDir, String configFilePath, List<FtpUploadConf> ftpUploadConfList)
			throws Exception {
		String wkdir = workBaseDir + "/" + new File(configFilePath).getName();
		initWkDir(workBaseDir, configFilePath);

		String xmlconf = FileUtil.getTextFromFile(configFilePath, "utf-8");
		FtpConf conf = (FtpConf) XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
		Map<String, String> lastModMap = null;
		try {
			lastModMap = readLastModMap(wkdir);
		} catch (RuntimeException e) {
			if ((e.getCause() instanceof FileNotFoundException)) {

				formater(workBaseDir, configFilePath);
				return;
			}

			throw e;
		}
		Iterator localIterator2;
		for (Iterator localIterator1 = ftpUploadConfList.iterator(); localIterator1.hasNext(); localIterator2
				.hasNext()) {
			FtpUploadConf ftpUploadConf = (FtpUploadConf) localIterator1.next();
			localIterator2 = expandFtpUploadConf(conf, ftpUploadConf).iterator();
			FtpUploadConf expandFtpUploadConf = (FtpUploadConf) localIterator2.next();

			lastModMap.put(expandFtpUploadConf.getClientPath(), expandFtpUploadConf.getFileMd5());
			continue;
		}

		writeLastModMap(wkdir, lastModMap);
	}

	public static Map<String, String> readLastModMap(String wkdir) {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(wkdir + "/lastModMap.d"));
			Map<String, String> lastModMap = (Map) ois.readObject();
			return lastModMap;
		} catch (FileNotFoundException localFileNotFoundException) {
			throw new BizException("Not Format!");
		} catch (Exception e) {
			throw new SRuntimeException(e);
		} finally {
			try {
				ois.close();
			} catch (Exception localException2) {
			}
		}
	}

	/* Error */
	public static void writeLastModMap(String wkdir, Map<String, String> lastModMap) {
		// Byte code:
		// 0: aconst_null
		// 1: astore_2
		// 2: new 278 java/io/ObjectOutputStream
		// 5: dup
		// 6: new 280 java/io/FileOutputStream
		// 9: dup
		// 10: new 108 java/lang/StringBuilder
		// 13: dup
		// 14: aload_0
		// 15: invokestatic 157 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 18: invokespecial 112 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 21: ldc -3
		// 23: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 26: invokevirtual 122 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 29: invokespecial 282 java/io/FileOutputStream:<init>
		// (Ljava/lang/String;)V
		// 32: invokespecial 283 java/io/ObjectOutputStream:<init>
		// (Ljava/io/OutputStream;)V
		// 35: astore_2
		// 36: aload_2
		// 37: aload_1
		// 38: invokevirtual 286 java/io/ObjectOutputStream:writeObject
		// (Ljava/lang/Object;)V
		// 41: goto +38 -> 79
		// 44: pop
		// 45: new 265 org/sexftp/core/exceptions/BizException
		// 48: dup
		// 49: ldc_w 267
		// 52: invokespecial 269 org/sexftp/core/exceptions/BizException:<init>
		// (Ljava/lang/String;)V
		// 55: athrow
		// 56: astore_3
		// 57: new 270 org/sexftp/core/exceptions/SRuntimeException
		// 60: dup
		// 61: aload_3
		// 62: invokespecial 272
		// org/sexftp/core/exceptions/SRuntimeException:<init>
		// (Ljava/lang/Throwable;)V
		// 65: athrow
		// 66: astore 4
		// 68: aload_2
		// 69: invokevirtual 290 java/io/ObjectOutputStream:close ()V
		// 72: goto +4 -> 76
		// 75: pop
		// 76: aload 4
		// 78: athrow
		// 79: aload_2
		// 80: invokevirtual 290 java/io/ObjectOutputStream:close ()V
		// 83: goto +4 -> 87
		// 86: pop
		// 87: return
		// Line number table:
		// Java source line #182 -> byte code offset #0
		// Java source line #184 -> byte code offset #2
		// Java source line #185 -> byte code offset #36
		// Java source line #186 -> byte code offset #44
		// Java source line #187 -> byte code offset #45
		// Java source line #188 -> byte code offset #56
		// Java source line #189 -> byte code offset #57
		// Java source line #192 -> byte code offset #66
		// Java source line #194 -> byte code offset #68
		// Java source line #195 -> byte code offset #75
		// Java source line #198 -> byte code offset #76
		// Java source line #194 -> byte code offset #79
		// Java source line #195 -> byte code offset #86
		// Java source line #199 -> byte code offset #87
		// Local variable table:
		// start length slot name signature
		// 0 88 0 wkdir String
		// 0 88 1 lastModMap Map<String, String>
		// 1 79 2 os java.io.ObjectOutputStream
		// 56 6 3 e java.io.IOException
		// 66 11 4 localObject Object
		// 44 1 5 localFileNotFoundException FileNotFoundException
		// 75 1 6 localException1 Exception
		// 86 1 7 localException2 Exception
		// Exception table:
		// from to target type
		// 2 41 44 java/io/FileNotFoundException
		// 2 41 56 java/io/IOException
		// 2 66 66 finally
		// 68 72 75 java/lang/Exception
		// 79 83 86 java/lang/Exception
	}

	public static List<FtpUploadConf> anyaCanUploadFiles(String workBaseDir, String configFilePath, FtpConf ftpconf,
			FtpUploadConf ftpUploadConf) throws Exception {
		String wkdir = workBaseDir + "/" + new File(configFilePath).getName();

		Map<String, String> lastModMap = readLastModMap(wkdir);

		List<FtpUploadConf> expandFtpUploadConfList = expandFtpUploadConf(ftpconf, ftpUploadConf);
		List<FtpUploadConf> canFtpUploadConfList = new ArrayList();
		for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
			String path = expandFtpUploadConf.getClientPath();
			if ((!lastModMap.containsKey(path))
					|| (!((String) lastModMap.get(path)).equals(expandFtpUploadConf.getFileMd5()))) {
				canFtpUploadConfList.add(expandFtpUploadConf);
			}
		}
		return canFtpUploadConfList;
	}

	/* Error */
	public static void executeUpload(FtpConf conf, List<FtpUploadConf> expandFtpUploadConfList,
			IFtpStreamMonitor monitor, AbstractSexftpView srcView) {
		// Byte code:
		// 0: new 314 org/sexftp/core/ftp/FtpPools
		// 3: dup
		// 4: aload_0
		// 5: new 316 org/sexftp/core/ftp/FtpUtil$2
		// 8: dup
		// 9: aload_2
		// 10: invokespecial 318 org/sexftp/core/ftp/FtpUtil$2:<init>
		// (Lsexftp/views/IFtpStreamMonitor;)V
		// 13: invokespecial 321 org/sexftp/core/ftp/FtpPools:<init>
		// (Lorg/sexftp/core/ftp/bean/FtpConf;Lorg/sexftp/core/ftp/Consoleable;)V
		// 16: astore 4
		// 18: aload 4
		// 20: invokevirtual 324 org/sexftp/core/ftp/FtpPools:getFtp
		// ()Lorg/sexftp/core/ftp/XFtp;
		// 23: astore 5
		// 25: aload 5
		// 27: dup
		// 28: astore 6
		// 30: monitorenter
		// 31: aload 4
		// 33: invokevirtual 328 org/sexftp/core/ftp/FtpPools:getConnectedFtp
		// ()Lorg/sexftp/core/ftp/XFtp;
		// 36: pop
		// 37: new 331 org/sexftp/core/utils/ExistFtpFile
		// 40: dup
		// 41: aload 5
		// 43: invokespecial 333 org/sexftp/core/utils/ExistFtpFile:<init>
		// (Lorg/sexftp/core/ftp/XFtp;)V
		// 46: astore 7
		// 48: aconst_null
		// 49: astore 8
		// 51: aload_1
		// 52: invokeinterface 336 1 0
		// 57: istore 9
		// 59: aconst_null
		// 60: astore 10
		// 62: aload_1
		// 63: invokeinterface 190 1 0
		// 68: astore 12
		// 70: goto +327 -> 397
		// 73: aload 12
		// 75: invokeinterface 194 1 0
		// 80: checkcast 200 org/sexftp/core/ftp/bean/FtpUploadConf
		// 83: astore 11
		// 85: aload 11
		// 87: invokevirtual 340
		// org/sexftp/core/ftp/bean/FtpUploadConf:getServerPath
		// ()Ljava/lang/String;
		// 90: aload 8
		// 92: invokevirtual 306 java/lang/String:equals (Ljava/lang/Object;)Z
		// 95: ifne +42 -> 137
		// 98: aload_2
		// 99: new 108 java/lang/StringBuilder
		// 102: dup
		// 103: ldc_w 343
		// 106: invokespecial 112 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 109: aload 11
		// 111: invokevirtual 340
		// org/sexftp/core/ftp/bean/FtpUploadConf:getServerPath
		// ()Ljava/lang/String;
		// 114: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 117: invokevirtual 122 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 120: invokeinterface 345 2 0
		// 125: aload 5
		// 127: aload 11
		// 129: invokevirtual 340
		// org/sexftp/core/ftp/bean/FtpUploadConf:getServerPath
		// ()Ljava/lang/String;
		// 132: invokeinterface 350 2 0
		// 137: aload 11
		// 139: invokevirtual 340
		// org/sexftp/core/ftp/bean/FtpUploadConf:getServerPath
		// ()Ljava/lang/String;
		// 142: astore 8
		// 144: aload 7
		// 146: new 108 java/lang/StringBuilder
		// 149: dup
		// 150: aload 8
		// 152: invokestatic 157 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 155: invokespecial 112 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 158: new 69 java/io/File
		// 161: dup
		// 162: aload 11
		// 164: invokevirtual 217
		// org/sexftp/core/ftp/bean/FtpUploadConf:getClientPath
		// ()Ljava/lang/String;
		// 167: invokespecial 71 java/io/File:<init> (Ljava/lang/String;)V
		// 170: invokevirtual 128 java/io/File:getName ()Ljava/lang/String;
		// 173: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 176: invokevirtual 122 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 179: invokevirtual 353
		// org/sexftp/core/utils/ExistFtpFile:existsFtpFile
		// (Ljava/lang/String;)Lorg/sexftp/core/ftp/bean/FtpFile;
		// 182: astore 13
		// 184: aload 13
		// 186: ifnull +33 -> 219
		// 189: new 108 java/lang/StringBuilder
		// 192: dup
		// 193: ldc_w 357
		// 196: invokespecial 112 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 199: aload 13
		// 201: invokevirtual 359 org/sexftp/core/ftp/bean/FtpFile:getName
		// ()Ljava/lang/String;
		// 204: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 207: ldc_w 362
		// 210: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 213: invokevirtual 122 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 216: goto +6 -> 222
		// 219: ldc_w 364
		// 222: astore 14
		// 224: aload 13
		// 226: ifnull +136 -> 362
		// 229: invokestatic 366 sexftp/uils/PluginUtil:overwriteTips
		// ()Ljava/lang/Boolean;
		// 232: invokevirtual 372 java/lang/Boolean:booleanValue ()Z
		// 235: ifeq +127 -> 362
		// 238: iconst_0
		// 239: istore 15
		// 241: aload 10
		// 243: ifnonnull +74 -> 317
		// 246: new 377 org/sexftp/core/ftp/FtpUtil$Question
		// 249: dup
		// 250: invokespecial 379 org/sexftp/core/ftp/FtpUtil$Question:<init>
		// ()V
		// 253: aload_3
		// 254: new 108 java/lang/StringBuilder
		// 257: dup
		// 258: ldc_w 380
		// 261: invokespecial 112 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 264: aload 13
		// 266: invokevirtual 359 org/sexftp/core/ftp/bean/FtpFile:getName
		// ()Ljava/lang/String;
		// 269: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 272: ldc_w 382
		// 275: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 278: invokevirtual 122 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 281: ldc_w 384
		// 284: invokevirtual 386 org/sexftp/core/ftp/FtpUtil$Question:question
		// (Lsexftp/views/AbstractSexftpView;Ljava/lang/String;Ljava/lang/String;)Lorg/eclipse/jface/dialogs/MessageDialogWithToggle;
		// 287: astore 16
		// 289: aload 16
		// 291: invokevirtual 390
		// org/eclipse/jface/dialogs/MessageDialogWithToggle:getToggleState ()Z
		// 294: ifeq +13 -> 307
		// 297: aload 16
		// 299: invokevirtual 395
		// org/eclipse/jface/dialogs/MessageDialogWithToggle:getReturnCode ()I
		// 302: invokestatic 398 java/lang/Integer:valueOf
		// (I)Ljava/lang/Integer;
		// 305: astore 10
		// 307: aload 16
		// 309: invokevirtual 395
		// org/eclipse/jface/dialogs/MessageDialogWithToggle:getReturnCode ()I
		// 312: istore 15
		// 314: goto +10 -> 324
		// 317: aload 10
		// 319: invokevirtual 403 java/lang/Integer:intValue ()I
		// 322: istore 15
		// 324: iload 15
		// 326: iconst_2
		// 327: if_icmpeq +35 -> 362
		// 330: iload 15
		// 332: iconst_1
		// 333: if_icmpne +11 -> 344
		// 336: new 406 org/sexftp/core/exceptions/AbortException
		// 339: dup
		// 340: invokespecial 408
		// org/sexftp/core/exceptions/AbortException:<init> ()V
		// 343: athrow
		// 344: iload 15
		// 346: iconst_3
		// 347: if_icmpne +9 -> 356
		// 350: iinc 9 -1
		// 353: goto +44 -> 397
		// 356: iinc 9 -1
		// 359: goto +38 -> 397
		// 362: aload_2
		// 363: aload 11
		// 365: lconst_0
		// 366: lconst_0
		// 367: ldc_w 364
		// 370: invokeinterface 409 7 0
		// 375: aload 5
		// 377: aload 11
		// 379: invokevirtual 217
		// org/sexftp/core/ftp/bean/FtpUploadConf:getClientPath
		// ()Ljava/lang/String;
		// 382: new 413 org/sexftp/core/ftp/FtpUtil$3
		// 385: dup
		// 386: aload_2
		// 387: aload 14
		// 389: invokespecial 415 org/sexftp/core/ftp/FtpUtil$3:<init>
		// (Lsexftp/views/IFtpStreamMonitor;Ljava/lang/String;)V
		// 392: invokeinterface 418 3 0
		// 397: aload 12
		// 399: invokeinterface 202 1 0
		// 404: ifne -331 -> 73
		// 407: aload_2
		// 408: new 108 java/lang/StringBuilder
		// 411: dup
		// 412: ldc_w 422
		// 415: invokespecial 112 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 418: iload 9
		// 420: invokevirtual 424 java/lang/StringBuilder:append
		// (I)Ljava/lang/StringBuilder;
		// 423: ldc_w 427
		// 426: invokevirtual 118 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 429: invokevirtual 122 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 432: invokeinterface 345 2 0
		// 437: goto +12 -> 449
		// 440: pop
		// 441: goto +8 -> 449
		// 444: astore 17
		// 446: aload 17
		// 448: athrow
		// 449: aload 6
		// 451: monitorexit
		// 452: goto +7 -> 459
		// 455: aload 6
		// 457: monitorexit
		// 458: athrow
		// 459: return
		// Line number table:
		// Java source line #238 -> byte code offset #0
		// Java source line #243 -> byte code offset #18
		// Java source line #244 -> byte code offset #25
		// Java source line #245 -> byte code offset #31
		// Java source line #246 -> byte code offset #37
		// Java source line #248 -> byte code offset #48
		// Java source line #249 -> byte code offset #51
		// Java source line #250 -> byte code offset #59
		// Java source line #251 -> byte code offset #62
		// Java source line #252 -> byte code offset #85
		// Java source line #254 -> byte code offset #98
		// Java source line #255 -> byte code offset #125
		// Java source line #257 -> byte code offset #137
		// Java source line #258 -> byte code offset #144
		// Java source line #259 -> byte code offset #184
		// Java source line #260 -> byte code offset #224
		// Java source line #262 -> byte code offset #238
		// Java source line #263 -> byte code offset #241
		// Java source line #265 -> byte code offset #246
		// Java source line #266 -> byte code offset #289
		// Java source line #268 -> byte code offset #297
		// Java source line #270 -> byte code offset #307
		// Java source line #274 -> byte code offset #317
		// Java source line #276 -> byte code offset #324
		// Java source line #280 -> byte code offset #330
		// Java source line #282 -> byte code offset #336
		// Java source line #284 -> byte code offset #344
		// Java source line #286 -> byte code offset #350
		// Java source line #287 -> byte code offset #353
		// Java source line #290 -> byte code offset #356
		// Java source line #291 -> byte code offset #359
		// Java source line #295 -> byte code offset #362
		// Java source line #296 -> byte code offset #375
		// Java source line #251 -> byte code offset #397
		// Java source line #309 -> byte code offset #407
		// Java source line #310 -> byte code offset #440
		// Java source line #313 -> byte code offset #444
		// Java source line #319 -> byte code offset #446
		// Java source line #244 -> byte code offset #449
		// Java source line #322 -> byte code offset #459
		// Local variable table:
		// start length slot name signature
		// 0 460 0 conf FtpConf
		// 0 460 1 expandFtpUploadConfList List<FtpUploadConf>
		// 0 460 2 monitor IFtpStreamMonitor
		// 0 460 3 srcView AbstractSexftpView
		// 16 16 4 ftpPools FtpPools
		// 23 353 5 ftp XFtp
		// 28 428 6 Ljava/lang/Object; Object
		// 46 99 7 existFtpFile org.sexftp.core.utils.ExistFtpFile
		// 49 102 8 servPath String
		// 57 362 9 uploadCount int
		// 60 258 10 remembert Integer
		// 83 295 11 expandFtpUploadConf FtpUploadConf
		// 68 330 12 localIterator Iterator
		// 182 83 13 existsFtpFile org.sexftp.core.ftp.bean.FtpFile
		// 222 166 14 existsr String
		// 239 106 15 returncode int
		// 287 21 16 t MessageDialogWithToggle
		// 444 3 17 localObject1 Object
		// 440 1 18 localAbortException
		// org.sexftp.core.exceptions.AbortException
		// Exception table:
		// from to target type
		// 48 437 440 org/sexftp/core/exceptions/AbortException
		// 48 441 444 finally
		// 31 452 455 finally
		// 455 458 455 finally
	}

	public static class Question {
		private MessageDialogWithToggle tg;

		public MessageDialogWithToggle question(final AbstractSexftpView srcView, final String msg,
				final String toggle) {
			Display.getDefault().syncExec(new SexftpRun(srcView) {
				public void srun() throws Exception {
					FtpUtil.Question.this.tg = srcView.showQuestion(msg, toggle);
				}
			});
			return this.tg;
		}
	}

	private static List<FtpUploadConf> expandFtpUploadConf(FtpUploadConf ftpUploadConf) {
		String clientPath = ftpUploadConf.getClientPath();
		File file = new File(clientPath);
		List<FtpUploadConf> expandFtpUploadConfList = new ArrayList();
		expandFtpUploadConf(file, ftpUploadConf, expandFtpUploadConfList);
		return expandFtpUploadConfList;
	}

	private static void expandFtpUploadConf(File file, FtpUploadConf ftpUploadConf,
			List<FtpUploadConf> expandFtpUploadConfList) {
		if (file.isHidden()) {
			return;
		}
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = subFiles).length;
			for (int i = 0; i < j; i++) {
				File subFile = arrayOfFile1[i];
				expandFtpUploadConf(subFile, ftpUploadConf, expandFtpUploadConfList);
			}
		} else {
			String clientFileFolderPath = file.getParentFile().getAbsolutePath();

			String srClientPath = ftpUploadConf.getClientPath();
			String srServerPath = ftpUploadConf.getServerPath();
			if (!srServerPath.endsWith("/"))
				throw new RuntimeException(srServerPath + " not end with /");
			String abPath = "";
			if (clientFileFolderPath.length() >= srClientPath.length()) {
				abPath = clientFileFolderPath.substring(srClientPath.length()).replace('\\', '/');
			}
			if (!abPath.endsWith("/")) {
				abPath = abPath + "/";
			}
			FtpUploadConf expandFtpUploadConf = new FtpUploadConf();
			expandFtpUploadConf.setClientPath(file.getAbsolutePath());
			expandFtpUploadConf.setServerPath((srServerPath + abPath).replaceAll("//", "/"));
			expandFtpUploadConf.setFileMd5(FileMd5.getMD5(file));
			expandFtpUploadConfList.add(expandFtpUploadConf);
		}
	}
}
