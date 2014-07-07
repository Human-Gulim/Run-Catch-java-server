package org.human.gulim.runcatch.bean;

import org.json.simple.JSONObject;



public interface Jsonable {
	/**
	 * 해당 object를 JSONObject로 바꾸는 method를 강제한다.
	 * @return the JSONObject made from the object that implements this interface.
	 */
	public JSONObject toJSONObject();
}
