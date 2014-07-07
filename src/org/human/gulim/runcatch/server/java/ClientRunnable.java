package org.human.gulim.runcatch.server.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.human.gulim.runcatch.bean.RoomInfo;
import org.human.gulim.runcatch.bean.Team;
import org.human.gulim.runcatch.bean.User;
import org.human.gulim.runcatch.network.NetworkMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientRunnable implements Runnable {

	private ObjectInputStream oIn;
	private ObjectOutputStream oOut;
	private Socket socket;
	private RoomMap roomMap;
	private ClientMap clientMap;
	private static String TAG="[Runable]: ";

	public ClientRunnable() {

		socket = null;
		oIn = null;
		oOut = null;
		roomMap = RoomMap.getRoomMap();
		clientMap = ClientMap.getClientMap();
	}

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		Object value = null;
		RoomInfo roomInfo;
		String resultString;
		try {
			oIn = new ObjectInputStream(new BufferedInputStream(
					socket.getInputStream()));
			
	
			value = oIn.readObject();
			
			try {
				roomInfo = getByEvent((String) value);
				if(roomInfo!=null)
				{
					resultString = roomInfo.toJSONObject().toJSONString();
					System.out.println(TAG+" normal return: "+resultString);
				}
				else
				{
					resultString = "";
					System.out.println(TAG+" error? or isStarted false ");
				}
				
				oOut = new ObjectOutputStream(new BufferedOutputStream(
						socket.getOutputStream()));
				oOut.writeObject(resultString);
				oOut.flush();
				System.out.println("success");

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		} finally {
			freeResources(socket, oIn, oOut);
		}

	}

	/**
	 * It frees socket, inputstream, outputstream.
	 * 
	 * @param socket
	 *            to be freed.
	 * @param in
	 *            to be freed.
	 * @param out
	 *            to be freed.
	 */
	private void freeResources(Socket socket, InputStream in, OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	public RoomInfo getByEvent(String value) throws ParseException {
		JSONParser parser = new JSONParser();
		//System.out.println(value);
		JSONObject obj = (JSONObject) parser.parse(value);
		JSONObject data = (JSONObject) obj.get("data");
		String event = (String) obj.get("event");
		String id_room = null;
		/**
		 * { "event": "eventName", "data" : { RoomInfo bean }}
		 */
		if (event.equals(NetworkMethod.ADD_ALL_MEMBERS))// event ==
														// NetworkMethod.ADD_ALL_MEMBERS
		{
			System.out.println(TAG+"event: "+NetworkMethod.ADD_ALL_MEMBERS+" data: "+data.toJSONString());

			add_all_members(data);
			id_room = (String) data.get("id_room");
					}

		/**
		 * { "event": "eventName", "data" : { "user": {user bean} } }
		 */
		else if (event.equals(NetworkMethod.CATCH_USER)) {
			updateUser(obj);
			id_room = (String) data.get("id_room");
		}

		
		/**
		 * { "event": "eventName", "data" : {user bean} }
		 */
		else if (event.equals(NetworkMethod.CREATE_ROOM))// 방장
		{
			createRoom(data);
			id_room = (String) data.get("id_room");
			System.out.println(TAG+"event: "+NetworkMethod.CREATE_ROOM+" data: "+data.toJSONString());
		}

		/**
		 * { "event": "eventName", "data" : {roomInfo bean} }
		 */
		else if (event.equals(NetworkMethod.DELETE_ROOM)) {
			deleteRoom(data);
			id_room = (String) data.get("id_room");
		}

		/**
		 * { "event": "eventName", "data" : {user bean} }
		 */
		else if (event.equals(NetworkMethod.IS_STARTED))//
		{
			id_room = (String) clientMap.get(data.get("id"));
			System.out.println(TAG+"event: "+NetworkMethod.IS_STARTED+" data: "+data.toJSONString());
			if(id_room==null)
				id_room="";
		}

		/**
		 * { "event": "eventName", "data" : {user bean} }
		 */
		else if (event.equals(NetworkMethod.UPDATE_POS)) {
			updatePosition(obj);
			id_room = (String) clientMap.get(data.get("id_room"));
		} else {
			System.out.println("ERROR: " + value);
		}

		return roomMap.get(id_room);
	}

	private void updateUser(JSONObject obj) {
		User user = User.getUserFromJson(obj);
		RoomInfo currentRoom;
		Team team;
		User me;
		currentRoom = roomMap.get(user.getId_room());
		team = currentRoom.getTeam(user.getId_team());
		me = team.remove(user.getId());
		// swap.
		me.setId_team(user.getFuture_id_team());
		team = currentRoom.getTeam(me.getId_team());
		team.put(me.getId(), me);

	}

	private void updatePosition(JSONObject obj) {
		User user = User.getUserFromJson(obj);
		RoomInfo currentRoom;
		Team team;
		User me;
		currentRoom = roomMap.get(user.getId_room());
		team = currentRoom.getTeam(user.getId_team());
		me = team.get(user.getId());
		me.setLatitude(user.getLatitude());
		me.setLongitude(user.getLongitude());
	}

	private void createRoom(JSONObject obj) {
		RoomInfo roomInfo = new RoomInfo();
		roomInfo.setId_room((String) obj.get("id_room"));
		roomInfo.setMode(RoomInfo.WAIT_MODE);
		roomInfo.setTime((Long) obj.get("time"));
		roomInfo.setTime_left((Long) obj.get("time_left"));
		
		roomMap.put(roomInfo.getId_room(), roomInfo);
		System.out.println("create room's mapping is completed");//TODO
	}

	private void deleteRoom(JSONObject obj) {
		RoomInfo roomInfo = roomMap.remove(obj.get("id_room"));
		
		if(roomInfo!=null)
		{
			for(Team team : roomInfo.getTeams().values())
			{
				for(User user : team.getMembers())
				{
					clientMap.remove(user.getId());
				}
			}
		}
	}

	private void add_all_members(JSONObject obj) {
		RoomInfo roomInfo = RoomInfo.getRoomInfoFromJson(obj);
		RoomInfo currentRoom = roomMap.get(obj.get("id_room"));
		
		System.out.println("parsed room info: "+roomInfo.toJSONObject().toJSONString());
		if (currentRoom != null) {
			currentRoom.setTeams(roomInfo.getTeams());
			
			for(Team team: currentRoom.getTeams().values())
			{
				System.out.println("	team id"+team.getId_team());
				for(User user : team.getMembers())
				{
					System.out.println("		user id"+user.getId());
					user.setId_room(roomInfo.getId_room());
					clientMap.put(user.getId(), user.getId_room());
				}
			}
			// TODO start. push or another action.
		}

	}

}
