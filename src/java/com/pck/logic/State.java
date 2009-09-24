package com.pck.logic;

import java.util.ArrayList;
import java.util.List;

//import java.util.Map;

public class State {
	
	// aggregation	
	/*
	 * name: [a-zA-Z0-9\-\_]+
	 * tell: [a-zA-Z0-9\-\_\n]*
	 * ask: [a-zA-Z0-9\-\_\n]*
	 * inputs: [[a-zA-Z0-9\-\_\n]+]*
	 * flags: [[a-zA-Z0-9\-\_]+ = [a-zA-Z0-9\-\_]+]*
	 * nexts: [[0-9]+ :: <input> | <flag> == <[a-zA-Z0-9\-\_]+> | * :: <name>]*
	 */
	private String name = null;
	private String tell = null;
	private String ask = null;
	//private String[] inputs = null;
	private String[] flags = null;
	private List<Next> nexts = new ArrayList<Next>();

	public State() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTell() {
		return tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}
/*
	public String[] getInputs() {
		return inputs;
	}

	public void setInputs(String[] inputs) {
		this.inputs = inputs;
	}
*/
	public String[] getFlags() {
		return flags;
	}

	public void setFlags(String[] flags) {
		List<String> flagsArray = new ArrayList<String>();
		for (String flag : flags){
			String[] flagArray = flag.split("=");
			String flagKey = flagArray[0];
			flagsArray.add(flagKey);
		} 
		String[] flagsProcessed = flagsArray.toArray(new String[1]);
		this.flags = flagsProcessed;
	}

	public List<Next> getNexts() {
		return nexts;
	}

	public void setNexts(String[] nexts) {
		String[] splits = null;
		String[] inputOrFlag = null;
		for (String nextStr : nexts) {
			splits = nextStr.split("::");
			Next next = new Next();
			next.id = splits[0];
			inputOrFlag = splits[1].split("==");
			if (inputOrFlag.length > 1) {
				next.flag = inputOrFlag[0];
			} else {
				next.input = inputOrFlag[0];
			}
			next.nextState = splits[2];
			this.nexts.add(next);
		}
	}
	
	class Next {
		String id = null;
		String input = null;
		String flag = null;
		String nextState = null;
	}
}
