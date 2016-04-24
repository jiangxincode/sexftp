// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   DefaultCheckLicense.java

package org.sexftp.checkLisense;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;
import org.desy.common.util.DateTimeUtils;
import org.sexftp.core.exceptions.SFConnectionException;
import org.sexftp.core.license.LicenseLoader;
import org.sexftp.core.license.LicenseUtils;
import org.sexftp.core.utils.*;
import sexftp.uils.LogUtil;

public class DefaultCheckLicense
    implements Runnable
{

    public DefaultCheckLicense()
    {
    }

    public void updateCheckLicense(String actionName)
        throws FileNotFoundException, UnsupportedEncodingException, ParseException
    {
        String key = "nextCanDate";
        if(actionName != null)
            if(actionName.startsWith("Submain"))
                key = "subCanDate";
            else
            if(actionName.startsWith("StartUp"))
                key = "startUpCanDate";
        this.actionName = actionName;
        if(CAN_DATE.get(key) == null)
        {
            File file = new File((new StringBuilder("c:\\temp\\license")).append("/nonecheck/u.d").toString());
            if(!file.exists())
                file = new File((new StringBuilder("c:\\temp\\license")).append("/u.d").toString());
            if(file.exists())
            {
                java.io.InputStream is = new FileInputStream(file);
                byte data[] = FileUtil.readBytesFromInStream(is);
                data = ByteUtils.encryption(data);
                CAN_DATE.put(key, DateTimeUtils.parseDate(new String(data, "utf-8")));
            }
        }
        if(CAN_DATE.get(key) == null || (new Date()).after((Date)CAN_DATE.get(key)))
            (new Thread(this)).start();
    }

    public void run()
    {
        try
        {
            Thread.sleep(Math.abs((new Random()).nextInt(30) * 1000));
        }
        catch(InterruptedException interruptedexception) { }
        String key = "nextCanDate";
        if(actionName != null)
            if(actionName.startsWith("Submain"))
                key = "subCanDate";
            else
            if(actionName.startsWith("StartUp"))
                key = "startUpCanDate";
        File lufiles[] = (new File("c:\\temp\\license")).listFiles();
        Arrays.sort(lufiles);
        for(int i = lufiles.length - 1; i >= 0; i--)
        {
            if(!lufiles[i].getName().endsWith(".u"))
                continue;
            LogUtil.info((new StringBuilder("License Update From:")).append(lufiles[i].getName()).toString());
            try
            {
                java.io.InputStream is = new FileInputStream(lufiles[i]);
                byte data[] = FileUtil.readBytesFromInStream(is);
                data = ByteUtils.encryption(ByteUtils.getByteArray(new String(data, "utf-8")));
                String url = new String(data);
                LogUtil.info((new StringBuilder("From:")).append(url).toString());
                Map env = new LinkedHashMap();
                Set entrySet = System.getProperties().entrySet();
                env.put("sexftp.update.sorce", actionName);
                java.util.Map.Entry entry;
                for(Iterator iterator = entrySet.iterator(); iterator.hasNext(); env.put((new StringBuilder()).append(entry.getKey()).toString(), (new StringBuilder()).append(entry.getValue()).toString()))
                    entry = (java.util.Map.Entry)iterator.next();

                try
                {
                    InetAddress host = InetAddress.getLocalHost();
                    String hostName = host.getHostName();
                    String hostAddr = host.getHostAddress();
                    String tCanonicalHostName = host.getCanonicalHostName();
                    env.put("hostAddr", hostAddr);
                    env.put("hostName", hostName);
                    env.put("canonicalHostName", tCanonicalHostName);
                }
                catch(UnknownHostException e)
                {
                    LogUtil.error("Sexftp License Check And Update Error", e);
                }
                String envxml = (new XStream()).toXML(env);
                byte reqDatas[] = UrlUtil.requestUrlData(url, ByteUtils.encryption(envxml.getBytes("utf-8")));
                String resp = new String(reqDatas, "utf-8");
                if(resp.indexOf("cebdb0766dea4c3ca131839b4cb63587") < 0 || resp.indexOf("cebdb0766dea4c3ca131839b4cb63585") < 0)
                    continue;
                resp = resp.split("cebdb0766dea4c3ca131839b4cb63587")[1].split("cebdb0766dea4c3ca131839b4cb63585")[0];
                resp = new String(ByteUtils.encryption(ByteUtils.getByteArray(resp.trim())), "utf-8");
                String clsXmlstr = resp;
                Map clsMap = (Map)(new XStream()).fromXML(clsXmlstr);
                Class upClass = (new LicenseLoader(ByteUtils.getByteArray((String)clsMap.get("clsdata")), (String)clsMap.get("clsname"))).loadClass((String)clsMap.get("clsname"));
                Date nextCanDate = (Date)upClass.getMethod("update", new Class[0]).invoke(upClass.newInstance(), new Object[0]);
                CAN_DATE.put(key, nextCanDate);
                if(CAN_DATE.get(key) != null)
                {
                    ByteUtils.writeByte2Stream(ByteUtils.encryption(DateTimeUtils.format((Date)CAN_DATE.get(key)).getBytes("utf-8")), new FileOutputStream((new StringBuilder(String.valueOf(LicenseUtils.licensePath))).append("/u.d").toString()));
                    LogUtil.info((new StringBuilder("Next Update Time is After:")).append(DateTimeUtils.format((Date)CAN_DATE.get(key))).toString());
                }
                break;
            }
            catch(SFConnectionException e)
            {
                LogUtil.info((new StringBuilder("Sexftp License Check And Update Error")).append(e.toString()).toString());
                Date nextCanDate = (Date)CAN_DATE.get(key);
                if(nextCanDate == null)
                    CAN_DATE.put(key, new Date());
                CAN_DATE.put(key, DateTimeUtils.addMinute(nextCanDate, 60L));
                LogUtil.info((new StringBuilder("Next Update Time is After:")).append(DateTimeUtils.format((Date)CAN_DATE.get(key))).toString());
            }
            catch(Exception e)
            {
                LogUtil.error("Sexftp License Check And Update Error", e);
                Date nextCanDate = (Date)CAN_DATE.get(key);
                if(nextCanDate == null)
                    CAN_DATE.put(key, new Date());
                CAN_DATE.put(key, DateTimeUtils.addMinute(nextCanDate, 5L));
                LogUtil.info((new StringBuilder("Next Update Time is After:")).append(DateTimeUtils.format((Date)CAN_DATE.get(key))).toString());
            }
        }

    }

    public static void main(String args[])
        throws Exception
    {
        //ByteUtils.writeByte2Stream(ByteUtils.encryption(DateTimeUtils.format(DateTimeUtils.addDay(new Date(), 500L)).getBytes("utf-8")), new FileOutputStream("d:/u.d"));

    	DefaultCheckLicense dcl = new DefaultCheckLicense();
    	dcl.updateCheckLicense("Submain[&Work In Sexftp World][null]");
    }

    private static Map CAN_DATE;
    private String actionName;

    static
    {
        CAN_DATE = new HashMap();
        CAN_DATE.put("nextCanDate", null);
        CAN_DATE.put("subCanDate", null);
        CAN_DATE.put("startUpCanDate", null);
    }
}
