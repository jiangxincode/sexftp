package org.sexftp.core.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.sexftp.core.ftp.bean.FtpFile;
import sexftp.views.IFtpStreamMonitor;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

public class BACKMyFtp implements XFtp {
	FtpClient ftpClient;

	public void delete(String deleteFile) {
	}

	public void download(String downloadFile, String saveFile, IFtpStreamMonitor monitor) {
	}

	public List<FtpFile> listFiles() {
		return null;
	}

	public void prepareConnect(String host, int port, String username, String password, String encode) {
		/*
		 * try { this.ftpClient = new FtpClient(); System.out.println(host + ":"
		 * + port + "@" + username + " logining ...");
		 * this.ftpClient.openServer(host, port);
		 *
		 * this.ftpClient.login(username, password);
		 *
		 * System.out.println("login success!");
		 *
		 * } catch (IOException ex) {
		 *
		 * throw new RuntimeException(ex); }
		 */
	}

	public void delete(String directory, String deleteFile) {
	}

	public boolean isConnect() {
		if (this.ftpClient == null)
			return false;
		return false;
		// return this.ftpClient.serverIsOpen();
	}

	public void disconnect() {
		/*
		 * try { this.ftpClient.closeServer();
		 *
		 * System.out.println("disconnect success");
		 *
		 *
		 * } catch (IOException ex) {
		 *
		 * System.out.println("not disconnect");
		 *
		 * System.out.println(ex); }
		 */
	}

	public void download(String directory, String downloadFile, String saveFile) {
	}

	/* Error */
	public List<String> listFiles(String directory) {
		return null;
		// Byte code:
		// 0: aload_0
		// 1: aload_1
		// 2: invokevirtual 122 org/sexftp/core/ftp/BACKMyFtp:cd
		// (Ljava/lang/String;)V
		// 5: new 125 java/util/ArrayList
		// 8: dup
		// 9: invokespecial 127 java/util/ArrayList:<init> ()V
		// 12: astore_2
		// 13: aconst_null
		// 14: astore_3
		// 15: ldc -128
		// 17: astore 4
		// 19: new 130 java/io/BufferedReader
		// 22: dup
		// 23: new 132 java/io/InputStreamReader
		// 26: dup
		// 27: aload_0
		// 28: getfield 37 org/sexftp/core/ftp/BACKMyFtp:ftpClient
		// Lsun/net/ftp/FtpClient;
		// 31: invokevirtual 134 sun/net/ftp/FtpClient:list
		// ()Lsun/net/TelnetInputStream;
		// 34: invokespecial 138 java/io/InputStreamReader:<init>
		// (Ljava/io/InputStream;)V
		// 37: invokespecial 141 java/io/BufferedReader:<init>
		// (Ljava/io/Reader;)V
		// 40: astore_3
		// 41: goto +12 -> 53
		// 44: aload_2
		// 45: aload 4
		// 47: invokeinterface 144 2 0
		// 52: pop
		// 53: aload_3
		// 54: invokevirtual 150 java/io/BufferedReader:readLine
		// ()Ljava/lang/String;
		// 57: dup
		// 58: astore 4
		// 60: ifnonnull -16 -> 44
		// 63: goto +32 -> 95
		// 66: pop
		// 67: ldc -103
		// 69: astore 4
		// 71: aload_3
		// 72: invokevirtual 155 java/io/BufferedReader:close ()V
		// 75: goto +28 -> 103
		// 78: pop
		// 79: goto +24 -> 103
		// 82: astore 5
		// 84: aload_3
		// 85: invokevirtual 155 java/io/BufferedReader:close ()V
		// 88: goto +4 -> 92
		// 91: pop
		// 92: aload 5
		// 94: athrow
		// 95: aload_3
		// 96: invokevirtual 155 java/io/BufferedReader:close ()V
		// 99: goto +4 -> 103
		// 102: pop
		// 103: aload_2
		// 104: areturn
		// Line number table:
		// Java source line #102 -> byte code offset #0
		// Java source line #103 -> byte code offset #5
		// Java source line #104 -> byte code offset #13
		// Java source line #105 -> byte code offset #15
		// Java source line #107 -> byte code offset #19
		// Java source line #108 -> byte code offset #41
		// Java source line #109 -> byte code offset #44
		// Java source line #108 -> byte code offset #53
		// Java source line #111 -> byte code offset #66
		// Java source line #112 -> byte code offset #67
		// Java source line #116 -> byte code offset #71
		// Java source line #117 -> byte code offset #78
		// Java source line #114 -> byte code offset #82
		// Java source line #116 -> byte code offset #84
		// Java source line #117 -> byte code offset #91
		// Java source line #120 -> byte code offset #92
		// Java source line #116 -> byte code offset #95
		// Java source line #117 -> byte code offset #102
		// Java source line #122 -> byte code offset #103
		// Local variable table:
		// start length slot name signature
		// 0 105 0 this BACKMyFtp
		// 0 105 1 directory String
		// 12 92 2 downList List<String>
		// 14 82 3 dis java.io.BufferedReader
		// 17 53 4 dstr String
		// 82 11 5 localObject Object
		// 66 1 6 localException1 Exception
		// 78 1 7 localException2 Exception
		// 91 1 8 localException3 Exception
		// 102 1 9 localException4 Exception
		// Exception table:
		// from to target type
		// 19 63 66 java/lang/Exception
		// 71 75 78 java/lang/Exception
		// 19 71 82 finally
		// 84 88 91 java/lang/Exception
		// 95 99 102 java/lang/Exception
	}

