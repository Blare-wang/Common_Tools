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

    private static final Logger logger = LoggerFactory.getLogger(HttpIp.class);

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
     * 功能描述: 确认Ip是否在指定网段内
     * 参考：https://blog.51cto.com/u_6930123/2113151
     *
     * @param ip   IP地址
     * @param cidr 无类别域间路由 ip/mask 即IP+掩码
     * @return {@link boolean}
     * @method isInRange
     * @author Blare
     * @date 2021/4/21 10:52
     * @updator Blare
     */
    public static boolean isInRange(String ip, String cidr) {
        if (cidr == null)
            throw new NullPointerException("IP段不能为空！");
        if (ip == null)
            throw new NullPointerException("IP不能为空！");

        cidr = cidr.trim();
        ip = ip.trim();
        final String REGX_IP = "((25[0-5]|2[0-4]//d|1//d{2}|[1-9]//d|//d)//.){3}(25[0-5]|2[0-4]//d|1//d{2}|[1-9]//d|//d)";
        final String REGX_IPB = REGX_IP + "//-" + REGX_IP;
        if (!cidr.matches(REGX_IPB) || !ip.matches(REGX_IP))
            return false;

        // 以'.'为分隔拆分IP地址存入字符串数组ips
        final String[] ips = ip.split("\\.");
        // 通过移位和或运算把IP地址由字符串数组转化为整数
        final int ipAddr = (Integer.parseInt(ips[0]) << 24) | (Integer.parseInt(ips[1]) << 16) | (Integer.parseInt(ips[2]) << 8) | (Integer.parseInt(ips[3]));
        // 取出指定网段的子网掩码,即'/'后的数字,此为CIDR斜线记法
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        // 转化为整数表示
        int mask = 0xFFFFFFFF << (32 - type);
        // 取出子网IP
        String[] netIps = cidr.replaceAll("/.*", "").split("\\.");
        // 通过移位和或运算把子网IP由字符串数组转化为整数
        int netAddr = (Integer.parseInt(netIps[0]) << 24) | (Integer.parseInt(netIps[1]) << 16) | (Integer.parseInt(netIps[2]) << 8) | (Integer.parseInt(netIps[3]));
        // 两个IP分别与掩码做与运算,结果相等则返回True
        return (ipAddr & mask) == (netAddr & mask);
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
            if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                if (isEmptyAddressStr(ipAddress)) {
                    logger.debug("当前IP来源[X-Forwarded-For], 值[{}]", ipAddress);
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
                if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        logger.debug("当前IP来源[Proxy-Client-IP], 值[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        logger.debug("当前IP来源[WL-Proxy-Client-IP], 值[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        logger.debug("当前IP来源[HTTP_CLIENT_IP], 值[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    if (isEmptyAddressStr(ipAddress)) {
                        logger.debug("当前IP来源[HTTP_X_FORWARDED_FOR], 值[{}]", ipAddress);
                    }
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Real-IP");
                if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    logger.debug("当前IP来源[X-Real-IP], 值[{}]", ipAddress);
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                    logger.debug("当前IP来源[RemoteAddr], 值[{}]", ipAddress);
                }
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress) || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                        if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
                            logger.debug("当前IP来源[LocalHost], 值[{}]", ipAddress);
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logger.isDebugEnabled() && isEmptyAddressStr(ipAddress)) {
            logger.debug("最终获得IP地址, 值[{}]", ipAddress);
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
                                if (logger.isDebugEnabled()) {
                                    logger.info("IP地址：{}", ipAddress);
                                }
                                ip = ipAddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            if (logger.isDebugEnabled()) {
                logger.info("获取ip地址异常");
            }
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        logger.debug("最终获得IP地址, 值[{}]", ip);
        return ip;
    }

    /**
     * 功能描述: 判定IP地址是否未空值
     *
     * @param ipAdderss IP地址
     * @return {@link boolean}
     * @method isEmptyAddressStr
     * @author Blare
     * @date 2021/4/21 10:43
     * @updator Blare
     */
    private static boolean isEmptyAddressStr(String ipAdderss) {
        return (ipAdderss == null || "".equals(ipAdderss));
    }

    public static void main(String[] args) {
        System.out.println(isInRange("192.168.1.127", "192.168.1.64/28"));
        System.out.println(isInRange("192.168.1.2", "192.168.0.0/23"));
        System.out.println(isInRange("192.168.0.1", "192.168.0.0/24"));
        System.out.println(isInRange("192.168.0.0", "192.168.0.0/32"));
    }
}
