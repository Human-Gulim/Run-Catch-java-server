package org.human.gulim.runcatch.bean;

import java.util.List;
import java.util.Map;

import org.human.gulim.runcatch.factory.ListFactory;
import org.human.gulim.runcatch.factory.MapFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Team implements Jsonable{
	private Map<String,User> memberMap;
	private int id_team;
	
	
	
	public Team(){
		memberMap = MapFactory.getMap(String.class, User.class);
		this.id_team = -1;
	}
	
	public int getCount(){
		return memberMap.size();
	}
	
	public void put(String key, User user){
		memberMap.put(key, user);
	}
	
	public User get(String key)
	{
		return memberMap.get(key);
	}
	
	public List<User> getMembers()
	{
		List<User> members = ListFactory.getList(User.class);
		members.addAll(memberMap.values());
		return members;
	}
	public User remove(String key)
	{
		return memberMap.remove(key);
	}
	
	public int getId_team() {
		return id_team;
	}
	public void setId_team(int id_team) {
		this.id_team = id_team;
	}
	
	
	/**
	 * JSONObject를 Team object로 변환한다.
	 * @param obj
	 * @return The team object into which obj(input) is converted.
	 */
	 
	public static Team getTeamFromJson(JSONObject obj){
		if(obj ==null)
			return null;
		
		Team team = new Team();
		Object value;
		JSONArray jsonArray;
		JSONObject tmpObj;
		User tmpUser;
		
		value = obj.get("id_team");
		if(value!=null)
		{
			team.setId_team(Integer.parseInt(value.toString()));
		}
		
		jsonArray =(JSONArray) obj.get("members");
		for(int i=0;i<jsonArray.size();i++)
		{
			tmpObj = (JSONObject)jsonArray.get(i);
			tmpUser =User.getUserFromJson(tmpObj);
			if(tmpUser.getId()!=null)
				team.put(tmpUser.getId(), tmpUser);
		}
		
		return team;
	}

	/**
	 * 	  {} <- JSONObject.
	 * 	  [] <- JSONArray.
	 * 
	 * 		{ 	"id_team" : "팀 id",
	 * 			"members" : [{user bean},{user bean},..] 
	 * 		}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		JSONArray members =  new JSONArray(); 
		
		for(User member : memberMap.values())
		{
			members.add(member.toJSONObject());
		}
		obj.put("id_team", id_team);
		obj.put("members", members);
		
		return obj;
	}
}
