package sexftp.startup;

import org.eclipse.ui.IStartup;

public class StartUp implements IStartup {
	public void earlyStartup() {
		org.sexftp.core.license.LicenseUtils.checkUpdateAndLicense("StartUp");
	}
}
