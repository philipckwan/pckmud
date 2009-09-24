package com.pck.util;

public class MudException extends Exception {

	private static final long serialVersionUID = -2384575185398558936L;
	private ReturnValue _retValue;
	
	public MudException (){
		_retValue = new ReturnValue(ReturnValue.Retcode.FAILED);
	}
	
	public MudException(String msg){
		_retValue = new ReturnValue(ReturnValue.Retcode.FAILED, msg);
	}

	
	public ReturnValue getRetValue(){
		return _retValue;
	}
	
	public void setRetValue(ReturnValue retValue){
		_retValue = retValue;
	}
}
