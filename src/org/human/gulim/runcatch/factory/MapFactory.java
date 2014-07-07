package org.human.gulim.runcatch.factory;

import java.util.HashMap;
import java.util.Map;

public class MapFactory<V> {

	public static <K,V> Map<K,V> getMap(Class<K> key, Class<V> value){
		return new HashMap<K,V>();
	}
	
}
