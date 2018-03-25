package rpc;

import java.net.InetSocketAddress;

public class RPCTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Server server = new ServerCenter(8088);
					server.register(ServiceProvider.class, ServiceImp.class);
					server.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
		ServiceProvider serviceProvider = RPCClient.getRemoteProxyObj(ServiceProvider.class, new InetSocketAddress("localhost", 8088));
		System.out.println(serviceProvider.sum(1,2));
		System.out.println(serviceProvider.sub(2, 1));
	}

}
