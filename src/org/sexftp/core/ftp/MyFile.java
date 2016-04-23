package org.sexftp.core.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.sexftp.core.exceptions.FtpNoSuchFileException;
import org.sexftp.core.exceptions.SRuntimeException;
import org.sexftp.core.ftp.bean.FtpFile;

public class MyFile implements XFtp {
	private String curDir;

	public void prepareConnect(String host, int port, String username, String password, String encode) {
	}

	public void delete(String deleteFile) {
	}

	public void disconnect() {
	}

	public List<FtpFile> listFiles() {
		try {
			List<FtpFile> list = new ArrayList();
			File[] listFiles = new File(this.curDir).listFiles();
			if (listFiles != null) {
				File[] arrayOfFile1;
				int j = (arrayOfFile1 = listFiles).length;
				for (int i = 0; i < j; i++) {
					File file = arrayOfFile1[i];
					Calendar instance = Calendar.getInstance();
					instance.setTime(new Date(file.lastModified()));
					long available = 0L;
					if (file.isFile()) {
						available = file.length();
					}

					FtpFile ftpfile = new FtpFile(file.getName(), file.isDirectory(), available, instance);
					list.add(ftpfile);
				}
			}
			return list;
		} catch (Exception e) {
			throw new SRuntimeException(e);
		}
	}

	public void cd(String directory) {
		if (!new File(directory).exists()) {
			throw new FtpNoSuchFileException("No Such Folder:" + directory);
		}
		this.curDir = directory;
	}

