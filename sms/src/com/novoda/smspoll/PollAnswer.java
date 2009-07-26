package com.novoda.smspoll;

public class PollAnswer {

	public String answer;
	public String question;
	
	PollAnswer(String question){
		this.question = question;
	}
	
	PollAnswer(String question, String answer){
		this.question = question;
		this.answer = answer;
	}
}
