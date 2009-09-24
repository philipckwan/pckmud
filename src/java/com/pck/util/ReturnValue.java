package com.pck.util;

public class ReturnValue {
	public enum Retcode { NOT_SET, SUCCESS, FAILED }
	
	private Retcode retcode;
	private String msg;
	
	public ReturnValue (){
		this(Retcode.NOT_SET, "");
	}
	
	public ReturnValue (Retcode retcode){		
		this(retcode, "");		
	}
	
	public ReturnValue (String msg){
		this(Retcode.NOT_SET, msg);
	}
	
	public ReturnValue (Retcode retcode, String msg){
		this.retcode = retcode;
		this.msg = msg;
	}
	
	public void setStatus(Retcode retcode){
		this.retcode = retcode;
	}
	
	public Retcode getStatus(){
		return retcode;
	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}
}