	/* Error */
	public void download(String downloadFile, String saveFile, sexftp.views.IFtpStreamMonitor monitor) {
		// Byte code:
		// 0: aconst_null
		// 1: astore 4
		// 3: aconst_null
		// 4: astore 5
		// 6: new 37 java/io/File
		// 9: dup
		// 10: new 118 java/lang/StringBuilder
		// 13: dup
		// 14: aload_0
		// 15: getfield 39 org/sexftp/core/ftp/MyFile:curDir Ljava/lang/String;
		// 18: invokestatic 134 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 21: invokespecial 122 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 24: ldc -116
		// 26: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 29: aload_1
		// 30: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 33: invokevirtual 127 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 36: invokespecial 41 java/io/File:<init> (Ljava/lang/String;)V
		// 39: astore 6
		// 41: new 142 java/io/FileOutputStream
		// 44: dup
		// 45: aload_2
		// 46: invokespecial 144 java/io/FileOutputStream:<init>
		// (Ljava/lang/String;)V
		// 49: astore 4
		// 51: new 145 java/io/FileInputStream
		// 54: dup
		// 55: aload 6
		// 57: invokespecial 147 java/io/FileInputStream:<init>
		// (Ljava/io/File;)V
		// 60: astore 5
		// 62: aload 6
		// 64: invokevirtual 69 java/io/File:length ()J
		// 67: lstore 7
		// 69: sipush 1024
		// 72: newarray <illegal type>
		// 74: astore 9
		// 76: iconst_0
		// 77: istore 11
		// 79: goto +34 -> 113
		// 82: aload 4
		// 84: aload 9
		// 86: iconst_0
		// 87: iload 10
		// 89: invokevirtual 150 java/io/OutputStream:write ([BII)V
		// 92: iload 11
		// 94: iload 10
		// 96: iadd
		// 97: istore 11
		// 99: aload_3
		// 100: aconst_null
		// 101: iload 11
		// 103: i2l
		// 104: lload 7
		// 106: ldc -100
		// 108: invokeinterface 158 7 0
		// 113: aload 5
		// 115: aload 9
		// 117: invokevirtual 164 java/io/FileInputStream:read ([B)I
		// 120: dup
		// 121: istore 10
		// 123: iconst_m1
		// 124: if_icmpne -42 -> 82
		// 127: aload_3
		// 128: new 118 java/lang/StringBuilder
		// 131: dup
		// 132: ldc -88
		// 134: invokespecial 122 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 137: aload 6
		// 139: invokevirtual 74 java/io/File:getName ()Ljava/lang/String;
		// 142: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 145: invokevirtual 127 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 148: invokeinterface 170 2 0
		// 153: aload 5
		// 155: invokevirtual 173 java/io/FileInputStream:close ()V
		// 158: aload 4
		// 160: invokevirtual 176 java/io/OutputStream:close ()V
		// 163: goto +43 -> 206
		// 166: astore 6
		// 168: aload 6
		// 170: athrow
		// 171: astore 6
		// 173: new 177 java/lang/RuntimeException
		// 176: dup
		// 177: aload 6
		// 179: invokespecial 179 java/lang/RuntimeException:<init>
		// (Ljava/lang/Throwable;)V
		// 182: athrow
		// 183: astore 12
		// 185: aload 5
		// 187: invokevirtual 173 java/io/FileInputStream:close ()V
		// 190: goto +4 -> 194
		// 193: pop
		// 194: aload 4
		// 196: invokevirtual 176 java/io/OutputStream:close ()V
		// 199: goto +4 -> 203
		// 202: pop
		// 203: aload 12
		// 205: athrow
		// 206: aload 5
		// 208: invokevirtual 173 java/io/FileInputStream:close ()V
		// 211: goto +4 -> 215
		// 214: pop
		// 215: aload 4
		// 217: invokevirtual 176 java/io/OutputStream:close ()V
		// 220: goto +4 -> 224
		// 223: pop
		// 224: return
		// Line number table:
		// Java source line #87 -> byte code offset #0
		// Java source line #88 -> byte code offset #3
		// Java source line #91 -> byte code offset #6
		// Java source line #92 -> byte code offset #41
		// Java source line #93 -> byte code offset #51
		// Java source line #94 -> byte code offset #62
		// Java source line #96 -> byte code offset #69
		// Java source line #99 -> byte code offset #76
		// Java source line #100 -> byte code offset #79
		// Java source line #102 -> byte code offset #82
		// Java source line #103 -> byte code offset #92
		// Java source line #104 -> byte code offset #99
		// Java source line #100 -> byte code offset #113
		// Java source line #107 -> byte code offset #127
		// Java source line #109 -> byte code offset #153
		// Java source line #111 -> byte code offset #158
		// Java source line #114 -> byte code offset #166
		// Java source line #115 -> byte code offset #168
		// Java source line #117 -> byte code offset #171
		// Java source line #118 -> byte code offset #173
		// Java source line #120 -> byte code offset #183
		// Java source line #122 -> byte code offset #185
		// Java source line #123 -> byte code offset #193
		// Java source line #127 -> byte code offset #194
		// Java source line #128 -> byte code offset #202
		// Java source line #131 -> byte code offset #203
		// Java source line #122 -> byte code offset #206
		// Java source line #123 -> byte code offset #214
		// Java source line #127 -> byte code offset #215
		// Java source line #128 -> byte code offset #223
		// Java source line #132 -> byte code offset #224
		// Local variable table:
		// start length slot name signature
		// 0 225 0 this MyFile
		// 0 225 1 downloadFile String
		// 0 225 2 saveFile String
		// 0 225 3 monitor sexftp.views.IFtpStreamMonitor
		// 1 215 4 os java.io.OutputStream
		// 4 203 5 is java.io.FileInputStream
		// 39 99 6 file_in File
		// 166 3 6 e org.sexftp.core.exceptions.AbortException
		// 171 7 6 e Exception
		// 67 38 7 avi long
		// 74 42 9 bytes byte[]
		// 82 13 10 c int
		// 121 3 10 c int
		// 77 25 11 t int
		// 183 21 12 localObject Object
		// 193 1 15 localException1 Exception
		// 202 1 16 localException2 Exception
		// 214 1 17 localException3 Exception
		// 223 1 18 localException4 Exception
		// Exception table:
		// from to target type
		// 6 163 166 org/sexftp/core/exceptions/AbortException
		// 6 163 171 java/lang/Exception
		// 6 183 183 finally
		// 185 190 193 java/lang/Exception
		// 194 199 202 java/lang/Exception
		// 206 211 214 java/lang/Exception
		// 215 220 223 java/lang/Exception
	}

