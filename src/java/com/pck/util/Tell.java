package com.pck.util;

public class Tell {
	private String msg = "";
	
	public Tell(){
		this("");
	}
	
	public Tell(String msg){
		this.msg = msg;
	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}
}
