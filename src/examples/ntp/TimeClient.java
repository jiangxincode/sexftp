package examples.ntp;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import org.apache.commons.net.time.TimeUDPClient;

public final class TimeClient {
	/* Error */
	public static final void timeTCP(String host) throws IOException {
		// Byte code:
		// 0: new 19 org/apache/commons/net/time/TimeTCPClient
		// 3: dup
		// 4: invokespecial 21 org/apache/commons/net/time/TimeTCPClient:<init>
		// ()V
		// 7: astore_1
		// 8: aload_1
		// 9: ldc 22
		// 11: invokevirtual 23
		// org/apache/commons/net/time/TimeTCPClient:setDefaultTimeout (I)V
		// 14: aload_1
		// 15: aload_0
		// 16: invokevirtual 27
		// org/apache/commons/net/time/TimeTCPClient:connect
		// (Ljava/lang/String;)V
		// 19: getstatic 30 java/lang/System:out Ljava/io/PrintStream;
		// 22: aload_1
		// 23: invokevirtual 36
		// org/apache/commons/net/time/TimeTCPClient:getDate ()Ljava/util/Date;
		// 26: invokevirtual 40 java/io/PrintStream:println
		// (Ljava/lang/Object;)V
		// 29: goto +10 -> 39
		// 32: astore_2
		// 33: aload_1
		// 34: invokevirtual 46
		// org/apache/commons/net/time/TimeTCPClient:disconnect ()V
		// 37: aload_2
		// 38: athrow
		// 39: aload_1
		// 40: invokevirtual 46
		// org/apache/commons/net/time/TimeTCPClient:disconnect ()V
		// 43: return
		// Line number table:
		// Java source line #44 -> byte code offset #0
		// Java source line #47 -> byte code offset #8
		// Java source line #48 -> byte code offset #14
		// Java source line #49 -> byte code offset #19
		// Java source line #50 -> byte code offset #32
		// Java source line #51 -> byte code offset #33
		// Java source line #52 -> byte code offset #37
		// Java source line #51 -> byte code offset #39
		// Java source line #53 -> byte code offset #43
		// Local variable table:
		// start length slot name signature
		// 0 44 0 host String
		// 7 33 1 client org.apache.commons.net.time.TimeTCPClient
		// 32 6 2 localObject Object
		// Exception table:
		// from to target type
		// 8 32 32 finally
	}

	public static final void timeUDP(String host) throws IOException {
		TimeUDPClient client = new TimeUDPClient();

		client.setDefaultTimeout(60000);
		client.open();
		System.out.println(client.getDate(InetAddress.getByName(host)));
		client.close();
	}

	public static final void main(String[] args) {
		if (args.length == 1) {
			try {
				timeTCP(args[0]);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else if ((args.length == 2) && (args[0].equals("-udp"))) {
			try {
				timeUDP(args[1]);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			System.err.println("Usage: TimeClient [-udp] <hostname>");
			System.exit(1);
		}
	}
}
