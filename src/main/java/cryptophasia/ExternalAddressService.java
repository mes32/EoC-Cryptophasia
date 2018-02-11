/*
    ExternalAddressService.java

    Webservice that sends back this machine's external IP address
 */

package cryptophasia;

import java.io.*;
import java.net.*;

public class ExternalAddressService {
    public final static String WIP_SITE = "http://whatsmyip.org";
    //private final static String REPLACE_PATTERN = "(?s).*?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*";

    // For demo
    //public static void main(String[] args) {
    //    System.out.println(ExternalAddressService.whatIsMyIp());
    //}

    public static String whatIsMyIp() {
        String result = null;
        InputStream in = null;

        try {
            URLConnection conn = new URL(WIP_SITE).openConnection();
            //System.out.println(conn.getHeaderField("Content-Length"));
            // int length = Integer.valueOf(conn.getHeaderField("Content-Length"));
            // byte[] buf = new byte[length];
            // in = conn.getInputStream();
            // in.read(buf);
            // result = new String(buf);
            // System.out.println(result);


            String result2 = conn.getContent().toString();

            // buf = new byte[length];
            // in = conn.getInputStream();
            // in.read(buf);
            // String result2 = new String(buf);
            System.out.println(result2);

            //result = result.replaceAll(REPLACE_PATTERN, "$1");

            return result2;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) { /* ignore */
                }
            }
        }

        return result;
    }
}