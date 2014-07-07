package org.human.gulim.runcatch.factory;

import java.util.ArrayList;
import java.util.List;

public class ListFactory {
	
	public static <T> List<T> getList (Class<T> t)
	{
		return new ArrayList<T>();
	}

}
