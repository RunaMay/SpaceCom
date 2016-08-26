package com.confluence.plugin.roadmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project {
	
	private String projectName;
	private String program;
	private String url;
	private String originalCompletionEstimate;
	private String projectPhase;
	private String dateToday;
	private String complete;
	private String percentageComplete;
	private List<String> newEstimate;
	private List<String> delayReason;
	
	public Project(){
		    setProjectName(projectName);
		    setProgram(program);
		    setUrl(url);
		    setOriginalCompletionEstimate(originalCompletionEstimate);
		    setProjectPhase(projectPhase);
		    setDateToday(dateToday);
		    setComplete(complete);
		    setPercentageComplete(percentageComplete);
		    setNewEstimate(newEstimate);
		    setDelayReason(delayReason);

	}
	
	
	 public Project(String projectName, String program, String url, String originalCompletionEstimate, String projectPhase, 
		 String dateToday, String complete, String percentageComplete, List<String> newEstimate, List<String> delayReason) {
	    setProjectName(projectName);
	    setProgram(program);
	    setUrl(url);
	    setOriginalCompletionEstimate(originalCompletionEstimate);
	    setProjectPhase(projectPhase);
	    setDateToday(dateToday);
	    setComplete(complete);
	    setPercentageComplete(percentageComplete);
	    setNewEstimate(newEstimate);
	    setDelayReason(delayReason);

	    }




	public String getProjectName() {
		return projectName;
	}




	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}




	public String getProgram() {
		return program;
	}




	public void setProgram(String program) {
		this.program = program;
	}




	public String getUrl() {
		return url;
	}




	public void setUrl(String url) {
		this.url = url;
	}




	public String getOriginalCompletionEstimate() {
		return originalCompletionEstimate;
	}




	public void setOriginalCompletionEstimate(String originalCompletionEstimate) {
		this.originalCompletionEstimate = originalCompletionEstimate;
	}




	public String getProjectPhase() {
		return projectPhase;
	}




	public void setProjectPhase(String projectPhase) {
		this.projectPhase = projectPhase;
	}




	public String getDateToday() {
		return dateToday;
	}




	public void setDateToday(String dateToday) {
		this.dateToday = dateToday;
	}




	public String getComplete() {
		return complete;
	}




	public void setComplete(String complete) {
		this.complete = complete;
	}




	public String getPercentageComplete() {
		return percentageComplete;
	}




	public void setPercentageComplete(String percentageComplete) {
		this.percentageComplete = percentageComplete;
	}


	public List<String> getDelayReason() {
		return delayReason;
	}


	public void setDelayReason(List<String> delayReason) {
		this.delayReason = delayReason;
	}


	public List<String> getNewEstimate() {
		return newEstimate;
	}


	public void setNewEstimate(List<String> newEstimate) {
		this.newEstimate = newEstimate;
	}



	
}
