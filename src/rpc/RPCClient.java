package rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RPCClient<T> {
	@SuppressWarnings("unchecked")
	
	public static <T> T getRemoteProxyObj(final Class<?> serviceInterface,final InetSocketAddress addr) {
		 // 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
		return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, 
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// TODO Auto-generated method stub
						Socket socket = null;
						ObjectInputStream objectInputStream = null;
						ObjectOutputStream objectOutputStream = null;
						try {
							//创建socket连接
							socket = new Socket();
							socket.connect(addr);
							//将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
							objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
							objectOutputStream.writeUTF(serviceInterface.getName());
							objectOutputStream.writeUTF(method.getName());
							objectOutputStream.writeObject(method.getParameterTypes());
							objectOutputStream.writeObject(args);
							
							// 同步阻塞等待服务器返回应答，获取应答后返回
							objectInputStream = new ObjectInputStream(socket.getInputStream());
							return objectInputStream.readObject();
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						finally {
							if(socket!=null) socket.close();
							if(objectInputStream!=null) objectInputStream.close();
							if(objectOutputStream!=null) objectOutputStream.close();
						}
						return null;
					}
				});
	}
}
