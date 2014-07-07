package org.human.gulim.runcatch.exception;

public class NetworkMethodException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5417575284918020340L;
	
	private static String tag = NetworkMethodException.class.getName();
	public NetworkMethodException(Exception e)
	{
		super(tag+""+e.getMessage());
		this.setStackTrace(e.getStackTrace());
	}


}
