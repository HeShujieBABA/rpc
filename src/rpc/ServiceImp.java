package rpc;

public class ServiceImp implements ServiceProvider {

	@Override
	public int sum(int a, int b) {
		// TODO Auto-generated method stub
		return a+b;
	}
	@Override
	public int sub(int a, int b) {
		// TODO Auto-generated method stub
		return a-b;
	}
}
