package com.pck.util;

public class Logger {
	private static Logger instance = null;
	
	// 1: debug, 2: info, 3: error
	private int currentLevel = 1;
	
	// Display thread name or not
	private boolean threadName = true;
	
	private Logger(){	
	}
	
	public static Logger getInstance(){
		if (instance == null){
			synchronized(Logger.class) {
				if (instance == null){
					instance = new Logger();
				}
			}
		}
		return instance;
	}
	
	public void debug(String msg){
		if (currentLevel <= 1){
			synchronized(Logger.class){
				this.println(msg);
			}
		}
	}
	
	public void info(String msg){
		if (currentLevel <= 2){
			synchronized(Logger.class){
				this.println(msg);
			}
		}	
	}
	
	public void error(String msg){
		if (currentLevel <= 3){
			synchronized(Logger.class){
				this.println(msg);
			}
		}
	}
	
	public void error(Exception e){
		if (currentLevel <= 3){
			synchronized(Logger.class){
				this.println(e);
				/*
				StackTraceElement[] steArray = e.getStackTrace();
				for(int i = 0; i < steArray.length; i++){
					StackTraceElement ste = steArray[i];
					System.out.println(ste.toString());
				}
				*/
			}
		}
	}
	
	public void error(Exception e, String msg){
		this.error(msg);
		this.error(e);
	}
	
	private void println(Object msg){
		if (threadName) System.out.println(Thread.currentThread().toString()+ " - " + msg);
		else System.out.println(msg);
	}
}
