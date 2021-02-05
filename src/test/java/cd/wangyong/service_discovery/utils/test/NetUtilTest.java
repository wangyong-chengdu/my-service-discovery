package cd.wangyong.service_discovery.utils.test;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import cd.wangyong.service_discovery.utils.NetUtil;

/**
 * @author andy
 * @since 2021/2/4
 */
@RunWith(JUnit4.class)
public class NetUtilTest {

    @Test
    public void test() throws SocketException {
        List<InetAddress> allLocalIps = NetUtil.getAllLocalIps();
        System.out.println(allLocalIps);
    }

}