	public void cd(String directory) {
		/*
		 * try { if (directory.length() != 0) { cd(directory); }
		 * this.ftpClient.binary(); } catch (IOException e) { throw new
		 * RuntimeException("mkdirs " + directory + " fail!" + e, e); }
		 */
	}

	public void cdOrMakeIfNotExists(String directory) {
		/*
		 * try { if (directory.length() != 0) { this.ftpClient.cd(directory); }
		 * this.ftpClient.binary(); } catch (IOException localIOException1) {
		 * try { mkdirs(directory); } catch (IOException e1) { throw new
		 * RuntimeException("mkdirs " + directory + " fail!" + e1, e1); } }
		 */
	}

	private void mkdirs(String directory) throws IOException {
		/*
		 * String[] dirs = directory.split("/"); if (directory.startsWith("/"))
		 * { cd("/"); } String[] arrayOfString1; int j = (arrayOfString1 =
		 * dirs).length; for (int i = 0; i < j; i++) { String dir =
		 * arrayOfString1[i]; if (dir.length() > 0) { try { cd(dir); } catch
		 * (IOException localIOException) { this.ftpClient.sendServer("MKD " +
		 * dir + "\r\n"); cd(dir); this.ftpClient.readServerResponse(); } } }
		 * this.ftpClient.binary();
		 */
	}

