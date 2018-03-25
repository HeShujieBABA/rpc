package rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCenter implements Server {
	
	private static boolean isRunning = false;
	//创建了可用的线程池数
	private static ExecutorService excutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static int port;
	private static HashMap<String, Class> serviceRegister = new HashMap<String,Class>();
	
	public ServerCenter(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
	}
 	@Override
	public void stop() {
		// TODO Auto-generated method stub
		isRunning = false;
		excutor.shutdown();  
	}

	@Override
	public void start() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket();  //创建socket
		serverSocket.bind(new InetSocketAddress(port));  //绑定socket
		System.out.println("start server");
		try {
			while(true) {
				excutor.execute(new ServiceTask(serverSocket.accept()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			serverSocket.close();
		}
	}

	@Override
	public void register(Class serviceInterface, Class imp1) {
		// TODO Auto-generated method stub
		serviceRegister.put(serviceInterface.getName(), imp1);
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return isRunning;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return port;
	}

	//Task任务
	static class ServiceTask implements Runnable{
		Socket client = null;
		public ServiceTask(Socket client) {
			// TODO Auto-generated constructor stub
			this.client = client;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ObjectInputStream objectInputStream =  null;
			ObjectOutputStream objectOutputStream = null;
			try {
				//将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
				objectInputStream = new ObjectInputStream(client.getInputStream());
				String serviceName = objectInputStream.readUTF();
				String methodName = objectInputStream.readUTF();
				Class<?>[] parameterTypes = (Class<?>[])objectInputStream.readObject();
				Object[] arguments = (Object[]) objectInputStream.readObject();
				Class serviceClass = serviceRegister.get(serviceName);
				
				if(serviceClass == null) {
					throw new ClassNotFoundException(serviceName+"not found");
				}
				Method method = serviceClass.getMethod(methodName, parameterTypes);
				Object result = method.invoke(serviceClass.newInstance(), arguments);
				
				//将执行结果反序列化  发送给client
				objectOutputStream = new ObjectOutputStream(client.getOutputStream());
				objectOutputStream.writeObject(result);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally {
				if(objectOutputStream!=null) {
					try {
						objectOutputStream.close();	
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
					
				}
				if(objectInputStream!=null) {
					try {
						objectInputStream.close();
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
			}
		}
	}
}
