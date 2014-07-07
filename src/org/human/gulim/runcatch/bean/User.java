package org.human.gulim.runcatch.bean;

import org.json.simple.JSONObject;



public class User implements Jsonable{
	
	private String id_room;
	private String nickname;
	private String id;
	private String phone;
	private double latitude;
	private double longitude;
	private int id_team;
	private int future_id_team;
	
	public User(){
		this.id_room = null;
		this.nickname =null;
		this.id = null;
		this.phone = null;
		this.latitude = -1;
		this.longitude = -1;
		this.id_team = -1;
		this.future_id_team=-1;
	}
	
	public String getId_room() {
		return id_room;
	}
	public void setId_room(String id_room) {
		this.id_room = id_room;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * JSONObject를 user object로 변환한다.
	 * @param obj
	 * @return The user object into which obj(input) is converted.
	 */
	public static User getUserFromJson(JSONObject obj){
		if(obj == null)
			return null;
		
		User user = new User();
		
		Object value = obj.get("id");
		if(value != null)
		{
			user.setId(value.toString());
		}
		
		
		value = obj.get("id_room");	
		if(value!=null)
		{
			user.setId_room(value.toString());
		}
		
		value = obj.get("nickname");
		if(value!=null)
		{
			user.setNickname(value.toString());
		}
		
		value = obj.get("phone");
		if(value!=null)
		{
			user.setPhone(value.toString());
		}
		
		value = obj.get("latitude");
		if(value!=null)
		{
			user.setLatitude(Double.parseDouble(value.toString()));
		}
		
		value = obj.get("longitude");
		if(value!=null)
		{
			user.setLongitude(Double.parseDouble(value.toString()));
		}
		
		value = obj.get("id_team");
		if(value!=null)
		{
			user.setId_team(Integer.parseInt(value.toString()));
		}
		
		value = obj.get("future_id_team");
		if(value!=null)
		{
			user.setFuture_id_team(Integer.parseInt(value.toString()));
		}
		
		
		return user;
	}

	public int getId_team() {
		return id_team;
	}

	public void setId_team(int id_team) {
		this.id_team = id_team;
	}


	/**
	 * {} <- JSONObject.
	 * [] <- JSONArray.
	 * 
	 * {
	 * 		"id_room": "룸 아이디",
	 * 		"nickname": "닉네임",
	 * 		"phone": "전화번호",
	 * 		"latitude": "위도",
	 * 		"longitude": "경도",
	 * 		"id_team": "팀 아이디",
	 * 		"future_id_team": "팀 아이디",
	 * }
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("id_room", id_room);
		obj.put("nickname", nickname);
		obj.put("id", id);
		obj.put("phone", phone);
		obj.put("latitude", latitude);
		obj.put("id_team", id_team);
		obj.put("future_id_team", future_id_team);

		return obj;
	}

	public int getFuture_id_team() {
		return future_id_team;
	}

	public void setFuture_id_team(int future_id_team) {
		this.future_id_team = future_id_team;
	}

	

}
