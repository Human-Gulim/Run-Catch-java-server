package org.human.gulim.runcatch.server.java;

import java.util.concurrent.ConcurrentHashMap;

public class ClientMap extends ConcurrentHashMap<String, String> {
	private static ClientMap instance = new ClientMap();
	
	public static ClientMap getClientMap(){
		return instance;
	}
	

}
