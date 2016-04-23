package org.sexftp.core.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.desy.xbean.XbeanUtil;
import org.sexftp.core.ftp.bean.FtpConf;
import org.sexftp.core.ftp.bean.FtpUploadConf;
import org.sexftp.core.utils.FileUtil;

public class FtpUploader {
	public static Map<String, XFtp> FTP_MAP = new HashMap();

	static {
		FTP_MAP.put("ftp", new BACKMyFtp());
		FTP_MAP.put("sftp", new MySFTP());
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));
		String workBaseDir = args[0];

		String coffilepath = null;
		if ((args.length == 2) && (args[1].trim().length() > 0)) {
			coffilepath = args[1];
			System.out.println("chosed:" + new File(coffilepath).getName());

		} else {
			File FileCurDir = new File(".");
			File[] confFiles = FileCurDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith(".xml"))
						return true;
					return false;
				}
			});
			Arrays.sort(confFiles);
			String[] confNames = new String[confFiles.length];
			for (int i = 0; i < confFiles.length; i++) {
				confNames[i] = (i + 1 + " - " + confFiles[i].getName());
			}
			System.out.println("chose conf files indexs:");
			String index = readStringFromSystemin(null, confNames);
			System.out.println("chosed:" + confFiles[(Integer.parseInt(index) - 1)].getName());
			coffilepath = confFiles[(Integer.parseInt(index) - 1)].getAbsolutePath();
		}
		String wkdir = workBaseDir + "/" + new File(coffilepath).getName();

		File wkdirFile = new File(wkdir);
		if (!wkdirFile.exists()) {
			wkdirFile.mkdirs();
		}
		String xmlconf = FileUtil.getTextFromFile(coffilepath, "utf-8");
		FtpConf conf = (FtpConf) XbeanUtil.xml2Bean(FtpConf.class, xmlconf);

		List<FtpUploadConf> expandFtpUploadConfList = new ArrayList();
		for (FtpUploadConf ftpUploadConf : conf.getFtpUploadConfList()) {
			expandFtpUploadConfList.addAll(expandFtpUploadConf(ftpUploadConf));
		}

		File lastMoMap = new File(wkdir + "/lastModMap.d");

		if (!lastMoMap.exists()) {
			System.out.println("Not Format,Formating...");
			formater(expandFtpUploadConfList, wkdir);
			System.exit(0);
		}

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastMoMap));
		Map<String, String> lastModMap = (Map) ois.readObject();

		String chosedUploadCfPath = wkdir + "/chosedUploadCf.d";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(chosedUploadCfPath)));
		System.out.println("Uplad Queues:");
		boolean hasData = false;
		for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
			String path = expandFtpUploadConf.getClientPath();
			if ((!lastModMap.containsKey(path))
					|| (!((String) lastModMap.get(path)).equals(expandFtpUploadConf.getFileMd5()))) {
				System.out.println(expandFtpUploadConf.toSimpleString());
				bw.write(expandFtpUploadConf + "\r\n");
				hasData = true;
			}
		}
		bw.close();
		if (!hasData) {
			System.out.println("No files modifed after last format!");
			System.exit(0);
		}

		System.out.println("");
		String option = readStringFromSystemin("3",
				new String[] { "1 - Format", "2 - View Or Modify Upload Queues In Notpad", "3 - Upload" });

		if (option.equalsIgnoreCase("1")) {
			formater(expandFtpUploadConfList, wkdir);
			System.exit(0);
		} else if ((option.length() > 0) && (option.equalsIgnoreCase("2"))) {

			Runtime.getRuntime().exec("notepad " + chosedUploadCfPath);
			System.out.println("You Can View or Modify The Upload Queues In Notpad,After that,Chose Options as :");
			option = readStringFromSystemin("1", new String[] { "1 - Ok,Upload", "2 - Cancel" });
			if (option.equalsIgnoreCase("2")) {
				System.exit(0);
			}
		}

		Object OkUploadConfList = new ArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(chosedUploadCfPath)));
		for (;;) {
			String readLine = br.readLine();
			if (readLine == null)
				break;
			if (readLine.trim().length() > 0) {
				FtpUploadConf expandFtpUploadConf = new FtpUploadConf();
				expandFtpUploadConf.setClientPath(readLine.split("<->")[0].trim());
				expandFtpUploadConf.setServerPath(readLine.split("<->")[1].trim());
				((List) OkUploadConfList).add(expandFtpUploadConf);
			}
		}
		expandFtpUploadConfList = (List<FtpUploadConf>) OkUploadConfList;

		XFtp ftp = (XFtp) FTP_MAP.get(conf.getServerType());
		ftp.prepareConnect(conf.getHost(), conf.getPort().intValue(), conf.getUsername(), conf.getPassword(), null);

		for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
			ftp.cdOrMakeIfNotExists(expandFtpUploadConf.getServerPath());
			System.out.println("working... " + expandFtpUploadConf.getServerPath());
		}

		System.out.println("Finished!");
		ftp.disconnect();

		conf = (FtpConf) XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
		expandFtpUploadConfList = new ArrayList();
		for (FtpUploadConf ftpUploadConf : conf.getFtpUploadConfList()) {
			expandFtpUploadConfList.addAll(expandFtpUploadConf(ftpUploadConf));
		}
		formater(expandFtpUploadConfList, wkdir);

		System.exit(0);
	}

	public static List<FtpUploadConf> expandFtpUploadConf(FtpUploadConf ftpUploadConf) {
		String clientPath = ftpUploadConf.getClientPath();
		File file = new File(clientPath);
		List<FtpUploadConf> expandFtpUploadConfList = new ArrayList();
		expandFtpUploadConf(file, ftpUploadConf, expandFtpUploadConfList);
		return expandFtpUploadConfList;
	}

	public static void formater(List<FtpUploadConf> expandFtpUploadConfList, String wkdir) throws Exception {
		Map<String, String> lastModMap = new HashMap();
		for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
			lastModMap.put(expandFtpUploadConf.getClientPath(), expandFtpUploadConf.getFileMd5());
		}
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(wkdir + "/lastModMap.d"));
		os.writeObject(lastModMap);
		os.close();
		System.out.println("Format Success!");
	}

	private static void expandFtpUploadConf(File file, FtpUploadConf ftpUploadConf,
			List<FtpUploadConf> expandFtpUploadConfList) {
		if (file.isHidden())
			return;
		int j;
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			File[] arrayOfFile1;
			j = (arrayOfFile1 = subFiles).length;
			for (int i = 0; i < j; i++) {
				File subFile = arrayOfFile1[i];
				expandFtpUploadConf(subFile, ftpUploadConf, expandFtpUploadConfList);
			}
		} else {
			String clientFileFolderPath = file.getParentFile().getAbsolutePath();

			String[] excludes = ftpUploadConf.getExcludes();
			if (excludes != null) {
				String[] arrayOfString1;
				int k = (arrayOfString1 = excludes).length;
				for (j = 0; j < k; j++) {
					String exclude = arrayOfString1[j];
					if (file.getAbsolutePath().startsWith(exclude.replace('/', '\\').replaceAll("\\\\\\\\", "\\\\"))) {
						return;
					}
				}
			}
			String srClientPath = ftpUploadConf.getClientPath();
			String srServerPath = ftpUploadConf.getServerPath();
			if (!srServerPath.endsWith("/"))
				throw new RuntimeException(srServerPath + " not end with /");
			String abPath = "";
			if (clientFileFolderPath.length() >= srClientPath.length()) {
				abPath = clientFileFolderPath.substring(srClientPath.length()).replace('\\', '/');
			}
			FtpUploadConf expandFtpUploadConf = new FtpUploadConf();
			expandFtpUploadConf.setClientPath(file.getAbsolutePath());
			expandFtpUploadConf.setServerPath(srServerPath + abPath);
			expandFtpUploadConf.setFileMd5(FileMd5.getMD5(file));
			expandFtpUploadConfList.add(expandFtpUploadConf);
		}
	}

	public static byte[] readBytesFromInStream(InputStream in) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			byte[] buf = new byte['ࠀ'];
			int len = 0;
			try {
				while ((len = in.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static String readStringFromSystemin(String defaultVal, String... item) throws Exception {
		Set<String> set = new LinkedHashSet();
		String[] arrayOfString1;
		int j = (arrayOfString1 = item).length;
		for (int i = 0; i < j; i++) {
			String it = arrayOfString1[i];
			String[] os = it.split("-");
			if (os.length != 2)
				throw new RuntimeException(it + " use error '-' flag!");
			set.add(os[0].trim());
			System.out.println("        " + it);
		}
		String r;
		do {
			System.out.print("Please Chose " + Arrays.toString(set.toArray()) + " "
					+ (defaultVal == null ? "" : defaultVal) + ":");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			r = br.readLine();
			if ((r.trim().length() == 0) && (defaultVal != null)) {
				return defaultVal;
			}
		} while (!set.contains(r));

		return r;
	}
}
