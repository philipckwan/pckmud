package com.pck.util;

public class Ask {
	private String question = "";
	private String answer = "";
	
	public Ask(){
		this("");
	}
	
	public Ask(String lastLine){
		question = lastLine;
	}
	
	public void setQuestion(String lastLine){
		this.question = lastLine;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public void setAnswer(String answer){
		this.answer = answer;
	}
	
	public String getAnswer(){
		return answer;
	}
	
}
