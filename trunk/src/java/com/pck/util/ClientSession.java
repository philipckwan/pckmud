package com.pck.util;

import java.util.Hashtable;

public class ClientSession {
	private String ID;
	private Hashtable<String, Object> clientData;
	
	public ClientSession(){
		clientData = new Hashtable<String, Object>();
	}
		
	public void setID(String ID){
		this.ID = ID;
	}
	
	public String getID() {
		return ID;
	}

	public Object getClientData(String key) {
		return clientData.get(key);
	}
	
	public void setClientData(String key, Object obj){
		clientData.put(key, obj);
	}
	
	
}
