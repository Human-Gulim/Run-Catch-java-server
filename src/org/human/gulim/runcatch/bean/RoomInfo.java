package org.human.gulim.runcatch.bean;

import java.util.Map;

import org.human.gulim.runcatch.factory.MapFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RoomInfo implements Jsonable{
	
	private String id_room;
	private long time;
	private long time_left;
	private Map<Integer, Team>teamMap;
	private int mode;

	public static int WAIT_MODE = 0;//mode variable.
	public static int NORMAL_MODE = 1;
	public static int ESCAPE_MODE = 2;


	public RoomInfo(){
		this.id_room = null;
		this.time = -1;
		this.teamMap = MapFactory.getMap(Integer.class, Team.class);
		this.mode = -1;
	}
	
	public Map<Integer, Team> getTeams() {

		return teamMap;
	}

	public void setTeams(Map<Integer, Team> teamMap) {
		this.teamMap = teamMap;
	}
	
	public Team getTeam(Integer id_team)
	{
		return teamMap.get(id_team);
	}
	public void putTeam(Integer id_team, Team team)
	{
		teamMap.put(id_team, team);
	}

	public String getId_room() {
		return id_room;
	}

	public void setId_room(String id_room) {
		this.id_room = id_room;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	
	/**
	 * JSONObject
	 * @param obj
	 * @return The team object into which obj(input) is converted.
	 * 
	 * {} <- JSONObject.
	 * [] <- JSONArray.
	 * 
	 * {
	 * 		"id_room": "룸 id"
	 * 		"time" : "설정 시간"
	 * 		"time_left"	: "남은시간"
	 * 		"mode": "모드 (0,1 or 2 RoomInfo의 static 변수에 설정되어 있음.)" //대기모드인지, 시작되었는지, 탈출모드인지.
	 * 		"teams":[
	 * 			
	 * 			{ 	"id_team" : "팀 id",
	 * 				"members" : [{user bean},{user bean},..] },
	 * 			{ 	"id_team" : "팀 id",
	 * 				"members" : [{user bean},{user bean},..] },
	 * 		]
	 * }
	 */
	public static RoomInfo getRoomInfoFromJson(JSONObject obj)
	{
		if(obj==null)
			return null;
		RoomInfo roomInfo=new RoomInfo();
		Object value=null;
		JSONArray teams=null;
		JSONObject tmpObj=null;
		Team tmpTeam = null;
		
		value = obj.get("id_room");
		
		if(value!=null)
		{
			roomInfo.setId_room(value.toString());
		}
		
		value = obj.get("time");
		if(value!=null)
		{
			roomInfo.setTime(Long.parseLong(value.toString()));
		}
		value = obj.get("time_left");
		if(value!=null)
		{
			roomInfo.setTime_left(Long.parseLong(value.toString()));
		}
	
		
		value = obj.get("mode");
		if(value!=null)
		{
			roomInfo.setMode(Integer.parseInt(value.toString()));
		}
		
		teams =(JSONArray) obj.get("teams");
		for(int i=0;i<teams.size();i++)
		{
			tmpObj = (JSONObject)teams.get(i);
			if(tmpObj != null)
			{
				tmpTeam = Team.getTeamFromJson(tmpObj);
				if(tmpTeam!=null)
				{
					roomInfo.putTeam(tmpTeam.getId_team(), tmpTeam);
				}
			}
		}
		return roomInfo;
	}

	public long getTime_left() {
		return time_left;
	}

	public void setTime_left(long time_left) {
		this.time_left = time_left;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject obj;
		JSONArray teams;
		obj = new JSONObject();
		teams = new JSONArray();
		obj.put("id_room",id_room);
		obj.put("time", time);
		obj.put("time_left",time_left);
		obj.put("mode", mode);
		
		for(Team team: teamMap.values())
		{
			teams.add(team.toJSONObject());
		}
		
		obj.put("teams", teams);
		return obj;
	}



}
