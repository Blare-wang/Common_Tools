package com.itblare.itools.http;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.http
 * ClassName:   HttpIpUtil
 * Author:   Blare
 * Date:     Created in 2021/4/13 17:00
 * Description:    HTTP IP 处理器
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 17:00    1.0.0             HTTP IP 处理器
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 一句话功能简述:HTTP IP 处理器
 *
 * @author Blare
 * @create 2021/4/13 17:00
 * @since 1.0.0
 */
public class HttpIp {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpIp.class);

    /**
     * 功能描述: 获取本地IP地址
     *
     * @return {@link String}
     * @method getLocalIP
     * @author Blare
     * @date 2021/4/13 14:45
     * @updator Blare
     */
    public static String getLocalIP() throws UnknownHostException {
        if (isWindowsOS()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 功能描述：获取本地Host名称
     *
     * @return {@link String}
     * @method getLocalHostName
     * @author Blare
     * @date 2021/4/13 14:45
     * @updator Blare
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 功能描述: 获取用户请求的真实IP地址
     *
     * @param request http????
     * @return {@link String}
     * @method getIpAddress
     * @author Blare
     * @date 2020/3/7 14:20
     * @updator Blare
     */
    public static String getIpAddress(HttpServletRequest request) {

        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                if (isEmptyAddressStr(ipAddress)) {
                    LOGGER.debug("??IP??[X-Forwarded-For], ?[{}]", ipAddress);
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
                if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        LOGGER.debug("??IP??[Proxy-Client-IP], ?[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
                if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        LOGGER.debug("??IP??[WL-Proxy-Client-IP], ?[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
                if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        LOGGER.debug("??IP??[HTTP_CLIENT_IP], ?[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        LOGGER.debug("??IP??[HTTP_X_FORWARDED_FOR], ?[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Real-IP");
                if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    LOGGER.debug("??IP??[X-Real-IP], ?[{}]", ipAddress);
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    LOGGER.debug("??IP??[RemoteAddr], ?[{}]", ipAddress);
                }
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress) || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    // ??????????IP
                    try {
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                        if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                            LOGGER.debug("??IP??[LocalHost], ?[{}]", ipAddress);
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // ???????????????IP??????IP,??IP??','??
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (LOGGER.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
            LOGGER.debug("????IP??, ?[{}]", ipAddress);
        }
        return ipAddress;
    }

    /**
     * 功能描述: 判断操作系统是否是Windows
     *
     * @return {@link boolean}
     * @method isWindowsOS
     * @author Blare
     * @date 2020/3/7 14:14
     * @updator Blare
     */
    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 功能描述: 获取Linux下的IP地址
     *
     * @return {@link String}
     * @method getLinuxLocalIp
     * @author Blare
     * @date 2021/4/13 14:45
     * @updator Blare
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipAddress = inetAddress.getHostAddress();
                            if (!ipAddress.contains("::") && !ipAddress.contains("0:0:") && !ipAddress.contains("fe80")) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.info("IP???{}", ipAddress);
                                }
                                ip = ipAddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("??ip????");
            }
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        LOGGER.info("????Linux?IP???{}", ip);
        return ip;
    }

    private static boolean isEmptyAddressStr(String ipAdderss) {
        return (ipAdderss == null || "".equals(ipAdderss));
    }

}
