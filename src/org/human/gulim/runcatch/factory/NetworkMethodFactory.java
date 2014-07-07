package org.human.gulim.runcatch.factory;

import org.human.gulim.runcatch.network.NetworkMethod;
import org.human.gulim.runcatch.network.NetworkMethodJavaSocketImpl;

/**
 * 
 * @author KTS
 *
 */
public class NetworkMethodFactory {
	
	private static NetworkMethod instance = new NetworkMethodJavaSocketImpl();
	/**
	 * use case:<br/>
	 * NetworkMethod instance = NethorkMethod.getInstance();<br/>
	 * instance.emitEvent("event",{@link org.human.gulim.runcatch.bean.Jsonable});
	 * 
	 * @return the network method you will use.
	 */
	public static NetworkMethod getInstance(){
		return instance;
	}

}