	public boolean isConnect() {
		return true;
	}

	public void cdOrMakeIfNotExists(String directory) {
		this.curDir = directory;
		if (!new File(this.curDir).exists()) {
			new File(this.curDir).mkdirs();
		}
	}

	/* Error */
	public void upload(String uploadFile, sexftp.views.IFtpStreamMonitor monitor) {
		// Byte code:
		// 0: aconst_null
		// 1: astore_3
		// 2: aconst_null
		// 3: astore 4
		// 5: new 37 java/io/File
		// 8: dup
		// 9: aload_1
		// 10: invokespecial 41 java/io/File:<init> (Ljava/lang/String;)V
		// 13: astore 5
		// 15: new 37 java/io/File
		// 18: dup
		// 19: new 118 java/lang/StringBuilder
		// 22: dup
		// 23: aload_0
		// 24: getfield 39 org/sexftp/core/ftp/MyFile:curDir Ljava/lang/String;
		// 27: invokestatic 134 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 30: invokespecial 122 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 33: ldc -116
		// 35: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 38: aload 5
		// 40: invokevirtual 74 java/io/File:getName ()Ljava/lang/String;
		// 43: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 46: invokevirtual 127 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 49: invokespecial 41 java/io/File:<init> (Ljava/lang/String;)V
		// 52: invokevirtual 113 java/io/File:exists ()Z
		// 55: ifeq +3 -> 58
		// 58: new 142 java/io/FileOutputStream
		// 61: dup
		// 62: new 118 java/lang/StringBuilder
		// 65: dup
		// 66: aload_0
		// 67: getfield 39 org/sexftp/core/ftp/MyFile:curDir Ljava/lang/String;
		// 70: invokestatic 134 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 73: invokespecial 122 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 76: ldc -116
		// 78: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 81: aload 5
		// 83: invokevirtual 74 java/io/File:getName ()Ljava/lang/String;
		// 86: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 89: invokevirtual 127 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 92: invokespecial 144 java/io/FileOutputStream:<init>
		// (Ljava/lang/String;)V
		// 95: astore_3
		// 96: new 145 java/io/FileInputStream
		// 99: dup
		// 100: aload 5
		// 102: invokespecial 147 java/io/FileInputStream:<init>
		// (Ljava/io/File;)V
		// 105: astore 4
		// 107: aload 5
		// 109: invokevirtual 69 java/io/File:length ()J
		// 112: lstore 6
		// 114: sipush 1024
		// 117: newarray <illegal type>
		// 119: astore 8
		// 121: iconst_0
		// 122: istore 10
		// 124: goto +33 -> 157
		// 127: aload_3
		// 128: aload 8
		// 130: iconst_0
		// 131: iload 9
		// 133: invokevirtual 150 java/io/OutputStream:write ([BII)V
		// 136: iload 10
		// 138: iload 9
		// 140: iadd
		// 141: istore 10
		// 143: aload_2
		// 144: aconst_null
		// 145: iload 10
		// 147: i2l
		// 148: lload 6
		// 150: ldc -100
		// 152: invokeinterface 158 7 0
		// 157: aload 4
		// 159: aload 8
		// 161: invokevirtual 164 java/io/FileInputStream:read ([B)I
		// 164: dup
		// 165: istore 9
		// 167: iconst_m1
		// 168: if_icmpne -41 -> 127
		// 171: aload_2
		// 172: new 118 java/lang/StringBuilder
		// 175: dup
		// 176: ldc -52
		// 178: invokespecial 122 java/lang/StringBuilder:<init>
		// (Ljava/lang/String;)V
		// 181: aload 5
		// 183: invokevirtual 74 java/io/File:getName ()Ljava/lang/String;
		// 186: invokevirtual 123 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 189: invokevirtual 127 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 192: invokeinterface 170 2 0
		// 197: aload 4
		// 199: invokevirtual 173 java/io/FileInputStream:close ()V
		// 202: aload_3
		// 203: invokevirtual 176 java/io/OutputStream:close ()V
		// 206: goto +42 -> 248
		// 209: astore 5
		// 211: aload 5
		// 213: athrow
		// 214: astore 5
		// 216: new 177 java/lang/RuntimeException
		// 219: dup
		// 220: aload 5
		// 222: invokespecial 179 java/lang/RuntimeException:<init>
		// (Ljava/lang/Throwable;)V
		// 225: athrow
		// 226: astore 11
		// 228: aload 4
		// 230: invokevirtual 173 java/io/FileInputStream:close ()V
		// 233: goto +4 -> 237
		// 236: pop
		// 237: aload_3
		// 238: invokevirtual 176 java/io/OutputStream:close ()V
		// 241: goto +4 -> 245
		// 244: pop
		// 245: aload 11
		// 247: athrow
		// 248: aload 4
		// 250: invokevirtual 173 java/io/FileInputStream:close ()V
		// 253: goto +4 -> 257
		// 256: pop
		// 257: aload_3
		// 258: invokevirtual 176 java/io/OutputStream:close ()V
		// 261: goto +4 -> 265
		// 264: pop
		// 265: return
		// Line number table:
		// Java source line #151 -> byte code offset #0
		// Java source line #152 -> byte code offset #2
		// Java source line #155 -> byte code offset #5
		// Java source line #159 -> byte code offset #15
		// Java source line #167 -> byte code offset #58
		// Java source line #168 -> byte code offset #96
		// Java source line #169 -> byte code offset #107
		// Java source line #171 -> byte code offset #114
		// Java source line #174 -> byte code offset #121
		// Java source line #175 -> byte code offset #124
		// Java source line #177 -> byte code offset #127
		// Java source line #178 -> byte code offset #136
		// Java source line #179 -> byte code offset #143
		// Java source line #175 -> byte code offset #157
		// Java source line #182 -> byte code offset #171
		// Java source line #184 -> byte code offset #197
		// Java source line #186 -> byte code offset #202
		// Java source line #189 -> byte code offset #209
		// Java source line #190 -> byte code offset #211
		// Java source line #192 -> byte code offset #214
		// Java source line #193 -> byte code offset #216
		// Java source line #195 -> byte code offset #226
		// Java source line #197 -> byte code offset #228
		// Java source line #198 -> byte code offset #236
		// Java source line #202 -> byte code offset #237
		// Java source line #203 -> byte code offset #244
		// Java source line #206 -> byte code offset #245
		// Java source line #197 -> byte code offset #248
		// Java source line #198 -> byte code offset #256
		// Java source line #202 -> byte code offset #257
		// Java source line #203 -> byte code offset #264
		// Java source line #207 -> byte code offset #265
		// Local variable table:
		// start length slot name signature
		// 0 266 0 this MyFile
		// 0 266 1 uploadFile String
		// 0 266 2 monitor sexftp.views.IFtpStreamMonitor
		// 1 257 3 os java.io.OutputStream
		// 3 246 4 is java.io.FileInputStream
		// 13 169 5 file_in File
		// 209 3 5 e org.sexftp.core.exceptions.AbortException
		// 214 7 5 e Exception
		// 112 37 6 avi long
		// 119 41 8 bytes byte[]
		// 127 12 9 c int
		// 165 3 9 c int
		// 122 24 10 t int
		// 226 20 11 localObject Object
		// 236 1 14 localException1 Exception
		// 244 1 15 localException2 Exception
		// 256 1 16 localException3 Exception
		// 264 1 17 localException4 Exception
		// Exception table:
		// from to target type
		// 5 206 209 org/sexftp/core/exceptions/AbortException
		// 5 206 214 java/lang/Exception
		// 5 226 226 finally
		// 228 233 236 java/lang/Exception
		// 237 241 244 java/lang/Exception
		// 248 253 256 java/lang/Exception
		// 257 261 264 java/lang/Exception
	}

	public void connect() {
	}

	public void completed() {
	}
}
