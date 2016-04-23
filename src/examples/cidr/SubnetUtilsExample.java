package examples.cidr;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

public class SubnetUtilsExample {
	public static void main(String[] args) {
		String subnet = "192.168.0.3/31";
		SubnetUtils utils = new SubnetUtils(subnet);
		SubnetUtils.SubnetInfo info = utils.getInfo();

		System.out.printf("Subnet Information for %s:\n", new Object[] { subnet });
		System.out.println("--------------------------------------");
		System.out.printf("IP Address:\t\t\t%s\t[%s]\n",
				new Object[] { info.getAddress(), Integer.toBinaryString(info.asInteger(info.getAddress())) });
		System.out.printf("Netmask:\t\t\t%s\t[%s]\n",
				new Object[] { info.getNetmask(), Integer.toBinaryString(info.asInteger(info.getNetmask())) });
		System.out.printf("CIDR Representation:\t\t%s\n\n", new Object[] { info.getCidrSignature() });

		System.out.printf("Supplied IP Address:\t\t%s\n\n", new Object[] { info.getAddress() });

		System.out.printf("Network Address:\t\t%s\t[%s]\n", new Object[] { info.getNetworkAddress(),
				Integer.toBinaryString(info.asInteger(info.getNetworkAddress())) });
		System.out.printf("Broadcast Address:\t\t%s\t[%s]\n", new Object[] { info.getBroadcastAddress(),
				Integer.toBinaryString(info.asInteger(info.getBroadcastAddress())) });
		System.out.printf("Low Address:\t\t\t%s\t[%s]\n",
				new Object[] { info.getLowAddress(), Integer.toBinaryString(info.asInteger(info.getLowAddress())) });
		System.out.printf("High Address:\t\t\t%s\t[%s]\n",
				new Object[] { info.getHighAddress(), Integer.toBinaryString(info.asInteger(info.getHighAddress())) });

		System.out.printf("Total usable addresses: \t%d\n", new Object[] { Integer.valueOf(info.getAddressCount()) });
		System.out.printf("Address List: %s\n\n", new Object[] { Arrays.toString(info.getAllAddresses()) });

		System.out.println("Enter an IP address (e.g. 192.168.0.10):");
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String address = scanner.nextLine();
			System.out.println("The IP address [" + address + "] is " + (info.isInRange(address) ? "" : "not ")
					+ "within the subnet [" + subnet + "]");
			System.out.println("Enter an IP address (e.g. 192.168.0.10):");
		}
	}
}
