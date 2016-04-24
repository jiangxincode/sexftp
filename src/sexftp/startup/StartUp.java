package sexftp.startup;

import org.eclipse.ui.IStartup;
import org.sexftp.core.license.LicenseUtils;

public class StartUp implements IStartup {
	public void earlyStartup() {
		LicenseUtils.checkUpdateAndLicense("StartUp");
	}
}
