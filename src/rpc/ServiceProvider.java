package rpc;

/*
 RPC�ܹ���Ϊ�����֣�

1�������ṩ�ߣ������ڷ������ˣ��ṩ����ӿڶ��������ʵ���ࡣ

2���������ģ������ڷ������ˣ����𽫱��ط��񷢲���Զ�̷��񣬹���Զ�̷����ṩ������������ʹ�á�

3�����������ߣ������ڿͻ��ˣ�ͨ��Զ�̴���������Զ�̷���
 */

public interface ServiceProvider {
	
	abstract int sum(int a,int b);
	abstract int sub(int a,int b);
}

