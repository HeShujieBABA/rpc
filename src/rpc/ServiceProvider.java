package rpc;

/*
 RPC架构分为三部分：

1）服务提供者，运行在服务器端，提供服务接口定义与服务实现类。

2）服务中心，运行在服务器端，负责将本地服务发布成远程服务，管理远程服务，提供给服务消费者使用。

3）服务消费者，运行在客户端，通过远程代理对象调用远程服务。
 */

public interface ServiceProvider {
	
	abstract int sum(int a,int b);
	abstract int sub(int a,int b);
}

