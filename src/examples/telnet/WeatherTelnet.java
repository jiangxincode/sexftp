package examples.telnet;

import examples.util.IOUtil;
import java.io.IOException;
import org.apache.commons.net.telnet.TelnetClient;

public final class WeatherTelnet {
	public static final void main(String[] args) {
		TelnetClient telnet = new TelnetClient();

		try {
			telnet.connect("rainmaker.wunderground.com", 3000);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		IOUtil.readWrite(telnet.getInputStream(), telnet.getOutputStream(), System.in, System.out);

		try {
			telnet.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
}
