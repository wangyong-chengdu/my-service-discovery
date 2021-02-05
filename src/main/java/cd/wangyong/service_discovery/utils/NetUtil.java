package cd.wangyong.service_discovery.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络工具
 * @author andy
 * @since 2021/2/4
 */
public class NetUtil {

    /**
     * 获取本地所有网络IP地址
     */
    public static List<InetAddress> getAllLocalIps() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        if (networkInterfaces == null) return Collections.emptyList();

        List<InetAddress> res = new ArrayList<>();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (notLocalIp(networkInterface, inetAddress)) {
                    res.add(inetAddress);
                }
            }
        }
        return res;
    }

    private static boolean notLocalIp(NetworkInterface networkInterface, InetAddress inetAddress) throws SocketException {
        return inetAddress != null && !inetAddress.isLoopbackAddress() && (networkInterface.isPointToPoint() || !inetAddress.isLinkLocalAddress());
    }
}
