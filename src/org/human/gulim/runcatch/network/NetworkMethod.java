package org.human.gulim.runcatch.network;

import org.human.gulim.runcatch.bean.Jsonable;
import org.human.gulim.runcatch.bean.RoomInfo;
import org.human.gulim.runcatch.exception.NetworkMethodException;

/**
 * 
 * @author KTS
 *
 *	In android, you must use this method in <bold>other thread than UI thread.</bold>
 *	Singleton pattern.
 */
public interface NetworkMethod {
	
	
	public static String UPDATE_POS = "updatePos";
	public static String CATCH_USER = "catchUser";
	public static String IS_STARTED = "isStarted";
	public static String CREATE_ROOM = "createRoom";
	public static String ADD_ALL_MEMBERS = "addAllMembers";
	public static String DELETE_ROOM = "deleteRoom";
	
	/**
	 * use case: emitEvent("event name", JSONObject);<br/>
	 * ex) emitEvent(NetworkMethod.UPDATE_POS, user);
	 * @param event to be sent to server. It must be the static string in {@link org.human.gulim.runcatch.network.NetworkMethod}
	 * @param data the bean object.
	 * @return current room information.
	 * @throws NetworkMethodException 
	 */
	public RoomInfo emitEvent(String event, Jsonable data) throws NetworkMethodException;

}