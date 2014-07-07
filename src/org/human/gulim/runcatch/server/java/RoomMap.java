package org.human.gulim.runcatch.server.java;

import java.util.concurrent.ConcurrentHashMap;

import org.human.gulim.runcatch.bean.RoomInfo;

public class RoomMap extends ConcurrentHashMap<String,RoomInfo>{
	
	private static RoomMap instance= new RoomMap();

	public static RoomMap getRoomMap(){
		return instance;
	}


}