	/* Error */
	public void upload(String uploadFile, IFtpStreamMonitor monitor) {
		// Byte code:
		// 0: aconst_null
		// 1: astore_3
		// 2: aconst_null
		// 3: astore 4
		// 5: new 217 java/io/File
		// 8: dup
		// 9: aload_1
		// 10: invokespecial 219 java/io/File:<init> (Ljava/lang/String;)V
		// 13: astore 5
		// 15: ldc -128
		// 17: astore 6
		// 19: aconst_null
		// 20: astore 7
		// 22: new 220 java/io/DataInputStream
		// 25: dup
		// 26: aload_0
		// 27: getfield 37 org/sexftp/core/ftp/BACKMyFtp:ftpClient
		// Lsun/net/ftp/FtpClient;
		// 30: aload 5
		// 32: invokevirtual 222 java/io/File:getName ()Ljava/lang/String;
		// 35: invokevirtual 225 sun/net/ftp/FtpClient:nameList
		// (Ljava/lang/String;)Lsun/net/TelnetInputStream;
		// 38: invokespecial 229 java/io/DataInputStream:<init>
		// (Ljava/io/InputStream;)V
		// 41: astore 7
		// 43: aload 7
		// 45: invokevirtual 230 java/io/DataInputStream:readLine
		// ()Ljava/lang/String;
		// 48: dup
		// 49: astore 6
		// 51: ifnull +66 -> 117
		// 54: new 45 java/lang/StringBuilder
		// 57: dup
		// 58: ldc -25
		// 60: invokespecial 53 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 63: aload 6
		// 65: invokevirtual 57 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 68: ldc -23
		// 70: invokevirtual 57 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 73: invokevirtual 68 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 76: astore 6
		// 78: aload 7
		// 80: invokevirtual 235 java/io/DataInputStream:close ()V
		// 83: goto +34 -> 117
		// 86: pop
		// 87: ldc -103
		// 89: astore 6
		// 91: aload 7
		// 93: invokevirtual 235 java/io/DataInputStream:close ()V
		// 96: goto +30 -> 126
		// 99: pop
		// 100: goto +26 -> 126
		// 103: astore 8
		// 105: aload 7
		// 107: invokevirtual 235 java/io/DataInputStream:close ()V
		// 110: goto +4 -> 114
		// 113: pop
		// 114: aload 8
		// 116: athrow
		// 117: aload 7
		// 119: invokevirtual 235 java/io/DataInputStream:close ()V
		// 122: goto +4 -> 126
		// 125: pop
		// 126: aload_0
		// 127: getfield 37 org/sexftp/core/ftp/BACKMyFtp:ftpClient
		// Lsun/net/ftp/FtpClient;
		// 130: aload 5
		// 132: invokevirtual 222 java/io/File:getName ()Ljava/lang/String;
		// 135: invokevirtual 236 sun/net/ftp/FtpClient:put
		// (Ljava/lang/String;)Lsun/net/TelnetOutputStream;
		// 138: astore_3
		// 139: new 240 java/io/FileInputStream
		// 142: dup
		// 143: aload 5
		// 145: invokespecial 242 java/io/FileInputStream:<init>
		// (Ljava/io/File;)V
		// 148: astore 4
		// 150: aload 4
		// 152: invokevirtual 245 java/io/FileInputStream:available ()I
		// 155: istore 8
		// 157: sipush 1024
		// 160: newarray <illegal type>
		// 162: astore 9
		// 164: iconst_0
		// 165: istore 11
		// 167: goto +34 -> 201
		// 170: aload_3
		// 171: aload 9
		// 173: iconst_0
		// 174: iload 10
		// 176: invokevirtual 248 sun/net/TelnetOutputStream:write ([BII)V
		// 179: iload 11
		// 181: iload 10
		// 183: iadd
		// 184: istore 11
		// 186: aload_2
		// 187: aconst_null
		// 188: iload 11
		// 190: i2l
		// 191: iload 8
		// 193: i2l
		// 194: ldc -128
		// 196: invokeinterface 254 7 0
		// 201: aload 4
		// 203: aload 9
		// 205: invokevirtual 260 java/io/FileInputStream:read ([B)I
		// 208: dup
		// 209: istore 10
		// 211: iconst_m1
		// 212: if_icmpne -42 -> 170
		// 215: aload_2
		// 216: new 45 java/lang/StringBuilder
		// 219: dup
		// 220: ldc_w 264
		// 223: invokespecial 53 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 226: aload 5
		// 228: invokevirtual 222 java/io/File:getName ()Ljava/lang/String;
		// 231: invokevirtual 57 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 234: ldc_w 266
		// 237: invokevirtual 57 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 240: aload 6
		// 242: invokevirtual 57 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 245: invokevirtual 68 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 248: invokeinterface 268 2 0
		// 253: aload 4
		// 255: invokevirtual 271 java/io/FileInputStream:close ()V
		// 258: aload_3
		// 259: invokevirtual 272 sun/net/TelnetOutputStream:close ()V
		// 262: goto +42 -> 304
		// 265: astore 5
		// 267: aload 5
		// 269: athrow
		// 270: astore 5
		// 272: new 87 java/lang/RuntimeException
		// 275: dup
		// 276: aload 5
		// 278: invokespecial 89 java/lang/RuntimeException:<init>
		// (Ljava/lang/Throwable;)V
		// 281: athrow
		// 282: astore 12
		// 284: aload 4
		// 286: invokevirtual 271 java/io/FileInputStream:close ()V
		// 289: goto +4 -> 293
		// 292: pop
		// 293: aload_3
		// 294: invokevirtual 272 sun/net/TelnetOutputStream:close ()V
		// 297: goto +4 -> 301
		// 300: pop
		// 301: aload 12
		// 303: athrow
		// 304: aload 4
		// 306: invokevirtual 271 java/io/FileInputStream:close ()V
		// 309: goto +4 -> 313
		// 312: pop
		// 313: aload_3
		// 314: invokevirtual 272 sun/net/TelnetOutputStream:close ()V
		// 317: goto +4 -> 321
		// 320: pop
		// 321: return
		// Line number table:
		// Java source line #178 -> byte code offset #0
		// Java source line #179 -> byte code offset #2
		// Java source line #182 -> byte code offset #5
		// Java source line #185 -> byte code offset #15
		// Java source line #186 -> byte code offset #19
		// Java source line #188 -> byte code offset #22
		// Java source line #189 -> byte code offset #43
		// Java source line #190 -> byte code offset #54
		// Java source line #191 -> byte code offset #78
		// Java source line #192 -> byte code offset #83
		// Java source line #194 -> byte code offset #86
		// Java source line #195 -> byte code offset #87
		// Java source line #200 -> byte code offset #91
		// Java source line #201 -> byte code offset #99
		// Java source line #197 -> byte code offset #103
		// Java source line #200 -> byte code offset #105
		// Java source line #201 -> byte code offset #113
		// Java source line #204 -> byte code offset #114
		// Java source line #200 -> byte code offset #117
		// Java source line #201 -> byte code offset #125
		// Java source line #208 -> byte code offset #126
		// Java source line #209 -> byte code offset #139
		// Java source line #210 -> byte code offset #150
		// Java source line #213 -> byte code offset #157
		// Java source line #216 -> byte code offset #164
		// Java source line #217 -> byte code offset #167
		// Java source line #219 -> byte code offset #170
		// Java source line #220 -> byte code offset #179
		// Java source line #221 -> byte code offset #186
		// Java source line #217 -> byte code offset #201
		// Java source line #224 -> byte code offset #215
		// Java source line #226 -> byte code offset #253
		// Java source line #228 -> byte code offset #258
		// Java source line #231 -> byte code offset #265
		// Java source line #232 -> byte code offset #267
		// Java source line #234 -> byte code offset #270
		// Java source line #235 -> byte code offset #272
		// Java source line #237 -> byte code offset #282
		// Java source line #239 -> byte code offset #284
		// Java source line #240 -> byte code offset #292
		// Java source line #244 -> byte code offset #293
		// Java source line #245 -> byte code offset #300
		// Java source line #249 -> byte code offset #301
		// Java source line #239 -> byte code offset #304
		// Java source line #240 -> byte code offset #312
		// Java source line #244 -> byte code offset #313
		// Java source line #245 -> byte code offset #320
		// Java source line #250 -> byte code offset #321
		// Local variable table:
		// start length slot name signature
		// 0 322 0 this BACKMyFtp
		// 0 322 1 uploadFile String
		// 0 322 2 monitor IFtpStreamMonitor
		// 1 313 3 os TelnetOutputStream
		// 3 302 4 is FileInputStream
		// 13 214 5 file_in File
		// 265 3 5 e org.sexftp.core.exceptions.AbortException
		// 270 7 5 e Exception
		// 17 224 6 dstr String
		// 20 98 7 dis java.io.DataInputStream
		// 103 12 8 localObject1 Object
		// 155 37 8 avi int
		// 162 42 9 bytes byte[]
		// 170 12 10 c int
		// 209 3 10 c int
		// 165 24 11 t int
		// 282 20 12 localObject2 Object
		// 86 1 17 localException1 Exception
		// 99 1 18 localException2 Exception
		// 113 1 19 localException3 Exception
		// 125 1 20 localException4 Exception
		// 292 1 21 localException5 Exception
		// 300 1 22 localException6 Exception
		// 312 1 23 localException7 Exception
		// 320 1 24 localException8 Exception
		// Exception table:
		// from to target type
		// 22 83 86 java/lang/Exception
		// 91 96 99 java/lang/Exception
		// 22 91 103 finally
		// 105 110 113 java/lang/Exception
		// 117 122 125 java/lang/Exception
		// 5 262 265 org/sexftp/core/exceptions/AbortException
		// 5 262 270 java/lang/Exception
		// 5 282 282 finally
		// 284 289 292 java/lang/Exception
		// 293 297 300 java/lang/Exception
		// 304 309 312 java/lang/Exception
		// 313 317 320 java/lang/Exception
	}

	public void upload(String[] fileList) {
		for (int i = 0; i < fileList.length; i++) {

			try {

				TelnetOutputStream os = null;
				// this.ftpClient.put(fileList[i]);

				File file_in = new File("e://12//" + fileList[i]);

				FileInputStream is = new FileInputStream(file_in);

				byte[] bytes = new byte['Ѐ'];

				int c;

				while ((c = is.read(bytes)) != -1) {

					os.write(bytes, 0, c);
				}

				System.out.println("upload success");

				is.close();

				os.close();
			} catch (IOException ex) {
				System.out.println("not upload");

				System.out.println(ex);
			}
		}
	}

	public void completed() {
	}

	public static void main(String[] args) {
	}

	public void connect() {
	}
}
