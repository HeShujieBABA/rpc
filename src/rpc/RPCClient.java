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
		 // 1.�����صĽӿڵ���ת����JDK�Ķ�̬�����ڶ�̬������ʵ�ֽӿڵ�Զ�̵���
		return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, 
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// TODO Auto-generated method stub
						Socket socket = null;
						ObjectInputStream objectInputStream = null;
						ObjectOutputStream objectOutputStream = null;
						try {
							//����socket����
							socket = new Socket();
							socket.connect(addr);
							//��Զ�̷����������Ľӿ��ࡢ�������������б�ȱ�����͸������ṩ��
							objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
							objectOutputStream.writeUTF(serviceInterface.getName());
							objectOutputStream.writeUTF(method.getName());
							objectOutputStream.writeObject(method.getParameterTypes());
							objectOutputStream.writeObject(args);
							
							// ͬ�������ȴ�����������Ӧ�𣬻�ȡӦ��󷵻�
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
