package org.human.gulim.runcatch.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutorFactory {
	private static ExecutorService sender = Executors.newSingleThreadExecutor();
	private static ExecutorService eventLoop = Executors.newSingleThreadExecutor();
	
	public static ExecutorService getSender()
	{
		return sender;
	}
	public static ExecutorService getEventLoop()
	{
		return eventLoop;
	}
}
