/*    */ package examples.cidr;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Arrays;
/*    */ import java.util.Scanner;
/*    */ import org.apache.commons.net.util.SubnetUtils;
/*    */ import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubnetUtilsExample
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 33 */     String subnet = "192.168.0.3/31";
/* 34 */     SubnetUtils utils = new SubnetUtils(subnet);
/* 35 */     SubnetUtils.SubnetInfo info = utils.getInfo();
/*    */     
/* 37 */     System.out.printf("Subnet Information for %s:\n", new Object[] { subnet });
/* 38 */     System.out.println("--------------------------------------");
/* 39 */     System.out.printf("IP Address:\t\t\t%s\t[%s]\n", new Object[] { info.getAddress(), 
/* 40 */       Integer.toBinaryString(info.asInteger(info.getAddress())) });
/* 41 */     System.out.printf("Netmask:\t\t\t%s\t[%s]\n", new Object[] { info.getNetmask(), 
/* 42 */       Integer.toBinaryString(info.asInteger(info.getNetmask())) });
/* 43 */     System.out.printf("CIDR Representation:\t\t%s\n\n", new Object[] { info.getCidrSignature() });
/*    */     
/* 45 */     System.out.printf("Supplied IP Address:\t\t%s\n\n", new Object[] { info.getAddress() });
/*    */     
/* 47 */     System.out.printf("Network Address:\t\t%s\t[%s]\n", new Object[] { info.getNetworkAddress(), 
/* 48 */       Integer.toBinaryString(info.asInteger(info.getNetworkAddress())) });
/* 49 */     System.out.printf("Broadcast Address:\t\t%s\t[%s]\n", new Object[] { info.getBroadcastAddress(), 
/* 50 */       Integer.toBinaryString(info.asInteger(info.getBroadcastAddress())) });
/* 51 */     System.out.printf("Low Address:\t\t\t%s\t[%s]\n", new Object[] { info.getLowAddress(), 
/* 52 */       Integer.toBinaryString(info.asInteger(info.getLowAddress())) });
/* 53 */     System.out.printf("High Address:\t\t\t%s\t[%s]\n", new Object[] { info.getHighAddress(), 
/* 54 */       Integer.toBinaryString(info.asInteger(info.getHighAddress())) });
/*    */     
/* 56 */     System.out.printf("Total usable addresses: \t%d\n", new Object[] { Integer.valueOf(info.getAddressCount()) });
/* 57 */     System.out.printf("Address List: %s\n\n", new Object[] { Arrays.toString(info.getAllAddresses()) });
/*    */     
/*    */ 
/* 60 */     System.out.println("Enter an IP address (e.g. 192.168.0.10):");
/* 61 */     Scanner scanner = new Scanner(System.in);
/* 62 */     while (scanner.hasNextLine()) {
/* 63 */       String address = scanner.nextLine();
/* 64 */       System.out.println("The IP address [" + address + "] is " + (
/* 65 */         info.isInRange(address) ? "" : "not ") + 
/* 66 */         "within the subnet [" + subnet + "]");
/* 67 */       System.out.println("Enter an IP address (e.g. 192.168.0.10):");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\cidr\SubnetUtilsExample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */