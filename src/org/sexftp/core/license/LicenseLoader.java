package org.sexftp.core.license;

public class LicenseLoader extends ClassLoader {
	private byte[] data;

	private String clsname;

	public LicenseLoader(byte[] data, String clsname) {
		this.data = data;
		this.clsname = clsname;
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (this.clsname.equals(name))
			return super.defineClass(name, this.data, 0, this.data.length);
		Class<?> loadClass = getClass().getClassLoader().loadClass(name);
		if (loadClass != null)
			return loadClass;
		return super.loadClass(name);
	}
}
