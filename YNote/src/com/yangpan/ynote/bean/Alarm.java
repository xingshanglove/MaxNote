package com.yangpan.ynote.bean;

public class Alarm {
	private String time;
	private String type;
	private int status;
	public Alarm() {
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Alarm(String time, String type, int status) {
		super();
		this.time = time;
		this.type = type;
		this.status = status;
	}
	@Override
	public String toString() {
		return "Alarm [time=" + time + ", type=" + type + ", status=" + status
				+ "]";
	}
	
}
