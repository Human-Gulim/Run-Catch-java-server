package org.human.gulim.runcatch.server.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.human.gulim.runcatch.bean.RoomInfo;
import org.human.gulim.runcatch.bean.Team;
import org.human.gulim.runcatch.bean.User;
import org.human.gulim.runcatch.network.NetworkMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientRunnable implements Runnable{

	private ObjectInputStream oIn;
	private ObjectOutputStream oOut;
	private Socket socket;
	private RoomMap roomMap;
	private ClientMap clientMap;
	
	public ClientRunnable(){
		
		socket = null;
		oIn = null;
		oOut = null;
		roomMap = RoomMap.getRoomMap();
		clientMap = ClientMap.getClientMap();
	}
	public ClientRunnable (Socket socket)
	{
		this.socket = socket;
	}
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		Object value=null;
		JSONParser parser = new JSONParser();
		RoomInfo roomInfo;
		try {
			oIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			value = oIn.readObject();
			try {
				roomInfo=getByEvent((String)value);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			oOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			
		} catch (IOException e) {
		
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public RoomInfo getByEvent(String value) throws ParseException
	{
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(value);
		JSONObject data = (JSONObject) obj.get("data");
		String event = (String) obj.get("event");
		String id_room=null;
		
		
		if(event.equals(NetworkMethod.ADD_ALL_MEMBERS))
		{
			add_all_members(data);
			id_room = (String)data.get("id_room");
		}
		else if(event.equals(NetworkMethod.CATCH_USER))
		{
			updateUser(obj);
			id_room = (String) data.get("id_room");
		}
		else if(event.equals(NetworkMethod.CREATE_ROOM))
		{
			createRoom(data);
			id_room =(String) data.get("id_room");
		}
		else if(event.equals(NetworkMethod.DELETE_ROOM))
		{
			deleteRoom(data);
			id_room = (String) data.get("id_room");
		}
		else if(event.equals(NetworkMethod.IS_STARTED))
		{
			id_room=(String)clientMap.get(data.get("id"));
		}
		else if(event.equals(NetworkMethod.UPDATE_POS))
		{
			updatePosition(obj);
		}
		else
		{
			System.out.println("ERROR: "+value);
		}
		
		
		return roomMap.get(id_room);
	}
	
	private void updateUser(JSONObject obj)
	{
		User user = User.getUserFromJson(obj);
		RoomInfo currentRoom;
		Team team;
		User me;
		currentRoom = roomMap.get(user.getId_room());
		team = currentRoom.getTeam(user.getId_team());
		me=team.remove(user.getId());
		//swap.
		me.setId_team(user.getFuture_id_team());
		team = currentRoom.getTeam(me.getId_team());
		team.put(me.getId(), me);
		
	}
	private void updatePosition(JSONObject obj)
	{
		User user = User.getUserFromJson(obj);
		RoomInfo currentRoom;
		Team team;
		User me;
		currentRoom = roomMap.get(user.getId_room());
		team = currentRoom.getTeam(user.getId_team());
		me=team.get(user.getId());
		me.setLatitude(user.getLatitude());
		me.setLongitude(user.getLongitude());
	}
	private void createRoom(JSONObject obj){
		RoomInfo roomInfo = new RoomInfo();
		roomInfo.setId_room((String)obj.get("id_room"));
		roomInfo.setMode(RoomInfo.WAIT_MODE);
		roomInfo.setTime((Long)obj.get("time"));
		roomInfo.setTime_left((Long)obj.get("time_left"));
		roomMap.put(roomInfo.getId_room(), roomInfo);
	}
	private void deleteRoom(JSONObject obj){
		roomMap.remove(obj.get("id_room"));
	}
	private void add_all_members(JSONObject obj)
	{
		RoomInfo roomInfo = RoomInfo.getRoomInfoFromJson(obj);
		RoomInfo currentRoom = roomMap.get(obj.get("id_room"));
		if(currentRoom!=null)
		{
			currentRoom.setTeams(roomInfo.getTeams());
			//TODO start. push or another action.
		}
		
	}


}
