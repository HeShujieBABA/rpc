package rpc;

import java.io.IOException;

public interface Server {
	abstract public void    stop();
	abstract public void    start() throws IOException;
	abstract public void    register(Class serviceInterface,Class imp1);
	abstract public boolean isRunning();
	abstract public int     getPort();
}
